package org.maxgamer.quickshop.shop.cost;

import org.bukkit.entity.Player;

import java.util.Map;

public class ItemCost implements IShopCost {
    @Override
    public String getPriceString(int amount) {
        return null;
    }

    @Override
    public boolean has(Player player) {
        return false;
    }

    @Override
    public <T> boolean update(T value, Map.Entry<T, T> restriction) {
        return false;
    }

    @Override
    public <T> boolean check(Map.Entry<T, T> restoration) {
        return false;
    }

    @Override
    public boolean buy(Player player, int amount) {
        return false;
    }

    @Override
    public boolean sell(Player player, int amount) {
        return false;
    }

    @Override
    public CostType getType() {
        return null;
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public String deserialize() {
        return null;
    }
}
