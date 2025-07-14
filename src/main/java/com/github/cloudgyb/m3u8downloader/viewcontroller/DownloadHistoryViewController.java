package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.database.Page;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskHistoryViewModel;
import com.github.cloudgyb.m3u8downloader.viewcontroller.cell.DownloadTaskHistoryButtonsTableCell;
import com.github.cloudgyb.m3u8downloader.viewcontroller.cell.TooltipTableCell;
import com.github.cloudgyb.m3u8downloader.viewcontroller.table.DownloadHistoryTableRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.github.cloudgyb.m3u8downloader.viewcontroller.BootstrapStyle.btnDangerStyle;
import static com.github.cloudgyb.m3u8downloader.viewcontroller.BootstrapStyle.btnSmStyle;

/**
 * 下载历史tab页视图控制器
 *
 * @author cloudgyb
 * 2021/5/19 10:25
 */
public class DownloadHistoryViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static int PAGE_SIZE = 20;
    @FXML
    public StackPane stackPane;
    @FXML
    private TableView<DownloadTaskHistoryViewModel> tableView;
    @FXML
    public TableColumn<DownloadTaskHistoryViewModel, Boolean> checkColumn;
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
    /**
     * 删除选中按钮
     */
    public Button deleteSelectedBtn;
    public Pagination pagination;
    public CheckBox selectAllCheckBox;
    // data
    private final ObservableList<DownloadTaskHistoryViewModel> items;

    public DownloadHistoryViewController() {
        items = FXCollections.observableList(new ArrayList<>());
    }

    public void initialize() {
        // 选择所有按钮点击监听
        listenSelectAllCheckBox();
        // 删除选择的按钮点击监听
        listenDeleteSelectedBtnClick();
        deleteSelectedBtn.setStyle(btnSmStyle + btnDangerStyle);
        tableView.setRowFactory(DownloadHistoryTableRow.tableRow());
        // 绑定属性
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setCellValueFactory(
                row -> row.getValue().checkedProperty()
        );
        urlColumn.setCellFactory(TooltipTableCell.cellFactory());
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        createTimeColumn.setCellFactory(TooltipTableCell.cellFactory());
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        finishTimeColumn.setCellFactory(TooltipTableCell.cellFactory());
        finishTimeColumn.setCellValueFactory(new PropertyValueFactory<>("finishTime"));
        durationColumn.setCellFactory(TooltipTableCell.cellFactory());
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        operaColumn.setCellFactory(DownloadTaskHistoryButtonsTableCell.cellFactory());
        tableView.setItems(items);
        stackPane.addEventHandler(Tab.SELECTION_CHANGED_EVENT, event -> {
            if (event.getTarget() == stackPane) {
                event.consume();
                if (logger.isDebugEnabled()) {
                    logger.debug("切换到: {}", tableView.getId());
                }
                getPageData(0);
            }
        });
        pagination.setPageFactory(this::getPageData);
    }

    private void listenDeleteSelectedBtnClick() {
        deleteSelectedBtn.setOnAction(e -> {
            FilteredList<DownloadTaskHistoryViewModel> checkedViewModels =
                    tableView.getItems().filtered(DownloadTaskHistoryViewModel::isChecked);
            if (checkedViewModels.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("请选择要删除的项");
                alert.showAndWait();
                return;
            }

            if (confirmDelete(checkedViewModels.size())) {
                checkedViewModels.forEach(
                        e1 -> e1.delete(tableView.getItems())
                );
                refreshDataIfCurrPageIsEmpty();
            }
        });
    }

    private void listenSelectAllCheckBox() {
        selectAllCheckBox
                .selectedProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                tableView.getItems()
                                        .forEach(
                                                downloadTaskHistoryViewModel -> downloadTaskHistoryViewModel
                                                        .checkedProperty().set(newValue))
                );
    }

    // 确认删除对话框
    private boolean confirmDelete(int count) {
        return Alerts.confirm("确认删除", null, "确定要删除这" + count + "项吗?");
    }

    private void refreshDataIfCurrPageIsEmpty() {
        if (tableView.getItems().isEmpty()) {
            int prePageNumber = pagination.getCurrentPageIndex() - 1;
            int currPageNumber = Math.max(prePageNumber, 0);
            pagination.setCurrentPageIndex(currPageNumber);
            getPageData(currPageNumber);
        }
    }

    /**
     * 注意：pageIndex 从0开始
     *
     * @param pageIndex 页码索引
     * @return 表格table
     */
    public Node getPageData(int pageIndex) {
        logger.info("获取第{}页数据", pageIndex);
        items.clear();
        Page<DownloadTaskHistoryViewModel> page = DownloadTaskHistoryViewModel
                .selectPage(pageIndex, PAGE_SIZE);
        int totalPage = page.getTotalPage();
        pagination.setPageCount(totalPage);
        items.addAll(page);
        return tableView;
    }
}