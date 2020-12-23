var loader = require("./loader");
var config = loader.config;

loader.load(function (NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */

    /**
     * 转账函数
     * @param secretPhrase
     * @param amount
     * @param recipient
     */
    function sendMoney(secretPhrase, amount, recipient) {
        var data = {
            recipient: NRS.getAccountIdFromRS(recipient), // public key to account id
            amountNQT: NRS.convertToNQT(amount), // MW to NQT conversion
            secretPhrase: secretPhrase,
            // encryptedMessageIsPrunable: "true" // Optional - make the attached message prunable
        };
        // Compose the request data
        data = Object.assign(
            data,
            NRS.getMandatoryParams(),
            // NRS.encryptMessage(NRS, "note to myself", config.secretPhrase, NRS.getPublicKey(converters.stringToHexString(config.secretPhrase)), true), // dispensable
            // NRS.encryptMessage(NRS, "message to recipient", config.secretPhrase, config.recipientPublicKey, false) // dispensable
        );
        // Submit the request to the remote node using the standard client function which performs local signing for transactions
        // and validates the data returned from the server.
        // This method will only send the passphrase to the server in requests for which the passphrase is required like startForging
        // It will never submit the passphrase for transaction requests
        NRS.sendRequest("sendMoney", data, function (response) {
            // Callback operations, custom processing
            NRS.logConsole(JSON.stringify(response));
        })
    }

    /**
     * 签名验证函数
     */
    function signCase() {
        let message = converters.stringToHexString("1608545346000");
        let secret = converters.stringToHexString("***");
        let public = NRS.getPublicKey(secret, false);
        let sign = NRS.signBytes(message, secret);
        let verify = NRS.verifySignature(
            sign,
            message,
            public, (res) => {
                NRS.logConsole(res);
            });
        console.log("verfiy " + verify);
        console.log("sign.length\n" + sign.toString().length);
        console.log("public\n" + public);
        console.log("sign\n" + sign);
    }
    signCase();
});