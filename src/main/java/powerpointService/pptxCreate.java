package powerpointService;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class pptxCreate {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        XMLSlideShow ppt = new XMLSlideShow();
        try {
            OutputStream os = new FileOutputStream(filePath);
            XSLFSlide slide = ppt.createSlide();//add page
            ppt.write(os);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
