package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * 主页面视图控制器
 *
 * @author cloudgyb
 * 2021/5/16 10:47
 */
public class MainViewController {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab newDownloadTaskTab;
    @FXML
    private Tab downloadListTab;
    @FXML
    private Tab downloadHistoryTab;
    @FXML
    private Tab downloadSettingTab;

    public void init() {
        ApplicationContext.getInstance().set("tabs", tabPane);
    }

    public void newDownloadTaskTabSelected() throws IOException {
        final Tab tab = tabPane.getSelectionModel().getSelectedItem();
        final boolean selected = tab.isSelected();
        if (selected) {
            FXMLLoader loader = new FXMLLoader(MainViewController.
                    class.getResource("/fxml/new_download_task.fxml"));
            final Pane pane = loader.load();
            final NewDownloadTaskViewController controller = loader.getController();
            controller.init();
            tab.setContent(pane);
        }
    }


    public void downloadListTabSelected() throws IOException {
        final Tab tab = tabPane.getSelectionModel().getSelectedItem();
        final boolean selected = tab.isSelected();
        if (selected) {
            FXMLLoader loader = new FXMLLoader(MainViewController.
                    class.getResource("/fxml/download_list.fxml"));
            final StackPane pane = loader.load();
            final DownloadListViewController controller = loader.getController();
            controller.init();
            pane.setAlignment(Pos.CENTER);
            tab.setContent(pane);
        }
    }

    public void downloadHistoryTabSelected() throws IOException {
        final Tab tab = tabPane.getSelectionModel().getSelectedItem();
        final boolean selected = tab.isSelected();
        if (selected) {
            FXMLLoader loader = new FXMLLoader(MainViewController.
                    class.getResource("/fxml/download_history.fxml"));
            final StackPane pane = loader.load();
            final DownloadHistoryViewController controller = loader.getController();
            controller.init();
            pane.setAlignment(Pos.CENTER);
            tab.setContent(pane);
        }
    }

    public void downloadSettingTabSelected() throws IOException {
        final Tab tab = tabPane.getSelectionModel().getSelectedItem();
        final boolean selected = tab.isSelected();
        if (selected) {
            FXMLLoader loader = new FXMLLoader(MainViewController.
                    class.getResource("/fxml/setting.fxml"));
            final Pane pane = loader.load();
            final DownloadSettingViewController controller = loader.getController();
            controller.init();
            tab.setContent(pane);
        }
    }
}
