var loader = require("./loader");
var config = loader.config;

loader.load(function(NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */

    // 不带公钥
    var data_none_publicKey = {
        recipient: config.recipient, // accountRS
        amountNQT: NRS.convertToNQT("1000"), // MW to NQT conversion
        secretPhrase: config.secretPhrase,
        // encryptedMessageIsPrunable: "true" // Optional - make the attached message prunable
    };
    // 带上公钥
    var data = {
        recipient: NRS.getAccountIdFromPublicKey(config.recipientPublicKey), // public key to account id
        amountNQT: NRS.convertToNQT("1"), // MW to NQT conversion
        recipientPublicKey: config.recipientPublicKey, // Optional - public key announcement to init a new account
        secretPhrase: config.secretPhrase,
        // encryptedMessageIsPrunable: "true" // Optional - make the attached message prunable
    };

    // Compose the request data
    data = Object.assign(
        data_none_publicKey,
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
    });
});
