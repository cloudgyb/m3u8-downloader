package com.github.cloudgyb.m3u8downloader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件工具类
 *
 * @author geng
 * @since 2023/03/18 20:24:45
 */
public final class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void mergeFiles(List<String> sourceFiles, String targetFile) throws IOException {
        List<File> files = sourceFiles.stream().map(File::new).collect(Collectors.toList());
        File file = new File(targetFile);
        mergeFiles(files, file);
    }

    public static void mergeFiles(List<File> sourceFiles, File targetFile) throws IOException {
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists()) {
            logger.info("{}目录不存在，创建它", parentFile.getAbsolutePath());
            boolean isCreated = parentFile.mkdirs();
            if (isCreated) {
                logger.info("{}目录已创建", parentFile.getAbsolutePath());
            } else {
                logger.info("{}目录创建失败", parentFile.getAbsolutePath());
            }
        }
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            for (File sourceFile : sourceFiles) {
                Files.copy(sourceFile.toPath(), fos);
            }
        }
    }
}
