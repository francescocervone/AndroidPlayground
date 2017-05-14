package com.francescocervone.movies.ui.common;


import java.util.List;

public class TextUtils {
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string);
    }

    public static String join(String separator, List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        StringBuilder stringBuilder = new StringBuilder(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            stringBuilder.append(separator)
                    .append(list.get(i));
        }
        return stringBuilder.toString();
    }
}
