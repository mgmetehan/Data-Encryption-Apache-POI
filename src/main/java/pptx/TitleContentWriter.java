package pptx;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class TitleContentWriter {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme1.pptx";
        XMLSlideShow ppt = new XMLSlideShow();
        try (OutputStream os = new FileOutputStream(fileName)) {
            XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
            XSLFSlideLayout tc = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
            XSLFSlide slide = ppt.createSlide(tc);
            XSLFTextShape title = slide.getPlaceholder(0);
            title.setText("Title here");
            XSLFTextShape body = slide.getPlaceholder(1);
            body.clearText();
            body.addNewTextParagraph().addNewTextRun().setText("This is a new slide created using Java program.");
            ppt.write(os);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}