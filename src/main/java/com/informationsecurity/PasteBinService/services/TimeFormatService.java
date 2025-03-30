package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Months;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimeFormatService {

    public String getTimeFormat(LocalDateTime time) {
        String[] s = DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm").format(time).split("_");
        return "%s %s %s, %s:%s".formatted(s[0], getMonth(s[1]), s[2], s[3], s[4]);
    }

    private String getMonth(String month) {
        return switch (month) {
            case "01" -> Months.JAN.getName();
            case "02" -> Months.FEB.getName();
            case "03" -> Months.MAR.getName();
            case "04" -> Months.APR.getName();
            case "05" -> Months.MAY.getName();
            case "06" -> Months.JUN.getName();
            case "07" -> Months.JUL.getName();
            case "08" -> Months.AUG.getName();
            case "09" -> Months.SEP.getName();
            case "10" -> Months.OCT.getName();
            case "11" -> Months.NOV.getName();
            case "12" -> Months.DEC.getName();
            default -> "";
        };
    }

}
