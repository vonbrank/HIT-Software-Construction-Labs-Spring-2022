import org.junit.Test;
import turtle.Vector.Vector2;

import static org.junit.Assert.*;

public class TurtleSoupTest {
    @Test
    public void vector2GetAngleDiffTest() {
        assertEquals(90, Vector2.getAngleDiff(new Vector2(1, 0), new Vector2(0, 1)), 0.001);
        assertEquals(180, Vector2.getAngleDiff(new Vector2(1, 0), new Vector2(-1, 0)), 0.001);
        assertEquals(60, Vector2.getAngleDiff(new Vector2(1, 0), new Vector2(1, 1.732)), 0.001);
    }
}
