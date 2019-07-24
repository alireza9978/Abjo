package coleo.com.abjo.data_class;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProfileData implements Serializable {

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

    public static ProfileData maker(HashSet<String> set) {
        if (set == null)
            return null;
        Iterator<String> iterator = set.iterator();
        User user = new User(iterator.next(), iterator.next(), iterator.next(), false);
        int coin = Integer.parseInt(iterator.next());
        int hours = Integer.parseInt(iterator.next());
        String note = iterator.next();
        UserLevel level = UserLevel.maker(iterator);
        return new ProfileData(user, coin, hours, level, note);
    }

    public Set<String> toSet() {
        HashSet<String> list = new HashSet<>(user.toSet());
        list.add("" + coins);
        list.add("" + hours);
        list.add("" + note);
        list.addAll(level.toSet());
        return list;
    }

}
