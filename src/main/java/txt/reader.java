package txt;


import java.io.*;

public class reader {
    private static String path = "C:\\Users\\mgmet\\Desktop\\a.txt";

    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String[] splited;

            while (line != null) {
                /*splited = line.split("\\s+");
                System.out.println(splited[0]);*/
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            splited = everything.split("\\s+");
            for (String word : splited) {
                System.out.println(word);
            }
            // System.out.println(everything);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
