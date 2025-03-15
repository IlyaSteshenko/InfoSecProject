package com.informationsecurity.PasteBinService.models;

public enum Months {
    JAN("января"),
    FEB("февраля"),
    MAR("марта"),
    APR("апреля"),
    MAY("мая"),
    JUN("июня"),
    JUL("июля"),
    AUG("августа"),
    SEP("сентября"),
    OCT("октября"),
    NOV("ноября"),
    DEC("декабря");

    private String value;

    Months(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}
