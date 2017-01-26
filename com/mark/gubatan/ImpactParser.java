package com.mark.gubatan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Parses Impact attendance PDF files
 */
public class ImpactParser {

    public static HashMap<String, String> parseIMPACT(String pdf) throws IOException {
        HashMap<String, String> map = new HashMap<>();

        BufferedReader bufReader = new BufferedReader(new StringReader(pdf));

        String line;
        while ((line = bufReader.readLine()) != null) {
            if(lineIsStudent(line)) {
                int attendanceIdx = indexOfAttendance(line.toLowerCase());
                String attendance = line.substring(attendanceIdx);
                String student = line.substring(0,attendanceIdx - 1);
                if(attendance.contains("Tardy"))
                    attendance = "Present";
                if(attendance.contains("Abs Unex FD"))
                    attendance = "Abs Unex";
                map.put(student.trim(), attendance);
            }
        }

        return map;
    }

    private static boolean lineIsStudent(String line) {
        return !(line.contains(":") || line.contains("Student Name Attendance Comment Change Reason")
                || line.contains("Dalencia Wells") || line.matches(".*\\d+.*") || line.contains("HS"));
    }

    private static int indexOfAttendance(String line) {
        int tardy = line.indexOf("tardy");
        int present = line.indexOf("present");
        int absUnex = line.indexOf("abs unex");
        int absUnexHD = line.indexOf("abs unex hd");
        int hH = line.indexOf("home/hosp");
        int absExc = line.indexOf("abs exc hd");
        int absExcFd = line.indexOf("abs exc");
        return Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(present, absUnex), absUnexHD), tardy), hH), absExc), absExcFd);
    }
}
