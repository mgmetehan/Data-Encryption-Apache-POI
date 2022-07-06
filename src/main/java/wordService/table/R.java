package wordService.table;


import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class R {


    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";

        try {
            XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(fileName)));
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            String docText = xwpfWordExtractor.getText();
            //System.out.println(docText);
            docText="a";

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
