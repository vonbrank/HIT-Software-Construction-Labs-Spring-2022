import org.junit.Test;

import static org.junit.Assert.*;

public class MagicSquareTest {
    @Test
    public void testIsNotPositiveInteger() {
        assertEquals(true, MagicSquare.isNotPositiveInteger("1 1"));
        assertEquals(true, MagicSquare.isNotPositiveInteger("-123dsf"));
        assertEquals(true, MagicSquare.isNotPositiveInteger("-123"));
        assertEquals(false, MagicSquare.isNotPositiveInteger("1"));
        assertEquals(false, MagicSquare.isNotPositiveInteger("18957"));
        assertEquals(false, MagicSquare.isNotPositiveInteger("0123"));
    }

    @Test
    public void testGetFullPathString() {
        System.out.println(MagicSquare.getFullPathString("/src/P1/txt/", "1.txt"));
    }
}
