package me.pta;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;

public class ProtectionListener implements Listener {
    private final String world;

    public ProtectionListener(FileConfiguration config) {
        world = config.getString("world");
    }

    //检测生物破坏
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getBlock().getWorld().getName().equals(world)) {
            event.setCancelled(true);
        }
    }


    //液体流动和龙蛋移动
    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (event.getBlock().getWorld().getName().equals(world)) {
            event.setCancelled(true);
        }
    }

    //玩家放置方块
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().equals(world) &&!event.getPlayer().hasPermission("protectall.op")) {
            event.setCancelled(true);
        }
    }

    //玩家破坏方块
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getName().equals(world) &&!event.getPlayer().hasPermission("protectall.op")) {
            event.setCancelled(true);
        }
    }

    //树叶消失
    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        if (event.getBlock().getWorld().getName().equals(world)) {
            event.setCancelled(true);
        }
    }


    //方块转变事件
    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if(event.getBlock().getWorld().getName().equals(world)) {
            event.setCancelled(true);
        }
    }


    //生物破坏农田
    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if(event.getBlock().getWorld().getName().equals(world)) {
            if(event.getBlock().getType() == Material.FARMLAND) {
                event.setCancelled(true);
            }
        }
    }

    //玩家破坏农田、使用骨粉
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.hasBlock()){
            if (event.getPlayer().getWorld().getName().equals(world)) {
                if(!event.getPlayer().hasPermission("protectall.op")){
                    if(event.getAction() == Action.PHYSICAL) {
                        if(event.getClickedBlock().getType() == Material.FARMLAND) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
        if(event.hasItem()){
            if (event.getPlayer().getWorld().getName().equals(world)) {
                if(!event.getPlayer().hasPermission("protectall.op")){
                    if(event.getItem().getType() == Material.BONE_MEAL){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    //对盔甲架、画、物品展示框的互动
    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getWorld().getName().equals(world) &&!event.getPlayer().hasPermission("protectall.op")) {
            EntityType type = event.getRightClicked().getType();
            if (type == EntityType.ITEM_FRAME || type == EntityType.ARMOR_STAND || type == EntityType.GLOW_ITEM_FRAME || type == EntityType.PAINTING) {
                event.setCancelled(true);
            }
        }
    }

    //桶的使用
    @EventHandler
    public void onBucketUnfill(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().getWorld().getName().equals(world) &&!event.getPlayer().hasPermission("protectall.op")) {
            event.setCancelled(true);
        }
    }

    //桶的填充
    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (event.getPlayer().getWorld().getName().equals(world) &&!event.getPlayer().hasPermission("protectall.op")) {
            event.setCancelled(true);
        }
    }

    //盔甲架、地图、物品展示框、画破坏
    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if(event.getEntity().getWorld().getName().equals(world)) {
            Entity p = event.getRemover();
            if(p instanceof Player) {
                if(!p.hasPermission("protectall.op")){
                    event.setCancelled(true);
                }
            } else{
                event.setCancelled(true);
            }
        }
    }

    //物品展示框、画、绳放置
    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if(event.getPlayer()!=null){
            if(event.getPlayer().getWorld().getName().equals(world) &&!event.getPlayer().hasPermission("protectall.op")){
                event.setCancelled(true);
            }
        }
    }

    //对地图、物品展示框、盔甲架、画的伤害
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        if(e.getWorld().getName().equals(world)) {
            Entity p = event.getDamager();
            if(e instanceof Hanging || e instanceof ArmorStand) {
                if(p instanceof Player) {
                    if(!p.hasPermission("protectall.op")){
                        event.setCancelled(true);
                    }
                } else{
                    event.setCancelled(true);
                }
            }
        }

    }

    //禁止放置盔甲架
    @EventHandler
    public void onEntityPlace(EntityPlaceEvent event) {
        if(event.getPlayer() != null) {
            if(event.getPlayer().getWorld().getName().equals(world) && !event.getPlayer().hasPermission("protectall.op")) {
                if(event.getEntity() instanceof ArmorStand) {
                    event.setCancelled(true);
                }
            }
        }
    }

    //禁止除玩家外的生物坐上矿车和船
    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        if(event.getEntity().getWorld().getName().equals(world)) {
            if (!(event.getEntity() instanceof Player) && (event.getMount().getType() == EntityType.BOAT || event.getMount().getType() == EntityType.MINECART)) {
                event.setCancelled(true);
            }
        }
    }

    //切换世界和登录时强制设置游戏模式
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.updateGamemode(event.getPlayer());
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        this.updateGamemode(event.getPlayer());
    }

    private void updateGamemode(Player player) {
        if(!player.hasPermission("protectall.op")) {
            if(player.getWorld().getName().equals(world)) {
                player.setGameMode(GameMode.ADVENTURE);
            }else {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

}