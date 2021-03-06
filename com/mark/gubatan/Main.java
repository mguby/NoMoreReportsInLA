package com.mark.gubatan;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Main {

    // Change date before each run
    private static final String schoolYear = "2018-2019";
    private static final String startDate = "040919";
    private static final String endDate = "052419";

    // Change downloadPath before first run
    private static final String attendance = "TeamingSolution Attendance";
//    private static final String downloadPath = "D:/Downloads/";
    private static final String downloadPath = "C:/Users/DoomBot/Google Drive/YCCS Campus Data/";
    private static final String outputPath = "C:/Users/Doombot/1337hacking/";
//    private static final String outputPath = downloadPath;

    private static Map<String, String> schoolMap = new HashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyy");

    public static void main(String[] args) throws IOException, ParseException {
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
        LocalDate myEndDate = LocalDate.parse(endDate, DATE_FORMATTER).plusDays(1);
        LocalDate aDate = LocalDate.parse(startDate, DATE_FORMATTER);
        while (!aDate.equals(myEndDate))
        {
            String month = aDate.getMonth().name().toLowerCase();
            month = month.substring(0, 1).toUpperCase() + month.substring(1);
            for(Map.Entry<String, String> cur : schoolMap.entrySet()) {
                runReports(cur.getKey(), cur.getValue(), aDate.format(DATE_FORMATTER), month);
            }
            aDate = aDate.plusDays(1);
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Execution finished in: " + ((endTime - startTime)/1000.0) + " seconds.");
    }

    private static void runReports(String id, String val, String date, String month) {
        String path = downloadPath + id + "- " + val + "/" + attendance + "/" + schoolYear + "/" + month + "/";
//        String path = downloadPath;

        try {
            PDFManager pdfManager = new PDFManager();
            //IMPACT
//            pdfManager.setFilePath(path + "Impact/IMPACT_" + val.replaceAll("\\s+","") + "_" + date + ".pdf");
//            String impactPDF = pdfManager.ToText();
//            HashMap<String, String> impact = ImpactParser.parseIMPACT(impactPDF);
            HashMap<String, String> aspen = AspenCsvParser.parseIMPACT(path + "Impact/ASPEN_" + val.replaceAll("\\s+","") + "_" + date + ".csv");

            //POWER SCHOOL
            pdfManager.setFilePath(path + "PowerSchool/Monthly_Attendance_" + val.replaceAll("\\s+","") + "_" + date + ".pdf");
            String madPDF = pdfManager.ToText();
            HashMap<String, String> mad = PowerschoolParser.parseMonthlyAttendanceReport(madPDF);

            String dateMo = date.substring(0, 2);
            String dateDay = date.substring(2, 4);
            String dateYr = date.substring(4);
            String dateXD = dateMo + "/" + dateDay + "/" + dateYr;
            String differences = DiscrepancyDetector.getDiscrepancies(aspen, mad, dateXD);

            PrintWriter writer = new PrintWriter(outputPath + val + "_" + date + ".csv", "UTF-8");
            writer.println(differences);
            writer.close();
        }
        catch (IOException e) {
            // Probably due to a file not existing (Does the name match the school in the school list?)
            System.out.println(e.getMessage());
        }
    }


}
