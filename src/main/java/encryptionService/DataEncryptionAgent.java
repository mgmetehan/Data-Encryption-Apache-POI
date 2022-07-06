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
        arr.add("Teknik");
        arr.add("Çalıkuş");
        arr.add("Manisa");
        arr.add("Ömer ");
        for (int i = 0; i < arr.size(); i++) {
            String str = (String) arr.get(i);
            str = str.replaceAll("\\s", "");
            noWhiteSpace.add(str);
        }
        String filePath = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        agent.EncryptionAgent(noWhiteSpace, filePath);
    }

    public void EncryptionAgent(ArrayList arrList, String filePath) {
        NewFilePath nFilePath = new NewFilePath();
        ArrayList noWhiteSpaceList = new ArrayList();
        String str;

        if (filePath.toString().endsWith("msg") || filePath.toString().endsWith("pst")) {
            System.out.println("Currently not supported!");
        } else {
            String maskPath = nFilePath.createNewFilePath(filePath);
            File originalWb = new File(filePath);
            File clonedWb = new File(maskPath);
            try {
                Files.copy(originalWb.toPath(), clonedWb.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < arrList.size(); i++) {
                str = (String) arrList.get(i);
                str = str.replaceAll("\\s", "");
                noWhiteSpaceList.add(str);
            }
            System.out.println(noWhiteSpaceList.size() + " " + noWhiteSpaceList);

            if (filePath.toString().endsWith("xlsx")) {
                ExcelReader er = new ExcelReader();
                er.ReadCellData(noWhiteSpaceList, clonedWb.getAbsolutePath());
            } else if (filePath.toString().endsWith("docx")) {
                WordReader wr = new WordReader();
                wr.ReadData(noWhiteSpaceList, clonedWb.getAbsolutePath());
            } else if (filePath.toString().endsWith("txt")) {
                TxtReader txtR = new TxtReader();
                txtR.Read(noWhiteSpaceList, clonedWb.getAbsolutePath());
            }
        }
    }
}
