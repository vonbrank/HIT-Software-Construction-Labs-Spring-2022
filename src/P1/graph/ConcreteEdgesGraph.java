/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * An implementation of Graph.
 *
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {

    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    //   AF(vertices, edges) = a graph with all vertices in the filed vertices and all the edges in the filed edges.
    // Representation invariant:
    //   vertices is the set of all vertices. Every vertex is labeled with a String which is unique in a Graph
    //   edges is the list of all edges. Once an edge has a source "a" and a target "b", then the vertices "a" and "b"
    //   must be in the set vertices
    // Safety from rep exposure:
    //   All fields are private;
    //   vertices is a HashSet<String>, so every element in the Set is guaranteed immutable, and the whole field
    //   vertices itself is a mutable, so the method vertices() makes a defensive copy to avoid sharing the ref of
    //   the field vertices
    //   edges is mutable, but no edge will be returned
    // TODO constructor

    // TODO checkRep
    public void checkRep() {
        edges.forEach(Edge::checkRep);
    }

    @Override
    public boolean add(L vertex) {
        if (vertices.contains(vertex)) return false;
        else vertices.add(vertex);
        checkRep();
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        add(source);
        add(target);

        if (weight == 0) {
            AtomicInteger previousWeight = new AtomicInteger();
            edges.removeIf(edge -> {
                if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                    previousWeight.set(edge.getWeight());
                    return true;
                }
                return false;
            });
            return previousWeight.get();
        }

        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getSource().equals(source) && edges.get(i).getTarget().equals(target)) {
                int previousWeight = edges.get(i).getWeight();
                edges.set(i, edges.get(i).setWeight(weight));
                return previousWeight;
            }
        }

        edges.add(new Edge<L>(source, target, weight));
        checkRep();
        return 0;
    }

    @Override
    public boolean remove(L vertex) {
        if (!vertices.contains(vertex)) return false;
        vertices.removeIf(currentVertex -> currentVertex.equals(vertex));
        edges.removeIf(edge -> (edge.getSource().equals(vertex) || edge.getTarget().equals(vertex)));
        checkRep();
        return true;
    }

    @Override
    public Set<L> vertices() {
        return new HashSet<>(vertices);
    }

    @Override
    public Map<L, Integer> sources(L target) {
        return edges.stream()
                .filter(edge -> edge.getTarget().equals(target))
                .collect(Collectors.toMap(Edge::getSource, Edge::getWeight));
    }

    @Override
    public Map<L, Integer> targets(L source) {
        return edges.stream()
                .filter(edge -> edge.getSource().equals(source))
                .collect(Collectors.toMap(Edge::getTarget, Edge::getWeight));
    }

    // TODO toString()


    @Override
    public String toString() {
        return "ConcreteEdgesGraph{" +
                "vertices=" + vertices +
                ", edges=" + edges +
                '}';
    }
}

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {

    // TODO fields
    private final L source;
    private final L target;
    private final int weight;

    // Abstraction function:
    //   AF(source, target, weight) = an edge from source to target has a weight.
    // Representation invariant:
    //   source and target are Strings
    //   weight is the weight of the edge and must be positive. Once the weight is zero, the edge should not exist
    // Safety from rep exposure:
    //   source and target are String, so are guaranteed immutable.
    //   the whole Edge class is immutable, so all fields are private final

    // TODO constructor

    /**
     * the constructor of the Edge
     *
     * @param source the source of the Edge
     * @param target the target of the Edge
     * @param weight the weight of the Edge
     */
    Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    // TODO checkRep

    /**
     * check the representation exposure
     */
    public void checkRep() {
        assert weight > 0;
    }

    // TODO methods

    /**
     * Get the source of the Edge
     *
     * @return return the label of the source of the Edge
     */
    public L getSource() {
        return source;
    }


    /**
     * Get the target of the Edge
     *
     * @return return the label of the target of the Edge
     */
    public L getTarget() {
        return target;
    }

    /**
     * Get the weight of the Edge
     *
     * @return the weight of the Edge
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Set the weight of the Edge
     *
     * @param weight set the weight of the Edge with the param weight
     */
    public Edge<L> setWeight(int weight) {
        return new Edge<L>(source, target, weight);
    }

    // TODO toString()
    @Override
    public String toString() {
        return "Edge{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", weight=" + weight +
                '}';
    }
}
