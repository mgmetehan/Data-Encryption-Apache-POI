package development.excel.update;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class UpdateRowExcel {
    //Tüm sütunu değiştiriyor
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    public static void main(String[] args) {
        UpdateRowExcel updateExcel = new UpdateRowExcel();
        updateExcel.updateCellData(1, 0);
    }

    public void updateCellData(int vRow, int vColumn) {
        try {
            // Create an object of FileInputStream class to read excel file
            FileInputStream fis = new FileInputStream(new File(filePath));

            // Create object of XSSFWorkbook class
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // Read excel sheet by sheet name
            XSSFSheet sheet = workbook.getSheetAt(0);


            for (int rowLastIndex = 0; rowLastIndex < sheet.getLastRowNum(); rowLastIndex++) {
                XSSFCell cell = sheet.getRow(vRow++).getCell(vColumn);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("*********");
            }

            FileOutputStream out = new FileOutputStream(new File(filePath));
            workbook.write(out);

            System.out.println("update successfully.");
            workbook.close();
            out.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
