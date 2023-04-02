package uk.suuft.lastcraft.task;

import com.hakan.core.plugin.Plugin;
import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import uk.suuft.lastcraft.task.listener.CombinationListener;
import uk.suuft.lastcraft.task.thing.AbstractThing;
import uk.suuft.lastcraft.task.thing.implementation.*;

import java.util.HashMap;
import java.util.Map;

@Plugin(name = "TestTask", version = "1.0", authors = "suuft <suuft@naifu.works>", website = "https://suuft.naifu.works")
public class TaskPlugin extends JavaPlugin {

    private Map<String, AbstractThing> combinationThingMap = new HashMap<>();

    @Override
    public void onEnable() {
        register(1, 1, 1, new Fangs());
        register(2, 2, 1, new Dynamite());
        register(2, 3, 3, new WindPower());
        register(2, 1, 1, new Knockback());
        register(1, 2, 3, new Lightning());
        register(3, 3, 1, new RoughMagic());
        register(1, 1, 3, new Retaliation());
        register(2, 2, 3, new MagmaEscape());
        register(3, 3, 3, new Invisibility());

        getServer().getPluginManager().registerEvents(new CombinationListener(combinationThingMap, new HashMap<>()), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    /**
     * It's not a better solution, but it works.
     * Kambet don't look!
     */
    private void register(int one, int two, int three, @NonNull AbstractThing thing) {
        combinationThingMap.put(one + "" + two + "" + three, thing);
        combinationThingMap.put(one + "" + three + "" + two, thing);

        combinationThingMap.put(two + "" + three + "" + one, thing);
        combinationThingMap.put(two + "" + one + "" + three, thing);

        combinationThingMap.put(three + "" + one + "" + two, thing);
        combinationThingMap.put(three + "" + two + "" + one, thing);

        getServer().getPluginManager().registerEvents(thing, this);
    }
}
