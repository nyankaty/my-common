package com.company.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CurrencyUtil {

    private static final Locale localeVN = new Locale("vi", "VN");
    private static final Locale localeEN = new Locale("en", "EN");
    private static final DecimalFormat df = new DecimalFormat("#");

    public static String roundingAndFormat(BigDecimal bigDecimal) {
        NumberFormat numberFormat = NumberFormat.getInstance(localeEN);
        return numberFormat.format(Long.parseLong(df.format(bigDecimal)));
    }

    public static String formatVN(BigDecimal bigDecimal) {
        NumberFormat numberFormat = NumberFormat.getInstance(localeVN);
        return numberFormat.format(bigDecimal);
    }

    public static String formatEN(BigDecimal bigDecimal) {
        NumberFormat numberFormat = NumberFormat.getInstance(localeEN);
        return numberFormat.format(bigDecimal);
    }

    @SuppressWarnings("java:S3864")
    public static String num2String(long num) {

        if (num == 0L) {
            return "không";
        }

        if (num < 0L) {
            return "âm " + num2String(-num);
        }

        String str = Long.toString(num);

        // zero padding in front of string to prepare for splitting
        switch (str.length() % 3) {
            case 1:
                str = "00" + str;
                break;
            case 2:
                str = "0" + str;
                break;
            default:
                break;
        }

        // Split into chunks of 3 digits each
        List<String> groupOfThousand = Arrays.asList(str.split("(?<=\\G.{3})"));

        boolean showZeroHundred = doShowZeroHundred(groupOfThousand);

        AtomicInteger index = new AtomicInteger();
        return groupOfThousand.stream()
                .map(triplet -> readTriple(triplet, showZeroHundred && index.get() > 0))
                .map(vnString -> vnString.trim().isEmpty()
                        ? ""
                        : vnString + " " + thousandsName.get(groupOfThousand.size() - 1 - index.get())
                )
                .peek(s -> index.getAndIncrement())
                .collect(Collectors.joining(" "))
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static final List<String> digitsName = Arrays.asList(
            "không",
            "một",
            "hai",
            "ba",
            "bốn",
            "năm",
            "sáu",
            "bảy",
            "tám",
            "chín"
    );
    private static final List<String> thousandsName = Arrays.asList(
            "",
            "nghìn",
            "triệu",
            "tỷ",
            "nghìn tỷ",
            "triệu tỷ",
            "tỷ tỷ"
    );

    /**
     * turn triplet digits into vietnamese words
     *
     * @param triplet         a string of 3 digit integer
     * @param showZeroHundred whether to show Zero hundred
     * @return vietnamese string represent the input number
     */
    private static String readTriple(String triplet, boolean showZeroHundred) {
        List<Integer> digitList = stringToInt(triplet);

        int a = digitList.get(0);
        int b = digitList.get(1);
        int c = digitList.get(2);

        if (a == 0) {
            if (b == 0 && c == 0) {
                return "";
            }

            if (showZeroHundred) {
                return "không trăm " + readPair(b, c);
            }

            if (b == 0) {
                return digitsName.get(c);
            } else {
                return readPair(b, c);
            }

        }

        return digitsName.get(a) + " trăm " + readPair(b, c);
    }


    private static String readPair(int b, int c) {
        String temp;

        switch (b) {
            case 0:
                return c == 0 ? "" : "lẻ " + digitsName.get(c);
            case 1:
                switch (c) {
                    case 0:
                        temp = " ";
                        break;
                    case 5:
                        temp = "lăm ";
                        break;
                    default:
                        temp = digitsName.get(c);
                        break;
                }

                return "mười " + temp;
            default:
                switch (c) {
                    case 0:
                        temp = "";
                        break;
                    case 1:
                        temp = "mốt ";
                        break;
                    case 4:
                        temp = "tư ";
                        break;
                    case 5:
                        temp = "lăm ";
                        break;
                    default:
                        temp = digitsName.get(c);
                        break;
                }

                return digitsName.get(b) + " mươi " + temp;
        }
    }

    private static List<Integer> stringToInt(String triplet) {
        return triplet.chars()
                .map(CurrencyUtil::charToInt)
                .boxed()
                .collect(Collectors.toList());
    }

    private static int charToInt(int c) {
        return c - '0';
    }

    /**
     * determine whether to show zero-hundred text
     * <p>
     * Explain: count the amount of consecutive "000" at the end of the number and
     * compare it with number length
     *
     * @param groupOfThousand number represented in group of 3 digits of each 1000^n
     * @return a boolean
     */
    private static boolean doShowZeroHundred(List<String> groupOfThousand) {
        int count = 0;
        int i = groupOfThousand.size() - 1;
        while (i >= 0 && groupOfThousand.get(i).equals("000")) {
            count++;
            i--;
        }

        return count < groupOfThousand.size() - 1;
    }

    private CurrencyUtil() {
        throw new IllegalStateException("Utility class");
    }
}
