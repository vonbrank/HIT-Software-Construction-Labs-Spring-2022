/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * <p>
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {

    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), new ConcreteEdgesGraph<Integer>().vertices());
    }

    // TODO test other vertex label types in Problem 3.2
    @Test
    public void testGraphIntegerLabel() {
        Graph<Integer> graph = Graph.empty();
        assertTrue(graph.add(1));
        assertTrue(graph.add(3));
        assertFalse(graph.add(1));
        assertTrue(graph.add(2));
        assertTrue(graph.add(5));
        assertFalse(graph.add(3));
        assertTrue(graph.add(0));
        assertTrue(graph.add(4));

        assertEquals(0, graph.set(0, 2, 1));
        assertEquals(0, graph.set(0, 4, 4));
        assertEquals(0, graph.set(0, 5, 2));
        assertEquals(1, graph.set(0, 2, 0));
        assertEquals(0, graph.set(0, 2, 4));
        assertEquals(0, graph.set(1, 4, 8));
        assertEquals(0, graph.set(1, 5, 5));
        assertEquals(0, graph.set(2, 3, 7));
        assertEquals(0, graph.set(2, 4, 3));
        assertEquals(5, graph.set(1, 5, 1));
        assertEquals(0, graph.set(4, 5, 5));
        assertEquals(8, graph.set(1, 4, 8));

        assertTrue(graph.remove(1));
        assertFalse(graph.remove(1));

        assertEquals(Map.of(0, 4, 2, 3), graph.sources(4));
        assertEquals(Map.of(5, 2, 4, 4, 2, 4), graph.targets(0));
    }

}
