package com.github.cloudgyb.m3u8downloader.viewcontroller.cell;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.model.ProgressAndStatus;
import com.github.cloudgyb.m3u8downloader.viewcontroller.BootstrapStyle;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.text.DecimalFormat;

/**
 * 带进度条和 Label 的表格 Cell
 *
 * @author cloudgyb
 * @since 2025/8/16 18:06
 */
public class ProgressBarWithLabelTableCell extends TableCell<DownloadTaskViewModel, ProgressAndStatus> {
    private final ProgressBar progressBar = new ProgressBar(0.0);
    private final Label label = new Label();
    private final StackPane pane = new StackPane(progressBar, label);

    public ProgressBarWithLabelTableCell() {
        setAlignment(Pos.CENTER);
    }

    public static Callback<TableColumn<DownloadTaskViewModel, ProgressAndStatus>,
            TableCell<DownloadTaskViewModel, ProgressAndStatus>> cellFactory() {
        return column -> new ProgressBarWithLabelTableCell();
    }

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
            DownloadTaskStatusEnum statusEnum = progressAndStatus.getStatus();
            if (statusEnum != null) {
                String labelText = statusEnum.getStatus();
                if (statusEnum == DownloadTaskStatusEnum.DOWNLOADING && progressValue != null) {
                    DecimalFormat decimalFormat = new DecimalFormat("##.0%");
                    labelText = decimalFormat.format(progressValue);
                } else if (statusEnum == DownloadTaskStatusEnum.FINISHED) {
                    TableView<DownloadTaskViewModel> tableView = getTableView();
                    tableView.getItems().remove(getIndex());
                    tableView.refresh();
                }
                label.setText(labelText);
                setStyleByTaskStatus(label, statusEnum);
            }
            setGraphic(pane);
        }
    }

    private void setStyleByTaskStatus(Label label, DownloadTaskStatusEnum status) {
        String style = "";
        if (status == DownloadTaskStatusEnum.NEW) {
            style = style + BootstrapStyle.textInfo;
        } else if (DownloadTaskStatusEnum.isFailed(status)) {
            style = style + BootstrapStyle.textDanger;
        } else {
            style = style + BootstrapStyle.textSuccess;
        }
        label.setStyle(style);
    }

}