package org.conch.sdk;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.conch.sdk.crypto.Crypto;
import org.conch.sdk.crypto.PassPhrase;
import org.conch.sdk.utils.Convert;
import org.conch.sdk.utils.HttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * Conch SDK
 * - account generate, verify, query and so on
 * - rs-address(rsAddress) is a formatted string, like: CDW-9UYJ-U6XK-P45Q-852QF
 * - secret phrase(secretPhrase) is a group of 12 words to access the address
 * - you can see use cases in ConchCase
 *
 * - 主要提供了用户生成账户信息的方法、验证地址正确性的方法等
 * - rs-address(rsAddress)是一个格式化的字符串，比如：CDW-9UYJ-U6XK-P45Q-852QF
 * - secret phrase(secretPhrase)是一组由12个单词组成的字符串，用于生成公私钥等信息
 * - 使用案例在ConchCase类中可供参考
 *
 * @author Zack, Ben
 */
public class ConchSdk {

    public static final String ACCOUNT_PREFIX = "CDW-";

    /**
     * Generate a new address
     *
     * 每个账户的信息都是通过一组由12个单词组成的字符串生成的，该方法用于生成一个这样的字符串来创建账户信息
     * @return secret phrase of new address
     * @return 返回一个新地址的密钥字符串
     */
    public static String generateAccount(){
        String secretPhrase = "";
        try {
            secretPhrase = PassPhrase.generatePassPhrase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretPhrase;
    }

    /**
     * Get public key from secret phrase
     *
     * 通过secretPhrase生成新地址的公钥，同一个secretPhrase生成的时同一个公钥
     * @param secretPhrase
     * @return
     */
    public static byte[] getPublicKey(String secretPhrase) {
        return Crypto.getPublicKey(secretPhrase);
    }

    /**
     * Get private key from secret phrase
     *
     * 通过secretPhrase生成新地址的私钥，同一个secretPhrase生成的是同一个私钥
     * @param secretPhrase
     * @return
     */
    public static byte[] getPrivateKey(String secretPhrase) {
        return Crypto.getPrivateKey(secretPhrase);
    }

    /**
     * Valid and compare rs-address and public key
     *
     * 通过rs-address可以获取用户id，通过public key也可以获取用户id，将获取的id进行对比，就能判断用户地址是否正确
     *
     * @param rsAddress
     * @param publicKey
     * @return
     */
    public static boolean isValidAccount(String rsAddress, byte[] publicKey){
        if (rsAddress == null || (rsAddress = rsAddress.trim()).isEmpty()) {
            return false;
        }
        long accountId = getAccountId(rsAddress);
        Long publicAccountId = getAccountId(publicKey);
        if (accountId == publicAccountId.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert the account id to rs-address
     *
     * 将用户id转换成rs-address
     *
     * @param accountId
     * @return
     */
    public static String getRsAddress(Long accountId) {
        return ACCOUNT_PREFIX + Crypto.rsEncode(accountId);
    }

    /**
     * Convert the rs-address to account id
     *
     * 将rs-address转换成用户id
     *
     * @param rsAddress
     * @return
     */
    public static long getAccountId(String rsAddress) {
        rsAddress = rsAddress.toUpperCase();
        long accountId;
        if (rsAddress.startsWith(ACCOUNT_PREFIX)) {
            accountId = Crypto.rsDecode(rsAddress.substring(4));
        } else {
            accountId = Long.parseUnsignedLong(rsAddress);
        }
        return accountId;
    }

    /**
     * Convert the public key to account id
     *
     * 通过公钥生成用户id
     *
     * @param publicKey
     * @return
     */
    public static Long getAccountId(byte[] publicKey) {
        byte[] bytes = Convert.parseHexString(Convert.toHexString(Crypto.sha256().digest(publicKey)));
        return Convert.fullHashToId(bytes);
    }

    /**
     * Valid and compare rs-address and public key
     *
     * 通过rs-address可以获取用户id，通过public key也可以获取用户id，将获取的id进行对比，就能判断用户地址是否正确
     *
     * @param rsAddress
     * @param publicKey
     * @return
     */
    public static boolean isValidAccount(String rsAddress, String publicKey){
        if (rsAddress == null || (rsAddress = rsAddress.trim()).isEmpty() || StringUtils.isBlank(publicKey)) {
            return false;
        }
        return isValidAccount(rsAddress, Convert.parseHexString(publicKey));
    }

    public static final String SEND_URL = "http://43.250.175.32:9216/sharder?requestType=sendMoney";    // testNode
    public static final String BROADCAST_URL = "http://43.250.175.32:9216/sharder?requestType=broadcastTransaction";    // testNode
    public static final BigDecimal ONE_SS = new BigDecimal(100000000);

    /**
     * create transaction by public key
     *
     * 通过公钥创建交易
     *
     * @param recipient
     * @param recipientPublicKey
     * @param publicKey
     * @param amountNQT
     * @param fee
     * @return
     */
    public static JSONObject sendTransaction(String recipient, String recipientPublicKey, String publicKey, BigDecimal amountNQT, BigDecimal fee) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("recipient", recipient);
        params.put("recipientPublicKey", recipientPublicKey);
        params.put("deadline", "1440");
        params.put("phased", "false");
        params.put("phasingLinkedFullHash", "");
        params.put("phasingHashedSecret", "");
        params.put("phasingHashedSecretAlgorithm", '2');
        params.put("publicKey", publicKey);
        params.put("feeNQT", fee.multiply(ONE_SS));
        params.put("amountNQT", amountNQT.multiply(ONE_SS));
        String s = HttpClient.doPost(SEND_URL, HttpClient.getPostParams(params));
        try {
            JSONObject res = (JSONObject) JSONValue.parseWithException(s);
            return res;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * sign the transaction and broadcast to the internet
     *
     * 将创建的交易进行签名并广播
     *
     * @param transactionBytes
     * @param prunableAttachmentJSON
     * @return
     */

    public static JSONObject broadcast(String transactionBytes, String prunableAttachmentJSON) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("transactionBytes", transactionBytes);
        params.put("prunableAttachmentJSON", prunableAttachmentJSON);
        String s = HttpClient.doPost(BROADCAST_URL, HttpClient.getPostParams(params));
        try {
            JSONObject res = (JSONObject) JSONValue.parseWithException(s);
            return res;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Use cases
     */
    static class ConchCase {
        //CASE: generate new address
        public static Map<String, String> generateAccount(){
            Map<String, String> accountData = new HashMap<>();
            String passPhrase = ConchSdk.generateAccount();
            byte[] publicKey = ConchSdk.getPublicKey(passPhrase);
            byte[] privateKey = ConchSdk.getPrivateKey(passPhrase);
            long accountId = ConchSdk.getAccountId(publicKey).longValue();
            String rsAddress = ConchSdk.getRsAddress(accountId);
            accountData.put("passPhrase", passPhrase);
            accountData.put("publicKey", Convert.toHexString(publicKey));
            accountData.put("privateKey", Convert.toHexString(privateKey));
            accountData.put("accountId", String.valueOf(accountId));
            accountData.put("rsAddress", rsAddress);
            System.out.println("Finished generateNewAccount");
            System.out.println("pass phrase: " + passPhrase);
            System.out.println("public key: " + Convert.toHexString(publicKey));
            System.out.println("private key: " + Convert.toHexString(privateKey));
            System.out.println("account id: " + accountId);
            System.out.println("rs address: " + rsAddress);
            return accountData;
        }

        //CASE: secret phrase -> rs-address + public key + private key
        public static void getAccountInfo(String secretPhrase){
            byte[] publicKey = ConchSdk.getPublicKey(secretPhrase);
            byte[] privateKey = ConchSdk.getPrivateKey(secretPhrase);
            String rsAddress = ConchSdk.getRsAddress(ConchSdk.getAccountId(publicKey).longValue());
            System.out.println("Finished getAccountInfo");
            System.out.println("public key:" + Convert.toHexString(publicKey));
            System.out.println("private key:" + Convert.toHexString(privateKey));
            System.out.println("rs address:" + rsAddress);
        }

        //CASE: rs-address -> account id
        public static void getAccountId(String rsAddress){
            long accountId = ConchSdk.getAccountId(rsAddress);
            System.out.println("Finished getAccountId");
            System.out.println("account id:" + accountId);
        }

        //CASE: account id -> rs-address
        public static void getRsAddress(long accountId){
            String rsAddress = ConchSdk.getRsAddress(accountId);
            System.out.println("Finished getRsAddress");
            System.out.println("rs-address:" + rsAddress);
        }

        //CASE: verify account
        public static void verifyAccount(String rsAddress, String publicKey){
            boolean validAccount = ConchSdk.isValidAccount(rsAddress, publicKey);
            System.out.println("Finished verifyAccount");
            System.out.println("VerifyAccount result:" + validAccount);
        }

        //CASE: verify account
        public static void verifyAccount(String rsAddress, byte[] publicKey){
            boolean validAccount = ConchSdk.isValidAccount(rsAddress, publicKey);
            System.out.println("Finished verifyAccount");
            System.out.println("VerifyAccount result:" + validAccount);
        }
        // CASE: signature
        public static String signature(String message, String secret) {
            byte[] bytes = Convert.parseHexString(message);
            byte[] sign = Crypto.sign(bytes, secret);
            return Convert.toHexString(sign);
        }
        // CASE: verify signature
        public static boolean verifySign(String message, String publicKey, String signStr) {
            byte[] parseHexString = Convert.parseHexString(signStr);
            byte[] msg = Convert.parseHexString(message);
            boolean verify = Crypto.verify(parseHexString, msg, Convert.parseHexString(publicKey), false);
            return verify;
        }

        // CASE: send transaction
        public static String sendTransaction(String recipient, String recipientPublicKey, String secretPhrase, String amountNQT, String fee) {
            String publicKey = Convert.toHexString(getPublicKey(secretPhrase));
            JSONObject transactionRes = ConchSdk.sendTransaction(recipient, recipientPublicKey, publicKey, new BigDecimal(amountNQT), new BigDecimal(fee));
            if (transactionRes != null && transactionRes.get("errorCode") == null) {
                String unsignedTransactionBytes = transactionRes.get("unsignedTransactionBytes").toString();
                String signature = ConchCase.signature(unsignedTransactionBytes, secretPhrase);
                if ( ! ConchCase.verifySign(unsignedTransactionBytes, publicKey, signature)) {
                    return "";
                }
                String transactionBytes = unsignedTransactionBytes.substring(0, 192) + signature + unsignedTransactionBytes.substring(320);
                String prunableAttachmentJSON = "";
                try {
                    JSONObject transactionJSON = (JSONObject) JSONValue.parseWithException(transactionRes.get("transactionJSON").toString());
                    prunableAttachmentJSON = transactionJSON.get("attachment").toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "";
                }
                JSONObject broadcastRes = broadcast(transactionBytes, prunableAttachmentJSON);
                if (broadcastRes != null && broadcastRes.get("errorCode") == null) {
                    return broadcastRes.get("fullHash").toString();
                }
            }
            return "";
        }
    }

    public static void main(String[] args) {
//        Map<String, String> account = ConchCase.generateAccount();
//        ConchCase.getAccountInfo(account.get("passPhrase"));
//        ConchCase.getAccountId(account.get("rsAddress"));
//        ConchCase.getRsAddress(Long.parseLong(account.get("accountId")));
//        ConchCase.verifyAccount(account.get("rsAddress"), account.get("publicKey"));
//        ConchCase.verifyAccount(account.get("rsAddress"), Convert.parseHexString(account.get("publicKey")));
        String transaction = ConchCase.sendTransaction("CDW-SBEN-K3SU-F5VK-6PTTW", "852f8a329eca7752511e8b97b264bcea44b6d9f7d54db1b28725a23b08862965", "impossible radio line tell prefer cut shoot conversation deliver prefer local dev2", "2000", "1");
        System.out.println("txHash:" + transaction);

    }
}
