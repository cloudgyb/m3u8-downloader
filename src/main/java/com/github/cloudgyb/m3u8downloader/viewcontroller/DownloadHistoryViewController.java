package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.util.SpringBeanUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * 下载历史tab页视图控制器
 *
 * @author cloudgyb
 * 2021/5/19 10:25
 */
public class DownloadHistoryViewController {
    private final Logger logger = Logger.getLogger(DownloadHistoryViewController.class.getSimpleName());
    private final DownloadTaskService downloadTaskService = SpringBeanUtil.getBean(DownloadTaskService.class);
    @FXML
    private TableView<DownloadTaskEntity> downloadHistoryTable;
    @FXML
    private TableColumn<DownloadTaskEntity, Object> idColumn;
    @FXML
    private TableColumn<DownloadTaskEntity, Object> urlColumn;
    @FXML
    private TableColumn<DownloadTaskEntity, Object> createTimeColumn;
    @FXML
    private TableColumn<DownloadTaskEntity, Object> finishTimeColumn;
    @FXML
    private TableColumn<DownloadTaskEntity, Object> durationColumn;
    @FXML
    private TableColumn<DownloadTaskEntity, Object> operaColumn;

    public void init() {
        List<DownloadTaskEntity> allFinishedTask = downloadTaskService.getAllFinishedTask();
        downloadHistoryTable.setRowFactory(p -> {
            final TableRow<DownloadTaskEntity> objectTableRow = new TableRow<>();
            objectTableRow.setStyle("-fx-pref-height: 50px");
            return objectTableRow;
        });
        downloadHistoryTable.getSortOrder().add(finishTimeColumn);
        // 绑定属性
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTimeText"));
        finishTimeColumn.setCellValueFactory(new PropertyValueFactory<>("finishTimeText"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationText"));

        operaColumn.setCellFactory(v -> new TableCell<>() {
            private final Button openFileBtn = new Button("打开文件");
            private final Button playBtn = new Button("播放");
            private final Button delBtn = new Button("删除");

            {
                openFileBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnPrimaryStyle);
                openFileBtn.setOnMouseClicked(e -> openFile(getIndex()));
                playBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnSuccessStyle);
                playBtn.setOnMouseClicked(e -> playFile(getIndex()));
                delBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnDangerStyle);
                delBtn.setOnMouseClicked(e -> deleteFileAndTask(getIndex()));
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(openFileBtn, playBtn, delBtn);
                    hBox.setSpacing(5);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });
        this.downloadHistoryTable.getItems().addAll(allFinishedTask);
        this.downloadHistoryTable.sort();
    }


    private void openFile(int index) {
        final DownloadTaskEntity task = this.downloadHistoryTable.getItems().get(index);
        final String filePath = task.getFilePath();
        final File file = new File(filePath);
        if (file.exists()) {
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + file.getAbsolutePath());
            } catch (Exception ignored) {
            }
        } else {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("文件被移动或者被删除无法打开！");
            alert.show();
        }
    }

    private void playFile(int index) {
        final DownloadTaskEntity task = this.downloadHistoryTable.getItems().get(index);
        final String filePath = task.getFilePath();
        System.out.println("play video path is " + filePath);
        try {
            Runtime.getRuntime().exec("cmd /c start " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("播放失败！");
            alert.show();
        }
    }

    private void deleteFileAndTask(int index) {
        final DownloadTaskEntity task = this.downloadHistoryTable.getItems().get(index);
        final String filePath = task.getFilePath();
        if (filePath != null) {
            final File file = new File(filePath);
            if (file.exists()) {
                final boolean delete = file.delete();
                if (delete) {
                    logger.info(filePath + " is deleted!");
                } else {
                    logger.warning("Failed to delete file " + filePath);
                }
            }
        }
        downloadTaskService.deleteById(task.getId());
        downloadHistoryTable.getItems().remove(index);
    }

}
