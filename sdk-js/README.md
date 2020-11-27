# cdwh-sdk-js
js version of cdwh chain sdk 

- [中文版](./README_CN.md)

## Functions
Implementation of offline signature, address generation, signature verification SDK. Specific input/output criteria & examples Please move to the sample code
### offline signature
> sample code `sample\offline.signature.js`

### address generation
> sample code `sample\address.generate.js`

### signature verification
> sample code `sample\verify.signature.js`

*notice： The required parameters are all in `sample\config.js`*

## How to use

### node & npm
> Please sure node version >= v10, npm version >= 6
```
$ node -v
v10.16.2

$ npm -v 
6.9.0
```

### configuration parameter
> profilePath： `sample\config.js`
```
{
  "url": "http://192.168.0.22:7216",  // Server program IP + port
  "secretPhrase": "", // Used for transfer transactions and signature
  "recipientPublicKey": "8d2ce5f55d8e9c1f53d278ce19a62ae77295391b3d94ab109a66eb752e131074", // Recipient's public key, used to transfer transactions related
  "isTestNet": true, // Whether to Testnet, default true
  "message": "hello,world", // To be signed message，For testing, please replace the formal environment 
  "signature": "3f6217f81aeb903960c94250cd51e2b2fb48cf2e227f7da740141ab62528910001fd2d7062516420af01fdb291bd8e2f8a96772c2d5b0e3bedf5bd8df1cd0fdf", // The signature gets the hexadecimal string, For testing
  "senderPublicKey": "6065fa6fda743b3714f1ccb66543e3d5da658f886662a698e840831d1b6f225f" // Signature verification public key, For testing 
}
```

### run SDK-JS sample code
In the directory `sdk-js`：
```
// installation of the NPM dependent package
$ npm install 

cd sammple/ 

// Run address generation instance
node address.generate.js
```
Run returns the result：
```
Started
done loading server constants
2020-11-26T02:49:25.998Z [Generate] secretPhrase = find misery speed hop afternoon linger shelter men deadly struggle discover claim
2020-11-26T02:49:26.010Z [Generate] publicKey = b4a7416b08b64cb6358bd52aba81fa7cb9c319cadbc3332e7649b2043729f94f
2020-11-26T02:49:26.011Z [Generate] addressRS = CDW-Z77S-R2NC-YLFH-B3Y7E

```
Run the other sample code：

```
node [flieName].js
```
### sample code run analysis

Project entry is `nrs.node.bridge.js`, via `node xxx.js` actual run：
> example `sample\basic.sample.js`

1. Load modules and configurations：
```js
var loader = require("./loader");
var config = loader.config;
```

2. run nrs.node.bridge.js load(),and load() internal execution content：create `jsdom` virtual window, mount the relevant file to `global` object。
```js
loader.load(function (NRS) {
    /**
     * 自定义回调处理逻辑
     *
     * loader.load()已对sdk进行初始化并载入到 NRS对象，NRS通过回调入参传入，可通过 NRS.function(params)调用sdk内部定义的方法
     */
});
```
3. load() final call rollback(global.client)，Code starts to execute **自定义回调处理逻辑**

### Attention

- Before executing SAMPLE, make sure that the service side is invoked correctly
- Insure`nrs.constants.js` the `LAST_KNOWN_BLOCK`,`LAST_KNOWN_TESTNET_BLOCK` is correct nubmer
- Some parameters are not required and are indicated in the notes