package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import uk.suuft.lastcraft.task.thing.AbstractThing;

public class Knockback implements AbstractThing {
    @Override
    public int getDelay() {
        return 25;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.PISTON_BASE)
                .name("Откидывание (палка x2 + кость x1)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        for (Entity entity : player.getNearbyEntities(3, 3, 3)) {
            if (!(entity instanceof Player)) continue;

            Player target = (Player) entity;

            if (player.getName().equals(target.getName())) continue;

            Vector knockback = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5);
            target.setVelocity(knockback);

            target.damage(9);
        }
    }
}
