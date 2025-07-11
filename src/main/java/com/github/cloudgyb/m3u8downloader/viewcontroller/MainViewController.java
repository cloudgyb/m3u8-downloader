package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationContext;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主页面视图控制器
 *
 * @author cloudgyb
 * 2021/5/16 10:47
 */
public class MainViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    private TabPane tabPane;
    @FXML
    public Tab newTaskTab;
    @FXML
    public Tab downloadingTab;
    @FXML
    public Tab downloadHistoryTab;
    @FXML
    public Tab settingTab;

    public void initialize() {
        ApplicationContext.getInstance().set("tabs", tabPane);
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTab, newTab) -> {
                    if (newTab != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("切换到: {}", newTab.getText());
                        }
                        Event.fireEvent(newTab.getContent(), new Event(Tab.SELECTION_CHANGED_EVENT));
                    }
                });
    }
}
