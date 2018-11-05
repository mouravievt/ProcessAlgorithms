import java.util.Comparator;

public interface Scheduler extends Comparator<ProcessInfo> {
    /***
     * Schedule a new process. This will get called when the {@link SimulatedSystem#getCurrentTime()} equals
     * {@link ProcessInfo#getArrivalTime()} of the parameter info
     * @param info The new process to schedule
     */
    void schedule(ProcessInfo info, SimulatedSystem system);

    /**
     * This is executed every tick, and should return the current scheduled process. This process may change
     * between ticks
     * @return The current scheduled process
     */
    ProcessInfo tick(SimulatedSystem system);
}
