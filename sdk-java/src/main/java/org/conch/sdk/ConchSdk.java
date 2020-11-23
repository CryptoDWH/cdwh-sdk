package org.conch.sdk;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.conch.sdk.crypto.Crypto;
import org.conch.sdk.crypto.PassPhrase;
import org.conch.sdk.utils.Convert;

/**
 * Conch SDK
 * - account generate, verify, query and so on
 * - rs-address(rsAddress) is a formatted string, like: CDW-9UYJ-U6XK-P45Q-852QF
 * - secret phrase(secretPhrase) is a group of 12 words to access the address
 * - you can see use cases in ConchCase
 *
 * @author Zack, Ben
 */
public class ConchSdk {

    public static final String ACCOUNT_PREFIX = "CDW-";

    /**
     * Generate a new address
     * @return secret phrase of new address
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
     * @param secretPhrase
     * @return
     */
    public static byte[] getPublicKey(String secretPhrase) {
        return Crypto.getPublicKey(secretPhrase);
    }

    /**
     *
     * @param secretPhrase
     * @return
     */
    public static byte[] getPrivateKey(String secretPhrase) {
        return Crypto.getPrivateKey(secretPhrase);
    }

    /**
     * Valid and compare rs-address and public key
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
        BigInteger publicAccountId = getAccountId(publicKey);
        if (accountId == publicAccountId.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert the account id to rs-address
     *
     * @param accountId
     * @return
     */
    private static String getRsAddress(Long accountId) {
        return ACCOUNT_PREFIX + Crypto.rsEncode(accountId);
    }

    /**
     * Convert the rs-address to account id
     *
     * @param rsAddress
     * @return
     */
    private static long getAccountId(String rsAddress) {
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
     * @param publicKey
     * @return
     */
    private static BigInteger getAccountId(byte[] publicKey) {
        byte[] bytes = Convert.parseHexString(Convert.toHexString(Crypto.sha256().digest(publicKey)));
        return new BigInteger(Arrays.copyOfRange(bytes, 0, 8));
    }

    /**
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

    /**
     * Use cases
     */
    static class ConchCase {
        //CASE: generate new address
        public static Map<String, String> generateAccount(){
            //TODO
            Map<String, String> accountData = new HashMap<>();
            try {
                String passPhrase = PassPhrase.generatePassPhrase();
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
                System.out.println("public key:" + Convert.toHexString(publicKey));
                System.out.println("private key:" + Convert.toHexString(privateKey));
                System.out.println("account id:" + accountId);
                System.out.println("rs address:" + rsAddress);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return accountData;
        }

        //CASE: secret phrase -> rs-address + public key + private key
        public static void getAccountInfo(String secretPhrase){
            //TODO
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
            //TODO
            boolean validAccount = ConchSdk.isValidAccount(rsAddress, publicKey);
            System.out.println("Finished verifyAccount");
            System.out.println("VerifyAccount result:" + validAccount);
        }

        //CASE: verify account
        public static void verifyAccount(String rsAddress, byte[] publicKey){
            //TODO
            boolean validAccount = ConchSdk.isValidAccount(rsAddress, publicKey);
            System.out.println("Finished verifyAccount");
            System.out.println("VerifyAccount result:" + validAccount);
        }
    }

    public static void main(String[] args) {
        Map<String, String> account = ConchCase.generateAccount();
        ConchCase.getAccountInfo(account.get("passPhrase"));
        ConchCase.getAccountId(account.get("rsAddress"));
        ConchCase.getRsAddress(Long.parseLong(account.get("accountId")));
        ConchCase.verifyAccount(account.get("rsAddress"), account.get("publicKey"));
        ConchCase.verifyAccount(account.get("rsAddress"), Convert.parseHexString(account.get("publicKey")));
    }
}
