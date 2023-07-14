import java.util.ArrayList;
import java.util.List;

public class Node {
    private Long id;
    private int x;

    private int y;
    private int energy;
    private int leaderId;
    private List<Node> group;

    public Node(int x, int y, int energy) {
        this.id = Utils.generateId();
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.leaderId = 0;
        this.group = new ArrayList<>();
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

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }


    public List<Node> getGroup() {
        return group;
    }

    public void addToGroup(Node node) {
        this.group.add(node);
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
                ", group=" + group +
                '}';
    }
}
