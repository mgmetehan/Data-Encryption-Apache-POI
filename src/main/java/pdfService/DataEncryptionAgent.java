package pdfService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class DataEncryptionAgent {
    public static void main(String[] args) {
        DataEncryptionAgent agent = new DataEncryptionAgent();
        ArrayList arr = new ArrayList();
        ArrayList noWhiteSpace = new ArrayList();
        String token = null;
        arr.add("Ankara");
        for (int i = 0; i < arr.size(); i++) {
            String str = (String) arr.get(i);
            str = str.replaceAll("\\s", "");
            noWhiteSpace.add(str);
        }
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";
        agent.EncryptionAgent(noWhiteSpace, filePath, token);
    }

    public void EncryptionAgent(ArrayList arrList, String filePath, String token) {
        NewFilePath nFilePath = new NewFilePath();
        ArrayList noWhiteSpaceList = new ArrayList();
        String str;

        for (int i = 0; i < arrList.size(); i++) {
            str = (String) arrList.get(i);
            str = str.replaceAll("\\s", "");
            noWhiteSpaceList.add(str);
        }
        System.out.println(noWhiteSpaceList.size() + " " + noWhiteSpaceList);

        String type = nFilePath.typeOfFile(filePath);

        if (type.equals("pdf")) {

        } else {
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
                er.ReadCellData(noWhiteSpaceList, clonedWb.getAbsolutePath());
            } else if (type.equals("docx")) {
                WordReader wr = new WordReader();
                wr.ReadData(noWhiteSpaceList, clonedWb.getAbsolutePath());
            }
        }
    }
}
