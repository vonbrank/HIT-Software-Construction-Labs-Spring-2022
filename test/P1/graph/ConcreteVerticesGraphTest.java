/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.dgc.Lease;
import java.util.Map;

/**
 * Tests for ConcreteVerticesGraph.
 * <p>
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * <p>
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }

    /*
     * Testing ConcreteVerticesGraph...
     */

    // Testing strategy for ConcreteVerticesGraph.toString()
    //   TODO

    // TODO tests for ConcreteVerticesGraph.toString()
    @Test
    public void testConcreteVerticesGraphToString() {
        Graph<String> graph = emptyInstance();
        graph.add("1");
        graph.add("0");
        graph.add("2");
        graph.add("3");
        graph.add("4");
        graph.add("5");
        graph.set("0", "4", 4);
        graph.set("0", "2", 1);
        graph.set("0", "5", 2);
        graph.set("1", "4", 8);
        graph.set("1", "5", 5);
        graph.set("2", "4", 3);
        graph.set("2", "3", 7);
        graph.set("4", "5", 1);
        assertEquals("Graph=(" +
                        "V={0,1,2,3,4,5}," +
                        "E={(0,{(2,1),(4,4),(5,2)})," +
                        "(1,{(4,8),(5,5)})," +
                        "(2,{(3,7),(4,3)})," +
                        "(4,{(5,1)})}" +
                        ")",
                graph.toString());
    }

    /*
     * Testing Vertex...
     */

    // Testing strategy for Vertex
    //   TODO

    // TODO tests for operations of Vertex
    @Test
    public void testConcreteVerticesGraphVertex() {
        Vertex<String> vertex = new Vertex<>("1");
        assertEquals("1", vertex.getLabel());
        vertex.addEdge("2", 3);
        assertEquals(3, vertex.getWeight("2"));
        assertEquals(0, vertex.getWeight("3"));
        assertEquals(Map.of("2", 3), vertex.getEdges());
        assertEquals(1, vertex.outDegree());
        assertEquals("(1,{(2,3)})", vertex.toString());
        vertex.removeEdge("2");
        assertEquals(Map.of(), vertex.getEdges());
    }
}
