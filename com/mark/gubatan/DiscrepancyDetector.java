package com.mark.gubatan;

import java.util.*;

/**
 * Created by Mark on 5/16/2016.
 * TODO: Handle names spread across multiple pages
 */
public class DiscrepancyDetector {

    public static String getDiscrepancies(HashMap<String, String> impact, HashMap<String, String> mad, String date) {
        List<String> lineList = new ArrayList<String>();
        Set<String> missingStudents = new HashSet<>();

        for (Map.Entry<String, String> entry : impact.entrySet()) {
            String line = getDiscrepancyLine(entry, mad, missingStudents, date, true);
            if(line != null) {
                lineList.add(line);
                missingStudents.add(entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : mad.entrySet()) {
            String line = getDiscrepancyLine(entry, impact, missingStudents, date, false);
            if(line != null) {
                lineList.add(line);
                missingStudents.add(entry.getKey());
            }
        }

        return sortAbsensesAlphabetically(lineList);
    }

    private static String getDiscrepancyLine(Map.Entry<String, String> entry, HashMap<String, String> destStudentMap,
                                             Set<String> missingStudents, String date, boolean isEntryImpact) {
        String student = entry.getKey();
        String studentNoMiddleName = student.substring(0, student.length() - 2);
        String val = entry.getValue();

        if((checkDiscrepancy(destStudentMap, student, val) && !missingStudents.contains(student)))
            return getDiscrepancyLine(destStudentMap, date, isEntryImpact, student, val);
        if (checkDiscrepancy(destStudentMap, studentNoMiddleName, val) && !missingStudents.contains(studentNoMiddleName)) {
            return getDiscrepancyLine(destStudentMap, date, isEntryImpact, studentNoMiddleName, val);
        }
        return null;
    }

    private static String getDiscrepancyLine(HashMap<String, String> destStudentMap, String date, boolean isEntryImpact, String student, String val) {
        String val2 = destStudentMap.get(student);
        String lastName = student.substring(0, student.indexOf(","));
        String firstName = student.substring(student.indexOf(',') + 2);
        String line = lastName + "," + firstName + ", ," + date;
        if(isEntryImpact) {
            line = line + "," + val + "," + val2 + '\n';
        }
        else {
            line = line + "," + val2 + "," + val + '\n';
        }
        return line;
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

