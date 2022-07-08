package pptx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class FileContentReplacer {

    private final Pattern pattern;
    private final String replaceMent;

    public FileContentReplacer(String pattern, String replaceMent) {
        this.pattern = Pattern.compile(pattern);
        this.replaceMent = replaceMent;
    }

    public  String matchAndReplace(String line) {
        return pattern.matcher(line).replaceAll(replaceMent);
    }

    public  void matchAndReplace(File inFile, File outFile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(matchAndReplace(line));
                bufferedWriter.newLine();
            }
        } finally {
            bufferedReader.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }


    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\deneme.pptx";
        String out = "C:\\Users\\mgmet\\Desktop\\11.pptx";

        File inFile = new File(fileName);
        File outFile = new File(out);
        matchAndReplace(inFile, outFile);
    }
}