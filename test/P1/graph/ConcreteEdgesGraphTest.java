/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Tests for ConcreteEdgesGraph.
 * <p>
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * <p>
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }

    /*
     * Testing ConcreteEdgesGraph...
     */

    // Testing strategy for ConcreteEdgesGraph.toString()
    //   TODO

    // TODO tests for ConcreteEdgesGraph.toString()
    @Test
    public void testConcreteEdgesGraphToString() {
        Graph<String> graph = emptyInstance();
        graph.add("0");
        graph.add("1");
        graph.add("2");
        graph.add("3");
        graph.add("4");
        graph.add("5");
        graph.set("0", "2", 1);
        graph.set("0", "4", 4);
        graph.set("0", "5", 2);
        graph.set("1", "4", 8);
        graph.set("1", "5", 5);
        graph.set("2", "3", 7);
        graph.set("2", "4", 3);
        graph.set("4", "5", 1);
        assertEquals("Graph=(" +
                        "V={0,1,2,3,4,5,6}," +
                        "E={(0,2,1),(0,4,4),(0,5,2),(1,4,8),(1,5,5),(2,3,7),(2,4,3),(4,5,1)}" +
                        ")",
                graph.toString());
    }

    /*
     * Testing Edge...
     */

    // Testing strategy for Edge
    //   TODO

    // TODO tests for operations of Edge

}
