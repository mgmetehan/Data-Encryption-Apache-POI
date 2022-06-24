package pdf.excel;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ConvertToPDF {

    public static void main(String[] args) throws FileNotFoundException, DocumentException, IOException {
        String excelpath, basicpath;
        ConvertToPDF excel = new ConvertToPDF();
        String path[] = excel.choosefile();
        excelpath = path[0];
        basicpath = path[1];
        Scanner scan = new Scanner(System.in);
        String pdfname;
        System.out.print("PDF name: ");
        pdfname = scan.nextLine();
        pdfname = pdfname + ".pdf";
        String pdfpath = basicpath + "\\" + pdfname;

        excel.createpdf(pdfpath, excelpath);
        System.out.print("Data succesfully store a : ");
        System.out.println(pdfpath);
    }

    public void readNwrite(Document document, String excelpath) throws IOException, DocumentException {
        try (Workbook workbook = WorkbookFactory.create(new File(excelpath))) {
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            int sheetnum = 0;
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                workbook.getSheetAt(sheetnum);
                sheetnum++;

                DataFormatter dataFormatter = new DataFormatter();

                PdfPTable table = new PdfPTable(7);
                Paragraph p;
                Font normal = new Font(FontFamily.TIMES_ROMAN, 12);
                boolean title = true;

                for (Row row : sheet) {
                    if (row.getRowNum() >= 4 && row.getRowNum() <= 155) {
                        for (Cell cell : row) {

                            String cellValue = dataFormatter.formatCellValue(cell);
                            table.addCell(cellValue);
//                            System.out.println(cell.getRowIndex() + " " + cell.getColumnIndex());
//                            System.out.print(cellValue + "\t");

                        }

                    } else if (row.getRowNum() < 4) {
                        for (Cell cell : row) {

                            String cellValue = dataFormatter.formatCellValue(cell);

                            p = new Paragraph(cellValue, title ? normal : normal);
                            p.setAlignment(Element.ALIGN_JUSTIFIED);
                            document.add(p);
                        }

                    }
                }
                document.add(new Paragraph(" "));
                float[] columnWidths = new float[]{5f, 0f, 35f, 7f, 7f, 5f, 15f};
                table.setWidths(columnWidths);
                table.setTotalWidth(550);
                table.setLockedWidth(true);
                document.add(table);
                for (Row row : sheet) {
                    if (row.getRowNum() > 154) {
                        for (Cell cell : row) {

                            String cellValue = dataFormatter.formatCellValue(cell);

                            p = new Paragraph(cellValue, title ? normal : normal);
                            p.setAlignment(Element.ALIGN_JUSTIFIED);
                            document.add(p);
                        }

                    }
                }
            }
        }
    }

    public void createpdf(String pdfpath, String excelpath) throws DocumentException, FileNotFoundException, IOException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfpath));
        document.open();
        readNwrite(document, excelpath);
        document.close();
    }


    public String[] choosefile() {
        //Choose File to Read
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Excel To PDF");
        //only choose excel file format
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel", "xls", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog(null);
        File selectedfile = fileChooser.getCurrentDirectory();
        String[] ret = new String[2];
        ret[0] = fileChooser.getSelectedFile().getPath();
        ret[1] = selectedfile.getPath();
        return ret;
    }
}
