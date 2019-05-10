package com.example.appointment;

public class date {

    private String Time_date,time_From,time_To;

    public date() {
    }

    public date(String time_date, String time_From, String time_To) {

        this.Time_date = time_date;
        this.time_From = time_From;
        this.time_To = time_To;
    }

    public String getTime_date() {
        return Time_date;
    }

    public void setTime_date(String time_date) {
        Time_date = time_date;
    }

    public String getTime_From() {
        return time_From;
    }

    public void setTime_From(String time_From) {
        this.time_From = time_From;
    }

    public String getTime_To() {
        return time_To;
    }

    public void setTime_To(String time_To) {
        this.time_To = time_To;
    }
}
