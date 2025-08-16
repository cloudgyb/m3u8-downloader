package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.model.ProgressAndStatus;
import com.github.cloudgyb.m3u8downloader.viewcontroller.cell.DownloadTaskOperateColumnTableCell;
import com.github.cloudgyb.m3u8downloader.viewcontroller.cell.ProgressBarWithLabelTableCell;
import com.github.cloudgyb.m3u8downloader.viewcontroller.cell.TooltipTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 下载列表tab页视图控制器
 *
 * @author cloudgyb
 * 2021/5/16 18:32
 */
public class DownloadListViewController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadListViewController.class);
    @FXML
    public StackPane stackPane;
    @FXML
    private TableView<DownloadTaskViewModel> downloadTable;
    @FXML
    private TableColumn<DownloadTaskViewModel, Integer> idColumn;
    @FXML
    public TableColumn<DownloadTaskViewModel, String> saveFilenameColumn;
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

    public void initialize() {
        // 设置 table 行样式
        downloadTable.setRowFactory(p -> {
            final TableRow<DownloadTaskViewModel> objectTableRow = new TableRow<>();
            objectTableRow.setStyle("-fx-pref-height: 50px");
            return objectTableRow;
        });
        downloadTable.getSortOrder().add(createTimeColumn);

        // 设置模型属性关联
        idColumn.setCellValueFactory((new PropertyValueFactory<>("id")));
        saveFilenameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        saveFilenameColumn.setCellValueFactory(new PropertyValueFactory<>("saveFilename"));
        saveFilenameColumn.setOnEditCommit(event -> {
            DownloadTaskViewModel viewModel = event.getRowValue();
            String newSaveFilename = event.getNewValue();
            viewModel.updateSaveFileName(newSaveFilename);
        });
        createTimeColumn.setCellFactory(TooltipTableCell.cellFactory());
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        urlColumn.setCellFactory(TooltipTableCell.cellFactory());
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        progressColumn.setCellFactory(ProgressBarWithLabelTableCell.cellFactory());
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progressAndStatus"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        operaColumn.setCellFactory(DownloadTaskOperateColumnTableCell.cellFactory());
        operaColumn.setCellValueFactory(new PropertyValueFactory<>("progressAndStatus"));

        stackPane.addEventHandler(Tab.SELECTION_CHANGED_EVENT, event -> {
            if (event.getTarget() == stackPane) {
                event.consume();
                if (logger.isDebugEnabled()) {
                    logger.debug("切换到: {}", downloadTable.getId());
                }
                refreshData();
            }
        });
        // 添加数据到 table
        refreshData();
    }

    private void refreshData() {
        downloadTable.getItems().clear();
        List<DownloadTaskViewModel> notFinishTasks = ApplicationStore.getNoFinishedTasks();
        downloadTable.getItems().addAll(notFinishTasks);
        downloadTable.sort();
    }

}
