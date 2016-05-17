package com.mark.gubatan;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mark on 5/16/2016.
 * TODO: Handle names spread across multiple pages
 */
public class DiscrepancyDetector {

    public static String getDiscrepancies(HashMap<String, String> impact, HashMap<String, String> mad) {
//        StringBuilder sb = new StringBuilder();
        StringBuilder absences = new StringBuilder();

        for (Map.Entry<String, String> entry : impact.entrySet()) {
            String key = entry.getKey();
            String key2 = key.substring(0, key.length() - 2);
            String val = entry.getValue();

            if(mad.containsKey(key)) {
                String val2 = mad.get(key);
                if(!val2.equals(val)) {
//                    System.out.println(val + " " + val2);
                    absences.append(key + " is " + val + " in IMPACT but " + val2 + " in Monthly Attendance Report\n");
                }
            }
            else if(mad.containsKey(key2)) {
                String val2 = mad.get(key2);
                if(!val2.equals(val)) {
//                    System.out.println(val + " " + val2);
                    absences.append(key2 + " is " + val + " in IMPACT but " + val2 + " in Monthly Attendance Report\n");
                }
            }
//            else {
//                sb.append(key + " is in IMPACT but not in Monthly Attendance Report\n");
//            }
        }

//        for (Map.Entry<String, String> entry : mad.entrySet()) {
//            String key = entry.getKey();
//            String val = entry.getValue();
//
//            if(impact.containsKey(key)) {
//                String val2 = impact.get(key);
//                if(!val2.equals(val)) {
//                    System.out.println(val + " " + val2);
//                    absences.append(key + " is " + val + " in Monthly Attendance Report but " + val2 + " in IMPACT\n");
//                }
//            }
//            else {
//                sb.append(key + " is in Monthly Attendance Report but not in IMPACT\n");
//            }
//        }

//        return sb.toString() + absences.toString();
        return absences.toString();
    }
}
