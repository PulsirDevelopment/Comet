package net.pulsir.comet.utils.calendar;

import java.util.Calendar;
import java.util.Date;

public record ICalendar(Date date) {

    public Date add(String string, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (string.equalsIgnoreCase("days")) {
            calendar.add(Calendar.DATE, amount);
        } else if (string.equalsIgnoreCase("hours")) {
            calendar.add(Calendar.HOUR_OF_DAY, amount);
        } else if (string.equalsIgnoreCase("minutes")) {
            calendar.add(Calendar.MINUTE, amount);
        } else if (string.equalsIgnoreCase("seconds")) {
            calendar.add(Calendar.SECOND, amount);
        }

        return this.date;
    }
}