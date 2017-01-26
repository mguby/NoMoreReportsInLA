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
            String student = entry.getKey();
            String studentNoMiddleName = student.substring(0, student.length() - 2);
            String val = entry.getValue();

            if(mad.containsKey(student)) {
                checkDiscrepancy(mad, lineList, missingStudents, student, val);
            }
            else if(mad.containsKey(studentNoMiddleName)) {
                checkDiscrepancy(mad, lineList, missingStudents, studentNoMiddleName, val);
            }
        }

        for (Map.Entry<String, String> entry : mad.entrySet()) {
            String student = entry.getKey();
            String studentNoMiddleName = student.substring(0, student.length() - 2);
            String val = entry.getValue();

            if(impact.containsKey(student) && !(missingStudents.contains(student))) {
                checkDiscrepancyReverse(impact, lineList, missingStudents, student, val);
            }
            else if(impact.containsKey(studentNoMiddleName) && !(missingStudents.contains(studentNoMiddleName))) {
                checkDiscrepancyReverse(impact, lineList, missingStudents, studentNoMiddleName, val);
            }
        }

        return sortAbsensesAlphabetically(lineList);
    }

    private static void checkDiscrepancy(HashMap<String, String> studentMap, List<String> lineList, Set<String> missingStudents, String student, String val) {
        String val2 = studentMap.get(student);
        if(!val2.equals(val)) {
            String appendLine = student + " is " + val + " in IMPACT but "
                    + val2 + " in Monthly Attendance Report\n";
            lineList.add(appendLine);
            missingStudents.add(student);
        }
    }

    private static void checkDiscrepancyReverse(HashMap<String, String> studentMap, List<String> lineList, Set<String> missingStudents, String student, String val) {
        String val2 = studentMap.get(student);
        if(!val2.equals(val)) {
            String appendLine = student + " is " + val + " in Monthly Attendance Report but "
                    + val2 + " in IMPACT\n";
            lineList.add(appendLine);
            missingStudents.add(student);
        }
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

