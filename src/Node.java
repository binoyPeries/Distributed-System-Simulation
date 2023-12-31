import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

enum ElectionParticipantStatus {
    PARTICIPANT,
    NON_PARTICIPANT
}

public class Node extends Thread {
    private static final Logger logger = Logger.getLogger(Node.class.getName());
    private final Long id;
    private final int x;
    private final int y;
    private int energy;
    private Cluster cluster;

    private ElectionParticipantStatus status;
    private final BlockingQueue<Message> messageQueue;

    public Node(int x, int y, int energy) {
        this.id = Util.generateId();
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.cluster = null;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.status = ElectionParticipantStatus.NON_PARTICIPANT;
    }

    public ElectionParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ElectionParticipantStatus status) {
        this.status = status;
    }

    public long getId() {
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

    public synchronized void setEnergy(int energy) {
        this.energy += energy;
    }


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
                ", energy=" + energy +
                ", leader=" + cluster.getLeader().getId() +
                '}';
    }

    public synchronized void sendMessage(Message message, Node receiver) {
        receiver.enqueueMessage(message);
        Node sender = message.getSender();
        sender.setEnergy(-2);


    }

    public void readMessages() {
        while (!messageQueue.isEmpty()) {
            try {
                Message message = messageQueue.take();
                if (message.getMessageType() == MsgType.OTHER) {
                    logger.info("[MESSAGE] Node " + this.getId() + " received the message " + message.getMsg() + " from " + message.getSender().getId());
                } else {
                    LeaderElection.handleElection(message, this);
                }
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
        }
    }

    private Long getLeader() {
        return this.cluster.getLeader().getLeader();
    }

    @Override
    public void run() {

        while (this.getEnergy() > 0) {

//            logger.info("[SYSTEM STATUS] Node of id " + this.getId() + " has energy level " + this.getEnergy());
            logger.info("[SYSTEM STATUS] " + this.toString());

            // randomly send msg between two nodes with in the same cluster
            Util.sendRandomMsgBetweenNodes(this);

            // read msg in the queue
            readMessages();
            // check whether leader is still active
            if (this.cluster.getLeader().getEnergy() <= 0) {
                LeaderElection.ringAlgorithm(this);
            }

            // this is to reduce energy every unit time?
            this.setEnergy(-1);
            //:TODO change to 1000
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("[DEATH] Node " + this.getId() + " has died.");
        this.cluster.removeMember(this);
    }
}
