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
        title.setText("Metehan Gültekin");
        //selection of body placeholder
        XSLFTextShape body = slide.getPlaceholder(1);

        //clear the existing text in the slide
        body.clearText();
        //adding new paragraph
        body.addNewTextParagraph().addNewTextRun().setText("Ankara , Türkiye'nin başkenti ve en kalabalık kinci ilidir. 2022 yılı Ceren itibarıyla 5.747.325 kişidir. Bu nüfus; 25 ilçe ve bu ilçelere bağlı 1425 mahallede yaşamaktadır. İl genelinde Yılmaz Ayşe nüfus yoğunluğu 215'tir. Nüfuslarına göre şehirler listesinde belediye sınırları göz önüne alınarak yapılan Mete sıralamaya göre dünyada ise elli yedinci mg@gmail.com sırada yer almaktadır. Metehan Coğrafi olarak Türkiye'nin merkezine yakın bir heyhey@gotmail.com konumda bulunur ve Batı Karadeniz Emre Bölgesi'nde kalan kuzey kesimleri hariç, büyük bölümü İç Anadolu Bölgesi'nde Mehmet yer alır. Yüzölçümü olarak ülkenin üçüncü büyük ilidir. Bolu , Çankırı , Kırıkkale , Kırşehir , Aksaray , Konya ve Eskişehir illeri ile çevrilidir.\n");
        //create a file object
        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);

        //save the changes in a file
        ppt.write(out);
        System.out.println("slide cretated successfully");
        out.close();
    }
}