package AgentWord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AgentWord {
    public void Agent(String foundText, String filePath) {

        NewFilePath nFilePath = new NewFilePath();
        String orjinalPath = nFilePath.createOrjinalFilePath(filePath);
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
        originalWb.renameTo(new File(filePath));
    }
}
