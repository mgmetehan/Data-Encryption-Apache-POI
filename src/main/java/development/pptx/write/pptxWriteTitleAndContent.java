package development.pptx.write;

import org.apache.poi.xslf.usermodel.*;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class pptxWriteTitleAndContent {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        XMLSlideShow ppt = new XMLSlideShow();
        try {
            OutputStream os = new FileOutputStream(filePath);
            XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
            XSLFSlideLayout tAndC = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
            XSLFSlide slide = ppt.createSlide(tAndC);
            XSLFTextShape title = slide.getPlaceholder(0);
            title.setText("Baslik baslik");
            XSLFTextShape body = slide.getPlaceholder(1);
            body.clearText();
            body.addNewTextParagraph().addNewTextRun().setText("AAAAAAAAAAAAAAAAAAAAAAAAAAA ");
            ppt.write(os);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
