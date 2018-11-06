import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import javax.swing.*;
import java.util.*;

public class GanttChart extends JFrame {

    private static boolean USE_PREMADE = true;
    private static ProcessInfo[] PREMADE_PROCESSES = new ProcessInfo[] {
        new ProcessInfo(12, 1, 0),
            new ProcessInfo(5, 2, 4),
            new ProcessInfo(2, 3, 7),
            new ProcessInfo(7, 4, 9),
            new ProcessInfo(9, 5, 3)
    };


    private static final Random RANDOM = new Random();
    private static final int PROCESS_COUNT = 5;
    private static final Scheduler TYPE = new FCFS();

    private List<ProcessInfo> processes = randomProcesses();
    private SimulatedSystem system = new SimulatedSystem(processes);

    public GanttChart() {
        super("Gantt Process Schedule");
        // Create dataset
        IntervalCategoryDataset dataset = getDataset();

        // Create chart
        JFreeChart chart = ChartFactory.createGanttChart(
                "Wait Time: " + system.getWaitTime() + "\nTurn Around Time: " + system.getTurnaroundTime(), // Chart title
                "Process Identifier (pID)", // X-Axis Label
                "Timeline", // Y-Axis Label
                dataset);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public IntervalCategoryDataset getDataset() {


        system.setScheduler(TYPE);

        while (!system.isIdle()) {
            system.tick();
        }

        List<SimulatedSystem.ProcessRunningInfo> allTimes = system.getProcessRunningTimes();


        //Weird thing to get multiple boxes to show in a row

        List<TaskSeries> allSeries = new ArrayList<>();
        allSeries.add(new TaskSeries("Process Running Time 0"));

        for (SimulatedSystem.ProcessRunningInfo time : allTimes) {
            for (int i = 0; i < allSeries.size(); i++) {
                TaskSeries series = allSeries.get(i);
                if (series.get("" + time.getProcess().getPid()) != null) {
                    if (i + 1 >= allSeries.size()) {
                        //If we are at the end of the series list, make a new series
                        series = new TaskSeries("Process Running Time " + (i + 1));
                        allSeries.add(series);
                    } else {
                        //If this series already has this task (process), then continue to the next series
                        continue;
                    }
                }
                Task t = new Task("" + time.getProcess().getPid(), new Date(time.getStartTime()), new Date(time.getEndTime()));
                series.add(t);

                //If we were able to add it, break and go to the next task (process)
                break;
            }
        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();

        //Add all the series
        allSeries.forEach(dataset::add);

        return dataset;
    }

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

        SwingUtilities.invokeLater(() -> {
            GanttChart example = new GanttChart();
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }

    public static List<ProcessInfo> randomProcesses() {
        if (USE_PREMADE)
            return new ArrayList<>(Arrays.asList(PREMADE_PROCESSES));

        ArrayList<ProcessInfo> processes = new ArrayList<>();
        for (int i = 0; i < PROCESS_COUNT; i++) {
            processes.add(
                    new ProcessInfo(RANDOM.nextInt(10) + 1, i, i)
            );
        }

        return processes;
    }
}
