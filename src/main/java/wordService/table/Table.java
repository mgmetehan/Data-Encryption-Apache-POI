package wordService.table;

import org.apache.poi.xwpf.usermodel.*;
import wordService.WordWriter;

import java.io.FileInputStream;
import java.io.IOException;

public class Table {
    public static void main(String[] args) {
        String path = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        XWPFDocument doc = null;
        String result = "";
        String[] splitWords;
        try {
            doc = new XWPFDocument(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    result = cell.getText();
                    splitWords = result.split("\\s+");
                    for (int i = 0; i < splitWords.length; i++) {
                        if (splitWords[i] == null) {
                            continue;
                        }
                        System.out.println("cell text: " + splitWords[i]);
                    }
                }
            }
        }
    }
}