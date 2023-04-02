package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.suuft.lastcraft.task.thing.AbstractThing;

public class Dynamite implements AbstractThing {
    @Override
    public int getDelay() {
        return 50;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.TNT)
                .name("Алхимическое чудо (кость x2 + палка x1)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        Location center = player.getLocation();
        TNTPrimed tnt = center.getWorld().spawn(center, TNTPrimed.class);

        tnt.setFuseTicks(20);

        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 200, (int) (center.getWorld().getNearbyEntities(center, 7, 7, 7)
                .stream()
                .filter(entity -> entity instanceof Player)
                .count())));
    }
}
