package coleo.com.abjo.data_class;

public class Transition extends DateAction {

    private String title;
    private int coin;

    public Transition(int day, int month, int year, int hour, int minute, int second, String dayName,
                      String monthName, String title, int coin) {
        super(day, month, year, hour, minute, second, dayName, monthName);
        this.title = title;
        this.coin = coin;
    }
    public Transition(DateAction dateAction, String title, int coin) {
        super(dateAction);
        this.title = title;
        this.coin = coin;
    }



    public String getTitle() {
        return title;
    }

    public int getCoin() {
        return coin;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
