# Spring Demo
一个初级的spring demo工程，写着玩玩。

## Features
1. Kotlin与Java混合编译，但是Java为主
2. jwt鉴权与控制，接口调用需要携带jwt-token
3. jasypt配置文件加密，启动时需要携带password参数
4. 调用百度的API实现了ocr功能


## 自动上传与部署
1. 配置ssh自动登录
2. 本地脚本 - 上传 jar 包：`python3 upload_local_file.py`
3. 远程脚本 - 扫描与启动最新 jar 包： `python3 auto_deploy.py jasypt_password &`