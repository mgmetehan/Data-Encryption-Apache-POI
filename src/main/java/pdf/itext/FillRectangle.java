package pdf.itext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class FillRectangle
{
    public static void main(String[] args)
    {
        try
        {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\mgmet\\Desktop\\deneme.pdf"));
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            //contentByte.rectangle(71.0, 88.10077, 8.664001, 6.696);
            contentByte.rectangle(100, 842.0, 50, 50);
            System.out.println(document.getPageSize());
            contentByte.setColorFill(BaseColor.CYAN);
            contentByte.fill();
            document.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
        }
    }
}