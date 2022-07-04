package txt;


import java.io.*;

public class reader {
    private static String path = "C:\\Users\\mgmet\\Desktop\\a.txt";

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String[] splited;
            while (line != null) {
                splited = line.split("\\s+");
                System.out.println(splited[0]);
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
                System.out.println();
            }
            String everything = sb.toString();
            // System.out.println(everything);
        } finally {
            br.close();
        }
    }
}
