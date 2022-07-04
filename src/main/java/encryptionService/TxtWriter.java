package encryptionService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TxtWriter {

    public void txtUpdate(String word,String filePath) {

        try {

            Scanner sc = null;
            sc = new Scanner(new File(filePath));

            StringBuffer buffer = new StringBuffer();
            while (sc.hasNextLine()) {
                buffer.append(sc.nextLine() + System.lineSeparator());
            }
            String fileContents = buffer.toString();

            sc.close();
            String oldLine = word;
            String newLine = "*****";
            fileContents = fileContents.replaceAll(oldLine, newLine);
            FileWriter writer = new FileWriter(filePath);
            System.out.println("Txt is updated successfully");
            writer.append(fileContents);
            writer.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
