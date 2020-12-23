var loader = require("./loader");
var config = loader.config;
var fs = require('fs'); //文件模块
var path = require('path'); //系统路径模块


loader.load(function (NRS) {

    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */

        // 引入密钥生成函数
    var PassPhraseGenerator = require("../crypto/passphrasegenerator");

    function generate(amountNQT) {
        // 密钥生成
        PassPhraseGenerator.generatePassPhrase();
        // 获取密钥
        let secretPhrase = PassPhraseGenerator.passPhrase;
        if (!secretPhrase) {
            NRS.logConsole("Failed to generate secretPhrase");
            return;
        }
        // NRS.logConsole("[Generate] secretPhrase = " + secretPhrase);
        // 根据密钥获取公钥
        let publicKey = NRS.generatePublicKey(secretPhrase);
        // NRS.logConsole("[Generate] publicKey = " + publicKey);
        // 根据公钥获取地址
        let addressRS = NRS.getAccountIdFromPublicKey(publicKey, true);
        NRS.logConsole("[Generate] addressRS = " + addressRS);

        let address = new MwAddress();
        address.set(addressRS);
        let accountID = address.account_id();
        // NRS.logConsole("[Generate] addressID = " + accountID);

        let item = {}
        item.amountNQT = amountNQT;
        item.recipientPublicKey = publicKey;
        item.recipientRS = addressRS;
        item.secretPhrase = secretPhrase;
        item.accountID = accountID;
        return item;
    }

    function toList(list, item) {
        delete item.secretPhrase;
        delete item.accountID;
        list.push(item);
        return list;
    }

    function toPRList(list, item) {
        delete item.amountNQT;
        list.push(item);
        return list;
    }

    function toAirdrop(list) {
        let airdrop = {}
        airdrop.feeNQT = "0";
        airdrop.deadline = "30";
        airdrop.secretPhrase = "****";
        airdrop.list = list;
        return airdrop;
    }

    function toAccountList(list, num) {
        let accountList = {};
        accountList.accountNum = num;
        accountList.list = list;
        return accountList;
    }

    function writeToJson(airdrop, filename) {
        //把data对象转换为json格式字符串
        var content = JSON.stringify(airdrop);
        //指定创建目录及文件名称，__dirname为执行当前js文件的目录
        var file = path.join(__dirname, filename);

        try { //写入文件
            fs.writeFileSync(file, content, {
                encoding: "utf8",
                mode: 0o666
            });
        } catch (err) {
            if (err) {
                return console.log(err);
            }
        }
        console.log('File create successful, Path：' + file);
    }

    /**
     * The number of accounts generated
     * @type {number}
     */
    let num = 10;
    /**
     * The amount of airdrop | Unit: 1 * 10^-8 MW
     * @type {string}
     */
    let amountNQT = "1";
    let list = [];
    let PRList = [];
    for (let i = 0; i < num; i++) {
        toList(list, generate(amountNQT));
        toPRList(PRList, generate(amountNQT));
    }
    writeToJson(toAirdrop(list), "airdrop.json");
    writeToJson(toAccountList(PRList, num), "accountPR.json");

});