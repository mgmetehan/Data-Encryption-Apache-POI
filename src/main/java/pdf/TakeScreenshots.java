package pdf;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class TakeScreenshots {

    public static void main(String[] args) {
        try {

            XWPFDocument docx = new XWPFDocument();
            XWPFRun run = docx.createParagraph().createRun();
            String outPath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
            FileOutputStream out = new FileOutputStream(outPath);

            for (int counter = 1; counter <= 5; counter++) {
                captureScreenShot(docx, run, out);
                TimeUnit.SECONDS.sleep(1);
            }

            docx.write(out);
            out.flush();
            out.close();
            docx.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void captureScreenShot(XWPFDocument docx, XWPFRun run, FileOutputStream out) throws Exception {

        String screenshot_name = System.currentTimeMillis() + ".png";
        BufferedImage image = new Robot()
                .createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        File file = new File("C:\\Users\\mgmet\\Desktop\\" + screenshot_name);
        ImageIO.write(image, "png", file);
        InputStream pic = new FileInputStream("C:\\Users\\mgmet\\Desktop\\" + screenshot_name);
        run.addBreak();
        run.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, screenshot_name, Units.toEMU(350), Units.toEMU(350));
        pic.close();
        file.delete();
    }

}