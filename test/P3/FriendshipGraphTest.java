import org.junit.Test;

import static org.junit.Assert.*;

public class FriendshipGraphTest {
    @Test
    public void friendshipGraphFeatureTest() {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        assertEquals(1, graph.getDistance(rachel, ross));
        assertEquals(2, graph.getDistance(rachel, ben));
        assertEquals(0, graph.getDistance(rachel, rachel));
        assertEquals(-1, graph.getDistance(rachel, kramer));
    }
}
