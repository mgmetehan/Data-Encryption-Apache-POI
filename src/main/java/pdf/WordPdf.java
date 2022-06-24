package pdf;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class WordPdf {
    private static final String docPath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
    private static final String pdfPath = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";


    public static void main(String[] args) throws Exception {

        InputStream in = new FileInputStream(new File(docPath));
        XWPFDocument document = new XWPFDocument(in);
        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(new File(pdfPath));
        PdfConverter.getInstance().convert(document, out, options);

        document.close();
        out.close();


    }
}
