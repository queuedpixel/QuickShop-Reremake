package org.maxgamer.quickshop.shop.cost;

import org.bukkit.entity.Player;

import java.util.Map;

public interface IShopCost {

    String getPriceString();

    boolean has(Player player);

    default <T> boolean update(T value) {
        return update(value, null);
    }

    <T> boolean update(T value, Map.Entry<T, T> restriction);

    <T> boolean check(Map.Entry<T, T> restoration);

    boolean buy(Player player, int amount);

    boolean sell(Player player, int amount);

    CostType getType();

    String serialize();

    String deserialize();
}
