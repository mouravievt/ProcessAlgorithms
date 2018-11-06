package com.company;

public class RoundRobin {

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
}