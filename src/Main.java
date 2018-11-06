import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Random RANDOM = new Random();
    private static final int PROCESS_COUNT = 100;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the amount processes you would like to schedule: ");
        int pidAmount = input.nextInt();
        ProcessInfo[] pidArray = new ProcessInfo[pidAmount];

        for (int ii = 0; ii < pidAmount; pidAmount++) {

            System.out.println("Please enter the arrival time for PID #" + ii + ": ");
            pidArray[ii].setArrivalTime(input.nextInt());
            System.out.println("Please enter the burst time for PID #" + ii + ": ");
            pidArray[ii].setBurstTime(input.nextInt());
        }

        List<ProcessInfo> processes = randomProcesses();

        SimulatedSystem system = new SimulatedSystem(processes);

        system.setScheduler(new SRT());

        while (!system.isIdle()) {
            system.tick();
        }
    }



    public static List<ProcessInfo> randomProcesses() {
        ArrayList<ProcessInfo> processes = new ArrayList<>();
        for (int i = 0; i < PROCESS_COUNT; i++) {
            processes.add(
                    new ProcessInfo(RANDOM.nextInt(10) + 1, i, i)
            );
        }

        return processes;
    }
}
