package development;

import java.io.FileOutputStream;

public class createFile {
    public static void main(String[]args)
    {
        try
        {
            String filename = "C:/Users/mgmet/Desktop/deneme.docx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            fileOut.close();
            System.out.println("Excel file has been generated successfully.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
