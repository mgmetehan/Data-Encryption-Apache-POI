package mask;
/*
package com.omreon.filediscoveryagent.mask;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelWriter {
    public void ExcelUpdateCell(int vRow, int vColumn, String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFCell cell = sheet.getRow(vRow).getCell(vColumn);

            cell.setCellType(CellType.STRING);
            cell.setCellValue("*****");

            FileOutputStream fileOut = new FileOutputStream(new File(filePath));
            workbook.write(fileOut);

            System.out.println("Excel is updated successfully");
            fileOut.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/