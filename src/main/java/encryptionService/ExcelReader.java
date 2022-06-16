package encryptionService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

public class ExcelReader {

    public void ReadCellData(String foundText, String path) {
        TcValidation TcValidation = new TcValidation();
        ExcelWriter writer = new ExcelWriter();
        String value = null, result = null;
        int vRow = 0, vColumn = 0;
        boolean check;
        try {
            FileInputStream file = new FileInputStream(new File(path));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    result = "";
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == CellType.STRING) {
                        value = cell.getStringCellValue();
                        result = value;
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        value = String.valueOf(cell.getNumericCellValue());
                        for (int i = 0; i < value.length() - 3; i++) {
                            if (i == 1) {
                                continue;
                            }
                            result += String.valueOf(value.charAt(i));
                        }
                    }
                    vRow = cell.getRowIndex();
                    vColumn = cell.getColumnIndex();
                    check = result.equals(foundText);
                    if (TcValidation.TcNoCheck(result)) {
                        writer.ExcelUpdateCell(vRow, vColumn, path);
                    } else if (check) {
                        writer.ExcelUpdateCell(vRow, vColumn, path);
                    }
                }
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
