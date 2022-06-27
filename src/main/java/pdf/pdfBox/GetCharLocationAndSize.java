package pdf.pdfBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * This is an example on how to get the x/y coordinates and size of each character in PDF
 */
public class GetCharLocationAndSize extends PDFTextStripper {

    public GetCharLocationAndSize() throws IOException {
    }

    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main(String[] args) throws IOException {
        PDDocument document = null;
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";
        try {
            document = PDDocument.load(new File(fileName));
            PDFTextStripper stripper = new GetCharLocationAndSize();
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(document.getNumberOfPages());
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * Override the default functionality of PDFTextStripper.writeString()
     */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        System.out.println(textPositions.get(0).getPageWidth() + " " + textPositions.get(0).getPageHeight());

        for (TextPosition text : textPositions) {
            System.out.println(text.getUnicode() + " [(X=" + text.getXDirAdj() + ",Y=" +
                    text.getYDirAdj() + ") height=" + text.getHeightDir() + " width=" +
                    text.getWidthDirAdj() + "]");
        }

    }
}