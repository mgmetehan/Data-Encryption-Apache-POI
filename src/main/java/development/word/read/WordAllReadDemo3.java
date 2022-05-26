package development.word.read;


import development.TcNoValidation;
import development.word.update.UpdateWord;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordAllReadDemo3 {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";

    public static void main(String[] args) {
        TcNoValidation validation = new TcNoValidation();
        UpdateWord up = new UpdateWord();
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(filePath)))) {

            // output the same as 8.1
            List<XWPFParagraph> list = doc.getParagraphs();
            for (XWPFParagraph paragraph : list) {
                String result = paragraph.getText();
                Boolean check = validation.TcNoCheck(result);
                if (check) {
                    //writer.ExcelUpdateCell(vRow, vColumn, filePath);
                    up.updateDocument(filePath,result);
                    System.out.println(result);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}