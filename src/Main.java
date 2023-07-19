import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your input file path: ");
        String filePath = scanner.nextLine();
        List<Node> nodesList = Util.extractNodes(filePath);
        List<Cluster> leaderElectedClusters = LeaderElection.electInitialLeaders(nodesList);
        System.out.println();
        for (Cluster c : leaderElectedClusters) {
            System.out.println("[CLUSTER INFORMATION] " + c);
        }

        System.out.println("========================== Starting System Simulation  ==========================\n");
        for (Cluster c : leaderElectedClusters) {
            System.out.println("[CLUSTER] Cluster with id " + c.getId() + " has started.");
            c.startNodes(c.getId());
        }
    }
}

