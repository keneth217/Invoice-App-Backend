package com.app.invoice.utils;

public class Base36Converter {
    // Define the custom base-30 alphabet, avoiding confusing characters
    private static final String CUSTOM_BASE30_ALPHABET = "12346789ABCDEFGHJKMNPQRTUVWXYZ";

    public static String toCustomBase30(long decimal) {
        if (decimal == 0) return "1";  // Start with "1" to avoid "0"

        StringBuilder base30 = new StringBuilder();
        long quotient = decimal;

        // Custom base-30 conversion
        while (quotient > 0) {
            long remainder = quotient % 30;
            base30.append(CUSTOM_BASE30_ALPHABET.charAt((int)remainder));
            quotient /= 30;
        }

        // Reverse to get the correct order
        return base30.reverse().toString();
    }
    public static String toBase36(long decimal) {
        if (decimal == 0) return "0";

        StringBuilder base36 = new StringBuilder();
        long quotient = decimal;

        // Base-36 conversion
        while (quotient > 0) {
            long remainder = quotient % 36;
            // Map remainder to 0-9 or A-Z
            if (remainder < 10) {
                base36.append((char) ('0' + remainder));
            } else {
                base36.append((char) ('A' + (remainder - 10)));
            }
            quotient /= 36;
        }

        // Reverse to get the correct order
        return base36.reverse().toString();
    }
}
