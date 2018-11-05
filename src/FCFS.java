import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FCFS implements Scheduler {
    protected List<ProcessInfo> queue = new LinkedList<>();

    protected ProcessInfo currentProcess;
    protected int startTime = 0;

    @Override
    public void schedule(ProcessInfo process, SimulatedSystem system) {
        queue.add(process);

        Collections.sort(queue, this);

        if (currentProcess == null) {
            startProcess(queue.get(0), system);
        }
    }

    @Override
    public ProcessInfo tick(SimulatedSystem system) {
        if (currentProcess == null)
            return null;

        //Calculate the end time
        int endTime = startTime + currentProcess.getBurstTime();

        //If the current clock equals the end time (or is greater)
        if (system.getCurrentTime() >= endTime) {
            //Then we have completed the process
            endProcess(currentProcess);
            //Remove from queue
            queue.remove(0);

            //If there are more processes
            if (queue.size() > 0) {
                //Start the next process
                startProcess(queue.get(0), system);
            } else {
                currentProcess = null;
            }
        }

        //Return the current running process
        return currentProcess;
    }

    @Override
    public int compare(ProcessInfo o1, ProcessInfo o2) {
        return o1.getArrivalTime() - o2.getArrivalTime();
    }

    protected void endProcess(ProcessInfo process) {
        process.complete();
    }

    protected void startProcess(ProcessInfo process, SimulatedSystem system) {
        currentProcess = process;
        startTime = system.getCurrentTime();
    }
}
