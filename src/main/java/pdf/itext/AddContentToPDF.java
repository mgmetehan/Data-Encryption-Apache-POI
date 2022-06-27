package pdf.itext;

import java.io.*;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import static com.itextpdf.text.BaseColor.BLACK;

public class AddContentToPDF {

    public static void main(String[] args) throws IOException, DocumentException {

        PdfReader reader = new PdfReader("C:\\Users\\mgmet\\Desktop\\deneme.pdf"); // input PDF
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream("C:\\Users\\mgmet\\Desktop\\deneme_update.pdf")); // output PDF
        System.out.println();
        Double maxY = Double.valueOf(reader.getPageSize(1).getHeight());
        Double maxX = Double.valueOf(reader.getPageSize(1).getWidth());

        PdfContentByte over = stamper.getOverContent(1);
        over.setColorFill(BLACK);

        over.setLineWidth(13);
        over.moveTo(71.0, maxY - 88.10077+2);
        over.lineTo(111.7, maxY - 88.10077+2);

        over.stroke();

        stamper.close();

    }
}