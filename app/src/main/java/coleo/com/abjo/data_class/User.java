package coleo.com.abjo.data_class;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String firstName;
    private String lastName;
    private String number;
    private boolean isWoman;

    public User(String firstName, String lastName, String number, boolean isWoman) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.isWoman = isWoman;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setWoman(boolean woman) {
        isWoman = woman;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNumber() {
        return number;
    }

    public boolean isWoman() {
        return isWoman;
    }

    public String getFullName() {
        return " " + firstName + " " + lastName + " ";
    }

    public Set<String> toSet() {
        HashSet<String> list = new HashSet<>();
        list.add(firstName);
        list.add(lastName);
        list.add(number);
        return list;
    }

}
