public class SJF extends FCFS {

    //SJF only changes what is sorted, so everything else is the same
    @Override
    public int compare(ProcessInfo o1, ProcessInfo o2) {
        int val = o1.getBurstTime() - o2.getBurstTime();
        if (val == 0)
            val = super.compare(o1, o2);
        return val;
    }
}
