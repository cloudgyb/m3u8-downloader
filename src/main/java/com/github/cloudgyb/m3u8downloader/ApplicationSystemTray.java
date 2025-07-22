package com.github.cloudgyb.m3u8downloader;

import com.github.cloudgyb.m3u8downloader.viewcontroller.Alerts;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 系统托盘
 *
 * @author cloudgyb
 * @since 2025/07/22 14:24
 */
public class ApplicationSystemTray {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private SystemTray systemTray;
    private TrayIcon trayIcon;
    private MenuItem showItem;
    private MenuItem exitItem;

    public ApplicationSystemTray() {
        Platform.setImplicitExit(false); // 防止主窗口关闭 JavaFX 线程退出

        if (isNotSystemTraySupported()) {
            return;
        }
        // Get the system tray instance
        systemTray = SystemTray.getSystemTray();
        // 创建 popup menu
        PopupMenu popupMenu = new PopupMenu();
        showItem = new MenuItem("Show MainWindow");
        exitItem = new MenuItem("Exit");
        // Add items to popup menu
        popupMenu.add(showItem);
        popupMenu.addSeparator();
        popupMenu.add(exitItem);
        // Create a tray icon
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        trayIcon = new TrayIcon(image, "M3U8 Downloader", popupMenu);
        // Set tray icon properties
        trayIcon.setImageAutoSize(true);
        // Add the tray icon to system tray
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            logger.error("TrayIcon could not be added.");
        }
    }

    public void init(Stage stage) {
        if (isNotSystemTraySupported()) {
            return;
        }
        // Add mouse listener for double-click
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { //鼠标左键点击
                    Platform.runLater(() -> {
                        if (stage.isIconified()) {
                            stage.setIconified(false);  // 如果最小化了，先恢复
                        }
                        if (!stage.isShowing()) {
                            stage.show();
                        }
                        stage.toFront();
                        stage.requestFocus();
                    });
                }
            }
        });

        // Add action listeners
        showItem.addActionListener(e ->
                Platform.runLater(() -> {
                    stage.show();
                    stage.toFront();
                    stage.requestFocus();
                })
        );

        exitItem.addActionListener(e -> Platform.runLater(() -> {
            if (Alerts.confirm("提示", null, "确定要退出吗？")) {
                systemTray.remove(trayIcon);
                Platform.exit();
            }
        }));

        // Handle stage close request (minimize to tray instead of exiting)
        stage.setOnCloseRequest(event -> {
            event.consume();
            stage.setIconified(true);
            stage.hide();
            if (trayIcon != null) {
                trayIcon.displayMessage("M3U8 Downloader 已最小化到系统托盘",
                        "未完成的下载任务在后台继续运行", TrayIcon.MessageType.INFO);
            }
        });
    }

    private boolean isNotSystemTraySupported() {
        if (!SystemTray.isSupported()) {
            logger.warn("SystemTray is not supported");
            return true;
        }
        return false;
    }
}
