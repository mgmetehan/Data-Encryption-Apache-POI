package encryptionService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TxtReader {
    TcValidation TcValidation = new TcValidation();
    TxtWriter txtWriter = new TxtWriter();

    public void Read(ArrayList<String> wordList, String path) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String[] splited;

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            splited = everything.split("\\s+");
            for (String word : splited) {
                if (TcValidation.TcNoCheck(word)) {
                    txtWriter.txtUpdate(word, path);
                }
            }
            for (String word : wordList) {
                txtWriter.txtUpdate(word, path);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        for (String word : wordList) {

            txtWriter.txtUpdate(word, path);

        }
    }
}
