package development.pptx.read;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.FileInputStream;

public class pptxRead {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        String[] splitWords;
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            XMLSlideShow ppt = new XMLSlideShow(inputStream);
            POIXMLProperties.CoreProperties props = ppt.getProperties().getCoreProperties();

            for (XSLFSlide slide : ppt.getSlides()) {
                XSLFShape[] shapes = slide.getShapes().toArray(new XSLFShape[0]);
                for (XSLFShape shape : shapes) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape textShape = (XSLFTextShape) shape;
                        String text = textShape.getText();
                        splitWords = text.split("\\s+");
                        for (int i = 0; i < splitWords.length; i++) {
                            if (splitWords[i] == null) {
                                continue;
                            }
                            System.out.println(splitWords[i]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
/*if you need to read the title use it
String title = props.getTitle();
System.out.println("Title: " + title);
 */