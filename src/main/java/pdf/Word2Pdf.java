package pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

public class Word2Pdf {
    private static final String docPath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
    private static final String pdfPath = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";

    public static void main( String[] args )
    {
        try {
            InputStream docFile = new FileInputStream(new File(docPath));
            XWPFDocument doc = new XWPFDocument(docFile);
            PdfOptions pdfOptions = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File(pdfPath));
            PdfConverter.getInstance().convert(doc, out, pdfOptions);
            doc.close();
            out.close();
            System.out.println("Done");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
