package org.maxgamer.quickshop.shop.cost;

import org.bukkit.entity.Player;

public interface IShopCost<T> {

    String getDisplay();

    boolean has(Player player);

    void set(T value);

    boolean buy(Player player, int amount);

    boolean sell(Player player, int amount);
}
