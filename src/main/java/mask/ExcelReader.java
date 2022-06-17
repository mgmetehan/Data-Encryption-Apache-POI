package mask;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelReader {
    public void ReadCellData(ArrayList arrList, String path) {
        TcValidation TcValidation = new TcValidation();
        ExcelWriter writer = new ExcelWriter();
        FileInputStream file;
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        Iterator<Row> rowIterator;
        Iterator<Cell> cellIterator;

        String value = null, result = null;
        int vRow = 0, vColumn = 0;
        boolean check;
        String foundText = null;
        try {
            for (int j = 0; j < arrList.size(); j++) {
                foundText = (String) arrList.get(j);
                file = new FileInputStream(new File(path));
                workbook = new XSSFWorkbook(file);
                sheet = workbook.getSheetAt(0);
                rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    cellIterator = row.cellIterator();

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
                        if (check) {
                            writer.ExcelUpdateCell(vRow, vColumn, path);
                        }
                        if (TcValidation.TcNoCheck(result)) {
                            writer.ExcelUpdateCell(vRow, vColumn, path);
                        }
                    }
                }
                file.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
