package com.github.cloudgyb.m3u8downloader.util;

import com.github.cloudgyb.m3u8downloader.conf.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * FFMPEG 工具类
 *
 * @author geng
 * @since 2023/03/23 10:59:13
 */
public final class FfmpegUtil {
    private static final Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);

    public static void mergeTS(List<String> sourceFiles, String targetFile, boolean deleteSourceFiles) throws IOException {
        ApplicationConfig config = ApplicationConfig.getInstance();
        File tsFileList = File.createTempFile("m3u8_ts_list", ".txt");
        try (PrintWriter fileWriter = new PrintWriter(tsFileList, StandardCharsets.UTF_8)) {
            for (String sourceFile : sourceFiles) {
                fileWriter.println("file '" + sourceFile + "'");
            }
        }
        File file = new File(targetFile);
        FileUtil.deleteFileQuiet(file);
        String[] command = {config.getFfmpegBinFilePath(), "-f", "concat", "-safe", "0", "-i", tsFileList.getAbsolutePath(),
                "-c", "copy", file.getAbsolutePath()};
        SystemCommandUtil.CommandExecResult commandExecResult = SystemCommandUtil.execWithExitCodeAndResult(command);
        int exitCode = commandExecResult.getExitCode();
        if (exitCode == 0) {
            logger.info("以成功合并 ts 文件到 {}", targetFile);
        } else {
            logger.info("合并 ts 文件到 {} 可能失败（code：{}）", targetFile, exitCode);
        }
        boolean delete = tsFileList.delete();
        if (delete) {
            logger.info("临时生成的 ts 列表文件已删除！");
        } else {
            logger.warn("临时生成的 ts 列表文件删除失败！");
        }
        if (deleteSourceFiles && !sourceFiles.isEmpty()) {
            File parentFile = new File(sourceFiles.get(0)).getParentFile();
            for (String sourceFile : sourceFiles) {
                File file1 = new File(sourceFile);
                FileUtil.deleteFileQuiet(file1);
            }
            FileUtil.deleteDirIfEmpty(parentFile);
        }
    }
}
