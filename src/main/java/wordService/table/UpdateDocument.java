package wordService.table;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UpdateDocument {

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        String fileName_mask = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";

        UpdateDocument obj = new UpdateDocument();

        obj.updateDocument(
                fileName,
                fileName_mask,
                "Kurucu");
    }

    private void updateDocument(String input, String output, String name)
            throws IOException {

        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(input)))
        ) {

            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();
            //Iterate over paragraph list and check for the replaceable text in each paragraph
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
                    System.out.println(xwpfRun);
                    String docText = xwpfRun.getText(0);
                    //replacement and setting position
                    docText = docText.replace("${"+name+"}", "*****");
                    xwpfRun.setText(docText, 0);
                }
            }

            // save the docs
            try (FileOutputStream out = new FileOutputStream(output)) {
                doc.write(out);
            }

        }

    }

}

