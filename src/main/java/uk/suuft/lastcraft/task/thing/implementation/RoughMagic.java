package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.suuft.lastcraft.task.TaskPlugin;
import uk.suuft.lastcraft.task.thing.AbstractThing;

import java.util.ArrayList;
import java.util.List;

public class RoughMagic implements AbstractThing {

    private List<String> players = new ArrayList<>();

    @Override
    public int getDelay() {
        return 25;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.IRON_HELMET)
                .name("Грубая магия (палка x1 + поршень x2)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        players.add(player.getName().toLowerCase());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) return;
        if (!players.contains(event.getDamager().getName().toLowerCase())) return;
        players.remove(event.getDamager().getName().toLowerCase());

        Player player = (Player) event.getEntity();

        ItemStack helmet = player.getInventory().getHelmet();

        if (helmet != null) {
            player.getInventory().setHelmet(null);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 4));
            player.damage((helmet.getType().getMaxDurability() - helmet.getDurability()) * 2.0, event.getDamager());

            Bukkit.getScheduler().runTaskLater(TaskPlugin.getProvidingPlugin(TaskPlugin.class), () -> {
                if (player.isOnline()) {
                    player.getInventory().setHelmet(helmet);
                }
            }, 7 * 20L);
        }
    }
}
