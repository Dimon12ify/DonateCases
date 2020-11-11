package ru.servbuy.donatecases.utils;

import java.text.*;
import java.util.*;

public class TimeUtil
{
    public static String getDate() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        final Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    
    public static String getTime() {
        final SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        final Date now = new Date();
        final String strDate = sdfDate.format(now);
        return strDate;
    }
}
