import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Node.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your input file path: ");
        String filePath = scanner.nextLine();
        Util.initializeLogger(logger);
        List<Node> nodesList = Util.extractNodes(filePath);
        List<Cluster> leaderElectedClusters = LeaderElection.electInitialLeaders(nodesList);
        System.out.println();
        for (Cluster c : leaderElectedClusters) {
            logger.info("[CLUSTER INFORMATION] " + c);
        }

        logger.info("========================== Starting System Simulation  ==========================\n");
        for (Cluster c : leaderElectedClusters) {
            logger.info("[CLUSTER] Cluster with id " + c.getId() + " has started.");
            c.startNodes(c.getId());
        }
    }
}

