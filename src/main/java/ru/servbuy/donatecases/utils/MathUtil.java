package ru.servbuy.donatecases.utils;

import ru.servbuy.donatecases.structure.Item;
import java.util.concurrent.*;

public class MathUtil
{
    public static Item getWinItem(final Item[] items) {
        final int x = ThreadLocalRandom.current().nextInt(100) + 1;
        int l = 0;
        for (final Item item : items) {
            final int c = item.getChance();
            if (x >= 1 && x <= l + c) {
                return item;
            }
            l += c;
        }
        return null;
    }
}
