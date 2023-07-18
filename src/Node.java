import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

enum ElectionParticipantStatus {
    PARTICIPANT,
    NON_PARTICIPANT
}

public class Node implements Runnable {
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
                ", x=" + x +
                ", y=" + y +
                ", energy=" + energy +
                ", cluster=" + cluster.getId() +
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
                    System.out.println("[MESSAGE] Node " + this.getId() + " received the message " + message.getMsg() + " from " + message.getSender().getId());
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

    @Override
    public void run() {

        while (this.getEnergy() > 0) {

            logger.info("[SYSTEM STATUS] Node of id " + this.getId() + " has energy level " + this.getEnergy());

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
            // this to ensure that //:TODO have finalize this
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("[DEATH] Node " + this.getId() + " has died.");
        this.cluster.removeMember(this);
    }
}
