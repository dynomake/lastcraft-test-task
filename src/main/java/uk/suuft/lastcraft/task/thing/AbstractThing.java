package uk.suuft.lastcraft.task.thing;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public interface AbstractThing extends Listener {

    int getDelay(); // in seconds
    ItemStack getItem();
    void use(@NonNull Player player);

    default void apply(@NonNull Player player) {
        use(player);
        player.setCooldown(getItem().getType(), getDelay() * 20);
    }

}
