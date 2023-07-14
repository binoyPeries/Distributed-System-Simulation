import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Node> nodesList = Utils.extractNodes("input/input1.txt");
        electInitialNodes(nodesList);
    }

    public static List<Node> electInitialNodes(List<Node> nodesList){
        /**
         * The algorithm is as follows: we consider how many possible nodes are within a radius of 20 for each node.
         * This is done by maintaining a hash map - each node id is a key and the nodes within a radius of 20 will be the value.
         * Initially all the nodes are ungrouped (which is why they are in the ungroupedNodes list
         * and once they are grouped, they will be added to the groupedNodes list.
         * The leader selection is done when the ungroupedNodes list is empty because by then all the nodes will be in groups
         */
        List<Node> groupedNodes = new ArrayList<>();
        List<Node> ungroupedNodes = nodesList;
        // to maintain a list of the initial leaders
        Set <Node> initialLeaders = new HashSet<>();

        Map<Long, List<Node>> nodeMap = new HashMap<>();

        /**
         * This loop calculates the number of nodes within a radius of 20 for each node
         */
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

        /**
         * This loop does the actual leader selection.
         * We take each node id of the hashmap (Call this A).
         * (The hashmap contains A as a key, and the nodes within a radius of 20 of that node A)
         * Then for each node under A, we iterate through the nodes within its radius => Done in the for loop.
         * For each node in that group that does not already have a leader id, we assign A as its leader,
         * set its group as A's group, add it to the groupedNodes list and remove it from the ungroupedNodes list.
         */
        while (!ungroupedNodes.isEmpty()){
            for (Long leaderNode : sortedNodeMap.keySet()) {
                // all the nodes within distance of 20 of this node (potentially a group)
                List<Node> groupNodesOfLeader = sortedNodeMap.get(leaderNode);

                for (Node groupNode : groupNodesOfLeader) {
                    if (groupNode.getLeaderId() == null){
                        groupNode.setLeaderId(leaderNode);
                        groupNode.addListToGroup(groupNodesOfLeader);
                        groupedNodes.add(groupNode);
                        ungroupedNodes.remove(groupNode);

                        // this is to get the actual node of the leader, since earlier we access it using the id only
                        // this is only necessary when we want the list of initial leaders
                        if (Objects.equals(groupNode.getId(), leaderNode)){
                            initialLeaders.add(groupNode);
                        }

                    }
                }
            }
        }
        System.out.println("===========================");
        Utils.printNodesList(new ArrayList<>(initialLeaders));

        return new ArrayList<>(initialLeaders);
    }
}

