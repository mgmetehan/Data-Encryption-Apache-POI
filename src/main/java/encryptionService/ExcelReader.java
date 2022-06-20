package encryptionService;

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
        String foundText = null;
        String[] splited;
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
                        if (!result.trim().isEmpty()) {
                            splited = result.split("\\s+");
                            for (int i = 0; i < splited.length; i++) {
                                ExcelUpdate(cell.getRowIndex(), cell.getColumnIndex(), splited[i].equals(foundText), splited[i], path);
                            }
                        }
                        ExcelUpdate(cell.getRowIndex(), cell.getColumnIndex(), result.equals(foundText), result, path);
                    }
                }
                file.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ExcelUpdate(int vRow, int vColumn, boolean check, String result, String path) {
        ExcelWriter writer = new ExcelWriter();
        TcValidation TcValidation = new TcValidation();

        if (check) {
            writer.ExcelUpdateCell(vRow, vColumn, path);
        }
        if (TcValidation.TcNoCheck(result)) {
            writer.ExcelUpdateCell(vRow, vColumn, path);
        }
    }
}
