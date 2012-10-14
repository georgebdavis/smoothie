package smoothie.pointer;

import com.google.common.base.Objects;

/**
 * Represents a cursor state, generalizing mouse, touchscreen and motion capture drivers.
 *
 * @author georgebdavis@github
 */
public class Pointer {

    /** Pointer mode. */
    public static enum  State {
        /** Signals pointer not detected. */                ABSENT,
        /** Signals pointer detected but not "clicked". */  UP,
        /** Signals pointer detected and "clicked". */      DOWN;
    }

    /** horizontal coordinate */
    public final int x;

    /** vertical coordinate */
    public final int y;

    /** pointer state */
    public final State state;

    /**
     * @param x - horizontal coordinate
     * @param y - vertical coordinate
     * @param pressed - activation state
     */
    public Pointer(State state, int x, int y) {
        this.state = state;
        this.x = x;
        this.y = y;
    }

    @Override public String toString () {
        return Objects.toStringHelper(this.getClass())
                      .add("state", state)
                      .add("x", x)
                      .add("y", y)
                      .toString();
    }
}
