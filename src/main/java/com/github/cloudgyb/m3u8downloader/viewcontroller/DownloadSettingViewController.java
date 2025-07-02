package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.entity.SystemConfig;
import com.github.cloudgyb.m3u8downloader.domain.dao.SystemConfigDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * 下载设置页面视图控制器
 *
 * @author cloudgyb
 * 2021/5/19 10:24
 */
public class DownloadSettingViewController {
    private SystemConfigDao configDao;
    private final SystemConfig systemConfig = ApplicationStore.getSystemConfig();
    @FXML
    private TextField downDirField;

    @FXML
    private Hyperlink changeDownDirLink;

    @FXML
    private Slider downThreadCountSlider;
    
    public void init() {
        configDao = new SystemConfigDao();
        downDirField.setText(systemConfig.getDownloadDir());
        downThreadCountSlider.setValue(systemConfig.getDefaultThreadCount());
        downThreadCountSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            int oldV = oldValue.intValue();
            int newV = newValue.intValue();
            if (oldV != newV)
                changeDefaultThreadCount();
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


}
