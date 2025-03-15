package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Months;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimeFormatService {

    public String getTimeFormat(LocalDateTime time) {
        String[] s = DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm").format(time).split("_");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(s[0]).append(" ")
                .append(getMonth(s[1])).append(" ")
                .append(s[2])
                .append(", ")
                .append(s[3]).append(":")
                .append(s[4]);
        return stringBuilder.toString();
    }

    private String getMonth(String month) {
        String result = "";
        switch (month) {
            case "01" -> result = Months.JAN.getName();
            case "02" -> result = Months.FEB.getName();
            case "03" -> result = Months.MAR.getName();
            case "04" -> result = Months.APR.getName();
            case "05" -> result = Months.MAY.getName();
            case "06" -> result = Months.JUN.getName();
            case "07" -> result = Months.JUL.getName();
            case "08" -> result = Months.AUG.getName();
            case "09" -> result = Months.SEP.getName();
            case "10" -> result = Months.OCT.getName();
            case "11" -> result = Months.NOV.getName();
            case "12" -> result = Months.DEC.getName();
        }
        return result;
    }

}
