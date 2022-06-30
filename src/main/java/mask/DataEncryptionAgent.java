package mask;
/*
package com.omreon.filediscoveryagent.mask;

import com.omreon.filediscoveryagent.beans.FileDiscoveryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DataEncryptionAgent {
    Logger logger = LoggerFactory.getLogger(DataEncryptionAgent.class);

    public void EncryptionAgent(String token, List<FileDiscoveryResult> fileDiscoveryResults) {
        NewFilePath nFilePath = new NewFilePath();
        ArrayList noWhiteSpaceList = new ArrayList();
        String str, filePath;
        ArrayList arrList = new ArrayList<>();

        for (int i = 0; i < fileDiscoveryResults.size(); i++) {
            arrList.add(fileDiscoveryResults.get(i).getFoundText());
        }
        filePath = fileDiscoveryResults.get(0).getFileFullPath();

        if (filePath.toString().endsWith("msg") || filePath.toString().endsWith("pst")) {
            logger.info("Currently not supported!");
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
            }
        }
    }
}
*/
