package wordService.table;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class WordTableRead {
    static String temp = "";
    static String cellValue;

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        //Satır satır okuğundan kesme işaretleri kata verir
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument doc = new XWPFDocument(fis);
        List<XWPFTable> tables = doc.getTables();

        for (XWPFTable table : tables) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    System.out.println(cell.getText());
                    String sFieldValue = cell.getText();
                    if (sFieldValue.matches("Onur Çalıkuş") || sFieldValue.matches("Approved")) {
                        System.out.println("The match as per the Document is True");
                    }
					//System.out.println("\t");
                }
                //System.out.println(" ");
            }
        }

    }
}
