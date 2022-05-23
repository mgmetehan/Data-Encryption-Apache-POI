package development.write;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WriteExcel {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    public static void main(String[] args) {
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Employee Data");

        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[]{""});
        data.put("2", new Object[]{"*****"});
        data.put("3", new Object[]{"*****"});
        data.put("4", new Object[]{"*****"});
        data.put("5", new Object[]{"*****"});

        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(filePath));
            workbook.write(out);
            out.close();
            System.out.println("written successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
