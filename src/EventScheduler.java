import java.util.*;

/**
 * Keeps track of events that have been scheduled.
 */
public final class EventScheduler
{
    private final PriorityQueue<Event> eventQueue;
    private final Map<Entity, List<Event>> pendingEvents;
    private final double timeScale;

    public EventScheduler(double timeScale) {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.timeScale = timeScale;
    }

    public void scheduleEvent(

            Entity entity,
            Action action,
            long afterPeriod)
    {
        long time = System.currentTimeMillis() + (long)(afterPeriod
                * this.timeScale);
        Event event = new Event(action, time, entity);

        this.eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = this.pendingEvents.getOrDefault(entity,
                new LinkedList<>());
        pending.add(event);
        this.pendingEvents.put(entity, pending);
    }

    public static void unscheduleAllEvents(
            EventScheduler scheduler, Entity entity)
    {
        List<Event> pending = scheduler.pendingEvents.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                scheduler.eventQueue.remove(event);
            }
        }
    }

    private static void removePendingEvent(
            EventScheduler scheduler, Event event)
    {
        List<Event> pending = scheduler.pendingEvents.get(event.entity);

        if (pending != null) {
            pending.remove(event);
        }
    }

    public void updateOnTime(long time) {
        while (!this.eventQueue.isEmpty()
                && this.eventQueue.peek().time < time) {
            Event next = this.eventQueue.poll();

            removePendingEvent(this, next);

            next.action.executeAction(this);
        }
    }
}
