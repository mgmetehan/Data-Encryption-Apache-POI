package development.pptx.read;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReadPptx {
    public static void main(String args[]) throws IOException {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        //creating a slideshow
        File file = new File(filePath);
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));

        //get slides
        List<XSLFSlide> slide = ppt.getSlides();

        //getting the shapes in the presentation
        System.out.println("Shapes in the presentation:");
        for (int i = 0; i < slide.size(); i++) {
            List<XSLFShape> sh = slide.get(i).getShapes();
            for (int j = 0; j < sh.size(); j++) {
                //name of the shape
                System.out.println(sh.get(j).getShapeName());
            }
        }
        FileOutputStream out = new FileOutputStream(file);
        ppt.write(out);
        out.close();
    }
}
