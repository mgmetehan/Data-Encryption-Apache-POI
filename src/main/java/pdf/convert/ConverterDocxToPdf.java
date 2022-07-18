package pdf.convert;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConverterDocxToPdf {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String input = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
        String file = "C:\\Users\\mgmet\\Desktop\\deneme";
        String output = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

       /* InputStream in = new BufferedInputStream(new FileInputStream(output));
       IConverter converter = LocalConverter.builder().baseFolder(new File(file)).workerPool(20, 25, 2, TimeUnit.SECONDS).processTimeout(5, TimeUnit.SECONDS).build();

        Future<Boolean> conversion = converter.convert(in).as(DocumentType.MS_WORD).to(bo).as(DocumentType.PDF).prioritizeWith(1000) // optional
                .schedule();
        conversion.get();
        try (OutputStream outputStream = new FileOutputStream(input)) {
            bo.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        in.close();
        bo.close();*/
    }
}