package net.pulsir.comet.utils.calendar;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public class ICalendar {

    private Date date;
    private Calendar calendar;

    public ICalendar(Date date){
        this.date = date;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
    }

    public Date add(String string, int amount) {
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