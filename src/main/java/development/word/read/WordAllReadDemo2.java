package development.word.read;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class WordAllReadDemo2 {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";

    public static void main(String[] args) {
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(filePath)))) {

            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            String docText = xwpfWordExtractor.getText();
            System.out.println(docText);

//!!!!            Bu parça ayırma işlemini tek tek okumak için kullana bilrisin

            // find number of words in the document
            long count = Arrays.stream(docText.split("\\s+")).count();
            System.out.println("Total words: " + count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
