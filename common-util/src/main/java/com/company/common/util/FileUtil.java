package com.company.common.util;

public final class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getExtension(String fileName) {
        String filename = FileUtil.getSafeFileName(fileName);
        int last = filename.lastIndexOf('.');
        return filename.substring(last + 1).toLowerCase();
    }

    public static String getSafeFileName(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '/' && c != '\\' && c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getActualFileName(String fileName) {
        String filename = FileUtil.getSafeFileName(fileName);
        int last = filename.lastIndexOf(StringPool.DOT);
        return filename.substring(0, last);
    }

    public static boolean validateExtension(String fileName) {
        if (fileName == null) {
            return false;
        }
        int last = fileName.lastIndexOf(StringPool.DOT);
        if (last < 0) {
            return false;
        }
        String fileType = fileName.substring(last + 1);
        return Constant.EXTENSIONS.contains(fileType.toLowerCase());
    }
}
