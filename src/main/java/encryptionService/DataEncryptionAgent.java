package encryptionService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class DataEncryptionAgent {
    public static void main(String[] args) {
        DataEncryptionAgent agent = new DataEncryptionAgent();
        ArrayList arr = new ArrayList();
        ArrayList noWhiteSpace = new ArrayList();
        arr.add("31.08.2000");

        for (int i = 0; i < arr.size(); i++) {
            String str = (String) arr.get(i);
            str = str.replaceAll("\\s", "");
            noWhiteSpace.add(str);
        }
        String filePath = "C:\\Users\\mgmet\\Desktop\\customer.xlsx";
        agent.EncryptionAgent(noWhiteSpace, filePath);
    }

    //xlsx docx
    public void EncryptionAgent(ArrayList arrList, String filePath) {
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
            er.ReadCellData(arrList, clonedWb.getAbsolutePath());
        } else if (type.equals("docx")) {
            WordReader wr = new WordReader();
            wr.ReadData(arrList, clonedWb.getAbsolutePath());
        }
    }
}
