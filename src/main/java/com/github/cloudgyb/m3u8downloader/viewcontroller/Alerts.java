package com.github.cloudgyb.m3u8downloader.viewcontroller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * 弹窗工具类
 *
 * @author cloudgyb
 * @since 2025/07/14 16:21
 */
public class Alerts {
    private static final Image icon = new Image("/icon.png");

    public static boolean confirm(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon); // 设置图标
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
