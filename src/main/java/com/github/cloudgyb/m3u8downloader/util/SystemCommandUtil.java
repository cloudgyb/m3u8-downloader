package com.github.cloudgyb.m3u8downloader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 系统命令执行
 *
 * @author geng
 * @since 2022/3/22 9:49
 */
public final class SystemCommandUtil {
    private final static Logger logger = LoggerFactory.getLogger(SystemCommandUtil.class);

    /**
     * 不要执行<code>cmd</code>等长时间不会退出的命令，否则会导致启动的子进程长时间不退出！
     *
     * @param command 命令
     */
    @SuppressWarnings("unused")
    public static void exec(String command) {
        execWithExitCode(command);
    }

    @SuppressWarnings("all")
    public static int execWithExitCode(String command) {
        CommandExecResult commandExecResult = execWithExitCodeAndResult(command);
        return commandExecResult.exitCode;
    }

    public static CommandExecResult execWithExitCodeAndResult(String[] cmdarray) {
        InputStream errorStream;
        InputStream inputStream;
        int exitCode = -1;
        String stdinOutput = "";
        String errorOutput = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(cmdarray);
            builder.inheritIO();
            Process exec = builder.start();
            inputStream = exec.getInputStream();
            stdinOutput = readStringFromInputStream(inputStream);
            logger.warn("执行命令'{}'标准流输出：\n{}", cmdarray, stdinOutput);
            //有些命令就算执行成功也会将输出放到错误流，所以这里也将错误流内容进行读取，便于调试
            errorStream = exec.getErrorStream();
            errorOutput = readStringFromInputStream(errorStream);
            logger.warn("执行命令'{}'错误流输出：\n{}", cmdarray, errorOutput);

            exitCode = exec.waitFor();
            if (exitCode != 0) {
                logger.warn("执行命令'{}'可能失败，退出code：{}！", Arrays.toString(cmdarray), exitCode);
            } else {
                logger.warn("执行命令'{}'成功，退出code：{}！", Arrays.toString(cmdarray), exitCode);
            }
        } catch (InterruptedException ie) {
            logger.error("执行命令'{}'被中断！", Arrays.toString(cmdarray));
        } catch (IOException e) {
            logger.error("执行命令'" + Arrays.toString(cmdarray) + "'抛出异常！", e);
        }
        return new CommandExecResult(exitCode, stdinOutput, errorOutput);
    }

    public static CommandExecResult execWithExitCodeAndResult(String command) {
        InputStream errorStream;
        InputStream inputStream;
        int exitCode = -1;
        String stdinOutput = "";
        String errorOutput = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.inheritIO();
            Process exec = builder.start();
            inputStream = exec.getInputStream();
            stdinOutput = readStringFromInputStream(inputStream);
            logger.warn("执行命令'{}'标准流输出：\n{}", command, stdinOutput);
            //有些命令就算执行成功也会将输出放到错误流，所以这里也将错误流内容进行读取，便于调试
            errorStream = exec.getErrorStream();
            errorOutput = readStringFromInputStream(errorStream);
            logger.warn("执行命令'{}'错误流输出：\n{}", command, errorOutput);

            exitCode = exec.waitFor();
            if (exitCode != 0) {
                logger.warn("执行命令'{}'可能失败，退出code：{}！", command, exitCode);
            } else {
                logger.warn("执行命令'{}'成功，退出code：{}！", command, exitCode);
            }
        } catch (InterruptedException ie) {
            logger.error("执行命令'{}'被中断！", command);
        } catch (IOException e) {
            logger.error("执行命令'" + command + "'抛出异常！", e);
        }
        return new CommandExecResult(exitCode, stdinOutput, errorOutput);
    }

    private static String readStringFromInputStream(InputStream stream) throws IOException {
        byte[] buff = new byte[128];
        int n;
        StringBuilder sb = new StringBuilder();
        try (stream) {
            while ((n = stream.read(buff)) != -1) {
                sb.append(new String(buff, 0, n));
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unused")
    public static class CommandExecResult {
        private int exitCode;
        private String stdinOutput;
        private String stderrOutput;

        public CommandExecResult() {
        }

        public CommandExecResult(int exitCode, String stdinOutput, String stderrOutput) {
            this.exitCode = exitCode;
            this.stdinOutput = stdinOutput;
            this.stderrOutput = stderrOutput;
        }

        public int getExitCode() {
            return exitCode;
        }

        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        public String getStdinOutput() {
            return stdinOutput;
        }

        public void setStdinOutput(String stdinOutput) {
            this.stdinOutput = stdinOutput;
        }

        public String getStderrOutput() {
            return stderrOutput;
        }

        public void setStderrOutput(String stderrOutput) {
            this.stderrOutput = stderrOutput;
        }
    }
}
