package com.github.cloudgyb.m3u8downloader.viewcontroller.cell;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

/**
 * 带 Tooltip 的 TableCell
 *
 * @param <S> ViewModel
 * @param <T> 列类型
 * @author cloudgyb
 * @since 2025/07/11 15:29
 */
public class TooltipTableCell<S, T> extends TableCell<S, T> {

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory() {
        return column1 -> new TooltipTableCell<>();
    }

    @Override
    protected void updateItem(T t, boolean b) {
        super.updateItem(t, b);
        if (t != null) {
            setText(t.toString());
            setTooltip(new Tooltip(t.toString()));
        }
    }
}
