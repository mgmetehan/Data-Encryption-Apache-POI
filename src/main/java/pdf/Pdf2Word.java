package pdf;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class Pdf2Word {
    //başlangıçta 2 pathi ver
    private static final String outPath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
    private static final String FILENAME = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";



    public static void main(String[] args) {
        try {
            generateDocFromPDF(FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateDocFromPDF(String filename) throws IOException {
        XWPFDocument doc = new XWPFDocument();

        String pdf = filename;
        PdfReader reader = new PdfReader(pdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            String text = strategy.getResultantText();
            XWPFParagraph p = doc.createParagraph();
            XWPFRun run = p.createRun();
            run.setText(text);
            run.addBreak(BreakType.PAGE);
        }
        FileOutputStream out = new FileOutputStream(outPath);
        doc.write(out);
        out.close();
        reader.close();
        doc.close();
    }
}
