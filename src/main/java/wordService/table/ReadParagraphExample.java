package wordService.table;

import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class ReadParagraphExample {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(fis));
            for (int i = 0; i < 3; i++) {
                List<XWPFParagraph> paragraphs = doc.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    System.out.println(paragraph.getText());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}