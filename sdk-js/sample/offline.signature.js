var loader = require("./loader");
var config = loader.config;

loader.load(function (NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */
    if (config.message && config.secretPhrase) {
        let signature = NRS.signBytes(config.message, converters.stringToHexString(config.secretPhrase));
        NRS.logConsole("signature success");
        NRS.logConsole("signature = " + signature);
    } else {
        NRS.logConsole("message or secretPhrase is empty");
    }

});