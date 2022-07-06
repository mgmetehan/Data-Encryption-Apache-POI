/*
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;
import com.omreon.filediscoveryagent.beans.*;
import com.omreon.filediscoveryagent.enums.FileDiscoveryStatus;
import com.omreon.filediscoveryagent.enums.Stages;
import com.omreon.filediscoveryagent.job.RegexCategoriesRefresher;
import com.omreon.filediscoveryagent.mask.DataEncryptionAgent;
import com.omreon.filediscoveryagent.services.external.*;
import com.omreon.filediscoveryagent.utils.FileDiscoveryHelper;
import com.omreon.filediscoveryagent.utils.PropertiesViaDatabase;
import com.omreon.filediscoveryagent.utils.PropertiesViaFile;
import com.omreon.filediscoveryagent.utils.RegexValidator;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ProcessService {

    Logger logger = LoggerFactory.getLogger(ProcessService.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy.HH.mm.ss");

    private int processedFilesCount = 0;
    private int sensitiveDataCount = 0;

    private String token = "";
    private String fileDiscoveryId = "";
    private int fileDiscoverySummaryId;

    private AuthenticationService authenticationService;
    private FileDiscoveryService fileDiscoveryService;
    private FileDiscoveryTaskService fileDiscoveryTaskService;
    private FileDiscoverySummaryService fileDiscoverySummaryService;
    private FileDiscoveryResultService fileDiscoveryResultService;
    private PropertiesViaDatabase propertiesViaDatabase;
    private PropertiesViaFile props;

    private List<RegexCategory> categories = new ArrayList<>();

    private FileDiscoveryHelper fileDiscoveryHelper;

    private List<FileDiscoveryResult> fileDiscoveryResults = new ArrayList<>();

    private DataEncryptionAgent agent = new DataEncryptionAgent();

    @Autowired
    public ProcessService(AuthenticationService authenticationService, PropertiesViaDatabase propertiesViaDatabase, FileDiscoveryService fileDiscoveryService, FileDiscoverySummaryService fileDiscoverySummaryService, FileDiscoveryTaskService fileDiscoveryTaskService, FileDiscoveryResultService fileDiscoveryResultService, FileDiscoveryHelper fileDiscoveryHelper, RegexCategoriesRefresher regexCategoriesRefresher,
                          PropertiesViaFile props) {

        long start = System.currentTimeMillis();

        logger.info("ProcessService has just started.");
        logger.info("Properties= " + propertiesViaDatabase.toString());

        this.authenticationService = authenticationService;
        this.fileDiscoveryService = fileDiscoveryService;
        this.fileDiscoverySummaryService = fileDiscoverySummaryService;
        this.fileDiscoveryResultService = fileDiscoveryResultService;
        this.fileDiscoveryTaskService = fileDiscoveryTaskService;
        this.fileDiscoveryHelper = fileDiscoveryHelper;
        this.propertiesViaDatabase = propertiesViaDatabase;
        this.props = props;

        regexCategoriesRefresher.getAllRegexCategory();
        process();

        long finish = System.currentTimeMillis();
        logger.info("ProcessService has been completed in " + (finish - start) + " milliseconds");

    }

    //Runs in every 2 minutes
    @Scheduled(cron = "${cron.expression}")
    private void process() {

        long start = System.currentTimeMillis();
        logger.info("ProcessService:process has just started.");

        // Check If Stage 2 is already exist in database (filediscovery_property table), if it's not just skip this step
        if (Stages.isAnExistingStageInProperties(propertiesViaDatabase.getStages(), Stages.PROCESS.getStageId()) == false) {
            logger.info("ProcessService:process is skipped. To enable it, add '2' in Stages inside database.");
        } else {

            //Get New Token
            token = authenticationService.getToken();

            //Get Oldest Ready File Discovery Summary.
            //If it exists then start to process it, otherwise wait for more 2 minutes for another check
            FileDiscoverySummary latestFds = fileDiscoverySummaryService.getOldestReadyFileDiscoverySummaryById(token);

            if (latestFds == null) {
                logger.info("ProcessService:process did NOT found any Ready FileDiscoverySummary and will try again after 2 minutes");
            } else {
                logger.info("ProcessService:process found a Ready FileDiscoverySummary(" + latestFds.getId() + ") and now will start to process");

                fileDiscoverySummaryId = latestFds.getId();

                while (process100(latestFds)) {

                }

                //Get New Token
                token = authenticationService.getToken();

                //Update Related File Discovery Summary 'counts' in Database
                //Also update its status to COMPLETED
                fileDiscoverySummaryService.updateFileDiscoverySummaryCountsAndCloseIt(token, fileDiscoverySummaryId, sensitiveDataCount, processedFilesCount);

                //Close File Discovery
                fileDiscoveryService.updateFileDiscovery(token, latestFds.getFileDiscovery().getId() + "");

                //Create FileDiscoveryResults in Database
                fileDiscoveryResultService.createFileDiscoveryResultsByArrayList(token, fileDiscoveryResults);
                //mask try

                //fileDiscoveryResultService.createFileDiscoveryResultsByArrayList(token, Integer.parseInt(fileDiscoverySummaryId), fileName, categories, sensitiveDataCount);
                maskResults();

                sensitiveDataCount = 0;
                processedFilesCount = 0;
                fileDiscoveryResults = new ArrayList<>();
            }
        }

        long finish = System.currentTimeMillis();
        logger.info("ProcessService:process has been completed in " + (finish - start) + " milliseconds");

    }

    private boolean process100(FileDiscoverySummary fds) {

        //Get New Token
        token = authenticationService.getToken();

        List<FileDiscoveryTask> tasks = fileDiscoveryTaskService.getOldest100ReadyFileDiscoveryTaskSummaryById(token, fds.getId());

        if (tasks == null || tasks.size() == 0) {
            return false;
        }

        for (int i = 0; i < tasks.size(); ++i) {

            String newStatus = processFile(tasks.get(i));
            tasks.get(i).setStatus(newStatus);
        }

        //Get New Token (Getting new token because above process may take long time)
        token = authenticationService.getToken();

        //Update Status of tasks to 'COMPLETED' or SKIPPED
        fileDiscoveryTaskService.updateFileDiscoveryTasks(token, tasks);

        return true;

    }

    private String processFile(FileDiscoveryTask task) {

        //requireComplianceFilesCount++;

        Path path = Paths.get(task.getPath());
        String fileName = path.toAbsolutePath().toString();

        String status = FileDiscoveryStatus.SKIPPED.name();

        if (fileName.contains(props.getSkipMaskFileTitle())) {
            logger.info("Skipping Mask File: {}", fileName);
            return status;
        }

        if (path.getFileName().toString().endsWith(".txt")
                || path.getFileName().toString().endsWith(".csv")
                || path.getFileName().toString().endsWith(".xml")) {
            status = applyValidationsOnTextBasedFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".pdf")) {
            status = applyValidationsOnPdfFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".doc")) {
            status = applyValidationsOnDocFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".docx")) {
            status = applyValidationsOnDocxFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".xls")) {
            status = applyValidationsOnXlsFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".xlsx")) {
            status = applyValidationsOnXlsxFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".ppt")) {
            status = applyValidationsOnPptFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".pptx")) {
            status = applyValidationsOnPptxFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".msg")) {
            status = applyValidationsOnMsgFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".pst")) {
            status = applyValidationsOnPstFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else if (path.getFileName().toString().endsWith(".ost")) {
            status = applyValidationsOnOstFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else {
            logger.info("ProcessService: Unrecognized File Format for " + path.getFileName().toString());
        }

        return status;
    }


    //Add New FileDiscoveryResults Into List
    private void addNewFileDiscoveryResultsIntoList(String fileName) {

        sensitiveDataCount += categories.size();

        for (RegexCategory regexCategory : categories) {

            FileDiscoveryResult fileDiscoveryResult = new FileDiscoveryResult();
            fileDiscoveryResult.setFileDiscoverySummary(new FileDiscoverySummary(fileDiscoverySummaryId));
            String discoveryDateTime = FileDiscoveryHelper.getCurrentTimestamp();
            fileDiscoveryResult.setDiscoveryTime(discoveryDateTime);
            fileDiscoveryResult.setFileFullPath(fileName);
            fileDiscoveryResult.setScope(regexCategory.getCategory().getLovTranslations().get(0).getLovValue());
            fileDiscoveryResult.setSubScope(regexCategory.getSubCategory().getLovTranslations().get(0).getLovValue());
            fileDiscoveryResult.setFrequency(checkFrequency(categories, regexCategory.getCategory(), regexCategory.getSubCategory()));
            fileDiscoveryResult.setProperties(getSubCategoryData(categories, regexCategory.getCategory(), regexCategory.getSubCategory()));
            fileDiscoveryResult.setFoundText(regexCategory.getFoundText());

            fileDiscoveryResults.add(fileDiscoveryResult);

        }

        categories = new ArrayList<>();

        //fileDiscoveryResultService.createFileDiscoveryResultsByArrayList(token, Integer.parseInt(fileDiscoverySummaryId), fileName, categories, sensitiveDataCount);
    }

    private int checkFrequency(List<RegexCategory> categories, Lov scope, Lov subScope) {
        int count = 0;
        for (RegexCategory regexCategory : categories) {
            if (regexCategory.getCategory().equals(scope) && regexCategory.getSubCategory().equals(subScope)) {
                count++;
            }
        }
        return count;
    }

    private Map<String, String> getSubCategoryData(List<RegexCategory> categories, Lov scope, Lov subScope) {
        Map<String, String> map = new HashMap<>();
        int count = 0;
        for (RegexCategory regexCategory : categories) {
            if (regexCategory.getCategory().equals(scope) && regexCategory.getSubCategory().equals(subScope)) {
                map.put(regexCategory.getFoundText(), String.valueOf(true));
            }
        }
        return map;
    }

    // TXT, CSV or XML
    private String applyValidationsOnTextBasedFile(String fileName) {

        logger.info("filename is " + fileName);

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            // TODO buralarda veriyi tutucaz, tam bu categories değil local bu file ile ilgili olan kısmı tutucaz
            stream.forEach(s -> {
                categories = new RegexValidator().validate(categories, s);
            });

            stream.close();

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (MalformedInputException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Failed
    }

    // PDF
    private String applyValidationsOnPdfFile(String fileName) {

        File file = new File(fileName);
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        PDFTextStripper pdfStripper = null;
        String pdfContent = null;
        try {
            pdfStripper = new PDFTextStripper();
            pdfContent = pdfStripper.getText(document);

            categories = new RegexValidator().validate(categories, pdfContent);
            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Failed

    }

    // DOC (HWPF)
    public String applyValidationsOnDocFile(String fileName) {

        //Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }

        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        HWPFDocument hwpfDocument = null;
        try {
            hwpfDocument = new HWPFDocument(fis);

            WordExtractor wordExtractor = new WordExtractor(hwpfDocument);
            String[] paragraphs = wordExtractor.getParagraphText();

            for (String paragraph : paragraphs) {
                categories = new RegexValidator().validate(categories, paragraph);
            }

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Failed
    }

    // DOCX (XSLF)
    private String applyValidationsOnDocxFile(String fileName) {

        //Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }

        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        String result = "";
        String[] splitWords;
        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(fis);
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            result = xwpfWordExtractor.getText();
            splitWords = result.split("\\s+");
            for (int i = 0; i < splitWords.length; i++) {
                if (splitWords[i] == null) {
                    continue;
                }
                categories = new RegexValidator().validate(categories, splitWords[i]);
            }
            processedFilesCount++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

*/
/*
        XWPFDocument xwpfDocument = null;
        try {
            xwpfDocument = new XWPFDocument(fis);

            List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();

            for (XWPFParagraph paragraph : paragraphs) {
                categories = new RegexValidator().validate(categories, paragraph.getText());
            }

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
*//*

        return FileDiscoveryStatus.FAILED.name(); //Failed

    }

    // XLS (HSSF)
    private String applyValidationsOnXlsFile(String fileName) {

        //Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }

        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(fis);

            for (int i = 0; i < hssfWorkbook.getNumberOfSheets(); ++i) {
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(i);

                // SheetName de tercihe gore categories listesine eklenebilir - xssfSheet.getSheetName();

                for (Row myrow : hssfSheet) {
                    for (Cell mycell : myrow) {
                        if (mycell.getCellType().equals(CellType.STRING)) {
                            categories = new RegexValidator().validate(categories, mycell.getStringCellValue());
                        } else if (mycell.getCellType().equals(CellType.NUMERIC)) {
                            categories = new RegexValidator().validate(categories, String.valueOf(mycell.getNumericCellValue()));
                        }
                    }
                }
            }

            hssfWorkbook.close();
            fis.close();

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Processed successfully
    }

    // XLSX (XSSF)
    private String applyValidationsOnXlsxFile(String fileName) {

        //Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }

        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(fis);

            for (int i = 0; i < xssfWorkbook.getNumberOfSheets(); ++i) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(i);

                // SheetName de tercihe gore categories listesine eklenebilir - xssfSheet.getSheetName();

                for (Row myrow : xssfSheet) {
                    for (Cell mycell : myrow) {
                        try {
                            if (mycell.getCellType().equals(CellType.STRING)) {
                                categories = new RegexValidator().validate(categories, mycell.getStringCellValue());
                            } else if (mycell.getCellType().equals(CellType.NUMERIC)) {
                                if (fileDiscoveryHelper.myIsADateFormat(mycell)) {
                                    categories = new RegexValidator().validate(categories, String.valueOf(mycell.getNumericCellValue()));
                                } else {
                                    DataFormatter fmt = new DataFormatter();
                                    String cellValue = fmt.formatCellValue(mycell);
                                    categories = new RegexValidator().validate(categories, String.valueOf(cellValue));
                                }
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
            }

            xssfWorkbook.close();
            fis.close();

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Processed successfully
    }

    // PPT (HSLF)
    private String applyValidationsOnPptFile(String fileName) {

        //Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }

        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        HSLFSlideShow ppt = null;
        try {
            ppt = new HSLFSlideShow(fis);

            // get slides
            for (org.apache.poi.hslf.usermodel.HSLFSlide slide : ppt.getSlides()) {
                for (org.apache.poi.hslf.usermodel.HSLFShape sh : slide.getShapes()) {
                    // name of the shape
                    String name = sh.getShapeName();
                    // shapes's anchor which defines the position of this shape in the slide
                    Rectangle2D anchor = (Rectangle2D) sh.getAnchor();
                    if (sh instanceof org.apache.poi.hslf.usermodel.HSLFTextBox) {
                        org.apache.poi.hslf.usermodel.HSLFTextBox shape = (org.apache.poi.hslf.usermodel.HSLFTextBox) sh;
                        // work with TextBox
                        categories = new RegexValidator().validate(categories, shape.getText());
                    } else if (sh instanceof org.apache.poi.hslf.usermodel.HSLFLine) {
                        org.apache.poi.hslf.usermodel.HSLFLine line = (org.apache.poi.hslf.usermodel.HSLFLine) sh;
                        // work with Line
                    } else if (sh instanceof org.apache.poi.hslf.usermodel.HSLFAutoShape) {
                        org.apache.poi.hslf.usermodel.HSLFAutoShape shape = (org.apache.poi.hslf.usermodel.HSLFAutoShape) sh;
                        // work with AutoShape
                        for (HSLFTextParagraph paragraphs : shape.getTextParagraphs()) {
                            for (org.apache.poi.hslf.usermodel.HSLFTextRun hslfTextRun : paragraphs.getTextRuns())
                                categories = new RegexValidator().validate(categories, hslfTextRun.getRawText());
                        }
                    } else if (sh instanceof org.apache.poi.hslf.usermodel.HSLFPictureShape) {
                        org.apache.poi.hslf.usermodel.HSLFPictureShape shape = (org.apache.poi.hslf.usermodel.HSLFPictureShape) sh;
                        // work with Picture
                    }
                }
            }

            ppt.close();
            fis.close();

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Failed
    }

    // PPTX (XSLF)
    private String applyValidationsOnPptxFile(String fileName) {

        //Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }

        File file = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        XMLSlideShow ppt = null;
        try {
            ppt = new XMLSlideShow(fis);

            // get slides
            for (XSLFSlide slide : ppt.getSlides()) {
                for (XSLFShape sh : slide.getShapes()) {
                    // name of the shape
                    String name = sh.getShapeName();
                    // shapes's anchor which defines the position of this shape in the slide
                    if (sh instanceof XSLFTextShape) {
                        XSLFTextShape shape = (XSLFTextShape) sh;
                        // work with a shape that can hold text
                        categories = new RegexValidator().validate(categories, shape.getText());
                    } else if (sh instanceof PlaceableShape) {
                        Rectangle2D anchor = ((PlaceableShape) sh).getAnchor();
                    } else if (sh instanceof XSLFConnectorShape) {
                        XSLFConnectorShape line = (XSLFConnectorShape) sh;
                        // work with Line
                    } else if (sh instanceof XSLFPictureShape) {
                        XSLFPictureShape shape = (XSLFPictureShape) sh;
                        // work with Picture
                    }
                }
            }

            ppt.close();
            fis.close();

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Failed
    }


    // MSG
    private String applyValidationsOnMsgFile(String fileName) {
        //Skip if the file is password protected
        */
