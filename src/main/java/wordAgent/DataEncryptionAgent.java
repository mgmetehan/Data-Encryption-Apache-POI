package wordAgent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DataEncryptionAgent {
    public static void main(String[] args) {
        DataEncryptionAgent agent = new DataEncryptionAgent();
        agent.EncryptionAgent("Metehan","C:\\Users\\mgmet\\Desktop\\deneme.docx");
    }
    public void EncryptionAgent(String foundText, String filePath) {

        NewFilePath nFilePath = new NewFilePath();
        String maskPath = nFilePath.createNewFilePath(filePath);

        File originalWb = new File(filePath);
        File clonedWb = new File(maskPath);
        try {
            Files.copy(originalWb.toPath(), clonedWb.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WordReader r = new WordReader();
        r.ReadData(foundText,clonedWb.getAbsolutePath());
    }
}
