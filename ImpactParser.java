package com.mark.gubatan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Parses Impact attendance PDF files
 * TODO: Turn Abs Unex FD (For charles?)
 */
public class ImpactParser {

    private String impactSchool;

    public ImpactParser(String school) {
        impactSchool = school;
    }


    public HashMap<String, String> parseIMPACT(String pdf) throws IOException {
        HashMap map = new HashMap<String, String>();

        BufferedReader bufReader = new BufferedReader(new StringReader(pdf));

        String line;
        while ((line = bufReader.readLine()) != null) {
            if(lineIsStudent(line)) {
                int attendanceIdx = indexOfAttendance(line.toLowerCase());
                String attendance = line.substring(attendanceIdx);
                String student = line.substring(0,attendanceIdx - 1);
                if(attendance.contains("Tardy"))
                    attendance = "Present";
                map.put(student.trim(), attendance);
                if(student.contains("Newsome, Dejsia"))
                    System.out.println(student + " " + attendance);
            }
        }

        return map;
    }

    private boolean lineIsStudent(String line) {
        return !(line.contains(":") || line.contains("Student Name Attendance Comment Change Reason")
                || line.contains("Dalencia Wells") || line.matches(".*\\d+.*") || line.contains(impactSchool));

    }

    private int indexOfAttendance(String line) {
        int tardy = line.indexOf("tardy");
        int present = line.indexOf("present");
        int absUnex = line.indexOf("abs unex");
        int absUnexHD = line.indexOf("abs unex hd");
        return Math.max(Math.max(Math.max(present, absUnex), absUnexHD), tardy);
    }
}
