public class ProcessInfo {
    private int priority;
    private int burstTime;
    private long pid;
    private int arrivalTime;
    private boolean completed;

    public ProcessInfo(int priority, int burstTime, long pid, int arrivalTime) {
        this.priority = priority;
        this.burstTime = burstTime;
        this.pid = pid;
        this.arrivalTime = arrivalTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public long getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void complete() {
        completed = true;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "priority=" + priority +
                ", burstTime=" + burstTime +
                ", pid=" + pid +
                ", arrivalTime=" + arrivalTime +
                ", completed=" + completed +
                '}';
    }
}
