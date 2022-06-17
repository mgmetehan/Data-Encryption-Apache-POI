package mask;

public class NewFilePath {
    public static String createNewFilePath(String path) {
        String maskPath;
        for (int i = 0, j = 0; i < path.length(); i++) {
            j += 1;
            if (path.substring(i, j).equals(".")) {
                System.out.println("New File Path Created.");
                maskPath = path.substring(0, j - 1) + "_mask" + path.substring(j - 1, path.length());
                return maskPath;
            }
        }
        System.out.println("Failed to Create New File Path!!");
        return null;
    }

    //xlsx docx
    public static String typeOfFile(String path) {
        String fileType;
        for (int i = 0, j = 0; i < path.length(); i++) {
            j += 1;
            if (path.substring(i, j).equals(".")) {
                fileType = path.substring(j, path.length());
                return fileType;
            }
        }
        return null;
    }
}
