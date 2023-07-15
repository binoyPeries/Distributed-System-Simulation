import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Node> nodesList = Util.extractNodes("input/input1.txt");
        List<Cluster> leaderElectedClusters = LeaderElection.electInitialLeaders(nodesList);
        System.out.println("===================== Clusters ============================");
        System.out.println(leaderElectedClusters);
        System.out.println("===================== Clusters ENDS :( ============================");


//        for (Node nodeMember : leaderElectedClusters.get(3).getNodeMembers()) {
//            Thread nodeThread = new Thread(nodeMember);
//            nodeThread.start();
//        }


        for (Cluster c : leaderElectedClusters) {
            c.startNodes();
        }


    }

}

