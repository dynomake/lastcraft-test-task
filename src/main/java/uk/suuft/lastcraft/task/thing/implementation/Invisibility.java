package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import uk.suuft.lastcraft.task.TaskPlugin;
import uk.suuft.lastcraft.task.thing.AbstractThing;

public class Invisibility implements AbstractThing {
    @Override
    public int getDelay() {
        return 40;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.SUGAR)
                .name("Невидимость (поршень x3)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        player.getInventory().setArmorContents(null);
        player.updateInventory();

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 30, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 30, 0));
        player.setLastDamageCause(null);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;
                if (ticks >= 20 * 30 || player.getLastDamageCause() != null) {
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    player.removePotionEffect(PotionEffectType.SPEED);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
                    player.getInventory().setArmorContents(armor);
                    player.updateInventory();
                    this.cancel();
                }
            }
        }.runTaskTimer(TaskPlugin.getProvidingPlugin(TaskPlugin.class), 0L, 1L);
    }
}
