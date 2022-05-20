package service.update;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUpdateCell {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    public static void main(String[] args) {
        try {

            // Create an object of FileInputStream class to read excel file
            FileInputStream fis = new FileInputStream(new File(filePath));

            // Create object of XSSFWorkbook class
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // Read excel sheet by sheet name
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Print data present at row 0 column 2
            System.out.println(sheet.getRow(4).getCell(1).getStringCellValue());

            // Get the Cell at index 3 from the above row
            XSSFCell cell = sheet.getRow(4).getCell(1);

            cell.setCellType(CellType.STRING);
            cell.setCellValue("Updated Value");

            // Write the output to the file
            FileOutputStream fileOut = new FileOutputStream(new File(filePath));
            workbook.write(fileOut);

            System.out.println("Id column in Excel is updated successfully");
            fileOut.close();

            // Closing the workbook
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
