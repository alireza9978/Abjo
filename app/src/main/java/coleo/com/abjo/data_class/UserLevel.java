package coleo.com.abjo.data_class;

public class UserLevel {

    private int level;
    private int point;
    private int totalPoint;
    private int levelMaxPoint;
    private int rank;

    public UserLevel() {
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public void setLevelMaxPoint(int levelMaxPoint) {
        this.levelMaxPoint = levelMaxPoint;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public int getPoint() {
        return point;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public int getLevelMaxPoint() {
        return levelMaxPoint;
    }

    public int getRank() {
        return rank;
    }
}
