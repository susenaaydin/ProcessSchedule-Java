# CPU Scheduling Simulator

A **CPU scheduling simulation** application developed using Java Swing.

The application runs different CPU scheduling algorithms on the same process data and compares their results. The execution order and time intervals of processes are represented using a Gantt chart, while performance metrics such as average waiting time, turnaround time, and CPU utilization are calculated.

## Features

* 📂 Load process data from a `.txt` file
* ✏️ Manual process input
* 🎲 Generate random process data
* ⏱️ Set the time quantum
* 📊 Generate Gantt chart representations
* 📈 Compare algorithm performance
* 🧮 Calculate average waiting time
* 🧮 Calculate average turnaround time
* 💻 Calculate CPU utilization
* 🤖 Rank algorithms based on performance results
* ⏳ Display simulation progress

## Implemented Algorithms

### 1. FCFS (First Come First Served)

Executes processes in the order of their arrival times.

**Characteristics:**

* Simple and easy-to-understand scheduling method
* Process order is determined by arrival time
* Once a process starts, it continues until completion

### 2. SJF (Shortest Job First)

Selects the process with the shortest burst time among the available processes.

**Characteristics:**

* Implemented as a non-preemptive algorithm
* Prioritizes processes with shorter burst times
* Can reduce average waiting time in certain workloads

### 3. Round Robin

Executes processes for a specified **time quantum**.

If a process is not completed within its time quantum, it is moved to the end of the queue and the next process is executed.

**Characteristics:**

* Preemptive scheduling algorithm
* Time quantum can be configured by the user
* Provides a more balanced CPU allocation between processes

### 4. Priority Scheduling

Schedules processes according to their priority values.

In this project, a **lower priority value represents a higher priority**.

**Characteristics:**

* Implemented as a non-preemptive algorithm
* Compares the priority values of processes
* Higher-priority processes are executed first

## Input Methods

The application provides two different methods for entering process data.

### File Input

Process information can be loaded from a `.txt` file.

Each line in the file must follow this format:

```text
ProcessID,ArrivalTime,BurstTime,Priority
```

Example:

```text
P1,0,5,2
P2,1,3,1
P3,2,8,3
P4,3,4,2
```

### Manual Input

Users can manually enter processes through the graphical interface.

The following information is used:

| Field        | Description                                |
| ------------ | ------------------------------------------ |
| Process ID   | Unique identifier of the process           |
| Arrival Time | Time at which the process arrives          |
| Burst Time   | CPU execution time required by the process |
| Priority     | Priority value of the process              |

The application also provides a feature for generating random process data.

## Performance Analysis

The following metrics are calculated for each scheduling algorithm.

### Turnaround Time

The total time from a process's arrival until its completion.

```text
Turnaround Time = Finish Time - Arrival Time
```

### Waiting Time

The total time a process spends waiting in the ready queue.

```text
Waiting Time = Turnaround Time - Burst Time
```

### CPU Utilization

The ratio of the time the CPU is actively executing processes to the total simulation time.

```text
CPU Utilization = Total Burst Time / Total Time × 100
```

## Gantt Chart

The execution order of processes on the CPU is displayed after the simulation.

Example:

```text
[0]---P1---[5]---P2---[8]---P3---[16]
```

This representation makes it possible to see the execution order of the processes and how long each process uses the CPU.

## Algorithm Comparison

After the simulation is completed, the algorithms are compared based on their **average waiting time**.

Algorithms are ranked from the lowest to the highest average waiting time.

Example:

```text
=== PERFORMANCE COMPARISON ===

1. SJF                  : 3.2500 units
2. Priority             : 4.0000 units
3. FCFS                 : 5.7500 units
4. Round                : 6.5000 units
```

The algorithm with the lowest average waiting time is also identified and displayed in the results section.

## User Interface

The application was developed with a graphical user interface using Java Swing.

The interface consists of three main tabs:

### Load from File

* Select a `.txt` file
* Set the time quantum
* Start the simulation

### Manual Input

* Add processes
* Edit process information
* Generate random data
* Clear process data
* Set the time quantum
* Run the simulation

### Analysis and Charts

* Compare algorithm performance
* Rank algorithms by average waiting time
* Identify the best-performing algorithm
* Display Gantt chart and performance visualization

## Technologies

* **Java**
* **Java Swing**
* **AWT**
* **Object-Oriented Programming (OOP)**
* **File I/O**
* **Data Structures**
* **CPU Scheduling Algorithms**

## Project Structure

```text
ProcessSchedule/
│
├── ProcessSchedule/
│   ├── ProcessScheduler.java
│   └── ProcessScheduler.jar
│
└── README.md
```

> `.class` files are compiled outputs generated from the Java source code. The main source file of the project is `ProcessScheduler.java`.

## How to Run

Java JDK must be installed on your computer.

### Run from Source Code

Open a terminal in the directory containing `ProcessScheduler.java`.

Compile the application:

```bash
javac ProcessScheduler.java
```

Run the application:

```bash
java ProcessScheduler
```

### Run the JAR File

To run the precompiled `.jar` file:

```bash
java -jar ProcessScheduler.jar
```

## Project Purpose

The purpose of this project is to provide a practical way to study and understand CPU scheduling algorithms.

By scheduling the same set of processes using different algorithms and comparing their results, the project allows users to observe performance differences between scheduling approaches.

The project also provides a graphical interface using Java Swing, allowing scheduling algorithms to be analyzed visually instead of relying only on console output.

## Future Improvements

* Add additional CPU scheduling algorithms
* Implement preemptive algorithms such as SRTF
* Create a more detailed Gantt chart
* Compare algorithms using additional performance metrics
* Export simulation results to files
* Improve performance visualizations
* Enhance the graphical user interface

## Developer

**Sude Sena Aydın**
