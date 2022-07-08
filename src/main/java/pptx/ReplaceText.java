package pptx;

import com.spire.presentation.*;

import java.util.HashMap;
import java.util.Map;

public class ReplaceText {
    public static void main(String []args) throws Exception {
        //Create a Presentation instance
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        Presentation ppt = new Presentation();
        //Load a PowerPoint document
        ppt.loadFromFile(fileName);

        //Get the first slide
        ISlide slide = ppt.getSlides().get(0);

        //Replace the first occurrence of a text
        //slide.replaceFirstText("Old String", "New String", false);
        //Replace all the occurrences of a text
        slide.replaceAllText("Ankara", "New String", false);

        //Save the document
        ppt.saveToFile("ReplaceText.pptx", FileFormat.PPTX_2016);
    }
}