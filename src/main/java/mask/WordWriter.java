package mask;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordWriter {
    public void updateDocument(String input, String name) {
        try {
            XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(input)));
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();

            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
                    String docText = xwpfRun.getText(0);
                    if (docText == null) {
                        continue;
                    }
                    docText = docText.replace(name, "*****");
                    xwpfRun.setText(docText, 0);
                }
            }
            System.out.println("Word is updated successfully");
            // save the docs
            FileOutputStream out = new FileOutputStream(input);
            doc.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
