enum MsgType {
    ELECTION,
    ELECTED,
    OTHER
}

public class Message {
    private final MsgType messageType;
    private final Node sender;
    private final Node receiver;
    private Node electionHolder;
    private final String msg;


    public Message(MsgType messageType, Node sender, Node receiver, String msg) {
        this.messageType = messageType;
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.electionHolder = null;
    }

    public Message(MsgType messageType, Node sender, Node receiver, Node electionHolder, String msg) {
        this.messageType = messageType;
        this.sender = sender;
        this.receiver = receiver;
        this.electionHolder = electionHolder;
        this.msg = msg;
    }

    public Node getElectionHolder() {
        return electionHolder;
    }

    public void setElectionHolder(Node electionHolder) {
        this.electionHolder = electionHolder;
    }

    public MsgType getMessageType() {
        return messageType;
    }

    public Node getSender() {
        return sender;
    }

    public Node getReceiver() {
        return receiver;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", electionHolder=" + electionHolder +
                ", msg='" + msg + '\'' +
                '}';
    }
}
