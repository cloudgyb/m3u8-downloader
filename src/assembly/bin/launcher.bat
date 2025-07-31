@echo off
set JLINK_VM_OPTIONS=-Dlogback.configurationFile=../conf/logback.xml
set DIR=%~dp0
cd /d "%~dp0"
start "m3u8 downloader" "%DIR%\javaw" %JLINK_VM_OPTIONS% -m m3u8downloader/com.github.cloudgyb.m3u8downloader.M3u8DownloaderApp %*
