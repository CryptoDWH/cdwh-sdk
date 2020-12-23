var loader = require("./loader");
var config = loader.config;

loader.load(function (NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */
        // 引入密钥生成函数
    var PassPhraseGenerator = require("../crypto/passphrasegenerator");
    // 密钥生成
    PassPhraseGenerator.generatePassPhrase();
    // 获取密钥
    let secretPhrase = PassPhraseGenerator.passPhrase;
    if (!secretPhrase) {
        NRS.logConsole("Failed to generate secretPhrase");
        return;
    }
    NRS.logConsole("[Generate] secretPhrase = " + secretPhrase);
    // 根据密钥获取公钥
    let publicKey = NRS.generatePublicKey(secretPhrase);
    NRS.logConsole("[Generate] publicKey = " + publicKey);
    // 根据公钥获取地址
    let addressRS = NRS.getAccountIdFromPublicKey(publicKey, true);
    NRS.logConsole("[Generate] addressRS = " + addressRS);
    // 根据地址生成账户ID
    let address = new MwAddress();
    address.set(addressRS);
    NRS.logConsole("[Generate] addressID = " + address.account_id());

});