package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import uk.suuft.lastcraft.task.TaskPlugin;
import uk.suuft.lastcraft.task.model.ChangedBlock;
import uk.suuft.lastcraft.task.thing.AbstractThing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagmaEscape implements AbstractThing {

    private Map<String, BukkitTask> playerMagmaTaskMap = new HashMap<>();
    private Map<String, List<ChangedBlock>> playerMagmaBlocksMap = new HashMap<>();

    @Override
    public int getDelay() {
        return 40;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.MAGMA)
                .name("Магмовое бегство (кость x2 + поршень x1)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        playerMagmaTaskMap.put(player.getName().toLowerCase(), Bukkit.getScheduler().runTaskTimer(TaskPlugin.getProvidingPlugin(TaskPlugin.class), new BukkitRunnable() {
            private int secondsLeft = 11;

            @Override
            public void run() {
                secondsLeft--;

                if (secondsLeft <= 0) {
                    cancel();
                    playerMagmaTaskMap.remove(player.getName().toLowerCase());

                    List<ChangedBlock> changedBlocks = playerMagmaBlocksMap.get(player.getName().toLowerCase());

                    if (changedBlocks == null) return;

                    for (ChangedBlock block: changedBlocks) block.restore();

                    playerMagmaBlocksMap.remove(player.getName().toLowerCase());
                    return;
                }

                if (player.getLocation().clone().subtract(0, 1, 0).getBlock().getType() == Material.MAGMA && player.getMaxHealth() > player.getHealth())
                    player.setHealth(player.getHealth() + player.getMaxHealth() - player.getHealth() < 3
                            ? player.getMaxHealth() - player.getHealth() : 3);

            }
        }, 0, 20));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        val task = playerMagmaTaskMap.remove(event.getPlayer().getName());
        if (task != null) task.cancel();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (playerMagmaTaskMap.containsKey(event.getPlayer().getName().toLowerCase())) {
            List<ChangedBlock> blocks = playerMagmaBlocksMap.getOrDefault(event.getPlayer().getName().toLowerCase(), new ArrayList<>());

            Block block = event.getTo().getBlock();

            if (block.getType() != Material.MAGMA && block.getType() != Material.AIR) {
                blocks.add(new ChangedBlock(block.getLocation(), block.getType()));
                block.setType(Material.MAGMA);
            }

            playerMagmaBlocksMap.put(event.getPlayer().getName().toLowerCase(), blocks);
        }
    }
}
