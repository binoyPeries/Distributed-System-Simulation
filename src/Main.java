import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Node> nodesList = Util.extractNodes("input/input1.txt");
        List<Cluster> leaderElectedClusters = LeaderElection.electInitialLeaders(nodesList);
        for (Cluster c : leaderElectedClusters) {
            System.out.println("[CLUSTER INFORMATION] " + c);
        }

        for (Cluster c : leaderElectedClusters) {
            System.out.println("[CLUSTER] Cluster with id " + c.getId() + " has started.");
            c.startNodes(c.getId());
        }
    }
}

