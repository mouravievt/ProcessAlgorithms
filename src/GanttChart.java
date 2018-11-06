import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GanttChart extends JFrame {

    private static final Random RANDOM = new Random();
    private static final int PROCESS_COUNT = 5;
    private static final Scheduler TYPE = new SRT();

    public GanttChart() {
        super("Gantt Process Schedule");
        // Create dataset
        IntervalCategoryDataset dataset = getDataset();

        // Create chart
        JFreeChart chart = ChartFactory.createGanttChart(
                "Gantt Process Schedule", // Chart title
                "Process Identifier (pID)", // X-Axis Label
                "Timeline", // Y-Axis Label
                dataset);

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public IntervalCategoryDataset getDataset() {
        List<ProcessInfo> processes = randomProcesses();

        SimulatedSystem system = new SimulatedSystem(processes);

        system.setScheduler(TYPE);

        while (!system.isIdle()) {
            system.tick();
        }

        List<SimulatedSystem.ProcessRunningInfo> allTimes = system.getProcessRunningTimes();

        TaskSeries series = new TaskSeries("Process Running Time");
        for (SimulatedSystem.ProcessRunningInfo time : allTimes) {
            Task t = new Task("" + time.getProcess().getPid(), new Date(time.getStartTime()), new Date(time.getEndTime()));
            series.add(t);
        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series);

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GanttChart example = new GanttChart();
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }

    public static List<ProcessInfo> randomProcesses() {
        ArrayList<ProcessInfo> processes = new ArrayList<>();
        for (int i = 0; i < PROCESS_COUNT; i++) {
            processes.add(
                    new ProcessInfo(0, RANDOM.nextInt(10) + 1, i, i)
            );
        }

        return processes;
    }
}
