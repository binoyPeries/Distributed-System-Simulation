import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderElection {

    public static List<Cluster> electInitialLeaders(List<Node> nodesList) {
        /**
         * The algorithm is as follows: we consider how many possible nodes are within a radius of 20 for each node.
         * This is done by maintaining a hash map - each node id is a key and the nodes within a radius of 20 will be the value.
         * Initially all the nodes are ungrouped (which is why they are in the ungroupedNodes list
         * and once they are grouped, they will be added to the groupedNodes list.
         * The leader selection is done when the ungroupedNodes list is empty because by then all the nodes will be in groups
         */
        List<Node> groupedNodes = new ArrayList<>();
        List<Node> ungroupedNodes = new ArrayList<>(nodesList);
        List<Cluster> finalClusterList = new ArrayList<>();

        Map<Node, List<Node>> nodeMap = new HashMap<>();


        /**
         * This loop calculates the number of nodes within a radius of 20 for each node
         */
        for (Node potentialLeader : nodesList) {
            nodeMap.put(potentialLeader, new ArrayList<>());

            for (Node node : nodesList) {
                // if the distance to a node from the potential leader is less than 20
                if (Util.calculateEuclideanDistance(potentialLeader, node) <= 20.00) {
                    // add that node to the potential leader's list
                    nodeMap.get(potentialLeader).add(node);
                }
            }
        }


        Map<Node, List<Node>> sortedNodeMap = Util.sortNodeMapByListLength(nodeMap);

        /**
         * This loop does the actual leader selection.
         * We take each node id of the hashmap (Call this A).
         * (The hashmap contains A as a key, and the nodes within a radius of 20 of that node A)
         * Then for each node under A, we iterate through the nodes within its radius => Done in the for loop.
         * For each node in that group that does not already have a leader id, we assign A as its leader,
         * set its group as A's group, add it to the groupedNodes list and remove it from the ungroupedNodes list.
         */
        while (!ungroupedNodes.isEmpty()) {
            for (Node leader : sortedNodeMap.keySet()) {
                // all the nodes within distance of 20 of this node (potentially a group)
                List<Node> groupNodesOfLeader = sortedNodeMap.get(leader);
                List<Node> finalGroupMembers = new ArrayList<>(groupNodesOfLeader);
                Cluster cluster = new Cluster();

                for (Node groupMember : groupNodesOfLeader) {
                    if (groupMember.getCluster() == null) {
//                        groupMember.setLeaderId(leader);
                        groupMember.setCluster(cluster);
                        groupedNodes.add(groupMember);
                        ungroupedNodes.remove(groupMember);

                    } else {
                        finalGroupMembers.remove(groupMember);
                    }
                }
//                finalGroupMembers.forEach(node -> node.updateGroup(finalGroupMembers));
                if (!finalGroupMembers.isEmpty()) {
                    cluster.setLeader(leader);
                    cluster.updateClusterMembers(finalGroupMembers);
                    finalClusterList.add(cluster);
                }

            }
        }
        finalClusterList.forEach(cluster -> cluster.setClusterList(finalClusterList));

        return finalClusterList;

    }

    public static void ringAlgorithm(Node electionHolder) {
        electionHolder.setStatus(ElectionParticipantStatus.PARTICIPANT);
        Node successor = Util.getNodeSuccessor(electionHolder);
        Message electionMsg = new Message(MsgType.ELECTION, electionHolder, successor, electionHolder, "Node is starting an election.");
        electionHolder.sendMessage(electionMsg, successor);
    }

}
