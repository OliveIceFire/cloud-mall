package com.example.cloud.mall.common.util;


import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {
    public static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
         //注意java版本
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
        }
        return null;
    }
}
