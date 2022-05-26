package development.excel.read;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ReadCellExample {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    public static void main(String[] args) {
        ReadCellExample read = new ReadCellExample();   //object of the class
        read.ReadCellData(1, 0);
    }

    public void ReadCellData(int vRow, int vColumn) {
        String value = null;
        Workbook wb = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            wb = new XSSFWorkbook(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);   //getting the XSSFSheet object at given index

        for (int rowLastIndex = 0; rowLastIndex < sheet.getLastRowNum(); rowLastIndex++) {
            Row row = sheet.getRow(vRow++); //returns the logical row
            Cell cell = row.getCell(vColumn); //getting the cell representing the given column
            if (cell.getCellType() == CellType.STRING) {
                value = cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                value = String.valueOf(cell.getNumericCellValue());
            }
            System.out.println(value);
        }
    }

}

