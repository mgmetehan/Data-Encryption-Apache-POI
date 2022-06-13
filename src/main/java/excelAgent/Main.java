package excelAgent;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        Main agent = new Main();
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";
        agent.EncryptionAgent("Metehan",filePath);
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

        Reader r = new Reader();
        r.ReadCellData(foundText,clonedWb.getAbsolutePath());
    }
}
