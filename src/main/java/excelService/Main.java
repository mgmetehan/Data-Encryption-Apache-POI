package excelService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    //Bir Hucrede boşluk bırakıp 2 adet tc yazarsan çalışmıyor
    public static void main(String[] args) {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

        NewFilePath nFilePath = new NewFilePath();
        String orjinalPath = nFilePath.createOrjinalFilePath(filePath);
        String maskPath = nFilePath.createNewFilePath(filePath);

        File originalWb = new File(filePath);
        File clonedWb = new File(orjinalPath);
        try {
            Files.copy(originalWb.toPath(), clonedWb.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Reader r = new Reader();
        r.ReadCellData(filePath);

        originalWb.renameTo(new File(maskPath));
        clonedWb.renameTo(new File(filePath));
        //ör tc 22260299888 / 11118495268
    }
}
