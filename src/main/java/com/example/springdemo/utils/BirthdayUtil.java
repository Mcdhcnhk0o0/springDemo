package com.example.springdemo.utils;

import java.util.Calendar;
import java.util.Date;

public class BirthdayUtil {

    public static final String[] CONSTELLATION_ARR = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
    public static final int[] CONSTELLATION_EDGE_DAY = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

    public static Date parseDateFrom8BitBirthday(String bitBirthday) {
        if (bitBirthday == null || bitBirthday.length() != 8) {
            throw new IllegalArgumentException("birthday length should equal to 8");
        }
        int year = Integer.parseInt(bitBirthday.substring(0, 4));
        int month = Integer.parseInt(bitBirthday.substring(4, 6));
        int day = Integer.parseInt(bitBirthday.substring(6, 8));
        return new Date(year, month, day);
    }

    public static String getConstellation(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day < CONSTELLATION_EDGE_DAY[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return CONSTELLATION_ARR[month];
        }
        return CONSTELLATION_ARR[11];
    }

}
