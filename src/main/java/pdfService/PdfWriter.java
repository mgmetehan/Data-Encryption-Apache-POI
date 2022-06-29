package pdfService;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfWriter {
    public static void main(String[] args) throws IOException, DocumentException {

        PdfReader reader = new PdfReader("C:\\Users\\mgmet\\Desktop\\deneme.pdf"); // input PDF
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream("C:\\Users\\mgmet\\Desktop\\deneme_mask.pdf")); // output PDF
        System.out.println();
        Double maxY = Double.valueOf(reader.getPageSize(1).getHeight());
        Double maxX = Double.valueOf(reader.getPageSize(1).getWidth());

        PdfContentByte over = stamper.getOverContent(1);
        over.setRGBColorFill(255, 0, 0);

        over.setLineWidth(13);
        over.moveTo(71.0, maxY - 88.10077 + 2);
        over.lineTo(104.96 + 6, maxY - 88.10077 + 2);

        over.stroke();

        stamper.close();

    }
}
