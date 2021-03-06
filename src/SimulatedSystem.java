import java.util.ArrayList;
import java.util.List;

public class SimulatedSystem {
    private List<ProcessInfo> processQueue = new ArrayList<>();
    private Scheduler scheduler;

    private int currentTime = 0;
    private boolean idle = false;
    private ProcessRunningInfo currentProcess;
    private List<ProcessRunningInfo> allProcesses = new ArrayList<>();

    private int waitTimeSum = 0;
    private int turnaroundTimeSum = 0;
    private int totalScheduled = 0;

    private double waitTime;
    private double turnaroundTime;

    public SimulatedSystem(List<ProcessInfo> processQueue) {
        this.processQueue = processQueue;
    }

    public SimulatedSystem(List<ProcessInfo> processQueue, Scheduler scheduler) {
        this.processQueue = processQueue;

        this.scheduler = scheduler;
    }

    public void tick() {
        currentTime++;

        if (processQueue.size() == 0 && currentProcess == null) {
            idle = true;
            return;
        } else {
            idle = false;
        }

        for (int i = 0; i < processQueue.size(); i++) {
            ProcessInfo p = processQueue.get(i);

            if (currentTime >= p.getArrivalTime()) {
                scheduler.schedule(p, this);
                processQueue.remove(i);
                totalScheduled++;
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

        currentProcess.setEndTime(currentTime);

        //Now save
        allProcesses.add(currentProcess);

        //Add waiting sum
        waitTimeSum += (currentTime - newProcess.getArrivalTime());

        //Add turnaround sum
        turnaroundTimeSum += (currentTime - oldProcess.getArrivalTime());
    }

    public void onQueueEmpty() {
        System.out.println("All processes completed!");

        currentProcess.setEndTime(currentTime);

        //Now save
        allProcesses.add(currentProcess);

        waitTime = (double)waitTimeSum / (double)totalScheduled;
        turnaroundTime = (double)turnaroundTimeSum / (double)totalScheduled;

    }

    public double getWaitTime() {
        return waitTime;
    }

    public double getTurnaroundTime() {
        return turnaroundTime;
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
            this.startTime = currentTime;
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
