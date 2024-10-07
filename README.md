# Netty实现的内网穿透工具

## 状态

> 本项目目前处于开发中...

## 工作原理

- 配置端口映射关系。例如：内网端口4456 服务端端口8867 服务端映射内网应用端口 8898。也就是访问服务端的8898端口可以访问到内网4456端口；
- 客户端向服务端发起连接请求，与服务端建立连接（连接隧道）；
- 服务端监听请求数据，若为Http，端口为8898,则将数据转发给客户端;
- 客户端接收到请求数据后，获取内网应用信息如4456，
- 客户端将接收到的数据转发内网目标应用，例如转发给本地4456端口；
- 客户端获取数据后，转发给服务端，服务端再进行最终反馈；


## 系统设计

### Client端

- 连接Server时发送AuthMessage(含ClientId)；
- Client发送CmdMessage时携带licenseKey
    - Client发送创建Server代理请求（携带proxyHost,proxyPort,openPort,protocol等信息）
- Client接收到来自服务端的transferData指令后，解析transferData中的数据，向proxyHost、proxyPort发起连接，拿到数据后发送给Server端，并关闭连接；


### Server端

- Server接收到Client的AuthMessage后进行校验，若校验通过，返回AuthResMessage(包含许可证密钥licenseKey)
> 说明Server需要有一个Map<ClientId,LicenseKey>

- Server接收到Client的创建代理请求时，校验licenseKey,校验通过，创建一个ServerChannel监听OpenPort端口，记录ChannelId
> 说明Server需要有一个Map<OpenPort,ProxyInfo>, Map<LicenseKey, List<OpenPort>>，其中ProxyInfo包含protocol、proxyHost、proxyPort、LicenseKey、ChannelId


- 外网访问OpenPort端口服务时，Server根据OpenPort查询ProxyInfo,然后向客户端发起transfer data指令（data包含：proxyInfo、实际数据）

- Server接收到Client数据后，再将data原封不动地返回给OpenPort对应的Channel