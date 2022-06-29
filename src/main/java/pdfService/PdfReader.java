package pdfService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import pdf.pdfBox.GetWordsFromPDF;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PdfReader extends PDFTextStripper {

    static List<String> words = new ArrayList<String>();

    public PdfReader() throws IOException {
    }

    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main(String[] args) throws IOException {
        PDDocument document = null;
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pdf"; // replace with your PDF file name
        try {
            document = PDDocument.load(new File(fileName));
            PDFTextStripper stripper = new GetWordsFromPDF();
            stripper.setSortByPosition(true);
            stripper.setStartPage(0);
            stripper.setEndPage(document.getNumberOfPages());

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);

            // print words
            for (String word : words) {
                System.out.println(word);
            }
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
    protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
        String[] wordsInStream = str.split(getWordSeparator());
        if (wordsInStream != null) {
            for (String word : wordsInStream) {
                words.add(word);
            }
        }
    }
}