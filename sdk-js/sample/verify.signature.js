var loader = require("./loader");
var config = loader.config;

loader.load(function (NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */
    if (config.signature && config.senderPublicKey && config.message) {
        // 验签方法
        if (!NRS.verifySignature(config.signature, config.message, config.senderPublicKey, (response)=>{
            NRS.logConsole(JSON.stringify(response));
        })) {
            NRS.logConsole("VerifySignature fail");
            return;
        }
        NRS.logConsole("VerifySignature success");
    } else {
        NRS.logConsole("VerifySignature signature or senderPublicKey or message is empty");
    }

});