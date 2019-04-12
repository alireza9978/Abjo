package coleo.com.abjo.data_class;

public class NavigationDrawerItem {

    private String name;
    private int icon;

    public NavigationDrawerItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}
