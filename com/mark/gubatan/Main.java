package com.mark.gubatan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

public class Main {

    // Change date before each run
    private static final String date = "012517";
    private static final String month = "January";
    private static final String schoolYear = "2016-2017";

    // Change downloadPath before first run
    private static final String attendance = "TeamingSolution Attendance";
//    private static final String downloadPath = "D:/Downloads/";
    private static final String downloadPath = "C:/Users/DoomBot/Google Drive/YCCS Campus Data/";
    private static final String outputPath = "C:/Users/Doombot/1337hacking/";

    private static Map<String, String> schoolMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        schoolMap.put("500", "YCLA");
        schoolMap.put("504", "Ada McKinley");
        schoolMap.put("506", "Chatham");
        schoolMap.put("520", "Sullivan");
        schoolMap.put("523", "Latino Youth");
        schoolMap.put("526", "Jane Addams");
        schoolMap.put("528", "CYDI");
        schoolMap.put("529", "Olive Harvey");
        schoolMap.put("530", "Truman");
        schoolMap.put("533", "Association House");
        schoolMap.put("535", "CCA");
        schoolMap.put("536", "ASA");
        schoolMap.put("537", "Albizu Campos");
        schoolMap.put("538", "Innovations");
        schoolMap.put("543", "West Town");
        schoolMap.put("544", "Austin");
        schoolMap.put("545", "Westside");
        schoolMap.put("547", "Aspira");
        schoolMap.put("553", "Charles Houston");

        final long startTime = System.currentTimeMillis();
        for(Map.Entry<String, String> cur : schoolMap.entrySet()) {
            runReports(cur.getKey(), cur.getValue());
        }
        final long endTime = System.currentTimeMillis();
        System.out.println("Execution finished in: " + ((endTime - startTime)/1000.0) + " seconds.");
    }

    private static void runReports(String id, String val) {
        String path = downloadPath + id + "- " + val + "/" + attendance + "/" + schoolYear + "/" + month + "/";
//        String path = downloadPath;

        try {
            PDFManager pdfManager = new PDFManager();
            //IMPACT
            pdfManager.setFilePath(path + "Impact/IMPACT_" + val.replaceAll("\\s+","") + "_" + date + ".pdf");
            String impactPDF = pdfManager.ToText();
            HashMap<String, String> impact = ImpactParser.parseIMPACT(impactPDF);

            //POWER SCHOOL
            pdfManager.setFilePath(path + "PowerSchool/Monthly_Attendance_" + val.replaceAll("\\s+","") + "_" + date + ".pdf");
            String madPDF = pdfManager.ToText();
            HashMap<String, String> mad = PowerschoolParser.parseMonthlyAttendanceReport(madPDF);

            String differences = DiscrepancyDetector.getDiscrepancies(impact, mad);

            PrintWriter writer = new PrintWriter(outputPath + val + "_" + date + ".txt", "UTF-8");
            writer.println(differences);
            writer.close();
        }
        catch (IOException e) {
            // Probably due to a file not existing (Does the name match the school in the school list?)
            System.out.println(e.getMessage());
        }
    }


}
