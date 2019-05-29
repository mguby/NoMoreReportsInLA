package com.mark.gubatan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

enum ImpactCsvParser {
    ;

    private static final HashMap<String, String> ATTENDENCE_CODES = new HashMap<>();

    static
    {
        ATTENDENCE_CODES.put("P", "Present");
        ATTENDENCE_CODES.put("AE-HD", "Abs Exc HD");
        ATTENDENCE_CODES.put("A-E", "Abs Exc FD");
        ATTENDENCE_CODES.put("HH", "Home/Hosp");
        ATTENDENCE_CODES.put("SUS", "Suspension");
        ATTENDENCE_CODES.put("SF", "Schl Func");
        ATTENDENCE_CODES.put("A", "Abs Unex");
        ATTENDENCE_CODES.put("AUHD", "Abs Unex HD");
    }

    static HashMap<String, String> parseIMPACT(String path) throws IOException {
        HashMap<String, String> studentsToAttendance = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        String splitter = ",";
        while ((line = br.readLine()) != null)
        {
            line = line.replace("\"", "");
            line = line.trim();
            String[] columns = line.split(splitter);
            String student = columns[1] + ", " + columns[0];
            String attendanceCode = ATTENDENCE_CODES.get(columns[3]);
            studentsToAttendance.put(student.trim(), attendanceCode);
        }
        System.out.println(studentsToAttendance);
        return studentsToAttendance;
    }
}
