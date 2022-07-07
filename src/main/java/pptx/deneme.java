package pptx;

import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class deneme {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        applyValidationsOnPptxFile(fileName);
    }


    public static void applyValidationsOnPptxFile(String fileName) {
        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        XMLSlideShow ppt = null;
        try {
            ppt = new XMLSlideShow(fis);

            // get slides
            for (XSLFSlide slide : ppt.getSlides()) {
                for (XSLFShape sh : slide.getShapes()) {
                    // name of the shape
                    String name = sh.getShapeName();
                    // shapes's anchor which defines the position of this shape in the slide
                    if (sh instanceof XSLFTextShape) {
                        XSLFTextShape shape = (XSLFTextShape) sh;
                        // work with a shape that can hold text
                        System.out.println(shape.getText());
                    }
                }
            }

            ppt.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