/*if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }*//*

        MsgParser msgp = new MsgParser();
        Message msg = null;
        try {
            msg = msgp.parseMsg(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        String from_email, from_name, subject, body, to_list, cc_list, bcc_list, split;
        String[] splitFrom_email, splitFrom_name, splitSubject, splitBody, splitTo_list, splitCc_list, splitBcc_list;

        try {
            from_email = msg.getFromEmail();
            from_name = msg.getFromName();
            subject = msg.getSubject();
            body = msg.getBodyText();
            to_list = msg.getDisplayTo();
            cc_list = msg.getDisplayCc();
            bcc_list = msg.getDisplayBcc();

            splitFrom_email = from_email.split("\\s+");
            splitFrom_name = from_name.split("\\s+");
            splitSubject = subject.split("\\s+");
            splitBody = body.split("\\s+");
            splitTo_list = to_list.split("\\s+");
            splitCc_list = cc_list.split("\\s+");
            splitBcc_list = bcc_list.split("\\s+");

            for (String word : splitFrom_email) {
                categories = new RegexValidator().validate(categories, word);
            }
            for (String word : splitFrom_name) {
                categories = new RegexValidator().validate(categories, word);
            }
            for (String word : splitSubject) {
                categories = new RegexValidator().validate(categories, word);
            }
            for (String word : splitBody) {
                categories = new RegexValidator().validate(categories, word);
            }
            for (String word : splitTo_list) {
                categories = new RegexValidator().validate(categories, word);
            }
            for (String word : splitCc_list) {
                categories = new RegexValidator().validate(categories, word);
            }
            for (String word : splitBcc_list) {
                categories = new RegexValidator().validate(categories, word);
            }

            processedFilesCount++;

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Failed
    }

    // PST
    private String applyValidationsOnPstFile(String fileName) {
        //Skip if the file is password protected
       */
