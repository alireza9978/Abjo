package coleo.com.abjo.data_class;

public class Introduce extends DateAction {

    private int coin;
    private int point;
    private static final String title = " امتیاز معرفی";

    public Introduce(DateAction dateAction, int coin, int point) {
        super(dateAction);
        this.coin = coin;
        this.point = point;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getCoin() {
        return coin;
    }

    public int getPoint() {
        return point;
    }

    public String getTitle() {
        return title;
    }
}
