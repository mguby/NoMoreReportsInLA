package com.mark.gubatan;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class Main {

    private static String school = "Hamilton Houston Alternative HS";

    public static void main(String[] args) throws IOException {

        PDFManager pdfManager = new PDFManager();

        //IMPACT
        pdfManager.setFilePath("D:/Downloads/IMPACT_Charles_051616.pdf");
        String impactPDF = pdfManager.ToText();
        ImpactParser ip = new ImpactParser(school);
        HashMap impact = ip.parseIMPACT(impactPDF);

        //POWER SCHOOL
        pdfManager.setFilePath("D:/Downloads/Monthly_Attendance_Charles_051616.pdf");
        String madPDF = pdfManager.ToText();
        HashMap mad = MonthlyAttendanceReportParser.parseMonthlyAttendanceReport(madPDF);

        String differences = DiscrepancyDetector.getDiscrepancies(impact, mad);
        System.out.println(differences);
    }


}