/* if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }*//*


        PSTFile pstFile = null;
        try {
            pstFile = new PSTFile(fileName);
            //System.out.println("Main Mail Filename: " + pstFile.getMessageStore().getDisplayName());
        } catch (Exception err) {
            err.printStackTrace();
            logger.error(err.getMessage());
        }

        try {
            processFolder(pstFile.getRootFolder());

            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return FileDiscoveryStatus.FAILED.name(); //Failed
    }

    private void processFolder(PSTFolder folder) {
        try {
            //System.out.println("Child Mail Filename: " + folder.getDisplayName());

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
                    //If you want to see the mail in console
               */
/* System.out.println("Email: " + email.getSubject());
                System.out.println("Body: " + email.getBody());*//*


                    String subject = email.getSubject();
                    String body = email.getBody();

                    String[] splitSubject = subject.split("\\s+");
                    String[] splitBody = body.split("\\s+");

                    for (String word : splitSubject) {
                        categories = new RegexValidator().validate(categories, word);
                    }
                    for (String word : splitBody) {
                        categories = new RegexValidator().validate(categories, word);
                    }
                    processedFilesCount++;
                    email = (PSTMessage) folder.getNextChild();
                }
            }
        } catch (PSTException e) {
           */
/* e.printStackTrace();
            logger.error(e.getMessage());*//*

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    // OST
    private String applyValidationsOnOstFile(String fileName) {
        //Skip if the file is password protected
       */
/* if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }*//*


        try {
            PSTFile OstFile = new PSTFile(fileName);
            processFolder(OstFile.getRootFolder());
            return FileDiscoveryStatus.COMPLETED.name(); //Processed successfully

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return FileDiscoveryStatus.FAILED.name(); //Failed
    }

    public void maskResults() {
        try {
            HashMap<String, List<String>> hashMap = fileDiscoveryHelper.resultsToHashMap(fileDiscoveryResults);
            for (String key : hashMap.keySet()) {
                agent.EncryptionAgent((ArrayList) hashMap.get(key), key);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}*/
