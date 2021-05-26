package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

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
    private final DownloadTaskDao downloadTaskDao = new DownloadTaskDao();
    @FXML
    private TableView<DownloadTaskDomain> downloadHistoryTable;
    @FXML
    private TableColumn<DownloadTaskDomain, Object> idColumn;
    @FXML
    private TableColumn<DownloadTaskDomain, Object> urlColumn;
    @FXML
    private TableColumn<DownloadTaskDomain, Object> createTimeColumn;
    @FXML
    private TableColumn<DownloadTaskDomain, Object> finishTimeColumn;
    @FXML
    private TableColumn<DownloadTaskDomain, Object> durationColumn;
    @FXML
    private TableColumn<DownloadTaskDomain, Object> operaColumn;

    public void init() {
        final List<DownloadTaskDomain> downloadHistoryList = downloadTaskDao.selectByStatus(DownloadTaskStatusEnum.FINISHED.getStatus());
        downloadHistoryTable.setRowFactory(p -> {
            final TableRow<DownloadTaskDomain> objectTableRow = new TableRow<>();
            objectTableRow.setStyle("-fx-pref-height: 50px");
            return objectTableRow;
        });
        downloadHistoryTable.getSortOrder().add(finishTimeColumn);
        Callback<TableColumn<DownloadTaskDomain, Object>,
                TableCell<DownloadTaskDomain, Object>> defaultCellFactory = v -> {
            final TableCell<DownloadTaskDomain, Object> cell = new TableCell<>() {
                @Override
                public void updateItem(Object item, boolean empty) {
                    if (item != null) {
                        setText(item.toString());
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        };

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(defaultCellFactory);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setCellFactory(defaultCellFactory);
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTimeText"));
        createTimeColumn.setCellFactory(defaultCellFactory);
        finishTimeColumn.setCellFactory(defaultCellFactory);
        finishTimeColumn.setCellValueFactory(new PropertyValueFactory<>("finishTimeText"));
        durationColumn.setCellFactory(defaultCellFactory);
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
        this.downloadHistoryTable.getItems().addAll(downloadHistoryList);
        this.downloadHistoryTable.sort();
    }


    private void openFile(int index) {
        final DownloadTaskDomain downloadTaskDomain = this.downloadHistoryTable.getItems().get(index);
        final String filePath = downloadTaskDomain.getFilePath();
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
        final DownloadTaskDomain downloadTaskDomain = this.downloadHistoryTable.getItems().get(index);
        final String filePath = downloadTaskDomain.getFilePath();
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
        final DownloadTaskDomain downloadTaskDomain = this.downloadHistoryTable.getItems().get(index);
        final String filePath = downloadTaskDomain.getFilePath();
        if (filePath != null) {
            final File file = new File(filePath);
            if (file.exists()) {
                final boolean delete = file.delete();
                if(delete){
                    logger.info(filePath+" is deleted!");
                }else {
                    logger.warning("Failed to delete file "+filePath);
                }
            }
        }
        downloadTaskDao.deleteById(downloadTaskDomain.getId());
        downloadHistoryTable.getItems().remove(index);
    }

}
