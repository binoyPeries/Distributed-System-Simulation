import java.util.*;
import java.util.logging.Logger;

public class LeaderElection {
    private static final Logger logger = Logger.getLogger(Node.class.getName());


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
                        groupMember.setCluster(cluster);
                        groupedNodes.add(groupMember);
                        ungroupedNodes.remove(groupMember);

                    } else {
                        finalGroupMembers.remove(groupMember);
                    }
                }
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

    public synchronized static void ringAlgorithm(Node electionHolder) {

        if (electionHolder.getStatus() == ElectionParticipantStatus.NON_PARTICIPANT) {
            logger.info("[ELECTION] Node with id " + electionHolder.getId() + " is starting an election.");
            electionHolder.setStatus(ElectionParticipantStatus.PARTICIPANT);
            Node successor = Util.getNodeSuccessor(electionHolder);
            if (successor != null) {
                Message electionMsg = new Message(MsgType.ELECTION, electionHolder, successor, electionHolder, "Node is starting an election.");
                electionHolder.sendMessage(electionMsg, successor);
            }
        }

    }

    public synchronized static void handleElection(Message message, Node currentNode) {
        Node successor = Util.getNodeSuccessor(currentNode);

        if (successor == null) {
            logger.info("[ELECTION - NEW LEADER] Node with id " + currentNode.getId() + " has become the leader.");
            currentNode.setStatus(ElectionParticipantStatus.NON_PARTICIPANT);
            currentNode.getCluster().setLeader(currentNode);
            return;
        }
        if (message.getMessageType() == MsgType.ELECTION) {
            int electionHolderEnergy = message.getElectionHolder().getEnergy();
            Long electionHolderId = message.getElectionHolder().getId();
            int currentNodeEnergy = currentNode.getEnergy();
            Long currentNodeId = currentNode.getId();

            // to check whether the two nodes are the same or not
            if (!Objects.equals(currentNodeId, electionHolderId)) {

                if (currentNodeEnergy <= electionHolderEnergy) {
                    logger.info("[ELECTION] Forwarding to successor " + successor.getId() + " as it is, by " + currentNode.getId());
                    message.setSender(currentNode);
                    currentNode.sendMessage(message, successor);

                } else {
                    if (currentNode.getStatus() == ElectionParticipantStatus.NON_PARTICIPANT) {
                        logger.info("[ELECTION] Node with id " + currentNode.getId() + "has started its own election.");
                        message.setElectionHolder(currentNode);
                        message.setReceiver(successor);
                        message.setSender(currentNode);
                        currentNode.setStatus(ElectionParticipantStatus.PARTICIPANT);
                        currentNode.sendMessage(message, successor);
                    }
                }
            } else {
                logger.info("[ELECTION - NEW LEADER] Node with id " + currentNode.getId() + " has become the leader.");
                currentNode.setStatus(ElectionParticipantStatus.NON_PARTICIPANT);
                currentNode.getCluster().setLeader(currentNode);
                Message electedMsg = new Message(MsgType.ELECTED, currentNode, successor, "I'm the new leader");
                currentNode.sendMessage(electedMsg, successor);
            }

        } else if (message.getMessageType() == MsgType.ELECTED) {
            Node sender = message.getSender();
            if (!Objects.equals(sender.getId(), currentNode.getId())) {
                currentNode.setStatus(ElectionParticipantStatus.NON_PARTICIPANT);
                logger.info("[ELECTED] New leader of the cluster is the node of id " + sender.getId());
            }
        }
    }
}
