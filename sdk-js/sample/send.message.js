var loader = require("./loader");
var config = loader.config;

loader.load(function(NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */
    var data = {
        recipient: NRS.getAccountIdFromPublicKey(config.recipientPublicKey),
        secretPhrase: config.secretPhrase,
        encryptedMessageIsPrunable: "true"
    };
    data = Object.assign(
        data,
        NRS.getMandatoryParams(),
        NRS.encryptMessage(NRS, "message to recipient", config.secretPhrase, config.recipientPublicKey, false)
    );
    NRS.sendRequest("sendMessage", data, function (response) {
        NRS.logConsole(JSON.stringify(response));
    });
});