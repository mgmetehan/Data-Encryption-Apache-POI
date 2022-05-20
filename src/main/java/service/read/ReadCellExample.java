package service.read;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class ReadCellExample {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    public static void main(String[] args) {
        ReadCellExample rc = new ReadCellExample();   //object of the class
        rc.ReadCellData(1, 0);
    }

    //method defined for reading a cell
    public void ReadCellData(int vRow, int vColumn) {
        String value = null;          //variable for storing the cell value
        Workbook wb = null;           //initialize Workbook null
        try {
//reading data from a file in the form of bytes
            FileInputStream fis = new FileInputStream(filePath);
//constructs an XSSFWorkbook object, by buffering the whole stream into the memory
            wb = new XSSFWorkbook(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);   //getting the XSSFSheet object at given index

        Row row = sheet.getRow(vRow); //returns the logical row
        Cell cell = row.getCell(vColumn); //getting the cell representing the given column
        if (cell.getCellType() == CellType.STRING) {
            value = cell.getStringCellValue() + "  ";
        } else if (cell.getCellType() == CellType.NUMERIC) {
            value = cell.getNumericCellValue() + "  ";
        }
        System.out.println(value + "\n");
    }

}

