/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of Graph.
 *
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {

    private final List<Vertex<L>> vertices = new ArrayList<>();

    // Abstraction function:
    //   AF(vertices) = a graph with all vertices in the field vertices and edges such that the target of each edge is
    //   the key of a element in the field edges of each Vertex, and the weight of the edge is the value of the element
    // Representation invariant:
    //   Each Vertex in the list vertices has a unique label
    // Safety from rep exposure:
    //   vertices is mutable, so it makes a defensive copy as a return value in the method vertices()

    // TODO constructor

    // TODO checkRep
    public void checkRep() {
        Set<L> verticesLabels = new HashSet<>();
        for (Vertex<L> vertex : vertices) {
            assert !verticesLabels.contains(vertex.getLabel());
            vertex.checkRep();
            verticesLabels.add(vertex.getLabel());
        }
    }

    @Override
    public boolean add(L vertex) {
        for (Vertex<L> currentVertex : vertices) {
            if (currentVertex.getLabel().equals(vertex))
                return false;
        }
        vertices.add(new Vertex<L>(vertex));
        checkRep();
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        add(source);
        add(target);

        int previousWeight = 0;
        Vertex<L> sourceVertex = null;
        for (Vertex<L> vertex : vertices) {
            if (vertex.getLabel().equals(source)) {
                sourceVertex = vertex;
                break;
            }
        }
        if (sourceVertex != null) {
            previousWeight = sourceVertex.getWeight(target);
            if (weight == 0) sourceVertex.removeEdge(target);
            else sourceVertex.addEdge(target, weight);
        }
        checkRep();
        return previousWeight;
    }

    @Override
    public boolean remove(L vertex) {
        if (vertices.stream().noneMatch(currentVertex -> currentVertex.getLabel().equals(vertex)))
            return false;
        vertices.removeIf(currentVertex -> currentVertex.getLabel().equals(vertex));
        for (Vertex<L> currentVertex : vertices) {
            currentVertex.removeEdge(vertex);
        }
        checkRep();
        return true;
    }

    @Override
    public Set<L> vertices() {
        Set<L> res = new HashSet<>();
        vertices.forEach(vertex -> res.add(vertex.getLabel()));
        checkRep();
        return res;
    }

    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> res = new HashMap<>();
        vertices.forEach(vertex -> {
            int weight = vertex.getWeight(target);
            if (weight != 0) res.put(vertex.getLabel(), weight);
        });
        return res;
    }

    @Override
    public Map<L, Integer> targets(L source) {
        Vertex sourceVertex = null;
        for (Vertex vertex : vertices) {
            if (vertex.getLabel().equals(source)) {
                sourceVertex = vertex;
                break;
            }
        }
        if (sourceVertex == null) return new HashMap<>();
        return sourceVertex.getEdges();

    }

    // TODO toString()

}

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 *
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {

    // TODO fields
    private final L label;
    private final Map<L, Integer> edges;

    // Abstraction function:
    //   AF(label, edges) = a vertex with the label, and is the source of some edges such that for each edge whose
    //   weight is w and target is v, satisfying edges.put(v) equals w.
    // Representation invariant:
    //   The weight of each edge is greater than zero
    // Safety from rep exposure:
    //   L is immutable.
    //   Each method that returns a Map, we construct a new one making it satisfying the post condition, then return.

    // TODO constructor
    Vertex(L label) {
        this.label = label;
        edges = new HashMap<>();
    }

    // TODO checkRep
    public void checkRep() {
        edges.forEach((key, value) -> {
            assert value > 0;
        });
    }

    // TODO methods
    public L getLabel() {
        return label;
    }

    public void addEdge(L target, int weight) {
        edges.put(target, weight);
    }

    public int getWeight(L target) {
        for (Map.Entry<L, Integer> entry : edges.entrySet()) {
            if (entry.getKey().equals(target)) return entry.getValue();
        }
        return 0;
    }

    public int removeEdge(L target) {
        int previousWeight = 0;
        if (edges.containsKey(target)) {
            previousWeight = edges.get(target);
            edges.remove(target);
        }
        return previousWeight;
    }

    public Map<L, Integer> getEdges() {
        return new HashMap<>(edges);
    }

    // TODO toString()


    @Override
    public String toString() {
        return "Vertex{" +
                "label=" + label +
                ", edges=" + edges +
                '}';
    }
}
