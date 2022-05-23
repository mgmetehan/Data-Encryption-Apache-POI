package service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import development.TcNoValidation;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class Reader {

    public void ReadCellData(String filePath) {
        TcNoValidation validation = new TcNoValidation();
        Writer writer=new Writer();
        String value = null, result = null;
        int vRow = 0, vColumn = 0;
        boolean check;

        try {
            FileInputStream file = new FileInputStream(new File(filePath));
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    //o satirdaki siradi hücreyi okur
                    result = "";
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == CellType.STRING) {
                        value = cell.getStringCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        value = String.valueOf(cell.getNumericCellValue());
                        for (int i = 0; i < value.length() - 3; i++) {
                            if (i == 1) {
                                continue;
                            }
                            result += String.valueOf(value.charAt(i));
                        }
                        vRow = cell.getRowIndex();
                        vColumn = cell.getColumnIndex();
                    }
                    check = validation.TcNoCheck(result);
                    if (check) {
                        writer.ExcelUpdateCell(vRow, vColumn,filePath);
                   }
                }
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
