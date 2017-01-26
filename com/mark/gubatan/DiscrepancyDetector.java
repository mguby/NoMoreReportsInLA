package com.mark.gubatan;

import java.util.*;

/**
 * Created by Mark on 5/16/2016.
 * TODO: Handle names spread across multiple pages
 */
public class DiscrepancyDetector {

    public static String getDiscrepancies(HashMap<String, String> impact, HashMap<String, String> mad) {
        List<String> lineList = new ArrayList<String>();
        Set<String> missingStudents = new HashSet<>();

        for (Map.Entry<String, String> entry : impact.entrySet()) {
            String line = getDiscrepancyLine(entry, mad, missingStudents,"IMPACT", "Monthly Attendance");
            if(line != null) {
                lineList.add(line);
                missingStudents.add(entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : mad.entrySet()) {
            String line = getDiscrepancyLine(entry, impact, missingStudents,"Monthly Attendance", "IMPACT");
            if(line != null) {
                lineList.add(line);
                missingStudents.add(entry.getKey());
            }
        }

        return sortAbsensesAlphabetically(lineList);
    }

    private static String getDiscrepancyLine(Map.Entry<String, String> entry, HashMap<String, String> destStudentMap,
                                             Set<String> missingStudents, String srcName, String destName) {
        String student = entry.getKey();
        String studentNoMiddleName = student.substring(0, student.length() - 2);
        String val = entry.getValue();

        if((checkDiscrepancy(destStudentMap, student, val) && !missingStudents.contains(student)) ||
                (checkDiscrepancy(destStudentMap, studentNoMiddleName, val) && !missingStudents.contains(student))) {
            String val2 = destStudentMap.get(student);
            String line = student + " is " + val + " in " + srcName + " but "
                    + val2 + " in " + destName + "\n";
            return line;
        }
        return null;
    }

    private static boolean checkDiscrepancy(HashMap<String, String> studentMap, String student, String val) {
        String val2 = studentMap.get(student);
        return val2 != null && !val2.equals(val);
    }

    private static String sortAbsensesAlphabetically(List<String> absences) {
        Collections.sort(absences);
        StringBuilder sb = new StringBuilder();
        for(String str : absences) {
            sb.append(str);
        }
        return sb.toString();
    }
}

