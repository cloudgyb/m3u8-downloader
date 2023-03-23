package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.model.ProgressAndStatus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.List;

/**
 * 下载列表tab页视图控制器
 *
 * @author cloudgyb
 * 2021/5/16 18:32
 */
public class DownloadListViewController {
    private final static String switchBtnStyle = BootstrapStyle.btnXsStyle;
    private final static String deleteBtnStyle = BootstrapStyle.btnDangerStyle + BootstrapStyle.btnXsStyle;
    @FXML
    private TableView<DownloadTaskViewModel> downloadTable;
    @FXML
    private TableColumn<DownloadTaskViewModel, Integer> idColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, String> createTimeColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, String> urlColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, ProgressAndStatus> progressColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, Object> rateColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, ProgressAndStatus> operaColumn;

    public void init() {
        // 设置 table 行样式
        downloadTable.setRowFactory(p -> {
            final TableRow<DownloadTaskViewModel> objectTableRow = new TableRow<>();
            objectTableRow.setStyle("-fx-pref-height: 50px");
            return objectTableRow;
        });
        downloadTable.getSortOrder().add(createTimeColumn);

        // 设置模型属性关联
        idColumn.setCellValueFactory((new PropertyValueFactory<>("id")));
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        progressColumn.setCellFactory(cell -> {
            TableCell<DownloadTaskViewModel, ProgressAndStatus> c1 = new ProgressBarWithLabelTableCell();
            c1.setAlignment(Pos.CENTER);
            return c1;
        });
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progressAndStatus"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        operaColumn.setCellFactory(cell -> new OperateColumnTableCell());
        operaColumn.setCellValueFactory(new PropertyValueFactory<>("progressAndStatus"));

        // 添加数据到 table
        List<DownloadTaskViewModel> notFinishTasks = ApplicationStore.getNoFinishedTasks();
        downloadTable.getItems().addAll(notFinishTasks);
        downloadTable.sort();
    }

    private void setStyleByTaskStatus(Label label, DownloadTaskStageEnum status) {
        String style = "";
        if (status == DownloadTaskStageEnum.NEW) {
            style = style + BootstrapStyle.textInfo;
        } else if (status == DownloadTaskStageEnum.M3U8_PARSING ||
                status == DownloadTaskStageEnum.DOWNLOAD_FINISHED ||
                status == DownloadTaskStageEnum.M3U8_PARSED) {
            style = style + BootstrapStyle.textSuccess;
        } else if (status == DownloadTaskStageEnum.M3U8_PARSE_FAILED ||
                status == DownloadTaskStageEnum.DOWNLOAD_FAILED) {
            style = style + BootstrapStyle.textDanger;
        }
        label.setStyle(style);
    }

    private void switchDownloadStatus(int index) {
        DownloadTaskViewModel downloadTaskViewModel = this.downloadTable.getItems().get(index);
        DownloadTaskStatusEnum status = downloadTaskViewModel.getStatus();
        if (status == DownloadTaskStatusEnum.RUNNING) {
            downloadTaskViewModel.progressAndStatusProperty()
                    .set(new ProgressAndStatus(DownloadTaskStatusEnum.STOPPED_MANUAL, null,
                            downloadTaskViewModel.getProgressAndStatus().getStage()));
            downloadTaskViewModel.stop();
        } else {
            downloadTaskViewModel.start();
        }
    }

    public void deleteDownload(int index) {
        final DownloadTaskViewModel downloadTaskViewModel = this.downloadTable.getItems().get(index);
        downloadTaskViewModel.remove();
        downloadTable.getItems().remove(index);
    }

    class ProgressBarWithLabelTableCell extends TableCell<DownloadTaskViewModel, ProgressAndStatus> {
        private final ProgressBar progressBar = new ProgressBar(0.0);
        private final Label label = new Label();
        private final StackPane pane = new StackPane(progressBar, label);

        /**
         * 根据任务的进度和状态做出 ui 的变化
         */
        @Override
        protected void updateItem(ProgressAndStatus progressAndStatus, boolean empty) {
            super.updateItem(progressAndStatus, empty);
            setGraphic(null);
            if (progressAndStatus != null) {
                Double progressValue = progressAndStatus.getProgress();
                if (progressValue != null) {
                    progressBar.setProgress(progressValue);
                }
                DownloadTaskStageEnum stage = progressAndStatus.getStage();
                if (stage != null) {
                    String labelText = stage.getStatus();
                    if (stage == DownloadTaskStageEnum.DOWNLOADING && progressValue != null) {
                        String s = progressValue * 100 + "";
                        labelText = (s.length() > 5 ? s.substring(0, 5) : s) + "%";
                    }
                    label.setText(labelText);
                    setStyleByTaskStatus(label, stage);
                }
                DownloadTaskStatusEnum status = progressAndStatus.getStatus();
                if (status == DownloadTaskStatusEnum.STOPPED_MANUAL) {
                    label.setText(status.getStatus());
                    setStyleByTaskStatus(label, stage);
                }
                setGraphic(pane);
            }
        }
    }

    class OperateColumnTableCell extends TableCell<DownloadTaskViewModel, ProgressAndStatus> {
        private final ToggleButton switchBtn = new ToggleButton("停止");
        private final ToggleButton delBtn = new ToggleButton("删除");

        {
            switchBtn.setStyle(switchBtnStyle);
            delBtn.setStyle(deleteBtnStyle);
            delBtn.setTooltip(new Tooltip("删除下载任务"));
            switchBtn.setOnMouseClicked(event -> switchDownloadStatus(getIndex()));
            delBtn.setOnMouseClicked(event -> deleteDownload(getIndex()));
        }

        /**
         * 根据状态变更更新操作按钮文本及样式
         */
        @Override
        protected void updateItem(ProgressAndStatus progressAndStatus, boolean empty) {
            super.updateItem(progressAndStatus, empty);
            if (empty) {
                setGraphic(null);
            } else if (progressAndStatus != null && progressAndStatus.getStatus() != null) {
                DownloadTaskStatusEnum status = progressAndStatus.getStatus();
                String text = "停止";
                String style = BootstrapStyle.btnXsStyle;
                if (status == DownloadTaskStatusEnum.NEW || status == DownloadTaskStatusEnum.STOPPED_MANUAL) {
                    style = style + BootstrapStyle.btnPrimaryStyle;
                    text = "开始";
                } else if (status == DownloadTaskStatusEnum.RUNNING) {
                    style = style + BootstrapStyle.btnWarningStyle;
                    text = "停止";
                } else if (status == DownloadTaskStatusEnum.FINISHED) {
                    downloadTable.getItems().remove(getIndex());
                    downloadTable.refresh();
                } else {
                    style = style + BootstrapStyle.btnPrimaryStyle;
                    text = "重试";
                }
                switchBtn.setText(text);
                switchBtn.setStyle(style);
                switchBtn.setTooltip(new Tooltip("点击" + text));
                HBox hBox = new HBox(switchBtn, delBtn);
                hBox.setSpacing(5);
                hBox.setAlignment(Pos.CENTER);
                setGraphic(hBox);
            }
        }

    }
}
