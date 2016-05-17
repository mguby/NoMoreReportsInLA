package com.mark.gubatan;
import java.io.IOException;
import java.util.HashMap;

//TODO: Command line UI
public class Main {

    private static String school = "Hamilton Houston Alternative HS";

    public static void main(String[] args) throws IOException {

        PDFManager pdfManager = new PDFManager();

        //IMPACT
        pdfManager.setFilePath("D:/Downloads/IMPACT_Charles_051616.pdf");
        String impactPDF = pdfManager.ToText();
        ImpactParser ip = new ImpactParser(school);
        HashMap<String, String> impact = ip.parseIMPACT(impactPDF);

        //POWER SCHOOL
        pdfManager.setFilePath("D:/Downloads/Monthly_Attendance_Charles_051616.pdf");
        String madPDF = pdfManager.ToText();
        HashMap<String, String> mad = PowerschoolParser.parseMonthlyAttendanceReport(madPDF);

        String differences = DiscrepancyDetector.getDiscrepancies(impact, mad);
        System.out.println(differences);
    }


}
