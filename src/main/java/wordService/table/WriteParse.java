package wordService.table;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WriteParse {
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        String result = "";
        String[] splitWords;
        try {
            XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(fileName)));
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();



            result = xwpfWordExtractor.getText();
            splitWords = result.split("\\s+");
            for (int i = 0; i < splitWords.length; i++) {
                if (splitWords[i] == null) {
                    continue;
                }
                //System.out.println(splitWords[i]);

                String sFieldValue = splitWords[i];
                String word = "Kurucu";
                if (sFieldValue.matches(word) || sFieldValue.matches("Approved")) {
                    System.out.println("The match as per the Document is True");
                    sFieldValue = sFieldValue.replace(word, "*****");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
