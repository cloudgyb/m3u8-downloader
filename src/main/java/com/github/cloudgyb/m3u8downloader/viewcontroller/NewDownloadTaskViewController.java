package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationContext;
import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.util.Optional;

/**
 * 新建下载任务视图控制器
 *
 * @author cloudgyb
 * 2021/5/16 18:15
 */
public class NewDownloadTaskViewController {
    @FXML
    private TextField urlTextField;
    @FXML
    private CheckBox showAdvOptionCheckBox;
    @FXML
    private VBox advOptionHBox;
    @FXML
    private Slider threadCountSlider;
    @FXML
    private TextField saveFilename;
    private volatile static boolean isShowAdvOption = false;

    private final NewDownloadTaskViewModel viewModel = new NewDownloadTaskViewModel();

    public void init() {
        threadCountSlider.valueProperty().bindBidirectional(viewModel.taskMaxThreadCountProperty());
        urlTextField.textProperty().bindBidirectional(viewModel.urlProperty());
        saveFilename.textProperty().bindBidirectional(viewModel.filenameProperty());
        this.threadCountSlider.setValue(ApplicationStore.getSystemConfig().getDefaultThreadCount());
        this.advOptionHBox.setVisible(isShowAdvOption);
        this.showAdvOptionCheckBox.setSelected(isShowAdvOption);
    }

    public void downBtnClick() {
        String url = urlTextField.getText();
        boolean is = validateUrl(url);
        if (!is)
            return;
        viewModel.download();
        final TabPane tabs = (TabPane) ApplicationContext.getInstance().get("tabs");
        tabs.getSelectionModel().select(1);
    }

    private boolean validateUrl(String url) {
        if (url == null || "".equals(url.trim())) {
            showAlert("请输入URL！");
            return false;
        }
        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            String path = uri.getPath();
            if ((!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) || !path.endsWith(".m3u8")) {
                showAlert("输入的 URL 不是一个正确的 m3u8 地址！");
                return false;
            }
        } catch (Exception ignore) {
            showAlert("输入的URL不合法！");
            return false;
        }
        return true;
    }

    public void showAdvOption() {
        final boolean selected = showAdvOptionCheckBox.isSelected();
        advOptionHBox.setVisible(selected);
        isShowAdvOption = selected;
    }

    private void showAlert(String msg) {
        final Alert urlErrAlert = new Alert(Alert.AlertType.WARNING);
        Image icon = new Image("/icon.png");
        Stage stage = (Stage) urlErrAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon); // 设置图标
        urlErrAlert.setHeaderText(msg);
        //urlErrAlert.setContentText(msg);
        final Optional<ButtonType> buttonType = urlErrAlert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            urlErrAlert.close();
        }
    }

}
