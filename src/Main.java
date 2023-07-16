import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Node> nodesList = Util.extractNodes("input/input3.txt");
        List<Cluster> leaderElectedClusters = LeaderElection.electInitialLeaders(nodesList);
        System.out.println("===================== List of clusters. ============================");
        for (Cluster c : leaderElectedClusters) {
            System.out.println(c);
        }
        System.out.println("===================== End of list of clusters. ============================");

        for (Cluster c : leaderElectedClusters) {
            System.out.println("[CLUSTER] Cluster with id " + c.getId() + " has started.");
            c.startNodes(c.getId());
        }
    }
}

