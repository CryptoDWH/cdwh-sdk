package org.sdk.crypto;

import org.apache.commons.lang3.StringUtils;
import org.sdk.utils.Convert;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AccountAddress {

    public static final String ACCOUNT_PREFIX = "CDW-"; //account prefix

    /**
     * get passPhrase
     * you can get publicKey and privateKey by PassPhrase with the method in class of Crypto
     * @return
     */
    public static String generateAccount(){
        String passPhrase = "";
        try {
            passPhrase = PassPhrase.generatePassPhrase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passPhrase;
    }

    /**
     * transfer the rsaccount to accountId and generate accountId by publicKey,then compare the accountId
     * @param accountAddress
     * @param publicKey
     * @return
     */
    public static boolean isValidAccount(String accountAddress,byte[] publicKey){
        if (accountAddress == null || (accountAddress = accountAddress.trim()).isEmpty()) {
            return false;
        }
        long accountId = getAccountId(accountAddress);
        BigInteger publicAccountId = getAccountId(publicKey);
        if (accountId == publicAccountId.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * transfer the rsaccount to accountId
     * @param accountAddress
     * @return
     */
    private static long getAccountId(String accountAddress) {
        accountAddress = accountAddress.toUpperCase();
        long accountId;
        if (accountAddress.startsWith(ACCOUNT_PREFIX)) {
            accountId = Crypto.rsDecode(accountAddress.substring(4));
        } else {
            accountId = Long.parseUnsignedLong(accountAddress);
        }
        return accountId;
    }

    /**
     * generate accountId by publicKey
     * @param publicKey
     * @return
     */
    private static BigInteger getAccountId(byte[] publicKey) {
        byte[] bytes = Convert.parseHexString(Convert.toHexString(Crypto.sha256().digest(publicKey)));
        return new BigInteger(Arrays.copyOfRange(bytes, 0, 8));
    }

    public static boolean isValidAccount(String accountAddress,String publicKey){
        if (accountAddress == null || (accountAddress = accountAddress.trim()).isEmpty() || StringUtils.isBlank(publicKey)) {
            return false;
        }
        return isValidAccount(accountAddress, Convert.parseHexString(publicKey));
    }
}
