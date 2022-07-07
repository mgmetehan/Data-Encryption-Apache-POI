package pptx;

import java.io.*;
import java.util.ArrayList;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.*;

public class Read {

    public static void main(String[] args) {
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        String word = "Metehan";
        readPPT(fileName, word);
    }

    public static void readPPT(String fileName, String word) {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(fileName);
            XMLSlideShow ppt = new XMLSlideShow(inputStream);

            String result = "";
            String[] splitWords;

            for (XSLFSlide slide : ppt.getSlides()) {
                System.out.println("Starting slide...");
                XSLFShape[] shapes = slide.getShapes().toArray(new XSLFShape[0]);
                for (XSLFShape shape : shapes) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape textShape = (XSLFTextShape) shape;
                        result = textShape.getText();
                        splitWords = result.split("\\s+");
                        for (int i = 0; i < splitWords.length; i++) {
                            if (splitWords[i] == null) {
                                continue;
                            }
                            String noPuncWord = punctuationCheck(splitWords[i]);
                            if (word.equals(noPuncWord)) {
                                noPuncWord = noPuncWord.replace(word, "*****");
                            }

                            System.out.println(textShape.getText());
                            //props.setText(noPuncWord);
                            // System.out.println(result);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String punctuationCheck(String word) {
        ArrayList arr = new ArrayList();
        arr.add(".");
        arr.add(",");
        arr.add(":");
        arr.add(";");
        arr.add("?");
        arr.add("!");
        arr.add("-");
        arr.add("\u201c");
        arr.add("\u201d");
        arr.add("\u2018");
        arr.add("\u2019");
        arr.add("\"");
        arr.add("'");
        arr.add("(");
        arr.add(")");
        arr.add("[");
        arr.add("]");
        arr.add("{");
        /*
        \u201c matches “ (left double quotation mark)
        \u201d matches ” (right double quotation mark)
        \u2018 matches ‘ (left single quotation mark)
        \u2019 matches ’ (right single quotation mark)
         */
        String[] parts = new String[0];
        for (int i = 0; i < arr.size(); i++) {
            if (word.startsWith(arr.get(i).toString()) || word.endsWith(arr.get(i).toString())) {
                word = word.replace((CharSequence) arr.get(i), "");
            }
        }

        return word;
    }
}
