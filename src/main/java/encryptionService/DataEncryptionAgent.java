package encryptionService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DataEncryptionAgent {
    public static void main(String[] args) {
        DataEncryptionAgent agent = new DataEncryptionAgent();
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";
        agent.EncryptionAgent("Gültekin", filePath);
    }

    //xlsx docx
    public void EncryptionAgent(String foundText, String filePath) {
        NewFilePath nFilePath = new NewFilePath();
        String type = nFilePath.typeOfFile(filePath);
        String maskPath = nFilePath.createNewFilePath(filePath);

        File originalWb = new File(filePath);
        File clonedWb = new File(maskPath);
        try {
            Files.copy(originalWb.toPath(), clonedWb.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (type.equals("xlsx")) {
            ExcelReader er = new ExcelReader();
            er.ReadCellData(foundText, clonedWb.getAbsolutePath());
        } else if (type.equals("docx")) {
            WordReader wr = new WordReader();
            wr.ReadData(foundText, clonedWb.getAbsolutePath());
        }
    }
}
