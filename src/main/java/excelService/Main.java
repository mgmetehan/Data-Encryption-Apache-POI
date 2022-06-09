package excelService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    //Bir Hucrede boşluk bırakıp 2 adet tc yazarsan çalışmıyor
    public static void main(String[] args) {
        String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

        File originalWb = new File("C:\\Users\\mgmet\\Desktop\\deneme.xlsx");
        File clonedWb = new File("C:\\Users\\mgmet\\Desktop\\deneme_orjinal.xlsx");
        try {
            Files.copy(originalWb.toPath(), clonedWb.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Reader r = new Reader();
        r.ReadCellData(filePath);
        originalWb.renameTo(new File("C:\\Users\\mgmet\\Desktop\\deneme_mask.xlsx"));
        clonedWb.renameTo(new File("C:\\Users\\mgmet\\Desktop\\deneme.xlsx"));
        //ör tc 22260299888 / 11118495268
    }
}
