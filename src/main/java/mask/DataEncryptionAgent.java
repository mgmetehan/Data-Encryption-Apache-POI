package mask;
/*
package com.omreon.filediscoveryagent.mask;

import com.omreon.filediscoveryagent.beans.FileDiscoveryResult;
import com.omreon.filediscoveryagent.utils.FileDiscoveryHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DataEncryptionAgent {
    //private FileDiscoveryHelper fileDiscoveryHelper;

    public void EncryptionAgent(String token, List<FileDiscoveryResult> fileDiscoveryResults) {
        NewFilePath nFilePath = new NewFilePath();
        ArrayList noWhiteSpaceList = new ArrayList();
        String str, filePath;
        ArrayList arrList = new ArrayList<>();

      */
/*
      if (fileDiscoveryResults != null && fileDiscoveryResults.size() > 0) {
            String json = FileDiscoveryHelper.generateJsonString(fileDiscoveryResults);
            String responseStr = fileDiscoveryHelper.invokeAPI(token, "POST", FileDiscoveryHelper.FILE_DISCOVERY_MASK_URL, json);
            System.out.println("ID: " + responseStr);
        }
        *//*



        for (int i = 0; i < fileDiscoveryResults.size(); i++) {
            arrList.add(fileDiscoveryResults.get(i).getFoundText());
        }
        filePath = fileDiscoveryResults.get(0).getFileFullPath();


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
        if (type.equals("xlsx")) {
            ExcelReader er = new ExcelReader();
            er.ReadCellData(noWhiteSpaceList, clonedWb.getAbsolutePath());
        } else if (type.equals("docx")) {
            WordReader wr = new WordReader();
            wr.ReadData(noWhiteSpaceList, clonedWb.getAbsolutePath());
        }
    }
}
*/
