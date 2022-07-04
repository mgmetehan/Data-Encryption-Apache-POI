package encryptionService;

import java.util.ArrayList;

public class TxtReader {
    TcValidation TcValidation = new TcValidation();
    TxtWriter txtWriter = new TxtWriter();

    public void Read(ArrayList<String> wordList, String path) {
        for (String word : wordList) {
            if (TcValidation.TcNoCheck(word)) {
                txtWriter.txtUpdate(word, path);
            }
            txtWriter.txtUpdate(word, path);

        }
    }
}
