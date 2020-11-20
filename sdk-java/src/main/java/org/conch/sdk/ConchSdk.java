package org.conch.sdk;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
        public static void generateNewAccount(){
            //TODO
            System.out.println("Finished generateNewAccount");
        }

        //CASE: secret phrase -> rs-address + public key + private key
        public static void getAccountInfo(){
            //TODO
            System.out.println("Finished getAccountInfo");
        }

        //CASE: rs-address -> account id
        public static void getAccountId(){
            //TODO
            System.out.println("Finished getAccountId");
        }

        //CASE: verify account
        public static void verifyAccount(){
            //TODO
            System.out.println("Finished verifyAccount");
        }
    }

    public static void main(String[] args) {
        ConchCase.generateNewAccount();
        ConchCase.getAccountInfo();
        ConchCase.getAccountId();
        ConchCase.verifyAccount();
    }
}
