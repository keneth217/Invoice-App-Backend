package com.app.invoice.utils;

public class Functions {
    public static double stringToDouble(String str){
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException er){
            er.printStackTrace();
            return 0;
        }
    }
    public static long stringToLong(String str){
        try {
            return Long.parseLong(str.replace(",", "").replace(" ", "").replace("+", ""));
        } catch (NumberFormatException er){
            er.printStackTrace();
            return 0;
        }
    }
}
