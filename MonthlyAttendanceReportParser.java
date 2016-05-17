package com.mark.gubatan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses Monthly_Attendance PDF files.
 */
public class MonthlyAttendanceReportParser {

    private static HashMap map;

    public static HashMap parseMonthlyAttendanceReport(String pdf) throws IOException {
        map = new HashMap();

        BufferedReader bufReader = new BufferedReader(new StringReader(pdf));

        String line = null;
        boolean studentSection = false;
        boolean clearBuffer = false;
        int curNum = 1;
        StringBuilder sb = new StringBuilder();
        while ((line = bufReader.readLine()) != null) {
            if(line.contains("1."))
                studentSection = true;
            if(line.contains("Grand Totals")) {
                studentSection = false;
                clearBuffer = true;
            }

            if(studentSection || clearBuffer) {
                if(line.substring(0, 1).matches(".*\\d+.*") && line.indexOf(".") < 3 && line.indexOf(".") >=0) {
                    clearBuffer = true;
                    curNum++;
                }

                if(clearBuffer) {
                    createStudent(sb.toString());
                    sb.setLength(0);
                }

                if(!line.contains("Grand Totals"))
                    sb.append(line.replace("\n", "") + " ");
            }
            clearBuffer = false;
        }
        return map;
    }

    private static void createStudent(String student) {
        if(student.length() == 0)
            return;
        student = student.substring(student.indexOf(".") + 2);
        int id = indexOfStudentID(student);
        String studentName = student.substring(0, id);
        String nums = student.substring(id);
        int code;
        String attendance;
        if(nums.contains("AEHD")) {
            attendance = "Abs Exc HD";
        }
        else if (nums.contains("AEFD")) {
            attendance = "Abs Exc FD";
        }
        else {
            if (nums.contains("A"))
                code = nums.indexOf("A") + 4;
            else
                code = nums.indexOf(" ") + 3;

            String codes = nums.substring(code);
            String abs = codes.substring(0, 3);

            if (abs.contains("1.0"))
                attendance = "Abs Unex";
            else if (abs.contains("0.5"))
                attendance = "Abs Unex HD";
            else {
                attendance = "Present";
            }
        if(studentName.contains("Newsome, Dejsia") || studentName.contains("Ortega, Se")) {
            System.out.println(studentName + " " + nums + " " + attendance);
        }
        }
        map.put(studentName.trim(), attendance);
    }

    private static int indexOfStudentID(String student) {
        Pattern pattern = Pattern.compile("^\\D*(\\d)");
        Matcher matcher = pattern.matcher(student);
        matcher.find();
        return matcher.start(1);
    }
}
