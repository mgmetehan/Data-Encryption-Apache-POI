package pptx;

import java.io.*;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.*;

public class Read {

    public static void main(String[] args) {
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        readPPT(fileName);
    }

    public static void readPPT(String fileName) {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(fileName);
            XMLSlideShow ppt = new XMLSlideShow(inputStream);

            POIXMLProperties.CoreProperties props = ppt.getProperties().getCoreProperties();
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
                            System.out.println("Text: " + splitWords[i]);
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
}
