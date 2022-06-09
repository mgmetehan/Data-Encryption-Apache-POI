package excelService;

public class NewFilePath {
    public static String createNewFilePath(String path) {
        for (int i = 0, j = 0; i < path.length(); i++) {
            j += 1;
            if (path.substring(i, j).equals(".")) {
                System.out.println("New File Path Created.");
                path = path.substring(0, j - 1) + "_mask" + path.substring(j - 1, path.length());
                return path;
            }
        }
        System.out.println("Failed to Create New File Path!!");
        return null;
    }
}
