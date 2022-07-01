package encryptionService;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordReader {
    public void ReadData(ArrayList arrList, String path) {
        TcValidation TcValidation = new TcValidation();
        WordWriter wwriter = new WordWriter();
        String result = "";
        String[] splitWords;
        Boolean check;
        String foundText = null, splitWordNoPunctuation;
        try {
            for (int j = 0; j < arrList.size(); j++) {
                foundText = (String) arrList.get(j);
                XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(path)));
                List<XWPFParagraph> list = doc.getParagraphs();
                for (XWPFParagraph paragraph : list) {
                    result = paragraph.getText();
                    splitWords = result.split("\\s+");
                    for (int i = 0; i < splitWords.length; i++) {
                        if (splitWords[i] == null) {
                            continue;
                        }
                        splitWordNoPunctuation = noPunctuation(splitWords[i]);

                        check = splitWordNoPunctuation.equals(foundText);
                        if (check) {
                            System.out.println(splitWordNoPunctuation + "//" + foundText);
                            wwriter.updateDocument(path, splitWordNoPunctuation);
                        }
                        if (TcValidation.TcNoCheck(result)) {
                            wwriter.updateDocument(path, splitWordNoPunctuation);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String noPunctuation(String word) {
        ArrayList arr = new ArrayList();
        arr.add(".");
        arr.add(",");
        arr.add(":");
        arr.add(";");
        arr.add("?");
        arr.add("!");
        arr.add("-");
        arr.add("\u201c");
        arr.add("\u201d");
        arr.add("\u2018");
        arr.add("\u2019");
        arr.add("\"");
        arr.add("'");
        arr.add("(");
        arr.add(")");
        arr.add("[");
        arr.add("]");
        arr.add("{");
        /*
        \u201c matches “ (left double quotation mark)
        \u201d matches ” (right double quotation mark)
        \u2018 matches ‘ (left single quotation mark)
        \u2019 matches ’ (right single quotation mark)
         */
        String[] parts = new String[0];
        for (int i = 0; i < arr.size(); i++) {
            if (word.startsWith(arr.get(i).toString()) || word.endsWith(arr.get(i).toString())) {
                word = word.replace((CharSequence) arr.get(i), "");
            }
        }

        return word;
    }
}