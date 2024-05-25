package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.domain.ProxyCaptureUrlDomain;
import com.github.cloudgyb.m3u8downloader.proxy.ProxyApplication;
import com.github.cloudgyb.m3u8downloader.util.FileUtil;
import com.github.cloudgyb.m3u8downloader.util.SystemCommandUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 下载历史tab页视图控制器
 *
 * @author cloudgyb
 * 2021/5/19 10:25
 */
public class CaptureM3u8ViewController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String[] windowsProxyEnableCommand = {
            "reg", "add", "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings", "/v", "ProxyEnable", "/t", "REG_DWORD", "/d", "1", "/f"};
    private static final String[] windowsProxyServerSetCommand = {
            "reg", "add", "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings", "/v", "ProxyServer", "/t", "REG_SZ", "/d", "http://localhost:" + ProxyApplication.port, "/f"
    };

    private static final String[] windowsProxyDisableCommand = {
            "reg", "add", "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings", "/v", "ProxyEnable", "/t", "REG_DWORD", "/d", "0", "/f"
    };
    private static final String[] windowsProxyServerDelCommand = {
            "reg", "delete", "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings", "/v", "ProxyServer", "/f"
    };

    private static final String[] verifyCert = {"certutil", "-verifystore", "Root", "Proxy CA"};

    private static final String[] importCertToSystemCommand = {
            "certutil", "-addstore", "Root", FileUtil.tempDir + File.separator + "M3U8ProxyCACert.crt"
    };

    public static final ConcurrentLinkedDeque<ProxyCaptureUrlDomain> urlDeque = new ConcurrentLinkedDeque<>();
    public static final BlockingQueue<ProxyCaptureUrlDomain> blockingQueue = new LinkedBlockingQueue<>();
    @FXML
    private Button caInstallBtn;
    @FXML
    private Button proxyServerSwitch;
    @FXML
    private TableView<ProxyCaptureUrlDomain> urlTable;
    @FXML
    private TableColumn<ProxyCaptureUrlDomain, String> urlColumn;
    @FXML
    private TableColumn<ProxyCaptureUrlDomain, Object> operaColumn;

    private final NewDownloadTaskViewModel downloadTaskViewModel = new NewDownloadTaskViewModel();

    private volatile static Thread lastStartListenUrlThread = null;
    private final Callback<TableView<ProxyCaptureUrlDomain>, TableRow<ProxyCaptureUrlDomain>> rowFactory = p -> {
        final TableRow<ProxyCaptureUrlDomain> tableRow = new TableRow<>();
        tableRow.setStyle("-fx-pref-height: 50px");
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyUrlMenuItem = new MenuItem("复制url");
        copyUrlMenuItem.setOnAction(event -> {
            ProxyCaptureUrlDomain captureUrlDomain = tableRow.getItem();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            // 创建一个包含字符串数据的剪切板内容
            ClipboardContent content = new ClipboardContent();
            content.putString(captureUrlDomain.getUrl());
            // 将内容设置到剪贴板
            clipboard.setContent(content);
            logger.info("复制{}到剪贴板", captureUrlDomain.getUrl());
        });
        contextMenu.getItems().add(copyUrlMenuItem);
        tableRow.setContextMenu(contextMenu);
        return tableRow;
    };
    private final Callback<TableColumn<ProxyCaptureUrlDomain, String>, TableCell<ProxyCaptureUrlDomain, String>> cellFactory =
            column -> new TableCell<>() {
                @Override
                protected void updateItem(String v, boolean b) {
                    super.updateItem(v, b);
                    if (v != null) {
                        setText(v);
                        setTooltip(new Tooltip(v));
                    }
                }
            };

    public void init() {
        if (ProxyApplication.isRunning.get()) {
            proxyServerSwitch.setText("停止代理服务器");
        }
        caInstallBtn.setVisible(false);
        SystemCommandUtil.CommandExecResult commandExecResult =
                SystemCommandUtil.execWithExitCodeAndResult(verifyCert);
        logger.debug(commandExecResult.toString());
        int exitCode = commandExecResult.getExitCode();
        if (exitCode != 0) {
            Platform.runLater(() -> caInstallBtn.setVisible(true));
        }
        urlTable.setRowFactory(rowFactory);
        // 绑定属性
        urlColumn.setCellFactory(cellFactory);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        operaColumn.setCellFactory(v -> new TableCell<>() {
            private final Button addDwonBtn = new Button("添加到下载列表");
            private final Button delBtn = new Button("删除");

            {
                addDwonBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnPrimaryStyle);
                addDwonBtn.setOnMouseClicked(e -> addToDownloadList(getIndex()));
                delBtn.setStyle(BootstrapStyle.btnXsStyle + BootstrapStyle.btnDangerStyle);
                delBtn.setOnMouseClicked(e -> deleteUrlFromList(getIndex()));
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(addDwonBtn, delBtn);
                    hBox.setSpacing(5);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });
        this.urlTable.getItems().addAll(urlDeque);
        this.urlTable.sort();
        if (lastStartListenUrlThread != null)
            lastStartListenUrlThread.interrupt();
        startListenUrl();
    }

    private void startListenUrl() {
        lastStartListenUrlThread = new Thread(() -> {
            ProxyCaptureUrlDomain url;
            try {
                while (true) {
                    url = blockingQueue.take();
                    urlDeque.add(url);
                    CaptureM3u8ViewController.this.urlTable.getItems().add(url);
                }
            } catch (InterruptedException ignore) {
                logger.info("老的监听 URL 线程退出!");
            }
        });
        lastStartListenUrlThread.start();
    }


    private void addToDownloadList(int index) {
        ProxyCaptureUrlDomain urlDomain = this.urlTable.getItems().get(index);
        downloadTaskViewModel.download(urlDomain.getUrl());
        deleteUrlFromList(index);
    }

    private void deleteUrlFromList(int index) {
        ProxyCaptureUrlDomain url = this.urlTable.getItems().get(index);
        urlDeque.remove(url);
        urlTable.getItems().remove(index);
        urlTable.refresh();
    }

    public void proxySwitch(ActionEvent actionEvent) {
        actionEvent.consume();
        proxyServerSwitch.disableProperty().set(true);
        if (!ProxyApplication.isRunning.get()) {
            ProxyApplication.start(
                    (isStarted ->
                    {
                        if (isStarted) {
                            Platform.runLater(() -> proxyServerSwitch.setText("停止代理服务器"));
                            /*SystemCommandUtil.CommandExecResult commandExecResult =
                                    SystemCommandUtil.execWithExitCodeAndResult(windowsProxyEnableCommand);
                            logger.debug(commandExecResult.toString());
                            SystemCommandUtil.CommandExecResult commandExecResult1 =
                                    SystemCommandUtil.execWithExitCodeAndResult(windowsProxyServerSetCommand);
                            logger.debug(commandExecResult1.toString());*/
                        } else {
                            Platform.runLater(() -> proxyServerSwitch.setText("启动代理服务器"));
                        }
                        Platform.runLater(() -> proxyServerSwitch.disableProperty().set(false));
                    }
                    )
            );
        } else {
            ProxyApplication.stopProxyServer((isStopped -> {
                if (isStopped) {
                    Platform.runLater(() -> proxyServerSwitch.setText("启动代理服务器"));
                    SystemCommandUtil.CommandExecResult commandExecResult =
                            SystemCommandUtil.execWithExitCodeAndResult(windowsProxyDisableCommand);
                    logger.debug(commandExecResult.toString());
                    SystemCommandUtil.CommandExecResult commandExecResult1 =
                            SystemCommandUtil.execWithExitCodeAndResult(windowsProxyServerDelCommand);
                    logger.debug(commandExecResult1.toString());
                } else {
                    Platform.runLater(() -> proxyServerSwitch.setText("停止代理服务器"));
                }
                Platform.runLater(() -> proxyServerSwitch.disableProperty().set(false));
            }
            ));
        }
    }

    @SuppressWarnings("all")
    public static void addUri(String host, String url) {
        if (url.endsWith(".m3u8")) {
            try {
                ProxyCaptureUrlDomain captureUrlDomain = new ProxyCaptureUrlDomain();
                captureUrlDomain.setHost(host);
                captureUrlDomain.setUrl(url);
                CaptureM3u8ViewController.blockingQueue.offer(captureUrlDomain,
                        100,
                        TimeUnit.MILLISECONDS);

            } catch (InterruptedException ignore) {
            }
        }
    }

    public void caInstall() {
        File caTempFile = new File(FileUtil.tempDir + File.separator + "M3U8ProxyCACert.crt");
        ClassPathResource classPathResource = new ClassPathResource("CACert.crt");
        try {
            FileCopyUtils.copy(classPathResource.getInputStream(), new FileOutputStream(caTempFile));
            SystemCommandUtil.CommandExecResult commandExecResult =
                    SystemCommandUtil.execWithExitCodeAndResult(importCertToSystemCommand);
            logger.debug(commandExecResult.toString());
            if (commandExecResult.getExitCode() == 0) {
                caInstallBtn.setVisible(false);
                showAlert("CA 根证书已安装！");
            }
        } catch (Exception e) {
            logger.debug(e.toString());
            showAlert("CA 根证书安装失败！");
        }
    }

    private void showAlert(String msg) {
        final Alert urlErrAlert = new Alert(Alert.AlertType.WARNING);
        Image icon = new Image("/icon.png");
        Stage stage = (Stage) urlErrAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon); // 设置图标
        urlErrAlert.setHeaderText(msg);
        final Optional<ButtonType> buttonType = urlErrAlert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            urlErrAlert.close();
        }
    }

}
