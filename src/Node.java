import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {
    private final Long id;
    private final int x;
    private final int y;
    private int energy;
    private Cluster cluster;
    private final BlockingQueue<Message> messageQueue;

//    private Long leaderId;
//    private List<Node> group;

    public Node(int x, int y, int energy) {
        this.id = Util.generateId();
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.cluster = null;
        this.messageQueue = new LinkedBlockingQueue<>();

    }


    public Long getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy += energy;
    }

//    public Long getLeaderId() {
//        return leaderId;
//    }
//
//    public void setLeaderId(Long leaderId) {
//        this.leaderId = leaderId;
//    }
//
//
//    public List<Node> getGroup() {
//        return group;
//    }

//    public void addToGroup(Node node) {
//        this.group.add(node);
//    }
//
//    public void updateGroup(List<Node> nodeList) {
//        this.group = nodeList;
//    }
//
//    public void removeFromGroup(Node node) {
//        this.group.remove(node);
//    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", energy=" + energy +
                ", cluster=" + cluster.getId() +
                '}';
    }

    public void sendMessage(Message message, Node receiver) {
        receiver.enqueueMessage(message);
        System.out.println("sent to -" + receiver.id);


    }

    public void readMessages() {
        while (!messageQueue.isEmpty()) {
            try {
                Message message = messageQueue.take();
                System.out.println("Received -" + message.getMsg() + " from -" + message.getSender());
                //add some loge
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void enqueueMessage(Message message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
//            logger.warning("Interrupted while enqueuing a message from Node " + message.getSender().getId() +
//                    " to Node " + message.getReceiver().getId());
        }
    }

    @Override
    public void run() {
        Node node = cluster.getNodeMembers().get(0);
        Message message = new Message(MsgType.OTHER, this, node, "hello from the other side");
        this.sendMessage(message, node);

        readMessages();


    }
}
