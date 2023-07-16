# CS4262 - Distributed Systems - Programming Assignment

This repository contains the source code for a simulation of a dsitributed system.
It simulates the communication between various nodes of a distributed system that have
an initial amount of energy. Upon system initialization, groups of nodes are formed based on
their Euclidean distance to each other, and an initial leader for each group is selected. For each
timestep of the system, the energy of a node decreases by 1, and upon message transmission, the energy of
the sender decreases by 2. When the energy of a node becomes 0, it will be considered dead and removed from the system.
If the leader of a group reaches a 0 energy level, a leader election will happen within that group and a new leader will
be elected.
The goal of the system is to maximize its lifetime.

## Input

Inputs of the program are the x, y coordinates of each node and their initial energy level. The values should be
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
../input/input1.txt
```

So the input process would be:

```bash
[WORKING_DIR]/src> javac Main.java
[WORKING_DIR]/src> java Main
Enter your input file path: ../input/input1.txt
```

## Initial Leader Selection and Group Formation

Once inputs are read, the code checks to see which node has the most nodes within a radius of 20 (Euclidean distance).
This is done by maintaining a hash map - each node id is a key and the nodes within a radius of 20 will be the value.
The node which has the highest number of nodes within a radius of 20 is made the leader of that group. This process is
repeated until each node is either a leader or belongs to a group. One node is only assigned to one group, and it will
remain in
that group until the system finishes.

## Leader Election - Ring Algorithm

Once a leader dies (reaches 0 energy), that group (if it has any nodes left) will have a leader election. In this
system, leader election is done according to the ring algorithm.

