package ru.servbuy.donatecases.userdata;

import ru.servbuy.donatecases.Main;

public class UserData
{
    public static int getCases(final String p, final String donateCase) {
        return Main.getInstance().getStorage().getInt(donateCase + "." + p);
    }
    
    public static void addCases(final String p, final String donateCase, final int amount) {
        Main.getInstance().getStorage().set(donateCase + "." + p, getCases(p, donateCase) + amount);
        Main.getInstance().saveStorage();
        Main.getInstance().reloadStorage();
    }
    
    public static void setCases(final String p, final String donateCase, final int amount) {
        Main.getInstance().getStorage().set(donateCase + "." + p, amount);
        Main.getInstance().saveStorage();
        Main.getInstance().reloadStorage();
    }
    
    public static void removeCases(final String p, final String donateCase, final int amount) {
        if (getCases(p, donateCase) - amount < 0) {
            Main.getInstance().getStorage().set(donateCase + "." + p, 0);
        }
        else {
            Main.getInstance().getStorage().set(donateCase + "." + p, getCases(p, donateCase) - amount);
        }
        Main.getInstance().saveStorage();
        Main.getInstance().reloadStorage();
    }
}
