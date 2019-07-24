package coleo.com.abjo.data_class;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UserLevel {

    private int level;
    private int point;
    private int totalPoint;
    private int levelMaxPoint;
    private int rank;

    public UserLevel() {
    }

    public UserLevel(int level, int point, int totalPoint, int levelMaxPoint, int rank) {
        this.level = level;
        this.point = point;
        this.totalPoint = totalPoint;
        this.levelMaxPoint = levelMaxPoint;
        this.rank = rank;
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

    public static UserLevel maker(Iterator<String> iterator) {
        int level = Integer.parseInt(iterator.next());
        int point = Integer.parseInt(iterator.next());
        int total = Integer.parseInt(iterator.next());
        int max = Integer.parseInt(iterator.next());
        int rank = Integer.parseInt(iterator.next());
        return new UserLevel(level, point, total, max, rank);
    }

    public Set<String> toSet() {
        HashSet<String> list = new HashSet<>();
        list.add("" + level);
        list.add("" + point);
        list.add("" + totalPoint);
        list.add("" + levelMaxPoint);
        list.add("" + rank);
        return list;
    }


}
