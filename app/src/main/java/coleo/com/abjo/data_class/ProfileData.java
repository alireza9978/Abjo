package coleo.com.abjo.data_class;

public class ProfileData {

    private User user;
    private int coins;
    private int hours;
    private String note;
    private UserLevel level;

    public ProfileData(User user, int coins, int hours, UserLevel level, String note) {
        this.user = user;
        this.coins = coins;
        this.hours = hours;
        this.level = level;
        setNote(note);
    }

    public void setNote(String note) {
        if (note == null) {
            this.note = " ";
        } else {
            if (note.isEmpty()) {
                this.note = " ";
            } else
                this.note = note;
        }
    }

    public String getNote() {
        return note;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }

    public User getUser() {
        return user;
    }

    public int getCoins() {
        return coins;
    }

    public String getCoinsText() {
        return " " + coins + " سکه ";
    }

    public String getHoursText() {
        return " " + hours + " ساعت ";
    }

    public int getHours() {
        return hours;
    }

    public UserLevel getLevel() {
        return level;
    }
}
