package com.mark.gubatan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Parses Impact attendance PDF files
 */
public class ImpactParser {
    private static String[] attendanceCodes = {"tardy", "present", "abs unex", "abs unex hd", "home/hosp", "abs exc hd",
                                                "abs exc", "schl func", "suspension"};

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
//                if(attendance.contains("Abs Unex FD"))
//                    attendance = "Abs Unex";
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
        int max = -1;
        for(String code : attendanceCodes) {
            int cur = line.indexOf(code);
            if (cur > max)
                max = cur;
        }
        return max;
    }
}
