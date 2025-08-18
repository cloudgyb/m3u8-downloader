package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.conf.ProxyConfig;
import com.github.cloudgyb.m3u8downloader.domain.dao.SystemConfigDao;
import com.github.cloudgyb.m3u8downloader.domain.entity.SystemConfig;
import com.github.cloudgyb.m3u8downloader.util.HttpClientUtil;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 下载设置页面视图控制器
 *
 * @author cloudgyb
 * 2021/5/19 10:24
 */
public class DownloadSettingViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private SystemConfigDao configDao;
    private final SystemConfig systemConfig = ApplicationStore.getSystemConfig();
    @FXML
    public StackPane stackPane;
    @FXML
    private TextField downDirField;
    @FXML
    private Slider downThreadCountSlider;
    public CheckBox proxyEnableCheckBox;
    public ChoiceBox<Proxy.Type> proxyTypeChoiceBox;
    public TextField proxyHostTextField;
    public TextField proxyPortTextField;
    public TextField proxyUsernameTextField;
    public PasswordField proxyPasswordTextField;

    public void initialize() {
        configDao = new SystemConfigDao();
        downDirField.setText(systemConfig.getDownloadDir());
        downThreadCountSlider.setValue(systemConfig.getDefaultThreadCount());
        downThreadCountSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            int oldV = oldValue.intValue();
            int newV = newValue.intValue();
            if (oldV != newV)
                changeDefaultThreadCount();
        });
        proxyTypeChoiceBox.getItems().addAll(
                Arrays.stream(Proxy.Type.values()).filter(type -> type != Proxy.Type.DIRECT)
                        .collect(Collectors.toList()));
        proxyPortTextField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            try {
                int port = Integer.parseInt(newText);
                if (port < 0 || port > 65535) {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null; // 拒绝非法输入
            }
            return change; // 拒绝非法输入
        }));

        ChangeListener<Boolean> proxyConfigChangeListener = (observable, oldValue, newValue) -> {
            if (!newValue) { // 失去焦点
                proxyConfigChange();
            }
        };
        proxyTypeChoiceBox.setOnAction(event1 -> {
            proxyConfigChange();
            event1.consume();
        });
        proxyHostTextField.focusedProperty().addListener(proxyConfigChangeListener);
        proxyPortTextField.focusedProperty().addListener(proxyConfigChangeListener);
        proxyUsernameTextField.focusedProperty().addListener(proxyConfigChangeListener);
        proxyPasswordTextField.focusedProperty().addListener(proxyConfigChangeListener);
        proxyEnableCheckBox.setOnAction(event1 -> {
            proxyConfigChange();
            event1.consume();
        });
        stackPane.addEventHandler(Tab.SELECTION_CHANGED_EVENT, event -> {
            if (event.getTarget() == stackPane) {
                event.consume();
                if (logger.isDebugEnabled()) {
                    logger.debug("切换到: 设置 Tab");
                }
                downThreadCountSlider.setValue(systemConfig.getDefaultThreadCount());
                // 千万不要调换下面字段的初始化顺序
                proxyHostTextField.setText(systemConfig.getProxyConfig().getProxyHost());
                proxyPortTextField.setText(systemConfig.getProxyConfig().getProxyPort() + "");
                proxyUsernameTextField.setText(systemConfig.getProxyConfig().getProxyUsername());
                proxyPasswordTextField.setText(systemConfig.getProxyConfig().getProxyPassword());
                proxyEnableCheckBox.setSelected(systemConfig.getProxyConfig().isProxyEnabled());
                proxyTypeChoiceBox.setValue(systemConfig.getProxyConfig().getProxyType());
            }
        });


    }

    public void changeDownDirLinkClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        final String downloadDir = systemConfig.getDownloadDir();
        directoryChooser.setInitialDirectory(new File(downloadDir));
        directoryChooser.setTitle("选择下载目录");
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory == null) //点击了取消
            return;
        System.out.println(selectedDirectory.getAbsolutePath());
        if (!selectedDirectory.canWrite()) {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(selectedDirectory.getAbsolutePath() + "不可写！");
            alert.show();
            changeDownDirLinkClick();
        } else {
            downDirField.setText(selectedDirectory.getAbsolutePath());
            systemConfig.setDownloadDir(selectedDirectory.getAbsolutePath());
            configDao.update(systemConfig);
        }

    }

    public void changeDefaultThreadCount() {
        final double value = this.downThreadCountSlider.getValue();
        systemConfig.setDefaultThreadCount((int) value);
        configDao.update(systemConfig);
    }

    public void proxyConfigChange() {
        ProxyConfig proxyConfig = systemConfig.getProxyConfig();
        // 配置是否发生改变
        boolean isNeedUpdate = proxyConfig.isProxyEnabled() != proxyEnableCheckBox.isSelected() ||
                proxyConfig.getProxyType() != proxyTypeChoiceBox.getValue() ||
                !proxyConfig.getProxyHost().equals(proxyHostTextField.getText()) ||
                !(proxyConfig.getProxyPort() + "").equals(proxyPortTextField.getText());
        boolean isNeedProxyAuthUpdate =  !proxyConfig.getProxyUsername().equals(proxyUsernameTextField.getText()) ||
                !proxyConfig.getProxyPassword().equals(proxyPasswordTextField.getText());
        proxyConfig.setProxyType(proxyTypeChoiceBox.getValue());
        proxyConfig.setProxyHost(proxyHostTextField.getText());
        proxyConfig.setProxyPort(Integer.parseInt(
                Objects.equals(proxyPortTextField.getText(), "") ? "0" : proxyPortTextField.getText()));
        proxyConfig.setProxyUsername(proxyUsernameTextField.getText());
        proxyConfig.setProxyPassword(proxyPasswordTextField.getText());
        if (proxyEnableCheckBox.isSelected() && validateProxyConfig()) {
            proxyConfig.setProxyEnabled(true);
            configDao.update(systemConfig);
        } else {
            proxyEnableCheckBox.setSelected(false);
            proxyConfig.setProxyEnabled(false);
            configDao.update(systemConfig);
        }
        if(isNeedProxyAuthUpdate) {
            HttpClientUtil.proxyAuthUpdate(proxyConfig);
        }
        if (isNeedUpdate) {
            HttpClientUtil.proxyConfigUpdate(proxyConfig);
        }
    }

    @SuppressWarnings("all")
    private boolean validateProxyConfig() {
        String proxyHost = proxyHostTextField.getText();
        String proxyPort = proxyPortTextField.getText();
        String regx = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|" +
                "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::(?:[0-9a-fA-F]{1,4}:){0,6}[0-9a-fA-F]{1,4}$|" +
                "^[0-9a-fA-F]{1,4}::(?:[0-9a-fA-F]{1,4}:){0,5}[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){1,2}:(?:[0-9a-fA-F]{1,4}:){0,4}[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){1,3}:(?:[0-9a-fA-F]{1,4}:){0,3}[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){1,4}:(?:[0-9a-fA-F]{1,4}:){0,2}[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){1,5}:(?:[0-9a-fA-F]{1,4}:)?[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}$|^::(?:[0-9a-fA-F]{1,4}:){0,6}[0-9a-fA-F]{1,4}$|" +
                "^[0-9a-fA-F]{1,4}::(?:[0-9a-fA-F]{1,4}:){0,5}[0-9a-fA-F]{1,4}$|" +
                "^(?:[0-9a-fA-F]{1,4}:){1,2}:(?:[0-9a-fA-F]{1,4}:){0,4}[0-9a-fA-F]{1,4}$|" +
                "^(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$";
        boolean matches = Pattern.matches(regx, proxyHost);
        if (!matches) {
            Alerts.alert("代理主机格式错误", "提示", "请输入正确的代理主机！");
            logger.error("代理主机格式错误: {}", proxyHost);
            return false;
        }

        try {
            int port = Integer.parseInt(proxyPort);
            if (port <= 0 || port > 65535) {
                logger.error("代理端口范围错误: {}", proxyPort);
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            logger.error("代理端口格式错误: {}", proxyPort);
            Alerts.alert("代理端口号格式错误", "提示", "请输入正确的代理端口号！");
            return false;
        }
        return true;
    }
}
