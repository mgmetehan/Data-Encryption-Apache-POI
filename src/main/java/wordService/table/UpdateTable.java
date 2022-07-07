package wordService.table;

import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class UpdateTable {
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";

        XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(fileName)));

        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            System.out.println(text);
                            if (text != null && text.contains("Kurucu")) {
                                text = text.replace("Kurucu", "*****");
                                r.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}
