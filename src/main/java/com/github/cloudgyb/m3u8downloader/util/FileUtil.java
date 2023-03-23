package com.github.cloudgyb.m3u8downloader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件工具类
 *
 * @author geng
 * @since 2023/03/18 20:24:45
 */
public final class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void deleteFileQuiet(File file) {
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                logger.debug("文件 {} 已删除！", file.getAbsolutePath());
            } else {
                logger.debug("文件 {} 删除失败！", file.getAbsolutePath());
            }
        }
    }

    public static void deleteDirIfEmpty(File file) {
        if (file.exists() && file.isDirectory()) {
            boolean delete = file.delete();
            if (delete) {
                logger.debug("目录 {} 已删除！", file.getAbsolutePath());
            } else {
                logger.debug("目录 {} 删除失败！", file.getAbsolutePath());
            }
        }
    }


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

    public static void ensureDirExist(File tempDir) {
        if (!tempDir.exists()) {
            logger.warn("目录{}不存在，创建它", tempDir.getAbsolutePath());
            boolean isCreate = tempDir.mkdirs();
            if (isCreate) {
                logger.info("目录{}已创建", tempDir.getAbsolutePath());
            } else {
                logger.error("目录{}创建失败", tempDir.getAbsolutePath());
            }
        }
    }

    private static final Map<Integer, String> byteUnit = new HashMap<>();

    static {
        byteUnit.put(0, "B/s");
        byteUnit.put(1, "KB/s");
        byteUnit.put(2, "MB/s");
        byteUnit.put(3, "GB/s");
        byteUnit.put(4, "TB/s");

    }

    public static String bytesToHumanReadable(long bytes) {
        DecimalFormat format = new DecimalFormat("0.00");
        String rateHumanReadable = format.format(bytes) + " B/s";
        int i = 1;
        while (bytes > 1024) {
            bytes = bytes / 1024;
            String unit = byteUnit.get(i);
            rateHumanReadable = format.format(bytes) + " " + unit;
            i++;
        }
        return rateHumanReadable;
    }
}
