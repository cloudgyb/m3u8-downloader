@echo off
set JLINK_VM_OPTIONS=
set DIR=%~dp0
set VM_OPTIONS=-Dlogback.configurationFile="%DIR%"\..\conf\logback.xml
"%DIR%\javaw" %VM_OPTIONS% %JLINK_VM_OPTIONS% -m m3u8downloader/com.github.cloudgyb.m3u8downloader.M3u8DownloaderApp %*
