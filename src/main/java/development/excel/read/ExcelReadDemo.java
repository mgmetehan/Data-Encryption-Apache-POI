package development.excel.read;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class ExcelReadDemo {
    private static final String filePath = "C:\\Users\\mgmet\\Desktop\\deneme.xlsx";

    public static void main(String[] args) {
        try {
            FileInputStream excelFile=new FileInputStream(new File(filePath));
            Workbook book=new XSSFWorkbook(excelFile);
            Sheet paper=book.getSheetAt(0);//1. calıisma sayfasisini alir
            //Sayfadaki butun satirlari al
            Iterator<Row> step=paper.iterator();
            while(step.hasNext()) {
                Row line =step.next();//sradaki satiri okur
                Iterator<Cell> stepCell=line.iterator();
                //o satirdaki tum hucreleri al
                while(stepCell.hasNext()) {
                    //o satirdaki siradi hücreyi okur
                    Cell cell=stepCell.next();
                    if(cell.getCellType()== CellType.STRING){
                        System.out.print(cell.getStringCellValue()+"  ");
                    }else if(cell.getCellType()== CellType.NUMERIC){
                        System.out.print(cell.getNumericCellValue()+"  ");
                    }
                }
                System.out.println("");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
