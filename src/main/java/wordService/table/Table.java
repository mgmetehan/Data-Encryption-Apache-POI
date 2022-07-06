package wordService.table;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Table {
    public static void main(String[] args) {
        String path = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    System.out.println("cell text: " + cell.getText());
                }
            }
        }
    }
}
