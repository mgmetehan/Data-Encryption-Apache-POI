package txt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Update {
    public static void main(String args[]) {
        try {
            String filePath = "C:\\Users\\mgmet\\Desktop\\a.txt";
            Scanner sc = null;
            sc = new Scanner(new File(filePath));

            StringBuffer buffer = new StringBuffer();
            while (sc.hasNextLine()) {
                buffer.append(sc.nextLine() + System.lineSeparator());
            }
            String fileContents = buffer.toString();
            System.out.println("Contents of the file: " + fileContents);

            sc.close();
            String oldLine = "sgdds";
            String newLine = "31";
            fileContents = fileContents.replaceAll(oldLine, newLine);
            FileWriter writer = new FileWriter(filePath);
            writer.append(fileContents);
            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
