import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Util {
    private static final AtomicReference<Long> currentTime =
            new AtomicReference<>(System.currentTimeMillis());

    public static Long generateId() {
        return currentTime.accumulateAndGet(System.currentTimeMillis(),
                (prev, next) -> next > prev ? next : prev + 1);
    }

    public static List<Node> extractNodes(String filePath) {
        List<Node> nodes = new ArrayList<>();
        String[] triplet = {};

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                triplet = line.split(",");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < triplet.length - 2; i += 3) {
            int x = Integer.parseInt(triplet[i].replace("(", "").trim());
            int y = Integer.parseInt(triplet[i + 1].trim());
            int energy = Integer.parseInt(triplet[i + 2].replace(")", "").trim());
            Node node = new Node(x, y, energy);
            nodes.add(node);
//            System.out.println(node);
        }

        return nodes;

    }

    public static double calculateEuclideanDistance(Node leader, Node node) {
        double dx = leader.getX() - node.getX();
        double dy = leader.getY() - node.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static Map<Node, List<Node>> sortNodeMapByListLength(Map<Node, List<Node>> nodeMap) {
        List<Map.Entry<Node, List<Node>>> entries = new ArrayList<>(nodeMap.entrySet());

        entries.sort((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()));

        Map<Node, List<Node>> sortedNodeMap = new LinkedHashMap<>();
        for (Map.Entry<Node, List<Node>> entry : entries) {
            sortedNodeMap.put(entry.getKey(), entry.getValue());
        }

        return sortedNodeMap;
    }

    public static void printNodesList(List<Node> nodesList) {
        for (Node node : nodesList) {
            System.out.println(node);
        }
    }

    public static Node getNodeSuccessor(Node node) {
        List<Node> groupNodes = node.getCluster().getNodeMembers();
        int index = groupNodes.indexOf(node);
        int nextIndex = 0;
        if (index != groupNodes.size() - 1) {
            nextIndex = index + 1;
        }
        return groupNodes.get(nextIndex);
    }
}