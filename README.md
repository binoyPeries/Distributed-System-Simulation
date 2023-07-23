# CS4262 - Distributed Systems - Programming Assignment

This repository contains the source code for a simulation of a distributed system.
It simulates the communication between various nodes of a distributed system that have
an initial amount of energy. Upon system initialization, groups of nodes are formed based on
their Euclidean distance to each other, and an initial leader for each group is selected. For each
timestep of the system, the energy of a node decreases by 1, and upon message transmission, the energy of
the sender decreases by 2. When the energy of a node reaches 0, it will be considered dead and removed from the system.
If the leader of a group reaches a 0 energy level, a leader election will happen within that group, and a new leader
will
be elected.
The goal of the system is to maximize its lifetime.

## Input

The inputs of the program are the x, y coordinates of each node and their initial energy level. The values should be
provided in a text file.
Sample inputs have been provided in the input folder.

## Running the Program

From within the src folder, run the following commands in the terminal.

```bash
javac Main.java
java Main
```

The program will prompt you to input your input file path. This should be given relative to the input folder. For
example, if you wish to point to the sample input file input1.txt in the input folder,
you need to input

```bash
../io/input1.txt
```

So the input process would be:

```bash
[WORKING_DIR]/src> javac Main.java
[WORKING_DIR]/src> java Main
Enter your input file path: ../io/input1.txt
```

## System Design

* Each node is part of a cluster, and each cluster has a leader.
* Once the leader runs out of energy, the next leader is selected from within the cluster itself.
* Nodes within the cluster can communicate with each other.
* Each node is designed as a thread to mimic the independently functioning nodes in an actual distributed system.
* Node ID is based on the atomic timestamp of when the node was created. This is also used to mimic the functioning of
  an
  actual distributed system where nodes start at different times, and we also ensure that no two nodes will have the
  same
  node ID.
* A variation of the message queuing model is implemented in the nodes as the message passing mechanism.
* Each node contains a blocking queue where it stores all of its received messages. The blocking queue contains
  concurrency control mechanisms to manage access by multiple threads.
* Random message passing is done between random nodes to simulate the functioning of a distributed system.
* Unit time in the simulation is considered as 1 second.
* 1 unit of energy is decreased from each node per unit time, and 2 units of energy are decreased from the transmitter
  of
  a message when a message is passed.
* A variation of the ring algorithm is used for the leader election algorithm.
* In the leader election, priority of a node is decided based on its energy level, since the goal of the simulation is
  to
  maximize the system lifetime. Therefore nodes with higher energy are of higher priority.
* Once a node reaches an energy level of 0, it is considered to be dead.
  Concurrency control mechanisms like read and write locks have been used when necessary.

## Visualization

Outputs will be logged into the output.log file within the io folder. It will also be printed in the console. The
following section contains the types of output logs in the system.

### Output Logs

All input and output files are contained within the io folder. All output logs contain the type of log within square
brackets as listed below.

```bash
[SYSTEM STATUS] is a log of the status of a node within the system at a given timestamp. It prints out the node ID, energy and leader of the node.

[MESSAGE] is a log of a message passing between nodes, indicating the recipient, sender and contents of the message.

[ELECTION] is a log of an election occuring, which details what node is starting the election, and the forwarding of election messages between nodes.

[ELECTION - NEW LEADER] is a log of a new leader being elected, which displays which node was elected as the leader.

[DEATH] is a log indicating that a node has died.
```
