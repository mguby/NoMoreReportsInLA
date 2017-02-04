package com.mark.gubatan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses Powerschool PDF files.
 */
public class PowerschoolParser {

    private static HashMap<String, String> map;

    public static HashMap<String, String> parseMonthlyAttendanceReport(String pdf) throws IOException {
        map = new HashMap<>();

        BufferedReader bufReader = new BufferedReader(new StringReader(pdf));

        String line;
        boolean isStudentSection = false;
        boolean shouldCreateNewStudent = false;
        StringBuilder sb = new StringBuilder();
        while ((line = bufReader.readLine()) != null) {
            if(isStartOfStudentsOnPage(line))
                isStudentSection = true;
            if(isEndOfStudentsOnPage(line)) {
                isStudentSection = false;
                shouldCreateNewStudent = true;
            }

            if(isStudentSection || shouldCreateNewStudent) {
                if(isNewStudent(line)) {
                    shouldCreateNewStudent = true;
                }

                if(shouldCreateNewStudent) {
                    createStudent(sb.toString());
                    sb.setLength(0);
                }

                if(!isEndOfStudentsOnPage(line)) {
                    String appendLine = line.replace("\n", "") + " ";
                    sb.append(appendLine);
                }
            }
            shouldCreateNewStudent = false;
        }
        return map;
    }

    private static boolean isStartOfStudentsOnPage(String line) {
        return line.contains("1.") || (line.contains(".") && line.matches(".*\\d+.*")) && !line.contains(":");
    }

    private static boolean isEndOfStudentsOnPage(String line) {
        return line.contains("Grand Totals") || (line.contains("Page") && line.matches(".*\\d+.*"));
    }

    private static boolean isNewStudent(String line) {
        return line.substring(0, 1).matches(".*\\d+.*") && line.indexOf(".") < 5 && line.contains(".");
    }

    private static void createStudent(String student) {
        if(student.length() == 0)
            return;

        student = student.substring(student.indexOf(".") + 2);

        int id = indexOfStudentID(student);
        String studentName = student.substring(0, id);
        studentName = trimMiddleName(studentName.trim());

        String nums = student.substring(id);
        String attendance = getAttendance(nums);

        map.put(studentName.trim(), attendance);
    }

    private static int indexOfStudentID(String student) {
        Pattern pattern = Pattern.compile("^\\D*(\\d)");
        Matcher matcher = pattern.matcher(student);
        if(matcher.find())
            return matcher.start(1);
        return -1;
    }

    private static String trimMiddleName(String studentName) {
        int betweenLastFirstNames = studentName.indexOf(",");
        try { //TODO fix names spread across multiple lines?
            String lastName = studentName.substring(0, betweenLastFirstNames);
            String firstName = studentName.substring(betweenLastFirstNames + 2);

            int spaceBetweenFirstMiddleNames = firstName.indexOf(" ");
            if (spaceBetweenFirstMiddleNames != -1) {
                String middleName = firstName.substring(spaceBetweenFirstMiddleNames + 1);
                if (middleName.length() != 1)
                    return lastName + firstName.substring(0, spaceBetweenFirstMiddleNames + 2);
            }

            return studentName;
        }
        catch(StringIndexOutOfBoundsException e) {
            return "XD";
        }
    }

    private static String getAttendance(String nums) {
        String attendance;
        if(nums.contains("AEHD"))
            attendance = "Abs Exc HD";
        else if (nums.contains("AEFD"))
            attendance = "Abs Exc FD";
        else if (nums.contains("HH")) {
            attendance = "Home/Hosp";
        }
        else if (nums.contains("SUS")) {
            attendance = "Suspension";
        }
        else if (nums.contains("SF")) {
            attendance = "Schl Func";
        }
        else {
            int code;
            if (nums.contains("A"))
                code = nums.indexOf("A") + 4;
            else
                code = nums.indexOf(" ") + 3;

            String codes = nums.substring(code);
            String abs = codes.substring(0, 3);
            attendance = getAttendanceFromAbsenceValue(abs);
        }
        return attendance;
    }

    private static String getAttendanceFromAbsenceValue(String abs) {
        String attendance;
        if (abs.contains("1.0"))
            attendance = "Abs Unex FD";
        else if (abs.contains("0.5"))
            attendance = "Abs Unex HD";
        else
            attendance = "Present";
        return attendance;
    }
}
