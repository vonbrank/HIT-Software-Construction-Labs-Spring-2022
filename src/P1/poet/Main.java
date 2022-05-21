/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;

/**
 * Example program using GraphPoet.
 *
 * <p>PS2 instructions: you are free to change this example class.
 */
public class Main {

    /**
     * Generate example poetry.
     *
     * @param args unused
     * @throws IOException if a poet corpus file cannot be found or read
     */
    public static void main(String[] args) throws IOException {
        final GraphPoet nimoy = new GraphPoet(new File("src/P1/poet/mugar-omni-theater.txt"));
        final String inputNimoy = "Test the system.";
        System.out.println(inputNimoy + "\n>>>\n" + nimoy.poem(inputNimoy));
        System.out.println();
        final GraphPoet starTrek = new GraphPoet(new File("src/P1/poet/where-no-man-has-gone-before.txt"));
        final String inputStarTrek = "Seek to explore new and exciting synergies!";
        System.out.println(inputStarTrek + "\n>>>\n" + starTrek.poem(inputStarTrek));
    }

}
