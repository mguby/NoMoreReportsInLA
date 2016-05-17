package com.mark.gubatan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark on 5/16/2016.
 * TODO: Handle names spread across multiple pages
 */
public class DiscrepancyDetector {

    public static String getDiscrepancies(HashMap<String, String> impact, HashMap<String, String> mad) {
//        StringBuilder tableDiscrepancies = new StringBuilder();
        StringBuilder absences = new StringBuilder();

        for (Map.Entry<String, String> entry : impact.entrySet()) {
            String student = entry.getKey();
            String studentNoMiddleName = student.substring(0, student.length() - 2);
            String val = entry.getValue();

            if(mad.containsKey(student)) {
                String val2 = mad.get(student);
                if(!val2.equals(val)) {
                    String appendLine = student + " is " + val + " in IMPACT but "
                            + val2 + " in Monthly Attendance Report\n";
                    absences.append(appendLine);
                }
            }
            else if(mad.containsKey(studentNoMiddleName)) {
                String val2 = mad.get(studentNoMiddleName);
                if(!val2.equals(val)) {
                    String appendLine = studentNoMiddleName + " is " + val + " in IMPACT but "
                            + val2 + " in Monthly Attendance Report\n";
                    absences.append(appendLine);
                }
            }
//            else {
//                tableDiscrepancies.append(key + " is in IMPACT but not in Monthly Attendance Report\n");
//            }
        }

//        for (Map.Entry<String, String> entry : mad.entrySet()) {
//            String key = entry.getKey();
//            String val = entry.getValue();
//
//            if(!impact.containsKey(key))
//                tableDiscrepancies.append(key + " is in Monthly Attendance Report but not in IMPACT\n");
//        }

//        return tableDiscrepancies.toString() + absences.toString();
        return absences.toString();
    }
}
