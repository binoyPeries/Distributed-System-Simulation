import java.util.Objects;
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


    }

    public void readMessages() {
        while (!messageQueue.isEmpty()) {
            try {
                Message message = messageQueue.take();
                Node successor = Util.getNodeSuccessor(this);
                if (message.getMessageType() == MsgType.ELECTION) {
                    Long electionHolderId = message.getElectionHolder().getId();
                    if (this.getId() > electionHolderId) {
                        System.out.println("Forwarding to successor as it is, by " + this.getId());
                        this.sendMessage(message, successor);
                    } else if (this.getId() < electionHolderId) {

                        if (this.status == ElectionParticipantStatus.NON_PARTICIPANT) {
                            System.out.println("Starting my own election -  id -" + this.getId());
                            message.setElectionHolder(this);
                            this.setStatus(ElectionParticipantStatus.PARTICIPANT);
                            this.sendMessage(message, successor);
                        }
                    } else {
                        System.out.println("I have become the leader -  id -" + this.getId());

                        this.setStatus(ElectionParticipantStatus.NON_PARTICIPANT);
                        this.cluster.setLeader(this);
                        Message electedMsg = new Message(MsgType.ELECTED, this, successor, "I'm the new leader");
                        sendMessage(electedMsg, successor);

                    }
                } else if (message.getMessageType() == MsgType.ELECTED) {
                    Node sender = message.getSender();
                    if (!Objects.equals(sender.getId(), this.getId())) {

                        this.status = ElectionParticipantStatus.NON_PARTICIPANT;
                        System.out.println("===================NEW LEADER=========================");
                        System.out.println("Setting new leader " + sender);
                    }

                } else {
                    System.out.println("Received -" + message.getMsg() + " from -" + message.getSender());

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
            System.out.println("Q - " + this.getId() + " " + messageQueue);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
//            logger.warning("Interrupted while enqueuing a message from Node " + message.getSender().getId() +
//                    " to Node " + message.getReceiver().getId());
        }
    }

    @Override
    public void run() {
        while (this.getEnergy() > 0) {
//            Node node = cluster.getNodeMembers().get(0);
//            Message message = new Message(MsgType.OTHER, this, node, "hello from the other side");
//            this.sendMessage(message, node);

//            System.out.println("MY energy  " + this.getId() + "  " + this.getEnergy());
//            System.out.println("MY STATUS  " + this.getId() + " " + this.getStatus());
            readMessages();

            if (this.cluster.getLeader().getEnergy() <= 0) {
                System.out.println("Node - " + this.getId() + " starting an election!!");
                LeaderElection.ringAlgorithm(this);
            }
            this.setEnergy(-2);
        }
        this.cluster.removeMember(this);

//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}
