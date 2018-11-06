import java.util.Collections;
import java.util.HashMap;

public class SRT extends SJF {
    private HashMap<Long, Integer> remainingTime = new HashMap<>();

    //We must keep track of some more info when we schedule a process
    @Override
    public void schedule(ProcessInfo process, SimulatedSystem system) {
        //Store the process's burst time as the current remaining time
        remainingTime.put(process.getPid(), process.getBurstTime());

        //Then schedule it normally
        super.schedule(process, system);
    }

    //We must calculate remaining time every tick
    @Override
    public ProcessInfo tick(SimulatedSystem system) {
        if (currentProcess == null)
            return null;

        //Every tick, calculate the remaining time of the current process and save it
        int remaining = remainingTime.get(currentProcess.getPid());
        remaining -= 1;
        remainingTime.put(currentProcess.getPid(), remaining);

        //A weird fix for FCFS logic and sorting logic
        //We must save the burst time so SJF tie breaker sorts properly
        //And we must shift the start time up to ensure FCFS continues to calculate the correct end time
        currentProcess.setBurstTime(remaining);
        startTime++;

        //Now sort the current queue, since the remaining times have changed
        Collections.sort(queue, this);

        //If more than one process exists in the queue, check the top process
        if (queue.size() > 0) {
            //If the top process is different than the current one, context switch
            if (queue.get(0) != currentProcess) {

                //Suspend the current process
                suspendProcess(currentProcess);

                //And start the new top process
                startProcess(queue.get(0), system);
            }
        }

        //Now execute normally
        return super.tick(system);
    }

    //We must change how we are sorting in SRT (but fallback to SJF in a tie)
    @Override
    public int compare(ProcessInfo o1, ProcessInfo o2) {
        int val = remainingTime.get(o1.getPid()) - remainingTime.get(o2.getPid()) ;
        if (val == 0)
            val = super.compare(o1, o2);
        return val;
    }

    protected void suspendProcess(ProcessInfo process) {
        //To suspend a process, we must set the burst time to how much time is actually remaining
        process.setBurstTime(remainingTime.get(process.getPid()));
    }
}
