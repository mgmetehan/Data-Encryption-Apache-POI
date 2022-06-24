package pdf;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.*;

public class WordPdf {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
    private static final String outPath = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";


    public static void main(String[] args) {
       /* try {
            InputStream docFile = new FileInputStream(new File(filePath));
            XWPFDocument doc = new XWPFDocument(docFile);
            PdfOptions pdfOptions = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File(outPath));
            PdfConverter.getInstance().convert(doc, out, pdfOptions);
            doc.close();
            out.close();
            System.out.println("Done");

        } catch (Exception e) {
            System.out.println(e);
        }
    }*/
        try {
            InputStream templateInputStream = new FileInputStream(filePath);
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(templateInputStream);
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            FileOutputStream os = new FileOutputStream(outPath);
            Docx4J.toPDF(wordMLPackage, os);
            os.flush();
            os.close();
        } catch (Throwable e) {

            e.printStackTrace();
        }
    }

}
