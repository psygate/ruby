/*
 *     Copyright (C) 2016 psygate (https://github.com/psygate)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 */

package com.psygate.minecraft.spigot.sovereignty.ruby.listeners;

import com.psygate.minecraft.spigot.sovereignty.amethyst.Amethyst;
import com.psygate.minecraft.spigot.sovereignty.amethyst.events.PlayerCreateReinforcementEvent;
import com.psygate.minecraft.spigot.sovereignty.amethyst.events.PlayerDamageReinforcementEvent;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.ruby.Ruby;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.Snitch;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.data.SnitchType;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordEntityType;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordLocationType;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.RecordPlayerType;
import com.psygate.minecraft.spigot.sovereignty.ruby.events.SnitchRecord;
import com.psygate.minecraft.spigot.sovereignty.ruby.logging.LogType;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.BroadcastManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordPersistenceManager;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.redstone.RedstoneEmitter;
import com.psygate.minecraft.spigot.sovereignty.ruby.util.RubyLocation;
import com.psygate.spatial.primitives.IntPoint3D;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.*;
import java.util.logging.Logger;

import static com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordFilterUtil.isRedstoneEmitable;
import static com.psygate.minecraft.spigot.sovereignty.ruby.util.RecordFilterUtil.isVanished;

/**
 * Created by psygate (https://github.com/psygate) on 22.03.2016.
 */
public class SnitchListener implements Listener {
    private final static Logger LOG = Ruby.getLogger(SnitchListener.class.getName());
    private final Set<EntityType> mountable = new HashSet<>();

