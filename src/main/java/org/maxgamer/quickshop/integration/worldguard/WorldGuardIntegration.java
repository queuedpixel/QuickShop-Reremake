/*
 * This file is a part of project QuickShop, the name is WorldGuardIntegration.java
 *  Copyright (C) Ghost_chu <https://github.com/Ghost-chu>
 *  Copyright (C) PotatoCraft Studio and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.maxgamer.quickshop.integration.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.integration.IntegrateStage;
import org.maxgamer.quickshop.integration.IntegratedPlugin;
import org.maxgamer.quickshop.integration.IntegrationStage;
import org.maxgamer.quickshop.util.Util;

import java.util.List;

@SuppressWarnings("DuplicatedCode")
@IntegrationStage(loadStage = IntegrateStage.onLoadAfter)
public class WorldGuardIntegration implements IntegratedPlugin {
    private List<WorldGuardFlags> createFlags;

    private List<WorldGuardFlags> tradeFlags;

    private final StateFlag createFlag = new StateFlag("quickshop-create", false);

    private final StateFlag tradeFlag = new StateFlag("quickshop-trade", true);

    private boolean anyOwner;

    private final QuickShop plugin;
    private boolean whiteList;

    public WorldGuardIntegration(QuickShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        this.whiteList = plugin.getConfig().getBoolean("integration.worldguard.whitelist-mode");
        this.anyOwner = plugin.getConfig().getBoolean("integration.worldguard.any-owner");
        createFlags =
                WorldGuardFlags.deserialize(
                        plugin.getConfig().getStringList("integration.worldguard.create"));
        tradeFlags =
                WorldGuardFlags.deserialize(
                        plugin.getConfig().getStringList("integration.worldguard.trade"));
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            registry.register(this.createFlag);
            registry.register(this.tradeFlag);
            plugin.getLogger().info(ChatColor.GREEN + getName() + " flags register successfully.");
            Util.debugLog("Success register " + getName() + " flags.");
        } catch (FlagConflictException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unload() {
    }

    @Override
    public @NotNull String getName() {
        return "WorldGuard";
    }

    @Override
    public boolean canCreateShopHere(@NotNull Player player, @NotNull Location location) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        com.sk89q.worldedit.util.Location wgLoc = BukkitAdapter.adapt(location);
        boolean canBypass =
                WorldGuard.getInstance()
                        .getPlatform()
                        .getSessionManager()
                        .hasBypass(localPlayer, BukkitAdapter.adapt(location.getWorld()));
        if (canBypass) {
            Util.debugLog(
                    "Player "
                            + player.getName()
                            + " bypassing the protection checks, because player have bypass permission in WorldGuard");
            return true;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (query.getApplicableRegions(wgLoc).getRegions().isEmpty()) {
            return !whiteList;
        }
        for (WorldGuardFlags flag : createFlags) {
            switch (flag) {
                case BUILD:
                    if (query.queryState(wgLoc, localPlayer, Flags.BUILD) == StateFlag.State.DENY) {
                        return false;
                    }
                    break;
                case FLAG:
                    if (query.queryState(wgLoc, localPlayer, this.createFlag) == StateFlag.State.DENY) {
                        return false;
                    }
                    break;
                case CHEST_ACCESS:
                    if (query.queryState(wgLoc, localPlayer, Flags.CHEST_ACCESS) == StateFlag.State.DENY) {
                        return false;
                    }
                    break;
                case INTERACT:
                    if (query.queryState(wgLoc, localPlayer, Flags.INTERACT) == StateFlag.State.DENY) {
                        return false;
                    }
                case OWN:
                    if (anyOwner) {
                        if (query.getApplicableRegions(wgLoc).getRegions().stream().noneMatch(region -> region.isOwner(localPlayer))) {
                            return false;
                        }
                    } else {
                        if (!query.getApplicableRegions(wgLoc).isOwnerOfAll(localPlayer)) {
                            return false;
                        }
                    }
            }
        }
        return true;
    }

    @Override
    public boolean canTradeShopHere(@NotNull Player player, @NotNull Location location) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        com.sk89q.worldedit.util.Location wgLoc = BukkitAdapter.adapt(location);
        boolean canBypass =
                WorldGuard.getInstance()
                        .getPlatform()
                        .getSessionManager()
                        .hasBypass(localPlayer, BukkitAdapter.adapt(location.getWorld()));
        if (canBypass) {
            Util.debugLog(
                    "Player "
                            + player.getName()
                            + " bypassing the protection checks, because player have bypass permission in WorldGuard");
            return true;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (query.getApplicableRegions(wgLoc).getRegions().isEmpty()) {
            return !whiteList;
        }
        for (WorldGuardFlags flag : tradeFlags) {
            switch (flag) {
                case BUILD:
                    if (!query.testState(wgLoc, localPlayer, Flags.BUILD)) {
                        return false;
                    }
                    break;
                case FLAG:
                    if (!query.testState(wgLoc, localPlayer, this.tradeFlag)) {
                        return false;
                    }
                    break;
                case CHEST_ACCESS:
                    if (!query.testState(wgLoc, localPlayer, Flags.CHEST_ACCESS)) {
                        return false;
                    }
                    break;
                case INTERACT:
                    if (!query.testState(wgLoc, localPlayer, Flags.INTERACT)) {
                        return false;
                    }
                case OWN:
                    if (anyOwner) {
                        return query.getApplicableRegions(wgLoc).getRegions().stream().anyMatch(region -> region.isOwner(localPlayer));
                    } else {
                        return query.getApplicableRegions(wgLoc).isOwnerOfAll(localPlayer);
                    }
            }
        }
        return true;
    }

}
