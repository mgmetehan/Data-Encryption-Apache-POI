package development.excel.write;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class ExcelWriteDemo {

    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    public static void main(String[] args) {
        XSSFWorkbook file = new XSSFWorkbook();
        XSSFSheet paper = file.createSheet("Örnek 1");
        Object[][] table = {{"Id", "Ad", "Tc"}, {1, "Mete", "12345"}, {2, "Ilayda", "54321"}};

        int lineNo = 0;
        System.out.println("Creating an Excel file.");

        for (Object[] tableLine : table) {
            Row line = paper.createRow(lineNo++);
            int columnNo = 0;
            for (Object tableCell : tableLine) {
                Cell cell = line.createCell(columnNo++);
                if (tableCell instanceof String) {//onun bir parcasi mi diye kontroll ediyor
                    cell.setCellValue((String) tableCell);
                } else if (tableCell instanceof Integer) {
                    cell.setCellValue((Integer) tableCell);
                }
            }
        }
        try {
            FileOutputStream fileData = new FileOutputStream(filePath);
            file.write(fileData);
            file.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Created an Excel file.");
    }
}

