package com.github.cloudgyb.m3u8downloader.model;

import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.util.OSUtil;
import com.github.cloudgyb.m3u8downloader.util.SystemCommandUtil;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * 下载历史视图模型
 *
 * @author cloudgyb
 * @since 2025/07/11 14:57
 */
public class DownloadTaskHistoryViewModel {
    private static final Logger log = LoggerFactory.getLogger(DownloadTaskHistoryViewModel.class);
    private final static DownloadTaskService downloadTaskService = DownloadTaskService.getInstance();

    private final BooleanProperty checked = new SimpleBooleanProperty(false);
    private final IntegerProperty id = new SimpleIntegerProperty(0);
    private final StringProperty url = new SimpleStringProperty("");
    private final StringProperty createTime = new SimpleStringProperty("");
    private final StringProperty finishTime = new SimpleStringProperty("");
    private final StringProperty duration = new SimpleStringProperty("");
    private final StringProperty saveFilename = new SimpleStringProperty("");
    private final StringProperty filepath = new SimpleStringProperty("");


    public DownloadTaskHistoryViewModel(DownloadTaskEntity entity) {
        this.checked.set(false);
        this.id.set(entity.getId());
        this.url.set(entity.getUrl());
        this.createTime.set(entity.getCreateTimeText());
        this.finishTime.set(entity.getFinishTimeText());
        this.duration.set(entity.getDurationText());
        this.saveFilename.set(entity.getSaveFilename());
        this.filepath.set(entity.getFilePath());
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty urlProperty() {
        return url;
    }

    public StringProperty createTimeProperty() {
        return createTime;
    }

    public StringProperty finishTimeProperty() {
        return finishTime;
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public StringProperty saveFilenameProperty() {
        return saveFilename;
    }

    public void openFile() {
        final File file = new File(filepath.get());
        String[] command;
        if (OSUtil.IS_WINDOWS) {
            command = new String[]{"explorer.exe", "/select," + file.getAbsolutePath()};
        } else if (OSUtil.IS_MAC) {
            command = new String[]{"open", file.getParent()};
        } else if (OSUtil.IS_LINUX) {
            command = new String[]{"xdg-open", file.getParent()};
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("该操作系统不支持！");
            alert.showAndWait();
            return;
        }
        if (file.exists()) {
            SystemCommandUtil.CommandExecResult commandExecResult =
                    SystemCommandUtil.execWithExitCodeAndResult(command);
            if (commandExecResult.getExitCode() == 0) {
                log.info("已打开 {}", file.getAbsolutePath());
            } else {
                log.warn("打开 {} 可能失败！", file.getAbsolutePath());
            }
        } else {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("文件被移动或者被删除无法打开！");
            alert.show();
        }
    }

    public void playFile() {
        String[] command;
        if (OSUtil.IS_WINDOWS) {
            command = new String[]{"cmd /c start " + filepath.get()};
        } else if (OSUtil.IS_MAC) {
            command = new String[]{"open", filepath.get()};
        } else if (OSUtil.IS_LINUX) {
            command = new String[]{"xdg-open", filepath.get()};
        } else {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("该操作操作系统不支持！");
            alert.showAndWait();
            return;
        }
        try {
            SystemCommandUtil.CommandExecResult commandExecResult =
                    SystemCommandUtil.execWithExitCodeAndResult(command);
            if (commandExecResult.getExitCode() == 0) {
                log.info("已播放 {}", filepath.get());
            } else {
                log.warn("播放 {} 可能失败！", filepath.get());
                throw new RuntimeException("播放失败！");
            }
        } catch (Exception e) {
            log.error("播放失败！", e);
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("播放失败！");
            alert.show();
        }
    }

    public void delete(ObservableList<DownloadTaskHistoryViewModel> items) {
        items.remove(this);
        String filePath = filepath.get();
        if (filePath != null) {
            final File file = new File(filePath);
            if (file.exists()) {
                final boolean delete = file.delete();
                if (delete) {
                    log.info("{} is deleted!", filePath);
                } else {
                    log.warn("Failed to delete file {}", filePath);
                }
            }
        }
        downloadTaskService.deleteById(id.get());
    }

    public static List<DownloadTaskHistoryViewModel> getAllFinishedTask() {
        return downloadTaskService.getAllFinishedTask();
    }
}
