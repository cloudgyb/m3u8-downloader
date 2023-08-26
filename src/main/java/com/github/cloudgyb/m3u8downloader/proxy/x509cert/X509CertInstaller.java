package com.github.cloudgyb.m3u8downloader.proxy.x509cert;


import com.github.cloudgyb.m3u8downloader.proxy.util.OSUtil;
import com.github.cloudgyb.m3u8downloader.proxy.util.SystemCommandUtil;

import java.io.File;

/**
 * 安装根证书
 *
 * @author cloudgyb
 */
public final class X509CertInstaller {
    public static void install(File CACertFile) {
        if (OSUtil.isWindows()) {
            String command = "cmd /c " + CACertFile.getAbsolutePath();
            int i = SystemCommandUtil.execWithExitCode(command);
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        install(new File("C:\\Users\\gyb\\IdeaProjects\\proxy-server\\src\\main\\resources\\CACert.crt"));
    }

}