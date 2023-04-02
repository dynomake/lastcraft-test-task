package uk.suuft.lastcraft.task.model;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;

@AllArgsConstructor
public class ChangedBlock {
    private Location location;
    private Material material;

    public void restore() {
        location.getBlock().setType(material);
    }
}
