import java.util.ArrayList;
import java.util.List;

public class SimulatedSystem {
    private List<ProcessInfo> processQueue = new ArrayList<>();
    private Scheduler scheduler;

    private int currentTime = 0;
    private boolean idle = false;
    private ProcessInfo currentProcess;

    public SimulatedSystem(List<ProcessInfo> processQueue) {
        this.processQueue = processQueue;
    }

    public SimulatedSystem(List<ProcessInfo> processQueue, Scheduler scheduler) {
        this.processQueue = processQueue;

        this.scheduler = scheduler;
    }

    public void tick() {
        if (processQueue.size() == 0 && currentProcess == null) {
            idle = true;
            currentTime++;
            return;
        } else {
            idle = false;
        }

        for (int i = 0; i < processQueue.size(); i++) {
            ProcessInfo p = processQueue.get(i);

            if (currentTime >= p.getArrivalTime()) {
                scheduler.schedule(p, this);
                processQueue.remove(i);
            }
        }

        ProcessInfo running = scheduler.tick(this);

        if (currentProcess == null) {
            currentProcess = running;
        } else if (currentProcess != running && running != null) {
            //TODO Context switching!
            onContextSwitch(currentProcess, running);

            currentProcess = running;
        } else if (running == null) {
            currentProcess = null;

            onQueueEmpty();
        }

        currentTime++;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void onContextSwitch(ProcessInfo oldProcess, ProcessInfo newProcess) {
        System.out.println("Switched from " + oldProcess.getPid() + " to " + newProcess.getPid() + " at time " + currentTime);
        System.out.println("Old Process: " + oldProcess);
        System.out.println("New Process: " + newProcess);
        System.out.println();
    }

    public void onQueueEmpty() {
        System.out.println("All processes completed!");

        //TODO Calculate averages
    }

    public boolean isIdle() {
        return idle;
    }

    public void queueProcess(ProcessInfo process) {
        processQueue.add(process);
    }
}
