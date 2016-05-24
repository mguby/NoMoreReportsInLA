package com.mark.gubatan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Main {

    // Change date before each run
    private static final String date = "052316";

    // Change downloadPath before first run
    private static final String downloadPath = "C:/Users/Mark/Google Drive/Attendance/" + date + "/";

    private static final String[] schools = {"YCLA", "ADA", "Chatham", "Sullivan", "Latino", "JaneAddams", "CYDI",
            "OHMC", "Truman", "ECA", "CCA", "ASA", "Campos", "Innovations", "WestTown", "WestSide", "APHS", "Charles"};

    public static void main(String[] args) throws IOException {
        final long startTime = System.currentTimeMillis();
        for(String cur : schools) {
            runReports(cur);
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Execution finished in: " + ((endTime - startTime)/1000.0) + " seconds.");
    }

    private static void runReports(String cur) {
        try {
            PDFManager pdfManager = new PDFManager();
            //IMPACT
            pdfManager.setFilePath(downloadPath + "IMPACT_" + cur + "_" + date + ".pdf");
            String impactPDF = pdfManager.ToText();
            HashMap<String, String> impact = ImpactParser.parseIMPACT(impactPDF);

            //POWER SCHOOL
            pdfManager.setFilePath(downloadPath + "Monthly_Attendance_" + cur + "_" + date + ".pdf");
            String madPDF = pdfManager.ToText();
            HashMap<String, String> mad = PowerschoolParser.parseMonthlyAttendanceReport(madPDF);

            String differences = DiscrepancyDetector.getDiscrepancies(impact, mad);

            PrintWriter writer = new PrintWriter(downloadPath + cur + "_" + date + ".txt", "UTF-8");
            writer.println(differences);
            writer.close();
        }
        catch (IOException e) {
            // Probably due to a file not existing (Does the name match the school in the school list?)
        }
    }


}
