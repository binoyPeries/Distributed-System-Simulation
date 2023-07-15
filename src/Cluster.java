import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cluster implements Runnable {
    private final Long id;
    private Node leader;
    private List<Node> nodeMembers;

    public void setClusterList(List<Cluster> clusterList) {
        this.clusterList = clusterList;
    }

    private List<Cluster> clusterList;

    private final ReadWriteLock lock;


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

    public void updateMemberEnergy(Node node, int updateBy) {
        lock.writeLock().lock();
        try {
            int index = nodeMembers.indexOf(node);
            nodeMembers.get(index).setEnergy(updateBy);
            System.out.println("energy reduced");
            System.out.println("done by-" + node.getId() + "" + this.nodeMembers);


        } finally {
            lock.writeLock().unlock();
        }
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

    @Override
    public void run() {
        for (Node nodeMember : nodeMembers) {
            Thread nodeThread = new Thread(nodeMember);
            nodeThread.start();
        }
    }


    @Override
    public String toString() {
        return "Cluster{" +
                "id=" + id +
                ", leader=" + leader +
                ", nodeMembers=" + nodeMembers +
                ", clusterList=" + clusterList.size() +
                '}' + '\n';
    }
}
