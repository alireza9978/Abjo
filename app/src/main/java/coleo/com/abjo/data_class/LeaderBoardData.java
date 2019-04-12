package coleo.com.abjo.data_class;

public class LeaderBoardData {

    private User user;
    private int rank;
    private int point;
    private boolean isMine;
    private boolean isBlur;

    public LeaderBoardData(User user, int rank, int point, boolean isMine, boolean isBlur) {
        this.user = user;
        this.rank = rank;
        this.point = point;
        this.isMine = isMine;
        this.isBlur = isBlur;
    }

    public LeaderBoardData(User user, int rank, int point) {
        this.user = user;
        this.rank = rank;
        this.point = point;
        this.isMine = false;
        this.isBlur = false;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getRank() {
        return rank;
    }

    public int getPoint() {
        return point;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBlur(boolean blur) {
        isBlur = blur;
    }

    public User getUser() {
        return user;
    }


    public boolean isBlur() {
        return isBlur;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isMine() {
        return isMine;
    }
}
