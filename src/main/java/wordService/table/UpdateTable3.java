package wordService.table;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UpdateTable3 {
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
                    String sFieldValue = cell.getText();
                    if (sFieldValue.matches("Kurucu") || sFieldValue.matches("Approved")) {
                        System.out.println("The match as per the Document is True");
                        sFieldValue = "*****";
                        cell.setText(sFieldValue);
                        System.out.println();
                    }
                    //System.out.println("\t");
                }
                //System.out.println(" ");
            }
        }
        FileOutputStream out = new FileOutputStream(path);
        doc.write(out);
    }
}
