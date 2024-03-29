package wordService.table;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WordReplaceText11 {

    private static final String SOURCE_FILE = "C:\\Users\\mgmet\\Desktop\\deneme.docx";
    private static final String OUTPUT_FILE = "C:\\Users\\mgmet\\Desktop\\new-lipsum.doc";

    public static void main(String[] args) throws Exception {
        WordReplaceText11 instance = new WordReplaceText11();
        try (HWPFDocument doc = new HWPFDocument(Files.newInputStream(Paths.get(SOURCE_FILE)))) {

                HWPFDocument newDoc = instance.replaceText(doc, "Levent", "*****");
                instance.saveDocument(newDoc, OUTPUT_FILE);

        }
    }

    private HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range range = doc.getRange();
        for (int numSec = 0; numSec < range.numSections(); ++numSec) {
            Section sec = range.getSection(numSec);
            for (int numPara = 0; numPara < sec.numParagraphs(); numPara++) {
                Paragraph para = sec.getParagraph(numPara);
                for (int numCharRun = 0; numCharRun < para.numCharacterRuns(); numCharRun++) {
                    CharacterRun charRun = para.getCharacterRun(numCharRun);
                    String text = charRun.text();
                    if (text.contains(findText)) {
                        charRun.replaceText(findText, replaceText);
                    }
                }
            }
        }
        return doc;
    }

    private HWPFDocument openDocument(String file) throws Exception {
        URL res = getClass().getClassLoader().getResource(file);
        HWPFDocument document = null;
        if (res != null) {
            document = new HWPFDocument(new POIFSFileSystem(
                    new File(res.getPath())));
        }
        return document;
    }

    private void saveDocument(HWPFDocument doc, String file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}