package outlook.msg;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class path {
    public static void main(String[] args) {
        String path = "C:\\Users\\mgmet\\Documents\\KVKK\\Test";
        List<String> results = new ArrayList<>();

        File[] files = new File(path).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getPath());
            }
        }

        for (String result : results) {
            System.out.println(result);
        }
    }
}
