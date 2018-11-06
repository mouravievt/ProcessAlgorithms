import java.util.ArrayList;
import java.util.List;

public class RoundRobin implements Scheduler {
    protected int quantum = 4;
    protected int cursor = 0;
    protected List<ProcessInfo> queue = new ArrayList<>();
    protected ProcessInfo currentProcess;
    protected int startTime = 0;

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public void schedule(ProcessInfo info, SimulatedSystem system) {
        queue.add(info);

        if (currentProcess == null) {
            startProcess(queue.get(0), system);
        }
    }

    @Override
    public ProcessInfo tick(SimulatedSystem system) {
        if (currentProcess == null)
            return null;

        //Calculate the end time
        int trueEndTime = startTime + currentProcess.getBurstTime();
        int roundRobinEndTime = startTime + quantum;

        int endTime = Math.min(trueEndTime, roundRobinEndTime);

        //If the current clock equals the end time (or is greater)
        if (system.getCurrentTime() >= endTime) {

            //If the process has completed
            if (endTime == trueEndTime) {
                //Then we have completed the process
                endProcess(currentProcess);
                //Remove from queue
                queue.remove(currentProcess);
            } else {
                //Otherwise suspend it
                //To suspend a process, we must set the burst time to how much time is actually remaining
                currentProcess.setBurstTime(trueEndTime - system.getCurrentTime());
                cursor++;
            }

            if (cursor >= queue.size())
                cursor = 0;

            //If there are more processes
            if (queue.size() > 0) {
                //Start the next process
                startProcess(queue.get(cursor), system);
            } else {
                currentProcess = null;
            }
        }

        //Return the current running process
        return currentProcess;
    }

    static void findWaitTime(int pids[ ], int numberOfPids, int burstTime[ ], int waitTime[ ], int quantum){

        int remainingBurstTime[ ] = new int[numberOfPids];

        for(int ii = 0; ii < numberOfPids; ii++) {

            remainingBurstTime[ii] = burstTime[ii];

        }

        int currentTime = 0;

        while(true) {

            boolean done = true;

            for(int ii = 0; ii < numberOfPids; ii++) {

                if(remainingBurstTime[ii] > 0) { //if the burst time of the process is > than 0, the process is available

                    done = false;

                    if(remainingBurstTime[ii] > 100) { //if remaining burst time is > than 100 ms

                        currentTime += 100;

                        remainingBurstTime[ii] -= 100; //decrease remaining burst time of current process by quantum(100 ms)
                    } else {

                        currentTime = currentTime + remainingBurstTime[ii];

                        waitTime[ii] = currentTime - burstTime[ii];

                        remainingBurstTime[ii] = 0;
                    }
                }
            }

            if(done == true)
                break;
        }

    }

    static void findTurnAroundTime(int pids[ ], int numberOfPids, int burstTime[ ], int waitTime[ ], int turnAroundTime[ ]) {

        for (int jj = 0; jj < numberOfPids; jj++) {

            turnAroundTime[jj] = burstTime[jj] + waitTime[jj];
        }
    }

    static void findAverageTime(int pids[ ], int numberOfPids, int burstTime[ ], int quantum) {

        int waitTime[] = new int[numberOfPids], turnAroundTime[] = new int[numberOfPids];

        int totalWaitTime = 0, totalTurnAroundTime = 0;

        findWaitTime(pids, numberOfPids, burstTime, waitTime, quantum);

        findTurnAroundTime(pids, numberOfPids, burstTime, waitTime, turnAroundTime);

        System.out.println("processes" + "Burst time" + "waiting time" + "Turn around time");

        for (int kk = 0; kk < numberOfPids; kk++) {

            totalWaitTime = totalWaitTime + waitTime[kk];

            totalTurnAroundTime = totalTurnAroundTime + turnAroundTime[kk];

            System.out.println(" " + (kk + 1) + "\t\t" + burstTime[kk] + "\t" + waitTime[kk] + "\t\t" + turnAroundTime[kk]);
        }

        System.out.println("Average waiting time=" + (float) totalWaitTime / (float) numberOfPids);
        System.out.println("Average turn around time=" + (float) totalTurnAroundTime / (float) numberOfPids);
    }

    @Override
    public int compare(ProcessInfo o1, ProcessInfo o2) {
        throw new IllegalAccessError("No sorting is done in round robin");
    }

    protected void endProcess(ProcessInfo process) {
        process.complete();
    }

    protected void startProcess(ProcessInfo process, SimulatedSystem system) {
        currentProcess = process;
        startTime = system.getCurrentTime();
    }
}