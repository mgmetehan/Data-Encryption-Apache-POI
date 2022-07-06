package wordService.table;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadParseDocument1 {

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        String result = "";
        String[] splitWords;
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(fileName)))) {

            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            result = xwpfWordExtractor.getText();
            splitWords = result.split("\\s+");
            for (int i = 0; i < splitWords.length; i++) {
                if (splitWords[i] == null) {
                    continue;
                }
                //System.out.println(splitWords[i]);

                String sFieldValue = splitWords[i];
                if (sFieldValue.matches("Kurucu") || sFieldValue.matches("Approved")) {
                    System.out.println("aaaaaaaaaaaaaaaa");
                }
            }
        }
    }
}