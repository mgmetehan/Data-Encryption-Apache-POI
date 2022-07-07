package pptx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class TitleAndBodyWriter {
    public static void main(String args[]) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme1.pptx";

        //creating presentation
        XMLSlideShow ppt = new XMLSlideShow();

        //getting the slide master object
        XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);

        //select a layout from specified list
        XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);

        //creating a slide with title and content layout
        XSLFSlide slide = ppt.createSlide(slidelayout);

        //selection of title place holder
        XSLFTextShape title = slide.getPlaceholder(0);

        //setting the title in it
        title.setText("introduction");

        //selection of body placeholder
        XSLFTextShape body = slide.getPlaceholder(1);

        //clear the existing text in the slide
        body.clearText();

        //adding new paragraph
        body.addNewTextParagraph().addNewTextRun().setText("this is  my first slide body");

        //create a file object
        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);

        //save the changes in a file
        ppt.write(out);
        System.out.println("slide cretated successfully");
        out.close();
    }
}