package coleo.com.abjo.data_class;

import android.content.Intent;

public class NavigationDrawerItem {

    private String name;
    private int icon;
    private Intent intent;

    public NavigationDrawerItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public NavigationDrawerItem(String name, int icon, Intent intent) {
        this.name = name;
        this.icon = icon;
        this.intent = intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
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
