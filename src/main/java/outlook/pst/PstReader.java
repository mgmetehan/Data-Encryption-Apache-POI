package outlook.pst;

import com.pff.*;

import java.util.*;

public class PstReader {
    public static void main(String[] args) {
        String path = "C:\\Users\\mgmet\\Desktop\\8.pst";
        new PstReader(path);
    }

    public PstReader(String filename) {
        try {
            PSTFile pstFile = new PSTFile(filename);
            System.out.println("Main Filename: " + pstFile.getMessageStore().getDisplayName());
            processFolder(pstFile.getRootFolder());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void processFolder(PSTFolder folder) throws PSTException, java.io.IOException {
        System.out.println("Child Filename: " + folder.getDisplayName());

        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processFolder(childFolder);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {
            PSTMessage email = (PSTMessage) folder.getNextChild();
            while (email != null) {
                System.out.println("------------------------------------------");
                System.out.println("Email: " + email.getSubject());
                System.out.println("Body: " + email.getBody());
                email = (PSTMessage) folder.getNextChild();
            }
            System.out.println("------------------------------------------");
        }
    }
}