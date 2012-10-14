package smoothie.pointer;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;

import smoothie.ProcessingEvent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractPointerService extends AbstractIdleService implements PointerService {
    /** Event bus on which to publish. */
    protected final EventBus eventBus;

    /** Most recent pointer. */
    protected AtomicReference<Pointer> aPointer =
            new AtomicReference<Pointer> (new Pointer(Pointer.State.ABSENT, 0, 0));

    public AbstractPointerService(EventBus eventBus) {
        super();
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public synchronized Pointer getPointer() {
        Preconditions.checkArgument(this.isRunning(), "Pointer service not yet started.");
        return aPointer.get();
    }

    /**
     * Concrete class uses this to set and publish a pointer.
     *
     * @param pointer to set / publish
     */
    protected synchronized void setPointer(Pointer pointer) {
        Preconditions.checkArgument(this.isRunning(), "Pointer service not yet started.");
        Preconditions.checkNotNull(pointer, "Pointer must be provided.");
        aPointer.set(pointer);
        eventBus.post(pointer);
    }

    /**
     * Automatically start if bound to Smoothlet that issues Setup event.
     *
     * TODO (gbd): Is this the right thing to do?  Other PointerServices might have other
     * dependencies (e.g. Kinect), requiring different timing and breaking the usage pattern.
     *
     * @param setupEvent issued by Smoothlet
     */
    @Subscribe
    public void onSetup(ProcessingEvent.Setup setupEvent) {
        this.start();
    }
}