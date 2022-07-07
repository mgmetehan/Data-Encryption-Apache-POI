package wordService.table;

import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateTable2 {
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";

        XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(fileName)));
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    replaceSimpleText(cell, "Kurucu");
                }
            }
        }
    }

    private static void replaceSimpleText(XWPFTableCell cellToChangeTextIn, String textToReplaceOn) {
        for (XWPFParagraph p : cellToChangeTextIn.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null) {
                    if (text.equals(textToReplaceOn)) {
                        System.out.println(text);
                        text = text.replace(text, textToReplaceOn);
                        r.setText(text, 0);
                    }
                }
            }
        }
    }
}
