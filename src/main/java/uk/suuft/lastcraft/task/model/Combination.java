package uk.suuft.lastcraft.task.model;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Combination {
    byte one = 0;
    byte two = 0;
    byte three = 0;

    @Override
    public String toString() {
        return String.format("%03d%03d%03d", one, two, three);
    }

    public void clear() {
        one = 0;
        two = 0;
        three = 0;
    }

    public boolean isDone() {
        return three != 0;
    }

    public void add(byte value) {
        if (one == 0) one = value;
        else if (two == 0) two = value;
        else if (three == 0) three = value;
    }
}
