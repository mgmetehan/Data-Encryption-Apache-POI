package wordService;

import development.TcNoValidation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordReader {
    public void ReadData(String filePath) {
        TcNoValidation validation = new TcNoValidation();
        WordWriter wwriter = new WordWriter();
        String result = "";
        Boolean check;
        try {
            XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(filePath)));
            List<XWPFParagraph> list = doc.getParagraphs();
            for (XWPFParagraph paragraph : list) {
                result = paragraph.getText();
                check = validation.TcNoCheck(result);
                if (check) {
                    wwriter.updateDocument(filePath, result);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}