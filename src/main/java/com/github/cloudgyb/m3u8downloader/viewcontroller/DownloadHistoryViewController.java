package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.model.DownloadTaskHistoryViewModel;
import com.github.cloudgyb.m3u8downloader.viewcontroller.cell.DownloadTaskHistoryButtonsTableCell;
import com.github.cloudgyb.m3u8downloader.viewcontroller.cell.TooltipTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 下载历史tab页视图控制器
 *
 * @author cloudgyb
 * 2021/5/19 10:25
 */
public class DownloadHistoryViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    public StackPane stackPane;
    @FXML
    private TableView<DownloadTaskHistoryViewModel> downloadHistoryTable;
    @FXML
    public TableColumn<DownloadTaskHistoryViewModel, Boolean> checkColumn;
    @FXML
    private TableColumn<DownloadTaskHistoryViewModel, Integer> idColumn;
    @FXML
    private TableColumn<DownloadTaskHistoryViewModel, String> urlColumn;
    @FXML
    private TableColumn<DownloadTaskHistoryViewModel, String> createTimeColumn;
    @FXML
    private TableColumn<DownloadTaskHistoryViewModel, String> finishTimeColumn;
    @FXML
    private TableColumn<DownloadTaskHistoryViewModel, String> durationColumn;
    @FXML
    private TableColumn<DownloadTaskHistoryViewModel, Integer> operaColumn;
    private final Callback<TableView<DownloadTaskHistoryViewModel>, TableRow<DownloadTaskHistoryViewModel>> rowFactory = p -> {
        final TableRow<DownloadTaskHistoryViewModel> tableRow = new TableRow<>();
        tableRow.setStyle("-fx-pref-height: 50px");
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyUrlMenuItem = new MenuItem("复制url");
        copyUrlMenuItem.setOnAction(event -> {
            DownloadTaskHistoryViewModel item = tableRow.getItem();
            String url = item.urlProperty().get();
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

    public void initialize() {
        downloadHistoryTable.setRowFactory(rowFactory);
        downloadHistoryTable.getSortOrder().add(finishTimeColumn);
        // 绑定属性
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setCellValueFactory(
                row -> row.getValue().checkedProperty()
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        urlColumn.setCellFactory(TooltipTableCell.cellFactory());
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        createTimeColumn.setCellFactory(TooltipTableCell.cellFactory());
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        finishTimeColumn.setCellFactory(TooltipTableCell.cellFactory());
        finishTimeColumn.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        durationColumn.setCellFactory(TooltipTableCell.cellFactory());
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        operaColumn.setCellFactory(DownloadTaskHistoryButtonsTableCell.cellFactory());
        refreshData();
        stackPane.addEventHandler(Tab.SELECTION_CHANGED_EVENT, event -> {
            if (event.getTarget() == stackPane) {
                event.consume();
                if (logger.isDebugEnabled()) {
                    logger.debug("切换到: {}", downloadHistoryTable.getId());
                }
                refreshData();
            }
        });
    }

    private void refreshData() {
        this.downloadHistoryTable.getItems().clear();
        List<DownloadTaskHistoryViewModel> allFinishedTask = DownloadTaskHistoryViewModel.getAllFinishedTask();
        this.downloadHistoryTable.getItems().addAll(allFinishedTask);
    }
}
