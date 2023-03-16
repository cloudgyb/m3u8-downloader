package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private TableColumn<DownloadTaskViewModel, Object> idColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, Object> createTimeColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, Object> urlColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, Object> progressColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, Object> durationColumn;
    @FXML
    private TableColumn<DownloadTaskViewModel, Object> operaColumn;
    private final DownloadTaskDao taskDao;

    public DownloadListViewController() {
        this.taskDao = new DownloadTaskDao();
    }

    public void init() {
        downloadTable.setRowFactory(p -> {
            final TableRow<DownloadTaskViewModel> objectTableRow = new TableRow<>();
            objectTableRow.setStyle("-fx-pref-height: 50px");
            return objectTableRow;
        });
        downloadTable.getSortOrder().add(createTimeColumn);

        Callback<TableColumn<DownloadTaskViewModel, Object>, TableCell<DownloadTaskViewModel, Object>> defaultCellFactory
                = downloadTaskStringTableColumn -> {
            final TableCell<DownloadTaskViewModel, Object> cell = new TableCell<>() {
                @Override
                public void updateItem(Object item, boolean empty) {
                    if (item != null) {
                        setText(item.toString());
                    } else {
                        setText(null);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        };
        idColumn.setCellFactory(defaultCellFactory);
        idColumn.setCellValueFactory((new PropertyValueFactory<>("id")));
        createTimeColumn.setCellFactory(defaultCellFactory);
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));
        urlColumn.setCellFactory(defaultCellFactory);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));

        progressColumn.setCellFactory(cell -> {
            TableCell<DownloadTaskViewModel, Object> c1 = new TableCell<>() {
                private final ProgressBar progressBar = new ProgressBar(0.0);
                private final Label label = new Label();
                private final StackPane pane = new StackPane(progressBar, label);

                @Override
                protected void updateItem(Object value, boolean empty) {
                    super.updateItem(value, empty);
                    setGraphic(null);
                    if (value != null) {
                        final String s = value.toString();
                        if (s.endsWith("%")) { //是进度值更新
                            double progress = Double.parseDouble(s.substring(0, s.length() - 1));
                            progressBar.setProgress(progress / 100);
                        }
                        final int index = getIndex();
                        final DownloadTaskViewModel downloadTaskViewModel = downloadTable.getItems().get(index);
                        final Integer status = downloadTaskViewModel.getStatus();
                        if(status==DownloadTaskStatusEnum.RUNNING.getStatus() || !s.endsWith("%"))
                            label.setText(s);
                        setStyleByTaskStatus(label, status);
                        setGraphic(pane);
                    }
                }
            };
            c1.setAlignment(Pos.CENTER);
            return c1;
        });
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("statusText"));
        durationColumn.setCellFactory(defaultCellFactory);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        Callback<TableColumn<DownloadTaskViewModel, Object>, TableCell<DownloadTaskViewModel, Object>> operaColumnCellFactory = new Callback<>() {
            @Override
            public TableCell<DownloadTaskViewModel, Object> call(TableColumn param) {
                return new TableCell<>() {
                    private final ToggleButton switchBtn = new ToggleButton("停止");
                    private final ToggleButton delBtn = new ToggleButton("删除");

                    {
                        switchBtn.setStyle(switchBtnStyle);
                        delBtn.setStyle(deleteBtnStyle);
                        delBtn.setTooltip(new Tooltip("删除下载任务"));
                        switchBtn.setOnMouseClicked(event -> switchDownloadStatus(getIndex()));
                        delBtn.setOnMouseClicked(event -> deleteDownload(getIndex()));
                    }

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            int status = (Integer) item;
                            String text = "停止";
                            String style = BootstrapStyle.btnXsStyle;
                            if (status == DownloadTaskStatusEnum.NEW.getStatus() ||
                                    status == DownloadTaskStatusEnum.STOPPED.getStatus()) {
                                style = style + BootstrapStyle.btnPrimaryStyle;
                                text = "开始";
                            } else if (status == DownloadTaskStatusEnum.RUNNING.getStatus()) {
                                style = style + BootstrapStyle.btnWarningStyle;
                                text = "停止";
                            } else if (status == DownloadTaskStatusEnum.FAILED.getStatus()) {
                                style = style + BootstrapStyle.btnPrimaryStyle;
                                text = "重试";
                            } else if (status == DownloadTaskStatusEnum.FINISHED.getStatus()) {
                                downloadTable.getItems().remove(getIndex());
                                downloadTable.refresh();
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
                };
            }
        };
        operaColumn.setCellFactory(operaColumnCellFactory);
        operaColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        List<DownloadTaskViewModel> notFinishTasks = ApplicationStore.getNoFinishedTasks();
        downloadTable.getItems().addAll(notFinishTasks);
        downloadTable.sort();
        System.out.println("下载列表 init ");
    }

    private void setStyleByTaskStatus(Label label, Integer status) {
        String style = "";
        if (status == DownloadTaskStatusEnum.NEW.getStatus() ||
                status == DownloadTaskStatusEnum.STOPPED.getStatus()) {
            style = style + BootstrapStyle.textInfo;
        } else if (status == DownloadTaskStatusEnum.RUNNING.getStatus() ||
                status == DownloadTaskStatusEnum.FINISHED.getStatus()) {
            style = style + BootstrapStyle.textSuccess;
        } else if (status == DownloadTaskStatusEnum.FAILED.getStatus()) {
            style = style + BootstrapStyle.textDanger;
        }
        label.setStyle(style);
    }

    private void switchDownloadStatus(int index) {
        final DownloadTaskViewModel downloadTaskViewModel = this.downloadTable.getItems().get(index);
        final Integer status = downloadTaskViewModel.getStatus();
        if (status == DownloadTaskStatusEnum.NEW.getStatus() ||
                status == DownloadTaskStatusEnum.STOPPED.getStatus() ||
                status == DownloadTaskStatusEnum.FAILED.getStatus()) {
            try {
                downloadTaskViewModel.start();
            } catch (IOException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (status == DownloadTaskStatusEnum.RUNNING.getStatus())
            downloadTaskViewModel.stop();

    }

    public void deleteDownload(int index) {
        final DownloadTaskViewModel downloadTaskViewModel = this.downloadTable.getItems().get(index);
        downloadTaskViewModel.remove();
        taskDao.deleteById(downloadTaskViewModel.getId());
        downloadTable.getItems().remove(index);
    }
}
