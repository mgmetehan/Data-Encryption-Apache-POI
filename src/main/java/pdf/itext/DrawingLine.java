import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;

public class DrawingLine {
    public static void main(String args[]) throws Exception {
        // Creating a PdfWriter
        String dest = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument object
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Creating a Document object
        Document doc = new Document(pdfDoc);

        // Creating a new page
        PdfPage pdfPage = pdfDoc.addNewPage();

        // Creating a PdfCanvas object
        PdfCanvas canvas = new PdfCanvas(pdfPage);

        // Initial point of the line
        canvas.moveTo(100, 300);

        // Drawing the line
        canvas.lineTo(500, 300);

        // Closing the outlook.msg.path stroke
        canvas.closePathStroke();

        // Closing the document
        doc.close();

        System.out.println("Object drawn on pdf successfully");
    }
}