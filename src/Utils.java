import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {
    private static AtomicReference<Long> currentTime =
            new AtomicReference<>(System.currentTimeMillis());

    public static Long generateId() {
        return currentTime.accumulateAndGet(System.currentTimeMillis(),
                (prev, next) -> next > prev ? next : prev + 1);
    }

    public static List<Node> extractNodes(String filePath){
        List<Node> nodes = new ArrayList<>();
        String[] triplet = {};

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                triplet = line.split(",");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < triplet.length-2; i+=3) {
            int x = Integer.parseInt(triplet[i].replace("(", "").trim());
            int y = Integer.parseInt(triplet[i+1].trim());
            int energy = Integer.parseInt(triplet[i+2].replace(")", "").trim());
            Node node = new Node(x, y, energy);
            nodes.add(node);
            System.out.println(node);
        }

        return nodes;

    }
}