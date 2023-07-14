import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Node> nodesList = Util.extractNodes("input/input1.txt");
        List<Node> leaderElectedList = LeaderElection.electInitialLeaders(nodesList);
        System.out.println(nodesList);
        System.out.println(leaderElectedList);

    }

}

