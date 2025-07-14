package com.github.cloudgyb.m3u8downloader.viewcontroller.table;

import com.github.cloudgyb.m3u8downloader.model.DownloadTaskHistoryViewModel;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下载历史TableRow
 *
 * @author cloudgyb
 * @since 2025/07/14
 */
public class DownloadHistoryTableRow extends TableRow<DownloadTaskHistoryViewModel> {
    private final static Logger logger = LoggerFactory.getLogger(DownloadHistoryTableRow.class);

    public static Callback<TableView<DownloadTaskHistoryViewModel>, TableRow<DownloadTaskHistoryViewModel>> tableRow() {
        return rowFactory -> new DownloadHistoryTableRow();
    }

    public DownloadHistoryTableRow() {
        super();
        setStyle("-fx-pref-height: 50px");
        MenuItem copyUrlMenuItem = new MenuItem("复制url");
        copyUrlMenuItem.setOnAction(event -> {
            DownloadTaskHistoryViewModel item = getItem();
            String url = item.urlProperty().get();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            // 创建一个包含字符串数据的剪切板内容
            ClipboardContent content = new ClipboardContent();
            content.putString(url);
            // 将内容设置到剪贴板
            clipboard.setContent(content);
            logger.info("复制{}到剪贴板", url);
        });
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(copyUrlMenuItem);
        setContextMenu(contextMenu);
    }
}
