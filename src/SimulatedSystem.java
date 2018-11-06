import java.util.ArrayList;
import java.util.List;

public class SimulatedSystem {
    private List<ProcessInfo> processQueue = new ArrayList<>();
    private Scheduler scheduler;

    private int currentTime = 0;
    private boolean idle = false;
    private ProcessRunningInfo currentProcess;
    private long systemStartTime = System.currentTimeMillis();
    private List<ProcessRunningInfo> allProcesses = new ArrayList<>();

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
            currentProcess = new ProcessRunningInfo(running);
        } else if (currentProcess.getProcess() != running && running != null) {
            //TODO Context switching!
            onContextSwitch(currentProcess.getProcess(), running);

            currentProcess = new ProcessRunningInfo(running);
        } else if (running == null) {
            onQueueEmpty();
            currentProcess = null;
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

        currentProcess.setEndTime(systemStartTime + currentTime);

        //Now save
        allProcesses.add(currentProcess);
    }

    public void onQueueEmpty() {
        System.out.println("All processes completed!");

        currentProcess.setEndTime(systemStartTime + currentTime);

        //Now save
        allProcesses.add(currentProcess);
    }

    public boolean isIdle() {
        return idle;
    }

    public void queueProcess(ProcessInfo process) {
        processQueue.add(process);
    }

    public List<ProcessRunningInfo> getProcessRunningTimes() {
        return allProcesses;
    }

    public class ProcessRunningInfo {
        private long startTime;
        private long endTime;
        private ProcessInfo info;

        public ProcessRunningInfo(ProcessInfo info) {
            this.info = info;
            this.startTime = systemStartTime + currentTime;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public ProcessInfo getProcess() {
            return info;
        }
    }
}
