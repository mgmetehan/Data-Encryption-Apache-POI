package pdf.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

public class DocxToPdf {

    public static void main(String[] args) {
        String input = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
        String output = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";

     /*   File inputWord = new File(input);
        File outputFile = new File(output);
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}