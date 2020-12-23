# cdwh-sdk
sdk tools of cdwh chain

## 调试节点 [Devnet]
> 优先在开发网(DevNet)进行调试，需要测试币请联系官方团队

- http://mwfs.noip.cn:60
- http://mwfs.noip.cn:61

*注：开发网节点可能不稳定，若掉线请及时反馈至官方团队*

## 调试节点 [Testnet]
- [轻节点，优先使用]http://mwfs.noip.cn:55
- [备用]http://testna.mw.run:7216
- [备用] http://testnb.mw.run:7216
- [备用] http://testboot.mw.run:7216

*注：轻节点URL可能存在不定期的变动*

## API文档

https://docs.mw.run

## 批量创建账户并生成批量转账文件

操作步骤：
1. clone
2. cd sdk-js
3. npm install 或 cnpm install
4. cd sample/
5. node address.generate.js
6. 该目录下会自动生成 airdrop.json （批量转账文件，包含地址 + 公钥）
7. 打开airdrop.json, 输入自己的私钥至 secretPhrase
8. 搭建的本地节点开启空投功能（另外会补充一个开启本地节点空投的功能）
9. 通过UI上传该文件进行空投
10. 等待空投交易被确认，账户批量激活成功

## 开启空投（批量转账）
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