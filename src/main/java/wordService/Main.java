package wordService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.docx";

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
        r.ReadData(clonedWb.getAbsolutePath());
        originalWb.renameTo(new File(filePath));
        //ör tc 22260299888 / 11118495268
    }
}
