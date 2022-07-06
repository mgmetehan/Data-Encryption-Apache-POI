package wordService.table;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class update {
    static String path = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
    static String path_mask = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası_mask.docx";

    public static void main(String[] args) throws IOException {

        update obj = new update();

        obj.updateDocument(
                path,
                path_mask,
                "Acar");
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
                    String docText = xwpfRun.getText(0);
                    //replacement and setting position
                    docText = docText.replace(name,"*****");
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
