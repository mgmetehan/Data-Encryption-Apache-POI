import java.io.File;
import java.io.FilenameFilter;

public class path {
    public static void main(String[] args) {
        File f = new File("C:\\Users\\mgmet\\Documents\\KVKK\\Test");
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("msg") || name.endsWith("ost") || name.endsWith("pst");
            }
        });
       for (File matchingFile : matchingFiles) {
           System.out.println(matchingFile);
       }
    }
}
