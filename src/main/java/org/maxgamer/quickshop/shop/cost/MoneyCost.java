package org.maxgamer.quickshop.shop.cost;

import org.bukkit.entity.Player;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.util.Util;

import java.util.Map;

public class MoneyCost implements IShopCost {

    private double cost;

    @Override
    public String getPriceString() {
        return Util.format(cost);
    }

    @Override
    public boolean has(Player player) {
        return QuickShop.getInstance().getEconomy().getBalance(player.getUniqueId()) >= cost;
    }

    @Override
    public <T> boolean update(T value, Map.Entry<T, T> restriction) {
        cost = (Double) value;
        return check(restriction);
    }

    @Override
    public <T> boolean check(Map.Entry<T, T> restriction) {
        @SuppressWarnings("unchecked")
        Map.Entry<Double, Double> doubleRestriction = (Map.Entry<Double, Double>) restriction;
        if (restriction != null) {
            if (cost < doubleRestriction.getKey()) {
                cost = doubleRestriction.getKey();
                return false;
            } else if (cost > doubleRestriction.getValue()) {
                cost = doubleRestriction.getValue();
                return false;
            }
        }
        return true;
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
        return CostType.Money;
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
