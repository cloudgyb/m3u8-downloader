package com.github.cloudgyb.m3u8downloader;

import com.github.cloudgyb.m3u8downloader.database.DatabaseInitializer;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.signal.HttpServerHandler;
import com.github.cloudgyb.m3u8downloader.signal.RepeatProcessStartupSignalHandler;
import com.github.cloudgyb.m3u8downloader.signal.SignalServer;
import com.github.cloudgyb.m3u8downloader.viewcontroller.MainViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.github.cloudgyb.m3u8downloader.viewcontroller.BootstrapStyle.*;

/**
 * APP entrypoint
 *
 * @author cloudgyb
 * 2021/5/15 18:31
 */
public class M3u8DownloaderApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(M3u8DownloaderApp.class);
    private static volatile Stage primaryStage;
    private static final int signalServerPort = 65530;
    private static SignalServer signalServer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        M3u8DownloaderApp.primaryStage = primaryStage;
        final FXMLLoader loader = new FXMLLoader(M3u8DownloaderApp.class.getResource("/fxml/main.fxml"));
        final TabPane pane = loader.load();
        final MainViewController controller = loader.getController();
        controller.init();
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
        primaryStage.getIcons().add(new Image("/icon.png"));
        primaryStage.setTitle("M3U8下载器");
        primaryStage.getScene()
                .getStylesheets()
                .add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setOnCloseRequest(event -> {
            Alert exitConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            exitConfirm.setTitle("提示");
            exitConfirm.setHeaderText("确定退出吗？");
            // Load the image
            Image icon = new Image("/icon.png");
            Stage stage = (Stage) exitConfirm.getDialogPane().getScene().getWindow();
            stage.getIcons().add(icon); // 设置图标
            Button okButton = (Button) exitConfirm.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setStyle(btnSmStyle + btnDangerStyle);
            Button cancelButton = (Button) exitConfirm.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.setStyle(btnSmStyle + btnPrimaryStyle);
            Optional<ButtonType> result = exitConfirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                primaryStage.close();
            } else {
                //将事件消费掉，停止传播
                event.consume();
            }
        });
    }

    public static void main(String[] args) {
        try {
            logger.info("启动信号处理服务...");
            signalServer = new SignalServer(signalServerPort);
            signalServer.signalHandler(new RepeatProcessStartupSignalHandler(), new HttpServerHandler())
                    .start();
            logger.info("信号处理服务启动完成！");
        } catch (IOException e) {
            if (e instanceof BindException) {
                logger.error("信号处理端口已经占用，已经有进程启动，发送信号到已有进程...");
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(signalServerPort));
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream
                            .write(RepeatProcessStartupSignalHandler.signal.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException ex) {
                    logger.error("发送信号到已有进程发生异常：{}", ex.getMessage());
                }
                System.exit(0);
            }
        }
        logger.info("初始化数据库...");
        DatabaseInitializer.initDatabase();
        logger.info("数据库初始化完成！");
        logger.info("启动JavaFX...");
        launch(args);
    }

    @Override
    public void stop() {
        try {
            if (signalServer != null) {
                logger.info("停止信号处理服务...");
                signalServer.stop();
                logger.info("信号处理服务已停止！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("停止所有未完成的下载任务...");
        final List<DownloadTaskViewModel> noFinishedTasks = ApplicationStore.getNoFinishedTasks();
        for (DownloadTaskViewModel noFinishedTask : noFinishedTasks) {
            noFinishedTask.stop();
        }
        logger.info("所有未完成下载任务已停止！退出...");
        System.exit(0);
    }

    public static void toFront() {
        if (primaryStage != null) {
            Platform.runLater(() -> primaryStage.toFront());
        }
    }
}
