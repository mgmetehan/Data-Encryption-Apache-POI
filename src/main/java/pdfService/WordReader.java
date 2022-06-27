package pdfService;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordReader {
    public void ReadData(ArrayList arrList, String path) {
        TcValidation TcValidation = new TcValidation();
        WordWriter wwriter = new WordWriter();
        String result = "";
        String[] splitWords;
        Boolean check;
        String foundText = null;
        try {
            for (int j = 0; j < arrList.size(); j++) {
                foundText = (String) arrList.get(j);
                XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(path)));
                List<XWPFParagraph> list = doc.getParagraphs();
                for (XWPFParagraph paragraph : list) {
                    result = paragraph.getText();
                    splitWords = result.split("\\s+");
                    for (int i = 0; i < splitWords.length; i++) {
                        if (splitWords[i] == null) {
                            continue;
                        }
                        check = splitWords[i].equals(foundText);
                        System.out.println("Buldu");
                        if (check) {
                            wwriter.updateDocument(path, splitWords[i]);
                        }
                        if (TcValidation.TcNoCheck(result)) {
                            wwriter.updateDocument(path, splitWords[i]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}