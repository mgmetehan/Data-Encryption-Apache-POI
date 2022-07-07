package pptx;

import java.io.*;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.*;

public class Read {

    public static void main(String[] args) {

        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";

        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        XMLSlideShow ppt;

        try {
            ppt = new XMLSlideShow(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        readPPT(ppt);
    }

    public static void readPPT(XMLSlideShow ppt) {
        POIXMLProperties.CoreProperties props = ppt.getProperties().getCoreProperties();
        String title = props.getTitle();
        System.out.println("Title: " + title);

        for (XSLFSlide slide : ppt.getSlides()) {
            System.out.println("Starting slide...");
            XSLFShape[] shapes = slide.getShapes().toArray(new XSLFShape[0]);
            for (XSLFShape shape : shapes) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    String text = textShape.getText();
                    System.out.println("Text: " + text);
                }
            }
        }
    }
}
