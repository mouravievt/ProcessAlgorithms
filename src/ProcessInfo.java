public class ProcessInfo {
    private int burstTime;
    private long pid;
    private int arrivalTime;
    private boolean completed;

    public ProcessInfo(int burstTime, long pid, int arrivalTime) {
        this.burstTime = burstTime;
        this.pid = pid;
        this.arrivalTime = arrivalTime;
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

    public void setArrivalTime(int arrivalTime){ this.arrivalTime = arrivalTime; }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void complete() {
        completed = true;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "burstTime=" + burstTime +
                ", pid=" + pid +
                ", arrivalTime=" + arrivalTime +
                ", completed=" + completed +
                '}';
    }
}
