# cdwh-sdk-js
js version of cdwh chain sdk 

## Functions
实现离线签名、地址生成、签名校验sdk。具体输入输出条件&示例请移步示例代码
### 离线签名
> 示例代码 `sample\offline.signature.js`

### 地址生成
> 示例代码 `sample\address.generate.js`

### 签名校验
> 示例代码 `sample\verify.signature.js`

*注： 所需的相关参数均在 `sample\config.js`*

## How to use

### node & npm
> 请确保node版本 >= v10, npm版本 >= 6
```
$ node -v
v10.16.2

$ npm -v 
6.9.0
```

### 配置参数
> 配置文件路径： `sample\config.js`
```
{
  "url": "http://192.168.0.22:7216",  // 服务端程序 IP + 端口
  "secretPhrase": "", // 密钥，用于转账交易相关
  "recipientPublicKey": "8d2ce5f55d8e9c1f53d278ce19a62ae77295391b3d94ab109a66eb752e131074", // 接受者公钥，用于转账交易相关
  "isTestNet": true, // 是否为测试网，默认 true
  "message": "hello,world", // 将被签名的message，测试用，正式环境请替换 
  "signature": "3f6217f81aeb903960c94250cd51e2b2fb48cf2e227f7da740141ab62528910001fd2d7062516420af01fdb291bd8e2f8a96772c2d5b0e3bedf5bd8df1cd0fdf", // 对message进行签名得到的16进制字符串，测试用，正式环境请替换 
  "senderPublicKey": "6065fa6fda743b3714f1ccb66543e3d5da658f886662a698e840831d1b6f225f" // 签名校验公钥，测试用，正式环境请替换 
}
```

### 运行SDK-JS示例代码
在 `sdk-js` 目录下：
```
// 首先完成npm依赖包的安装
$ npm install 

cd sammple/ 

// 运行地址生成示例
node address.generate.js
```
运行返回结果：
```
Started
done loading server constants
2020-11-26T02:49:25.998Z [Generate] secretPhrase = find misery speed hop afternoon linger shelter men deadly struggle discover claim
2020-11-26T02:49:26.010Z [Generate] publicKey = b4a7416b08b64cb6358bd52aba81fa7cb9c319cadbc3332e7649b2043729f94f
2020-11-26T02:49:26.011Z [Generate] addressRS = CDW-Z77S-R2NC-YLFH-B3Y7E

```
运行其他示例代码：

```
node [flieName].js
```
### 示例代码运行分析

项目入口为 `nrs.node.bridge.js`, 通过 `node xxx.js` 实际运行过程：
> 以 `sample\basic.sample.js` 为例

1. 加载模块 和 配置：
```js
var loader = require("./loader");
var config = loader.config;
```

2. 运行nrs.node.bridge.js中load(),其中load()内部执行内容：创建 `jsdom` 虚拟窗口window， 将相关的文件挂载至 `global`全局对象。
```js
loader.load(function (NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */
});
```
3. load()内最终调用 rollback(global.client)，代码开始执行 **自定义回调处理逻辑**

### 批量创建账户并生成批量转账文件

操作步骤：
1. git clone (sdk-js-case分支)
2. cd sdk-js/
3. npm install 或 cnpm install
4. cd sample/
5. node batch.generate.js
6. 该目录下会生成以下文件：
    - airdrop.json (批量转账文件，包含：地址 + 公钥 + 转账金额)
    - accountPR.json (账户信息，包含：私钥 + 公钥 + 地址 + 账户ID)
7. 打开airdrop.json, 输入自己的私钥至 secretPhrase
8. 搭建的本地节点开启空投功能（开启空投移步下方）
9. 通过UI上传该文件进行空投
10. 等待空投交易被确认，账户批量激活成功


### 开启空投（批量转账）
> 为保证私钥安全性：请务必在本地 or 内网 环境下开启空投

1. 添加空投配置至 conf/sharder.properties:
    ```properties
    ### Airdrop ###
    # airdrop pathName
    sharder.airdrop.pathName=conf/airdrop.json
    # valid keys for airdrop, Split the account address through ";"
    sharder.airdrop.validKeys=key1key1key1;key2key2key2
    # airdrop append mode switch
    sharder.airdrop.isAppendMode=true
    # airdrop switch
    sharder.airdrop.enable=true
    # airdrop open account, Split the account address through ";"
    sharder.airdrop.account=CDW-87L3-HT5W-K9WD-3AQJ3;CDW-87L3-HT5W-K9WD-3AQJ4
    ```
    *注： sharder.airdrop.validKeys | sharder.airdrop.account 请自行配置，通过“;”进行分割，支持多key & 多账户*
2. 使用配置的账户登录UI系统（IP:7216）， 进入 账户页，刷新即可看到 “空投” 按钮
3. 点击上传生成的airdrop.json文件，空投密钥即为 sharder.airdrop.validKeys的key之一
4. 确认空投后，UI生成airdrop文件并自动下载至本地。点击打开该文件：
    - doneList为转账交易创建成功列表（未确认的交易，需等待全网确认）
    - failList为转账交易创建失败列表，每个item会注明失败原因
    - list为未处理的转账交易列表
5. 可通过“空投检测按钮”查询交易是否被确认，操作与上同理，将生成的文件再次上传并点击 检测 按钮，UI会自动返回新的文件，打开新的文件可以查看交易被确认的情况：
    - confirmedList为交易被确认的列表
    
*注：使用空投前务必检查空投文件的secretPhrase是否为正确的值*

### 注意事项

- 在执行sample之前，请确保可以正确调用服务端
- 确保`nrs.constants.js`中的`LAST_KNOWN_BLOCK`,`LAST_KNOWN_TESTNET_BLOCK`为正确值
- 部分参数非必填，注释已注明