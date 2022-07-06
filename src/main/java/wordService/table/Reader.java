package wordService.table;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

public class Reader {

    static String path = "C:\\Users\\mgmet\\Desktop\\Girişim Analizi ve Teknik Analiz Dosyası.docx";

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(path);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            Iterator bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = (IBodyElement) bodyElementIterator.next();

                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List tableList = element.getBody().getTables();
                    for (int m = 0; m < tableList.size(); m++) {
                        XWPFTable table = (XWPFTable) tableList.get(m);
                        System.out.println("Total Number of Rows of Table:" + table.getNumberOfRows());
                        for (int i = 0; i < table.getRows().size(); i++) {

                            for (int j = 0; j < table.getRow(i).getTableCells().size(); j++) {
                                System.out.println(table.getRow(i).getCell(j).getText());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}