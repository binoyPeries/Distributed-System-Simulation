import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class Cluster {
    private final Long id;
    private Node leader;
    private List<Node> nodeMembers;

    public void setClusterList(List<Cluster> clusterList) {
        this.clusterList = clusterList;
    }

    private List<Cluster> clusterList;

    private final ReadWriteLock lock;
    private static final Logger logger = Logger.getLogger(Node.class.getName());


    public Cluster() {
        this.id = Util.generateId();
        this.leader = null;
        this.nodeMembers = new ArrayList<>();
        this.clusterList = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();

    }

    public Long getId() {
        return id;
    }


    public Node getLeader() {
        return leader;
    }

    public void setLeader(Node leader) {
        this.leader = leader;
    }

    public void removeMember(Node node) {
        lock.writeLock().lock();
        try {
            nodeMembers.remove(node);

        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Node> getNodeMembers() {
        lock.readLock().lock();
        try {

            return nodeMembers;
        } finally {
            lock.readLock().unlock();
        }

    }

    public void updateClusterMembers(List<Node> nodeList) {
        this.nodeMembers = nodeList;
    }

    public void startNodes(Long clusterId) {
        List<Node> nodeList = this.getNodeMembers();
        for (Node nodeMember : nodeList) {
            nodeMember.start();
            logger.info("[NODE] Thread for node with id " + nodeMember.getId() + " in cluster " + clusterId + " has started.");
        }
    }

    private String getNodeIds() {
        StringBuilder nodeIds = new StringBuilder();
        for (int i = 0; i < nodeMembers.size(); i++) {
            Node node = nodeMembers.get(i);
            nodeIds.append("ID = ").append(node.getId()).append(", (x: " + node.getX() + ", y: " + node.getY() + ")").append(", energy level = ").append(node.getEnergy());
            nodeIds.append('\n');
        }
        return nodeIds.toString();
    }


    @Override
    public String toString() {

        return "Cluster " + id +
                " has leader node " + leader.getId() +
                " and a total of " + nodeMembers.size() + " member(s). Member nodes are:" + '\n' + getNodeIds();
    }
}
