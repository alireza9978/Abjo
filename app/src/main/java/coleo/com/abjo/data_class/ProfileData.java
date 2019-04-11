package coleo.com.abjo.data_class;

public class ProfileData {

    private User user;
    private int coins;
    private int hours;
    private UserLevel level;

    public ProfileData(User user, int coins, int hours, UserLevel level) {
        this.user = user;
        this.coins = coins;
        this.hours = hours;
        this.level = level;
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

    public int getHours() {
        return hours;
    }

    public UserLevel getLevel() {
        return level;
    }
}
