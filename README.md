# M3U8高速下载器

## 什么是M3U8？
M3U8 是 Unicode 版本的 M3U，用 UTF-8 编码。
"M3U" 和 "M3U8" 文件都是苹果公司使用的 HTTP Live Streaming（HLS） 协议格式的基础，这种协议格式可以在 iPhone 和 Macbook 等设备播放。
现在大多数视频网站都采用这种协议来提供视频播放服务。

> HLS 的工作原理是把整个流分成一个个小的基于 HTTP 的文件来下载，每次只下载一些。当媒体流正在播放时，客户端可以选择从许多不同的备用源中以不同的速率下载同样的资源，允许流媒体会话适应不同的数据速率。在开始一个流媒体会话时，客户端会下载一个包含元数据的 extended M3U (m3u8) playlist文件，用于寻找可用的媒体流。
HLS 只请求基本的 HTTP 报文，与实时传输协议（RTP）不同，HLS 可以穿过任何允许 HTTP 数据通过的防火墙或者代理服务器。它也很容易使用内容分发网络来传输媒体流。

## M3U8视频下载的原理
基于上面HLS的原理，首先将m3u8 playlist文件（.m3u8后缀）下载下来解析器其内容，获取每一段视频的链接(.ts为后缀的链接)
，采用多线程的方式分组下载，最终合成一个文件，这样就完成了下载。
下面是一个m3u8文件的例子：
```m3u8
#EXTM3U
#EXT-X-TARGETDURATION:10

#EXTINF:9.009,
https://media.example.com/first.ts
#EXTINF:9.009,
https://media.example.com/second.ts
#EXTINF:3.003,
https://media.example.com/third.ts
```
+ m3u8 文件的每一行要么是一个 URI，要么是空行，要么就是以 # 开头的字符串。不能出现空白字符，除了显示声明的元素。

+ m3u8 文件中以 # 开头的字符串要么是注释，要么就是标签。标签以 #EXT 开头，大小写敏感。

## 项目使用的技术及运行环境要求
+ javafx：11.0.2+
+ Java：11.0.1+
+ sqlite-jdbc：3.34.0
+ UI: BootstrapFX

## 项目特点
+ 多线程下载，下载速度贼快
+ 每个下载任务最多支持5个下载线程（源码可调）
+ 每个下载任务可以自定义下载线程数  
+ 提供下载历史列表，方便用户查看
+ 同时支持多个下载
+ 直接打开播放（使用外部系统默认播放器）
+ 默认下载目录为用户HOME目录的Downloads目录(可更改)
+ ...

## 运行与打包
> Java版本必须>=11.0.2，因为项目使用了Java模块化开发

运行，进入项目根目录执行：
```shell
mvn clean javafx:run 
```

打包：
```shell
mvn clean javafx:jlink
```
将会在target目录生成m3u8downloader文件夹(运行时环境)和m3u8-downloader.zip压缩的运行时环境。


## 截图
**新建下载任务**<br>
![新建下载](./docs/screenshots/new_download_task.png)

**下载列表**<br>
![下载列表](./docs/screenshots/download_list.png)

**下载历史列表**<br>
![下载历史列表](./docs/screenshots/download_history_list.png)

**下载设置**<br>
![下载设置](./docs/screenshots/setting.png)



