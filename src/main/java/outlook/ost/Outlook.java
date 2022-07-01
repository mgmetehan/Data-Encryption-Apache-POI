package outlook.ost;

import com.pff.PSTAttachment;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Vector;

public class Outlook {

    String locationForAttchedFileToWrite = "C:\\";
    boolean success = false;
    long emailID = 0;

//This function is used for reading the Folders and Email from Outlook
    // file path represents the location where PST/OST file store in the directory

    public void getOutlookMSG(String filePath) {
        try {
            // pass filepath into PSTFile object
            PSTFile pstFile = new PSTFile(filePath);

            // display the name of the header
            System.out.println(pstFile.getMessageStore().getDisplayName());

            // for getting the root folder of MS Outlook
            PSTFolder pstFolder = pstFile.getRootFolder();

            Vector<PSTFolder> folder = new Vector<PSTFolder>();

            // this will return all the subfolder under the root folder
            folder = pstFolder.getSubFolders();

            // this loop is used for processing all the folders of MS Outlook
            for (int i = 0; i < pstFolder.getSubFolderCount(); i++) {
                // for displaying the folder name
                System.out.println("Display Folder Name->" + folder.get(i).getDisplayName());

                Vector<PSTFolder> personalFolder = new Vector<PSTFolder>();

                // for getting the folder inside root folder
                personalFolder = folder.get(i).getSubFolders();

                // this loop is used for processing all the folders under root folder
                for (int j = 0; j < personalFolder.size(); j++) {
                    // display the name of the root folder
                    System.out.println("Display Name->" + personalFolder.get(j).getDisplayName());

                    // this condition is used for checking the name of the folder
                    // if folder name is Inbox then it will enter in IF body
                    if (personalFolder.get(j).getDisplayName().equalsIgnoreCase("Inbox")) {
                        // for checking inbox folder has email or not
                        if (personalFolder.get(j).getContentCount() > 0) {
                            // for getting the email message
                            PSTMessage email = (PSTMessage) personalFolder.get(j).getNextChild();

                            // if email is not null then it will enter into the loop
                            while (email != null) {
                                // for getting the subject of the current processing email
                                String subject = email.getSubject();
                                System.out.println("Email: " + email.getSubject());

                                // for getting the email descriptor id which uniquely identify the email
                                emailID = email.getDescriptorNodeId();
                                System.out.println("Descriptor ID-->" + emailID);
                                // for checking mail is already read or not
                                if (!email.isRead()) {
                                    //count number of attached file
                                    int noOfAttachment = email.getNumberOfAttachments();
                                    System.out.println("Attachment numbers-->" + j + " " + noOfAttachment);

                                    // if number of attachment is greater than 0
                                    if (noOfAttachment > 0) {
                                        // for accessing the all attachment files
                                        for (int x = 0; x < noOfAttachment; x++) {
                                            // getting the attachment object
                                            PSTAttachment attach = email.getAttachment(x);
                                            InputStream attachmentStream = attach.getFileInputStream();

                                            // both long and short filenames can be used for attachments
                                            String filename = attach.getLongFilename();

                                            if (filename.isEmpty()) {
                                                // getting the attached file name
                                                filename = attach.getFilename();
                                            }
                                            // used for defining where to write the attached file
                                            FileOutputStream out = new FileOutputStream(locationForAttchedFileToWrite + filename);

                                            // check the existence of the directory file
                                            if ((new File(locationForAttchedFileToWrite)).exists()) {
                                                // if directory exists then first delete already exists file
                                                freeDirectory(locationForAttchedFileToWrite);
                                                success = true;
                                            } else {
                                                // if directory not exists then create the directory
                                                success = (new File(locationForAttchedFileToWrite)).mkdir();
                                            }
                                            if (success) {
                                                // 8176 is the block size used internally and should give the best performance

                                                int bufferSize = 8176;
                                                byte[] buffer = new byte[bufferSize];
                                                int count = attachmentStream.read(buffer);

                                                while (count == bufferSize) {
                                                    out.write(buffer);
                                                    count = attachmentStream.read(buffer);
                                                }
                                                byte[] endBuffer = new byte[count];

                                                // this code is used for saving the attachment in the directory at the given path
                                                // i.e.,locationForAttchedFileToWrite
                                                System.arraycopy(buffer, 0, endBuffer, 0, count);
                                                out.write(endBuffer);
                                                out.close();
                                                attachmentStream.close();
                                            } else {
                                                System.out.println("No such folder exists");
                                                System.exit(1);
                                            }
                                        }
                                    }
                                }
                                email = (PSTMessage) personalFolder.get(j).getNextChild();
                            }

                        }
                    } else {
                        continue;
                    }

                }
            }
        } catch (Exception ex) {
        }
    }


    // This function is used for deleting the directory where attachment file resides
    public static void freeDirectory(String location) {
        File directory = new File(location);

        if (directory.exists()) {
            File[] file = directory.listFiles();
            try {
                for (int indx = 0; indx < file.length; indx++) {
                    file[indx].delete();
                }
            } catch (Exception x) {
                // File permission problems are caught here.
                System.err.println(x);
            }
        }
    }

    // main function
    public static void main(String args[]) {
        new Outlook().getOutlookMSG("C:\\Users\\mgmet\\Desktop\\deneme.ost");
    }

}