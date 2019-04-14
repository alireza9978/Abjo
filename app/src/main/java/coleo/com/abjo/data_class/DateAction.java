package coleo.com.abjo.data_class;

public class DateAction {

    private int day;
    private int month;
    private int year;

    private int hour;
    private int minute;
    private int second;

    private String dayName;
    private String monthName;

    public DateAction(int day, int month, int year, int hour, int minute, int second
            , String dayName, String monthName) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.dayName = dayName;
        this.monthName = monthName;
    }

    protected DateAction(DateAction dateAction) {
        this.day = dateAction.getDay();
        this.month = dateAction.getMonth();
        this.year = dateAction.getYear();
        this.hour = dateAction.getHour();
        this.minute = dateAction.getMinute();
        this.second = dateAction.getSecond();
        this.dayName = dateAction.getDayName();
        this.monthName = dateAction.getMonthName();
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getSecond() {
        return second;
    }

    public String getDayName() {
        return dayName;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getString() {
        return year +
                "/" + month +
                "/" + day +
                "\n" + hour +
                ":" + minute;
    }
}
