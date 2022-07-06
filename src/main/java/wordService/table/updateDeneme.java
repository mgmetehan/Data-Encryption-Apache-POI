package wordService.table;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class updateDeneme {

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        String result = "";
        String[] splitWords;
        String name = "Kurucu";
        try {
            XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(fileName)));
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);

            List<XWPFParagraph> xwpfParagraphList = xwpfWordExtractor.getDocument().getParagraphs();
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
                    String docText = String.valueOf(xwpfRun.getDocument());
                    if (docText == null) {
                        continue;
                    }
                    System.out.println(docText);
                    docText = docText.replace(name, "*****");
                    xwpfRun.setText(docText, 0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
