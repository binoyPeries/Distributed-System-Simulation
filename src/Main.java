import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Node> nodesList = Utils.extractNodes("input/input1.txt");
        electInitialNodes(nodesList);
    }

    public static void electInitialNodes(List<Node> nodesList){
        List<Node> groupedNodes = new ArrayList<>();
        List<Node> ungroupedNodes = nodesList;

        Map<Long, List<Node>> nodeMap = new HashMap<>();

        for (Node potentialLeader : nodesList) {

            nodeMap.put(potentialLeader.getId(), new ArrayList<>());
            nodeMap.get(potentialLeader.getId()).add(potentialLeader);

            for (Node node : nodesList) {
                if (!Objects.equals(potentialLeader.getId(), node.getId())){
                    // if the distance to a node from the potential leader is less than 20
                    if (Utils.calculateEuclideanDistance(potentialLeader, node) <=20 ){
                        // add that node to the potential leader's list
                        nodeMap.get(potentialLeader.getId()).add(node);
                    }
                }
            }
        }
        Map<Long, List<Node>> sortedNodeMap = Utils.sortNodeMapByListLength(nodeMap);

        while (!ungroupedNodes.isEmpty()){
            for (Long nodeId : sortedNodeMap.keySet()) {
                // all the nodes within distance of 20 of this node
                List<Node> groupNodes = sortedNodeMap.get(nodeId);

                for (Node groupNode : groupNodes) {
                    if (groupNode.getLeaderId() == null){
                        groupNode.setLeaderId(nodeId);
                        groupedNodes.add(groupNode);
                        ungroupedNodes.remove(groupNode);

                    }
                }
            }
        }
        System.out.println("===========================");
        Utils.printNodesList(groupedNodes);
        System.out.println("===========================");
        Utils.printNodesList(ungroupedNodes);
    }
}

