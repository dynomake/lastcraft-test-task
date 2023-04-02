package uk.suuft.lastcraft.task.thing.implementation;

import com.hakan.core.item.ItemBuilder;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.suuft.lastcraft.task.thing.AbstractThing;

public class Fangs implements AbstractThing {
    @Override
    public int getDelay() {
        return 15;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.IRON_BARDING)
                .name("Удар щупальцами (палка x3)")
                .build();
    }

    @Override
    public void use(@NonNull Player player) {
        EvokerFangs tentacle = player.getWorld().spawn(player.getLocation(), EvokerFangs.class);
        tentacle.setOwner(player);

        for (Entity nearbyEntity : tentacle.getNearbyEntities(3, 3, 3)) {
            if (nearbyEntity instanceof Player) {
                Player nearbyPlayer = (Player) nearbyEntity;
                if (!player.getName().equals(nearbyPlayer.getName())) {
                    nearbyPlayer.damage(12, player);
                }
            }
        }
    }
}
