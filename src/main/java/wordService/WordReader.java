package wordService;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordReader {
    public void ReadData(String filePath) {
        WordTcValidation validation = new WordTcValidation();
        WordWriter wwriter = new WordWriter();
        String result = "";
        String[] splitWords;
        Boolean check;
        try {
            XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(filePath)));
            List<XWPFParagraph> list = doc.getParagraphs();
            for (XWPFParagraph paragraph : list) {
                result = paragraph.getText();
                splitWords = result.split("\\s+");
                for (int i = 0; i < splitWords.length; i++) {
                    if (splitWords[i] == null) {
                        continue;
                    }
                    check = validation.TcNoCheck(splitWords[i]);
                    if (check) {
                        wwriter.updateDocument(filePath, splitWords[i]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}