package com.oocl.parkingreservationservice.utils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static boolean validateMobilePhone(String phone) {
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        return pattern.matcher(phone).matches();
    }

}
