
package coleo.com.abjo.data_class;

public class History extends DateAction {

    private int coin;
    private int point;
    private int distance;
    private ActivityKind kind;

    public History(DateAction dateAction, int coin, int point, int distance, ActivityKind kind) {
        super(dateAction);
        this.coin = coin;
        this.point = point;
        this.distance = distance;
        this.kind = kind;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setKind(ActivityKind kind) {
        this.kind = kind;
    }

    public int getCoin() {
        return coin;
    }

    public int getPoint() {
        return point;
    }

    public int getDistance() {
        return distance;
    }

    public ActivityKind getKind() {
        return kind;
    }
}
