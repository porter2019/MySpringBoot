package com.xxx.myspringboot.handler;

import org.apache.fesod.sheet.write.handler.SheetWriteHandler;
import org.apache.fesod.sheet.write.metadata.holder.WriteSheetHolder;
import org.apache.fesod.sheet.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Fesod 调整excel为1页宽1页高
 */
public class FitToPageHandler implements SheetWriteHandler {

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
                                 WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        // 启用“调整为”功能
        sheet.setFitToPage(true);
        // 开启自动分页符（配合 fitToPage 使用）
        sheet.setAutobreaks(true);

        PrintSetup printSetup = sheet.getPrintSetup();
        // 设置为 1 页宽
        printSetup.setFitWidth((short) 1);
        // 设置为 1 页高
        printSetup.setFitHeight((short) 1);
    }
}