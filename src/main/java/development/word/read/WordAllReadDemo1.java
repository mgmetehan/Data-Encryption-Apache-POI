package development.word.read;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;

public class WordAllReadDemo1 {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument file = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor ext = new XWPFWordExtractor(file);
            System.out.println(ext.getText());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
