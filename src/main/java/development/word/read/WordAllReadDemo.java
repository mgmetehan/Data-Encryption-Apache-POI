package development.word.read;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;

public class WordAllReadDemo {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";

    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(fis));
            java.util.List<XWPFParagraph> paragraphs = doc.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                System.out.println(paragraph.getText());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
