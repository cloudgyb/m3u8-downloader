package com.github.cloudgyb.m3u8downloader;

import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.util.SpringBeanUtil;
import com.github.cloudgyb.m3u8downloader.viewcontroller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.LogManager;

import static com.github.cloudgyb.m3u8downloader.viewcontroller.BootstrapStyle.*;

/**
 * APP entrypoint
 *
 * @author cloudgyb
 * 2021/5/15 18:31
 */
@SpringBootApplication
public class M3u8DownloaderApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(M3u8DownloaderApp.class);

    static {
        final LogManager logManager = LogManager.getLogManager();
        try {
            logManager.readConfiguration(M3u8DownloaderApp.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        SpringApplication.run(M3u8DownloaderApp.class);
        initDatabase();
        launch(args);
    }

    private static void initDatabase() {
        ClassPathResource schemeCreateRes = new ClassPathResource("/db.sql");
        String sql;
        try (InputStream inputStream = schemeCreateRes.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            sql = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("读取初始化 sql 文件失败！", e);
            return;
        }
        SqlSessionFactory sessionFactory = SpringBeanUtil.getBean(SqlSessionFactory.class);
        try (SqlSession sqlSession = sessionFactory.openSession();
             Connection connection = sqlSession.getConnection()) {
            String[] sqls = sql.split(";");
            for (String s : sqls) {
                try (PreparedStatement ps = connection.prepareStatement(s)) {
                    boolean isSuccess = ps.execute();
                    if (isSuccess) {
                        logger.info("表结构已初始化！");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("执行初始化 SQL 出错！{}", e.getMessage());
        }
    }

    @Override
    public void stop() {
        System.out.println("stop not finish task....");
        final List<DownloadTaskViewModel> noFinishedTasks = ApplicationStore.getNoFinishedTasks();
        for (DownloadTaskViewModel noFinishedTask : noFinishedTasks) {
            noFinishedTask.stop();
        }
        System.out.println("Close application...");
        System.exit(0);
    }
}
