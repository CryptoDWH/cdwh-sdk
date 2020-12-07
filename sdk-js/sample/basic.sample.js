var loader = require("./loader");
var config = loader.config;

loader.load(function (NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */
    function sendMoney(secretPhrase) {
        var data = {
            recipient: NRS.getAccountIdFromRS(config.recipient), // public key to account id
            amountNQT: NRS.convertToNQT("0.23"), // MW to NQT conversion
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
    let secretList = ["","",""];

    var accountRS = NRS.convertNumericToRSAccountFormat(NRS.account);

    NRS.logConsole("accountRS" + accountRS);
    for (i=0;i<3;i++){

        sendMoney(secretList[i]);
    }
});