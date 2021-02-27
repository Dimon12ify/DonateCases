package ru.servbuy.donatecases.utils;

import ru.servbuy.donatecases.structure.Item;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtil
{
    public static Item getWinItem(final Item[] items) {
        if (items.length == 0)
            return null;
        int sum = Arrays.stream(items).mapToInt(Item::getChance).sum();
        final int randomNum = ThreadLocalRandom.current().nextInt(sum);
        int currentSum = 0;
        for (final Item item : items) {
            int chance = item.getChance();
            if (randomNum <= currentSum + chance) {
                return item;
            }
            currentSum += chance;
        }
        return null;
    }
}
