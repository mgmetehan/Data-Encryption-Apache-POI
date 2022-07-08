package pptx;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;

public class ReadPPTXAllText {

    public static void main(String[] args) throws Exception {
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        XMLSlideShow slideShow = new XMLSlideShow(new FileInputStream(fileName));
        for (XSLFSlide slide : slideShow.getSlides()) {
            CTSlide ctSlide = slide.getXmlObject();
            XmlObject[] allText = ctSlide.selectPath(
                    "declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main'.//a:t"
            );
            for (int i = 0; i < allText.length; i++) {
                if (allText[i] instanceof XmlString) {
                    XmlString xmlString = (XmlString) allText[i];
                    String text = xmlString.getStringValue();
                    //System.out.println(text);
                    if (text.equals("Hilal")) {
                        text = text.replace("Hilal", "******");
                        xmlString.setStringValue(text);
                        System.out.println(text);
                    }
                }
            }
        }

        FileOutputStream out = new FileOutputStream("C:\\Users\\mgmet\\Desktop\\deneme11.pptx");
        slideShow.write(out);
        slideShow.close();
        out.close();
    }
}  /* if (text.toLowerCase().contains("Metehan")) {
        String result = text.replaceAll("Metehan", "XXXXXXXX");
        // String newText = text.replaceAll("(?i)" + "test", "XXXXXXXX");
        xmlString.setStringValue(result);
        }*/