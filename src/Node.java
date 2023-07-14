import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Long id;
    private final int x;
    private final int y;
    private int energy;
    private Long leaderId;
    private List<Node> group;

    public Node(int x, int y, int energy) {
        this.id = Util.generateId();
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.leaderId = null;
        this.group = new ArrayList<>();
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
        this.energy = energy;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }


    public List<Node> getGroup() {
        return group;
    }

    public void addToGroup(Node node) {
        this.group.add(node);
    }

    public void updateGroup(List<Node> nodeList) {
        this.group = nodeList;
    }

    public void removeFromGroup(Node node) {
        this.group.remove(node);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", energy=" + energy +
                ", leaderId=" + leaderId +
                ", group=" + group.size() +
                '}';
    }
}
