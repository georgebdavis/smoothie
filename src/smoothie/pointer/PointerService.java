package smoothie.pointer;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Service;

/**
 * Once started, publishes a stream of pointer events to the event bus.
 */
public interface PointerService extends Service {
    /** @return EventBus on which service is publishing */
    public EventBus getEventBus();

    /** @return most recent pointer state */
    public Pointer getPointer();
}
