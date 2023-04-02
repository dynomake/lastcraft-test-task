package uk.suuft.lastcraft.task.listener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.suuft.lastcraft.task.model.Combination;
import uk.suuft.lastcraft.task.thing.AbstractThing;

import java.util.Map;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CombinationListener implements Listener {

    Map<String, AbstractThing> combinationThingMap;
    Map<String, Combination> combinationMap;

    // эта тварь никогда не вернет null.
    private Combination getCombination(@NonNull Player player) {
        Combination combination = combinationMap.getOrDefault(player.getName().toLowerCase(), new Combination());
        combinationMap.put(player.getName().toLowerCase(), combination);
        return combination;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        combinationMap.remove(event.getPlayer().getName().toLowerCase());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        combinationMap.remove(event.getEntity().getName().toLowerCase());
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!event.hasItem()) return;

        Material material = event.getItem().getType();
        CombinationItem item = CombinationItem.fromMaterial(material);

        if (item == null) return;

        Combination combination = getCombination(event.getPlayer());

        combination.add((byte) item.id);
        // да, я знаю чем плохо приведение типов, но зато за меня IntegerPool и всего 1 байт

        if (combination.isDone()) {
            combinationThingMap.get(combination.toString()).apply(event.getPlayer());
            combination.clear();
        }
    }

    @FieldDefaults(makeFinal = true)
    @AllArgsConstructor
    private enum CombinationItem {
        STICK(1, Material.STICK),
        BONE(2, Material.BONE),
        PISTON(3, Material.PISTON_BASE);

        int id;
        Material material;

        public static CombinationItem fromMaterial(@NonNull Material material) {
            CombinationItem item = null;

            for (CombinationItem ci: values()) {
                if (ci.material == material) item = ci;
            }

            return item;
        }
    }

}
