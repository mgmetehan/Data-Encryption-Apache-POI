package pdfService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class DataEncryptionAgent {
    public static void main(String[] args) {
        encryptionService.DataEncryptionAgent agent = new encryptionService.DataEncryptionAgent();
        ArrayList arr = new ArrayList();
        ArrayList noWhiteSpace = new ArrayList();
        String token = null;
        arr.add("Ankara");
        arr.add("Ceren");

        for (int i = 0; i < arr.size(); i++) {
            String str = (String) arr.get(i);
            str = str.replaceAll("\\s", "");
            noWhiteSpace.add(str);
        }
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.pdf";
        agent.EncryptionAgent(noWhiteSpace, filePath, token);
    }

    public void EncryptionAgent(ArrayList arrList, String filePath, String token) {
        //public void EncryptionAgent(String token, List<FileDiscoveryResult> fileDiscoveryResults) {
        encryptionService.NewFilePath nFilePath = new encryptionService.NewFilePath();
        ArrayList noWhiteSpaceList = new ArrayList();
        String str;


        String type = nFilePath.typeOfFile(filePath);
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
        System.out.println("aa");

        WordReader wr = new WordReader();
        System.out.println("aa");
        wr.ReadData(noWhiteSpaceList, clonedWb.getAbsolutePath());

    }
}