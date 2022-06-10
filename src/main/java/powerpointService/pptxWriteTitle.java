package powerpointService;

import org.apache.poi.xslf.usermodel.*;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class pptxWriteTitle {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        XMLSlideShow ppt = new XMLSlideShow();
        try {
            OutputStream os = new FileOutputStream(filePath);
            XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
            XSLFSlideLayout titleLayout = defaultMaster.getLayout(SlideLayout.TITLE);
            XSLFSlide slide = ppt.createSlide(titleLayout);
            XSLFTextShape title = slide.getPlaceholder(0);
            title.setText("Mete mete");
            ppt.write(os);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
