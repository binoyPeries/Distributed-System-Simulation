import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

enum ElectionParticipantStatus {
    PARTICIPANT,
    NON_PARTICIPANT
}

public class Node implements Runnable {
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

    public void setEnergy(int energy) {
        this.energy += energy;
        System.out.println(this.cluster.getNodeMembers());
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

    public void sendMessage(Message message, Node receiver) {
        receiver.enqueueMessage(message);
        System.out.println("sent to -" + receiver.id);


    }

    public void readMessages() {
        while (!messageQueue.isEmpty()) {
            try {
                Message message = messageQueue.take();
                System.out.println("Received -" + message.getMsg() + " from -" + message.getSender());
                Node successor = Util.getNodeSuccessor(this);
                if (message.getMessageType() == MsgType.ELECTION) {
                    if (this.getId() > message.getElectionHolder().getId()) {
                        this.sendMessage(message, successor);
                    } else {
                        if (this.status == ElectionParticipantStatus.NON_PARTICIPANT) {
                            message.setElectionHolder(this);
                            this.setStatus(ElectionParticipantStatus.PARTICIPANT);
                            this.sendMessage(message, successor);
                        }
                    }
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
//        this.setEnergy(-1000);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}
