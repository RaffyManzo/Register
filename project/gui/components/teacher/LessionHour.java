package project.gui.components.teacher;

import javax.swing.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public enum LessionHour {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5),
    SIXTH(6),
    SEVENTH(7),
    EIGHTH(8);

    private int value;

    LessionHour(int value) {
        this.value = value;
    }


    public static LessionHour getHourByTime() throws Exception {
        Calendar c = Calendar.getInstance();

         class BadHourException extends Exception {
            BadHourException() {
                super("Hour out of bounds");

            }
        }

        return switch (c.get(Calendar.HOUR_OF_DAY)) {
            case 9 -> FIRST;
            case 10 -> SECOND;
            case 11 -> THIRD;
            case 12 -> FOURTH;
            case 13 -> FIFTH;
            case 14 -> SIXTH;
            case 15 -> SEVENTH;
            case 16 -> EIGHTH;
            default -> throw new BadHourException();
        };

    }

    public int getValue() {
        return value;
    }
}
