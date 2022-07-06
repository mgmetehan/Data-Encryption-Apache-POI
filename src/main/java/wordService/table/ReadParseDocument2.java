package wordService.table;

import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class ReadParseDocument2 {
    //Update
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";
        String word = "Kurucu";
        try {
            XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(fileName)));
            /*XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            String docText = xwpfWordExtractor.getText();
            System.out.println(docText);*/

            Iterator<IBodyElement> docElementsIterator = doc.getBodyElementsIterator();

            //Iterate through the list and check for table element type
            while (docElementsIterator.hasNext()) {
                IBodyElement docElement = docElementsIterator.next();
                if ("TABLE".equalsIgnoreCase(docElement.getElementType().name())) {
                    //Get List of table and iterate it
                    List<XWPFTable> xwpfTableList = docElement.getBody().getTables();
                    for (XWPFTable xwpfTable : xwpfTableList) {
                        //System.out.println("Total Rows : " + xwpfTable.getNumberOfRows());
                        for (int i = 0; i < xwpfTable.getRows().size(); i++) {
                            for (int j = 0; j < xwpfTable.getRow(i).getTableCells().size(); j++) {
                                String check = xwpfTable.getRow(i).getCell(j).getText();
                                if (check.equals(word)) {
                                    xwpfTable.getRow(i).getCell(j).setText("*****");
                                    System.out.println(xwpfTable.getRow(i).getCell(j).getText());
                                    System.out.println("aaaaaaaaaaaaaaaa");
                                }

                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}