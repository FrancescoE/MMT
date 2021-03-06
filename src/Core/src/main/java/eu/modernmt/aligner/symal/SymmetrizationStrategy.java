package eu.modernmt.aligner.symal;

import java.io.Serializable;

/**
 * Created by davide on 20/05/16.
 */
public interface SymmetrizationStrategy extends Serializable {

    static SymmetrizationStrategy forName(String name) {
        String className = SymmetrizationStrategy.class.getPackage().getName() + "." + name + "Strategy";

        try {
            return (SymmetrizationStrategy) Class.forName(className).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | ClassCastException e) {
            throw new IllegalArgumentException("Invalid strategy name " + name, e);
        }
    }

    int[][] symmetrize(int[][] forward, int[][] backward);

}
