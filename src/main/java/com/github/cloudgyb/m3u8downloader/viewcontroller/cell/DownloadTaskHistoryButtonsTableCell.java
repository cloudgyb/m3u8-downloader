package com.github.cloudgyb.m3u8downloader.viewcontroller.cell;

import com.github.cloudgyb.m3u8downloader.model.DownloadTaskHistoryViewModel;
import com.github.cloudgyb.m3u8downloader.viewcontroller.BootstrapStyle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * 下载历史记录操作列按钮组 table cell
 *
 * @author cloudgyb
 * @since 2025/7/11 15:58
 */
public class DownloadTaskHistoryButtonsTableCell extends TableCell<DownloadTaskHistoryViewModel, Integer> {
    private final Button openFileBtn = new Button("打开文件");
    private final Button playBtn = new Button("播放");
    private final Button delBtn = new Button("删除");

    public DownloadTaskHistoryButtonsTableCell() {
        openFileBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnPrimaryStyle);
        openFileBtn.setOnMouseClicked(e -> getTableView().getItems().get(getIndex()).openFile());
        playBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnSuccessStyle);
        playBtn.setOnMouseClicked(e -> getTableView().getItems().get(getIndex()).playFile());
        delBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnDangerStyle);
        delBtn.setOnMouseClicked(e -> getTableView().getItems().get(getIndex()).delete(getTableView().getItems()));
    }

    public static Callback<TableColumn<DownloadTaskHistoryViewModel, Integer>,
            TableCell<DownloadTaskHistoryViewModel, Integer>> cellFactory() {
        return column1 -> new DownloadTaskHistoryButtonsTableCell();
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        if (empty) {
            setGraphic(null);
        } else {
            HBox hBox = new HBox(openFileBtn, playBtn, delBtn);
            hBox.setSpacing(5);
            hBox.setAlignment(Pos.CENTER);
            setGraphic(hBox);
        }
    }
}
