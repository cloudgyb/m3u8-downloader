package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.dao.SystemConfigDao;
import com.github.cloudgyb.m3u8downloader.domain.entity.SystemConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
    private Hyperlink changeDownDirLink;
    @FXML
    private Slider downThreadCountSlider;

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
        stackPane.addEventHandler(Tab.SELECTION_CHANGED_EVENT, event -> {
            if (event.getTarget() == stackPane) {
                event.consume();
                if (logger.isDebugEnabled()) {
                    logger.debug("切换到: 设置 Tab");
                }
                downThreadCountSlider.setValue(systemConfig.getDefaultThreadCount());
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


}
