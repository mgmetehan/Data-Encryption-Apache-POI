package pdf.pdfBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an example on how to extract words from PDF document
 */
public class GetWordsFromPDF extends PDFTextStripper {

    static List<String> words = new ArrayList<String>();

    public GetWordsFromPDF() throws IOException {
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
        String s="Ankara";
        if (wordsInStream != null) {
            for (String word : wordsInStream) {
                if (word.equals(s)) {
                    for(int i = 0; i < s.length(); i++) {
                        System.out.println(textPositions.get(i).getXDirAdj() + " " + textPositions.get(i));
                        System.out.println();
                        break;
                    }
                }
                //words.add(word);
            }
        }


       /* for (TextPosition text : textPositions) {

            if (textPositions.equals("Ankara")) {
                break;
            }
            System.out.println(text.getUnicode() + " [(X=" + text.getXDirAdj() + ",Y=" +
                    text.getYDirAdj() + ") height=" + text.getHeightDir() + " width=" +
                    text.getWidthDirAdj() + "]");
        }*/


    }
}