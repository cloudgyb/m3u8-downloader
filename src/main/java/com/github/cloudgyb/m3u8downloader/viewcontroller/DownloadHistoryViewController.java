package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.util.SpringBeanUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * 下载历史tab页视图控制器
 *
 * @author cloudgyb
 * 2021/5/19 10:25
 */
public class DownloadHistoryViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
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
    private final Callback<TableView<DownloadTaskEntity>, TableRow<DownloadTaskEntity>> rowFactory = p -> {
        final TableRow<DownloadTaskEntity> tableRow = new TableRow<>();
        tableRow.setStyle("-fx-pref-height: 50px");
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyUrlMenuItem = new MenuItem("复制url");
        copyUrlMenuItem.setOnAction(event -> {
            DownloadTaskEntity item = tableRow.getItem();
            String url = item.getUrl();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            // 创建一个包含字符串数据的剪切板内容
            ClipboardContent content = new ClipboardContent();
            content.putString(url);
            // 将内容设置到剪贴板
            clipboard.setContent(content);
            logger.info("复制{}到剪贴板", url);
        });
        contextMenu.getItems().add(copyUrlMenuItem);
        tableRow.setContextMenu(contextMenu);
        return tableRow;
    };
    private final Callback<TableColumn<DownloadTaskEntity, Object>, TableCell<DownloadTaskEntity, Object>> cellFactory = column -> new TableCell<>() {
        @Override
        protected void updateItem(Object o, boolean b) {
            super.updateItem(o, b);
            if (o != null) {
                setText(o.toString());
                setTooltip(new Tooltip(o.toString()));
            }
        }
    };

    public void init() {
        List<DownloadTaskEntity> allFinishedTask = downloadTaskService.getAllFinishedTask();
        downloadHistoryTable.setRowFactory(rowFactory);
        downloadHistoryTable.getSortOrder().add(finishTimeColumn);
        // 绑定属性
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        urlColumn.setCellFactory(cellFactory);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        createTimeColumn.setCellFactory(cellFactory);
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTimeText"));
        finishTimeColumn.setCellFactory(cellFactory);
        finishTimeColumn.setCellValueFactory(new PropertyValueFactory<>("finishTimeText"));
        durationColumn.setCellFactory(cellFactory);
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
                    logger.warn("Failed to delete file " + filePath);
                }
            }
        }
        downloadTaskService.deleteById(task.getId());
        downloadHistoryTable.getItems().remove(index);
        downloadHistoryTable.refresh();
    }

}