    public SnitchListener() {
        mountable.add(EntityType.MINECART);
        mountable.add(EntityType.HORSE);

        for (World world : Bukkit.getWorlds()) {
            SnitchManager.getInstance().loadAll(world);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent ev) {
        SnitchManager.getInstance().loadAll(ev.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAccessibleOpen(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && Amethyst.getInstance().getConf().getSettings().isContainer(ev.getClickedBlock().getType())) {
            if (!isPlayerLoggable(ev.getPlayer())) {
                return;
            }

            if (SnitchManager.getInstance().hasValuesContaining(ev.getPlayer().getLocation())) {
                Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getPlayer().getLocation());

                for (Snitch snitch : snitches) {
                    SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_ACCESS);
                    pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
                    pev.add(RecordLocationType.BLOCK, new RubyLocation(ev.getClickedBlock().getLocation()));
                    pev.add(ev.getClickedBlock().getType());

                    RecordPersistenceManager.getInstance().persist(pev);
                    BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);

                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent ev) {
        if (ev.getRightClicked() != null) {
            if (!isPlayerLoggable(ev.getPlayer())) {
                return;
            }

            if (mountable.contains(ev.getRightClicked().getType())) {
                if (SnitchManager.getInstance().hasValuesContaining(ev.getPlayer().getLocation())) {
                    Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getPlayer().getLocation());

                    for (Snitch snitch : snitches) {
                        SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_MOUNT_ENTITY);
                        pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
                        pev.add(RecordEntityType.MOUNTED, ev.getRightClicked());
                        pev.add(RecordLocationType.ENTITY, new RubyLocation(ev.getRightClicked().getLocation()));
                        RecordPersistenceManager.getInstance().persist(pev);
                        BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerReinforce(PlayerCreateReinforcementEvent ev) {
        if (ev.getTarget().getType() == Ruby.getConf().getSnitches().getType()) {
            SnitchManager.getInstance().createSnitch(
                    ev.getTarget().getChunk().getX() * 16,
                    ev.getTarget().getChunk().getZ() * 16,
                    new BlockKey(ev.getTarget()),
                    ev.getGroupID(),
                    ev.getPlayer().getUniqueId(),
                    () -> ev.getPlayer().sendMessage(ChatColor.RED + "Snitch creation failed. Chunk limit exceeded."),
                    () -> ev.getPlayer().sendMessage(ChatColor.GREEN + "Snitch created."),
                    SnitchType.SNITCH
            );
        } else if (ev.getTarget().getType() == Ruby.getConf().getAlerts().getType()) {
            SnitchManager.getInstance().createSnitch(
                    ev.getTarget().getChunk().getX() * 16,
                    ev.getTarget().getChunk().getZ() * 16,
                    new BlockKey(ev.getTarget()),
                    ev.getGroupID(),
                    ev.getPlayer().getUniqueId(),
                    () -> ev.getPlayer().sendMessage(ChatColor.RED + "Alert creation failed. Chunk limit exceeded."),
                    () -> ev.getPlayer().sendMessage(ChatColor.GREEN + "Alert created."),
                    SnitchType.ALERT
            );
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReinforcementDestroy(PlayerDamageReinforcementEvent ev) {
        if (ev.getDamage() >= ev.getHealth()) {
            if (SnitchManager.getInstance().destroySnitch(new BlockKey(ev.getTarget()))) {
                ev.getPlayer().sendMessage(ChatColor.RED + "Snitch broken.");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerTeleportEvent ev) {
        processFromTo(ev.getPlayer(), ev.getFrom(), ev.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent ev) {
        processFromTo(ev.getPlayer(), ev.getFrom(), ev.getTo());
    }

    private void processFromTo(Player player, Location lfrom, Location lto) {
        if (!isPlayerLoggable(player)) {
            return;
        }

        IntPoint3D from = mapToPoint(lfrom);
        IntPoint3D to = mapToPoint(lto);

        if (SnitchManager.getInstance().hasValuesContaining(lto)) {
            Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(lto);
            for (Snitch snitch : snitches) {

                if (snitch.getBounds().contains(to) && !snitch.getBounds().contains(from)) {
                    SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_ENTER);
                    pev.add(RecordPlayerType.PLAYER, player);
                    pev.add(RecordLocationType.PLAYER, new RubyLocation(player.getLocation()));

                    RecordPersistenceManager.getInstance().persist(pev);
                    BroadcastManager.getInstance().broadcast(pev, player, snitch);
                    if (Ruby.getConf().isEmitRedstone(snitch.getSnitchType()) && isRedstoneEmitable(player, snitch)) {
                        RedstoneEmitter.getInstance().emitEnter(snitch, player);
                    }
                }
            }
        }


        if (SnitchManager.getInstance().hasValuesContaining(lfrom)) {
            Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(lfrom);
            for (Snitch snitch : snitches) {

                if (!snitch.getBounds().contains(to) && snitch.getBounds().contains(from)) {
                    SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_EXIT);
                    pev.add(RecordPlayerType.PLAYER, player);
                    pev.add(RecordLocationType.PLAYER, new RubyLocation(player.getLocation()));

                    RecordPersistenceManager.getInstance().persist(pev);
                    BroadcastManager.getInstance().broadcast(pev, player, snitch);
                    if (Ruby.getConf().isEmitRedstone(snitch.getSnitchType()) && isRedstoneEmitable(player, snitch)) {
                        RedstoneEmitter.getInstance().emitExit(snitch, player);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLogin(PlayerJoinEvent ev) {
        if (!isPlayerLoggable(ev.getPlayer())) {
            return;
        }

        if (SnitchManager.getInstance().hasValuesContaining(ev.getPlayer().getLocation())) {
            Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getPlayer().getLocation());

            for (Snitch snitch : snitches) {
                SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_LOGIN);
                pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
                pev.add(RecordLocationType.PLAYER, new RubyLocation(ev.getPlayer().getLocation()));

                RecordPersistenceManager.getInstance().persist(pev);
                BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLogout(PlayerQuitEvent ev) {
        if (!isPlayerLoggable(ev.getPlayer())) {
            return;
        }

        if (SnitchManager.getInstance().hasValuesContaining(ev.getPlayer().getLocation())) {
            Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getPlayer().getLocation());

            for (Snitch snitch : snitches) {
                SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_LOGOUT);
                pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
                pev.add(RecordLocationType.PLAYER, new RubyLocation(ev.getPlayer().getLocation()));

                RecordPersistenceManager.getInstance().persist(pev);
                BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLogout(PlayerDamageReinforcementEvent ev) {
        if (!isPlayerLoggable(ev.getPlayer())) {
            return;
        }

        if (SnitchManager.getInstance().hasValuesContaining(ev.getTarget())) {
            Material type = ev.getTarget().getWorld().getBlockAt(ev.getTarget().getLocation()).getType();
            UUID puuid = ev.getPlayer().getUniqueId();

            Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getTarget());

            for (Snitch snitch : snitches) {
                SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_REINFORCEMENT_DAMAGE);
                pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
                pev.add(RecordLocationType.BLOCK, new RubyLocation(ev.getTarget().getLocation()));
                pev.add(RecordLocationType.PLAYER, new RubyLocation(ev.getPlayer().getLocation()));
                pev.add(ev.getTarget().getType());
                RecordPersistenceManager.getInstance().persist(pev);
                BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent ev) {
        if (!isPlayerLoggable(ev.getPlayer())) {
            return;
        }

        Block block = ev.getBlock();

        for (Snitch snitch : SnitchManager.getInstance().getValuesContaining(block.getLocation())) {
            SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_BLOCK_PLACE);
            pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
            pev.add(RecordLocationType.BLOCK, new RubyLocation(block.getLocation()));
            pev.add(RecordLocationType.PLAYER, new RubyLocation(ev.getPlayer().getLocation()));
            pev.add(block.getType());
            RecordPersistenceManager.getInstance().persist(pev);
            BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBlockBreak(BlockBreakEvent ev) {
        if (!isPlayerLoggable(ev.getPlayer())) {
            return;
        }
        Block block = ev.getBlock();

        for (Snitch snitch : SnitchManager.getInstance().getValuesContaining(block.getLocation())) {
            SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_BLOCK_BREAK);
            pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
            pev.add(RecordLocationType.BLOCK, new RubyLocation(block.getLocation()));
            pev.add(RecordLocationType.PLAYER, new RubyLocation(ev.getPlayer().getLocation()));
            pev.add(block.getType());
            RecordPersistenceManager.getInstance().persist(pev);
            BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerReinforcement(PlayerCreateReinforcementEvent ev) {
        if (!isPlayerLoggable(ev.getPlayer())) {
            return;
        }

        if (SnitchManager.getInstance().hasValuesContaining(ev.getTarget())) {
            Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getTarget());

            for (Snitch snitch : snitches) {
                for (Block block : ev.getReinforcedBlocks()) {
                    SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_REINFORCEMENT_CREATE);
                    pev.add(RecordPlayerType.PLAYER, ev.getPlayer());
                    pev.add(RecordLocationType.BLOCK, new RubyLocation(block.getLocation()));
                    pev.add(RecordLocationType.PLAYER, new RubyLocation(ev.getPlayer().getLocation()));
                    pev.add(block.getType());
                    RecordPersistenceManager.getInstance().persist(pev);
                    BroadcastManager.getInstance().broadcast(pev, ev.getPlayer(), snitch);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent ev) {
        if (ev.getDamager() instanceof Player && !isPlayerLoggable((Player) ev.getDamager())) {
            return;
        }

        if (SnitchManager.getInstance().hasValuesContaining(ev.getEntity().getLocation())) {
            if (ev.getEntity() instanceof Player && ev.getDamager() instanceof Player) {
                playerDamagePlayer(ev);
            } else if (ev.getDamager() instanceof Player && !(ev.getEntity() instanceof Player) && ev.getEntity() instanceof LivingEntity) {
                playerDamageEntity(ev);
            }
        }
    }

    private void playerDamageEntity(EntityDamageByEntityEvent ev) {
        Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getEntity().getLocation());
        LivingEntity hurt = (LivingEntity) ev.getEntity();
        Player damager = (Player) ev.getDamager();

        for (Snitch snitch : snitches) {
            if (ev.getFinalDamage() > hurt.getHealth()) {
                SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_KILL_ENTITY);
                pev.add(RecordPlayerType.ATTACKER, damager);
                pev.add(RecordEntityType.VICTIM, hurt);
                pev.add(RecordLocationType.ATTACKER, new RubyLocation(damager.getLocation()));
                pev.add(RecordLocationType.VICTIM, new RubyLocation(hurt.getLocation()));

                RecordPersistenceManager.getInstance().persist(pev);
                BroadcastManager.getInstance().broadcast(pev, damager, snitch);
            } else {
                SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_DAMAGE_ENTITY);
                pev.add(RecordPlayerType.ATTACKER, damager);
                pev.add(RecordEntityType.VICTIM, hurt);
                pev.add(RecordLocationType.ATTACKER, new RubyLocation(damager.getLocation()));
                pev.add(RecordLocationType.VICTIM, new RubyLocation(hurt.getLocation()));
                RecordPersistenceManager.getInstance().persist(pev);
                BroadcastManager.getInstance().broadcast(pev, damager, snitch);
            }
        }
    }

    private void playerDamagePlayer(EntityDamageByEntityEvent ev) {
        Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getEntity().getLocation());
        Player hurt = (Player) ev.getEntity();
        Player damager = (Player) ev.getDamager();

        for (Snitch snitch : snitches) {
            if (ev.getFinalDamage() > hurt.getHealth()) {
                SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_KILL_PLAYER);
                pev.add(RecordPlayerType.ATTACKER, damager);
                pev.add(RecordPlayerType.VICTIM, hurt);
                pev.add(RecordLocationType.ATTACKER, new RubyLocation(damager.getLocation()));
                pev.add(RecordLocationType.VICTIM, new RubyLocation(hurt.getLocation()));
                RecordPersistenceManager.getInstance().persist(pev);
                BroadcastManager.getInstance().broadcast(pev, damager, snitch);
            } else {
                SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_DAMAGE_PLAYER);
                pev.add(RecordPlayerType.ATTACKER, damager);
                pev.add(RecordPlayerType.VICTIM, hurt);
                pev.add(RecordLocationType.ATTACKER, new RubyLocation(damager.getLocation()));
                pev.add(RecordLocationType.VICTIM, new RubyLocation(hurt.getLocation()));
                RecordPersistenceManager.getInstance().persist(pev);
                BroadcastManager.getInstance().broadcast(pev, damager, snitch);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent ev) {
        if (!isPlayerLoggable(ev.getEntity())) {
            return;
        }

        Collection<Snitch> snitches = SnitchManager.getInstance().getValuesContaining(ev.getEntity().getLocation());

        for (Snitch snitch : snitches) {
            SnitchRecord pev = new SnitchRecord(snitch, LogType.PLAYER_DEATH);
            pev.add(RecordPlayerType.PLAYER, ev.getEntity());
            pev.add(RecordLocationType.PLAYER, new RubyLocation(ev.getEntity().getLocation()));
            RecordPersistenceManager.getInstance().persist(pev);
            BroadcastManager.getInstance().broadcast(pev, ev.getEntity(), snitch);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && !ev.getPlayer().isSneaking()) {
            SnitchManager.getInstance().getSnitch(ev.getClickedBlock()).ifPresent(snitch -> {
                GroupManager.getInstance().getGroup(snitch.getRecord().getGroupId()).ifPresent(group -> {
                    if (group.hasMemberWithRankGE(ev.getPlayer().getUniqueId(), Rank.MODERATOR) || ev.getPlayer().isOp()) {
                        snitch.presentInventory(ev.getPlayer());
                        ev.setCancelled(true);
                    }
                });
            });
        }
    }

//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void onPlayerBlockPlace(PlayerBucketEmptyEvent ev) {
//        if (!isPlayerLoggable(ev.getPlayer())) {
//            return;
//        }
//
//        blockPlace(ev.getPlayer(), ev.getBucket(), ev.getBlockClicked().getRelative(ev.getBlockFace())));
//    }
//
//    private void blockPlace(Player player, Block block) {
//        for (Snitch snitch : SnitchManager.getInstance().getValuesContaining(block.getLocation())) {
//            PlayerPlaceBlockEvent ev = new PlayerPlaceBlockEvent(snitch, player, block);
//            RecordPersistenceManager.getInstance().persist(ev);
//            BroadcastManager.getInstance().broadcast(ev);
//        }
//    }

    private IntPoint3D mapToPoint(Location from) {
        return new IntPoint3D(from.getBlockX(), from.getBlockY(), from.getBlockZ());
    }

    private IntPoint3D mapToPoint(BlockKey source) {
        return new IntPoint3D(source.getX(), source.getY(), source.getZ());
    }

    private boolean isPlayerLoggable(Player player) {
        return (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                && !isVanished(player);
    }
}
