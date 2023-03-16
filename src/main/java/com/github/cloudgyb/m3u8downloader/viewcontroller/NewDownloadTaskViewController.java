package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationContext;
import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * 新建下载任务视图控制器
 *
 * @author cloudgyb
 * 2021/5/16 18:15
 */
public class NewDownloadTaskViewController {
    private final static String urlPattern = "^(http|https)://[A-Za-z0-9-_/.]+\\.m3u8$";
    private DownloadTaskDao taskDao;
    @FXML
    private TextField urlTextField;
    @FXML
    private CheckBox showAdvOptionCheckBox;
    @FXML
    private HBox advOptionHBox;
    @FXML
    private Slider threadCountSlider;

    public void init() {
        urlTextField.textProperty().bindBidirectional();
        this.taskDao = new DownloadTaskDao();
        this.threadCountSlider.setValue(ApplicationStore.getSystemConfig().getDefaultThreadCount());
    }

    public void downBtnClick() {
        String url = urlTextField.getText();
        boolean is = validateUrl(url);
        if (!is)
            return;
        final int threadCount = (int) threadCountSlider.getValue();
        final DownloadTaskDomain domain = new DownloadTaskDomain();
        domain.setUrl(url);
        domain.setUrlFinished(0);
        domain.setStatus(0);
        domain.setProgress(0);
        domain.setDuration(0);
        domain.setCreateTime(new Date());
        domain.setThreadCount(threadCount);
        taskDao.insert(domain);

        final DownloadTaskViewModel task = new DownloadTaskViewModel(domain);
        try {
            task.start();
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        ApplicationStore.getNoFinishedTasks().add(task);
        final TabPane tabs = (TabPane) ApplicationContext.getInstance().get("tabs");
        tabs.getSelectionModel().select(1);
    }

    private boolean validateUrl(String url) {
        if (url == null || "".equals(url.trim())) {
            showAlert("请输入URL！");
            return false;
        }
        final boolean matches = url.matches(urlPattern);
        if (!matches) {
            showAlert("输入的URL不合法或者不支持！");
            return false;
        }
        return true;
    }

    public void showAdvOption() {
        final boolean selected = showAdvOptionCheckBox.isSelected();
        advOptionHBox.setVisible(selected);
    }

    private void showAlert(String msg) {
        final Alert urlErrAlert = new Alert(Alert.AlertType.WARNING);
        urlErrAlert.setHeaderText(msg);
        //urlErrAlert.setContentText(msg);
        final Optional<ButtonType> buttonType = urlErrAlert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            urlErrAlert.close();
        }
    }

}
