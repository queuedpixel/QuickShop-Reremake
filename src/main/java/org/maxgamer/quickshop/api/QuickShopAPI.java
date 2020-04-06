package org.maxgamer.quickshop.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.util.IntegrationHelper;
import org.maxgamer.quickshop.util.compatibility.CompatibilityManager;

public class QuickShopAPI {
    private static QuickShop plugin;

    public static void setupApi(@NotNull QuickShop qs){
        plugin = qs;
    }

    /**
     * Gets apis about shop
     * @return The Shop API
     */
    public static @NotNull ShopAPI getShopAPI(){
        return new ShopAPI(plugin);
    }

    /**
     * Gets apis about display item
     * @return The DisplayItem API
     */
    public static @NotNull DisplayItemAPI getDisplayItemAPI(){
        return new DisplayItemAPI(plugin);
    }

    /**
     * Gets anti-cheat compatibility manager to allow you access and process.
     * If you calling this before plugin loaded up, you might get nothing.
     * @return Compatibility Manager
     */
    public static @Nullable CompatibilityManager getCompatibilityManager(){
        return plugin.getCompatibilityTool();
    }

    /**
     * Gets protection plugins integration helper to allow hook your plugin to our checks system.
     * If you calling this before plugin loaded up, you might get nothing.
     * @return IntegrationHelper
     */
    public static @Nullable IntegrationHelper getIntegrationManager(){
        return plugin.getIntegrationHelper();
    }
}
