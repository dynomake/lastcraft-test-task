package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import uk.suuft.lastcraft.task.thing.AbstractThing;

import java.util.HashMap;
import java.util.Map;

public class Retaliation implements AbstractThing {

    private Map<String, Integer> playerHitsRemain = new HashMap<>();

    @Override
    public int getDelay() {
        return 25;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.IRON_SWORD)
                .name("Ответный удар (палка x2 + поршень x1)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        playerHitsRemain.put(player.getName().toLowerCase(), 4);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) return;
        if (!playerHitsRemain.containsKey(event.getDamager().getName().toLowerCase())) return;

        event.setDamage(event.getDamage() + 3);

        // ужас какой, приведение типов, но если на сервере куча игроков, то метод бакита будет медленнее
        Player damager = (Player) event.getDamager();

        if (damager.getHealth() < damager.getMaxHealth())
            damager.setHealth(damager.getHealth() + damager.getMaxHealth() - damager.getHealth() < 3
                    ? damager.getMaxHealth() - damager.getHealth() : 3);

        int hitsRemain = playerHitsRemain.get(event.getDamager().getName().toLowerCase()) - 1;

        if (hitsRemain <= 0)  playerHitsRemain.remove(event.getDamager().getName().toLowerCase());
    }
}
