/*


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

    private List<RegexCategory> categories = new ArrayList<>();

    private FileDiscoveryHelper fileDiscoveryHelper;

    private List<FileDiscoveryResult> fileDiscoveryResults = new ArrayList<>();

    private DataEncryptionAgent agent = new DataEncryptionAgent();

    @Autowired
    public ProcessService(AuthenticationService authenticationService, PropertiesViaDatabase propertiesViaDatabase, FileDiscoveryService fileDiscoveryService, FileDiscoverySummaryService fileDiscoverySummaryService, FileDiscoveryTaskService fileDiscoveryTaskService, FileDiscoveryResultService fileDiscoveryResultService, FileDiscoveryHelper fileDiscoveryHelper, RegexCategoriesRefresher regexCategoriesRefresher) {

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
                try {
                    //Get New Token
                    token = authenticationService.getToken();

                    //agent.EncryptionAgent(token, fileDiscoveryResults);
                } catch (Exception e) {
                    System.out.println(e);
                }

                //fileDiscoveryResultService.createFileDiscoveryResultsByArrayList(token, Integer.parseInt(fileDiscoverySummaryId), fileName, categories, sensitiveDataCount);

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
        path = Paths.get("C:\\Users\\mgmet\\Documents\\KVKK\\Test\\MICROPROCESSORS.msg");
        fileName = path.toAbsolutePath().toString();

        System.out.println("1Meteeeee1212112");

        if (path.getFileName().toString().endsWith(".txt") || path.getFileName().toString().endsWith(".csv") || path.getFileName().toString().endsWith(".xml")) {
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
            System.out.println("Mete");
            System.out.println("cccccccccccccccccccccBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
            status = applyValidationsOnMsgFile(fileName);
            addNewFileDiscoveryResultsIntoList(fileName);
        } else {
            logger.info("ProcessService: Unrecognized File Format for " + path.getFileName().toString());
        }

        return status;
    }

    ////////////////////////////
    private String applyValidationsOnMsgFile(String fileName) {
      */
/*  //Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }*//*

        MsgParser msgp = new MsgParser();
        Message msg = null;
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        try {
            msg = msgp.parseMsg(fileName);
            //msg = msgp.parseMsg("C:\\Users\\mgmet\\Documents\\KVKK\\Test\\MICROPROCESSORS.msg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        String from_email, from_name, subject, body, to_list, cc_list, bcc_list, split;
        String[] splitFrom_email, splitFrom_name, splitSubject, splitBody, splitTo_list, splitCc_list, splitBcc_list, splitList;

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

         */
/*   splitList[0] = ("splitFrom_email");
            splitList[1] = ("splitFrom_name");
            splitList[2] = ("splitSubject");
            splitList[3] = ("splitBody");
            splitList[4] = ("splitTo_list");
            splitList[5] = ("splitCc_list");
            splitList[6] = ("splitBcc_list");
*//*



            for (String word : splitBody) {
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

        */
/*//*
/Skip if the file is password protected
        if (fileDiscoveryHelper.isEncryptedMSOfficeFile(fileName)) {
            logger.info("Filename:" + fileName + " is being skipped because it's password protected");
            return FileDiscoveryStatus.SKIPPED.name(); //File skipped
        }*//*

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
                        if (mycell.getCellType().equals(CellType.STRING)) {
                            categories = new RegexValidator().validate(categories, mycell.getStringCellValue());
                        } else if (mycell.getCellType().equals(CellType.NUMERIC)) {
                            categories = new RegexValidator().validate(categories, String.valueOf(mycell.getNumericCellValue()));
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
}*/
