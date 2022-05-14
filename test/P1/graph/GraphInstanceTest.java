/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 *
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

    // Testing strategy
    //   TODO

    /**
     * Overridden by implementation-specific test classes.
     *
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    // TODO other tests for instance methods of Graph

    @Test
    public void testInstanceMethodsOfGraph() {
        Graph<String> graph = emptyInstance();
        assertTrue(graph.add("1"));
        assertTrue(graph.add("3"));
        assertFalse(graph.add("1"));
        assertTrue(graph.add("2"));
        assertTrue(graph.add("5"));
        assertFalse(graph.add("3"));
        assertTrue(graph.add("0"));
        assertTrue(graph.add("4"));

        assertEquals(0, graph.set("0", "2", 1));
        assertEquals(0, graph.set("0", "4", 4));
        assertEquals(0, graph.set("0", "5", 2));
        assertEquals(1, graph.set("0", "2", 4));
        assertEquals(0, graph.set("1", "4", 8));
        assertEquals(0, graph.set("1", "5", 5));
        assertEquals(0, graph.set("2", "3", 7));
        assertEquals(0, graph.set("2", "4", 3));
        assertEquals(5, graph.set("1", "5", 1));
        assertEquals(0, graph.set("4", "5", 5));
        assertEquals(8, graph.set("1", "4", 8));

        assertTrue(graph.remove("1"));
        assertFalse(graph.remove("6"));

        assertEquals(Map.of("5", 2, "4", 4, "2", 4), graph.sources("0"));
        assertEquals(Map.of("0", 4, "2", 3), graph.targets("4"));

    }

}
