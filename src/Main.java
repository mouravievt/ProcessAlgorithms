import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final Random RANDOM = new Random();
    private static final int PROCESS_COUNT = 100;

    public static void main(String[] args) {
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
