package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.suuft.lastcraft.task.thing.AbstractThing;

public class Lightning implements AbstractThing {
    @Override
    public int getDelay() {
        return 30;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.QUARTZ)
                .name("Гнев небес (палка+кость+поршень)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        Location location = player.getLocation();

        for (Entity entity : location.getWorld().getNearbyEntities(location, 5, 5, 5)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                if (target != player) {
                    target.damage(5);
                    location.getWorld().strikeLightning(target.getLocation());

                    if (player.getHealth() < 20) player.setHealth(player.getHealth() + 5);

                    target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                }
            }
        }
    }
}
