package com.github.cloudgyb.m3u8downloader.viewcontroller.cell;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.model.ProgressAndStatus;
import com.github.cloudgyb.m3u8downloader.viewcontroller.BootstrapStyle;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * 任务下载列表视图操作列表格
 *
 * @author cloudgyb
 * @since 2025/8/16 17:55
 */
public class DownloadTaskOperateColumnTableCell extends TableCell<DownloadTaskViewModel, ProgressAndStatus> {
    private final static String switchBtnStyle = BootstrapStyle.btnXsStyle;
    private final static String deleteBtnStyle = BootstrapStyle.btnDangerStyle + BootstrapStyle.btnXsStyle;
    private final ToggleButton switchBtn = new ToggleButton("停止");
    private final ToggleButton delBtn = new ToggleButton("删除");

    public DownloadTaskOperateColumnTableCell() {
        switchBtn.setStyle(switchBtnStyle);
        delBtn.setStyle(deleteBtnStyle);
        delBtn.setTooltip(new Tooltip("删除下载任务"));
        switchBtn.setOnMouseClicked(event -> switchDownloadStatus(getIndex()));
        delBtn.setOnMouseClicked(event -> deleteDownload(getIndex()));
    }

    public static Callback<TableColumn<DownloadTaskViewModel, ProgressAndStatus>,
            TableCell<DownloadTaskViewModel, ProgressAndStatus>> cellFactory() {
        return column -> new DownloadTaskOperateColumnTableCell();
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
            if (status == DownloadTaskStatusEnum.NEW) {
                style = style + BootstrapStyle.btnPrimaryStyle;
                text = "开始";
            } else if (DownloadTaskStatusEnum.isRunning(status)) {
                style = style + BootstrapStyle.btnWarningStyle;
                text = "停止";
            } else if (status == DownloadTaskStatusEnum.FINISHED) {
                getTableView().getItems().remove(getIndex());
                getTableView().refresh();
            } else if (DownloadTaskStatusEnum.isFailed(status)) {
                style = style + BootstrapStyle.btnPrimaryStyle;
                text = "重试";
            } else {
                style = style + BootstrapStyle.btnPrimaryStyle;
                text = "继续";
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

    private void switchDownloadStatus(int index) {
        DownloadTaskViewModel downloadTaskViewModel = getTableView().getItems().get(index);
        DownloadTaskStatusEnum status = downloadTaskViewModel.getStatus();
        if (DownloadTaskStatusEnum.isRunning(status)) {
            downloadTaskViewModel.progressAndStatusProperty()
                    .set(new ProgressAndStatus(DownloadTaskStatusEnum.STOPPED, null,
                            downloadTaskViewModel.getProgressAndStatus().getStage()));
            downloadTaskViewModel.stop();
        } else {
            downloadTaskViewModel.start();
        }
    }

    public void deleteDownload(int index) {
        TableView<DownloadTaskViewModel> tableView = getTableView();
        final DownloadTaskViewModel downloadTaskViewModel = tableView.getItems().get(index);
        downloadTaskViewModel.remove();
        tableView.getItems().remove(index);
        tableView.refresh();
    }

}