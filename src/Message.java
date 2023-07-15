enum MsgType {
    ELECTION,
    ELECTED,
    OTHER
}

public class Message {
    private final MsgType messageType;
    private final Node sender;
    private final Node receiver;
    private final String msg;


    public Message(MsgType messageType, Node sender, Node receiver, String msg) {
        this.messageType = messageType;
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
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
}
