package ru.servbuy.donatecases.userdata;

import ru.servbuy.donatecases.Main;

public class UserData
{
    public static int getCases(final String p, final String caze) {
        return Main.getInstance().getStorage().getInt(caze + "." + p);
    }
    
    public static void addCases(final String p, final String caze, final int amount) {
        Main.getInstance().getStorage().set(caze + "." + p, getCases(p, caze) + amount);
        Main.getInstance().saveStorage();
        Main.getInstance().reloadStorage();
    }
    
    public static void setCases(final String p, final String caze, final int amount) {
        Main.getInstance().getStorage().set(caze + "." + p, amount);
        Main.getInstance().saveStorage();
        Main.getInstance().reloadStorage();
    }
    
    public static void removeCases(final String p, final String caze, final int amount) {
        if (getCases(p, caze) - amount < 0) {
            Main.getInstance().getStorage().set(caze + "." + p, 0);
        }
        else {
            Main.getInstance().getStorage().set(caze + "." + p, getCases(p, caze) - amount);
        }
        Main.getInstance().saveStorage();
        Main.getInstance().reloadStorage();
    }
}
