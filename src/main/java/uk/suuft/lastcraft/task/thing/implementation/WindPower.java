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

import java.util.HashMap;
import java.util.Map;

public class WindPower implements AbstractThing {
    private Map<String, Integer> playerHitsRemain = new HashMap<>();


    @Override
    public int getDelay() {
        return 35;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.QUARTZ)
                .name("Сила ветра (кость + поршень х2)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        playerHitsRemain.put(player.getName().toLowerCase(), 2);

        Bukkit.getScheduler().runTaskLater(TaskPlugin.getProvidingPlugin(TaskPlugin.class), () ->
                playerHitsRemain.remove(player.getName().toLowerCase()), 20 * 30);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) return;
        if (!playerHitsRemain.containsKey(event.getEntity().getName().toLowerCase())) return;

        Player player = (Player) event.getDamager();
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));

        int remain = playerHitsRemain.get(event.getEntity().getName().toLowerCase()) - 1;

        if (remain <= 0) {
            playerHitsRemain.remove(event.getEntity().getName().toLowerCase());
            return;
        }

        playerHitsRemain.put(player.getName().toLowerCase(), remain);
    }
}
