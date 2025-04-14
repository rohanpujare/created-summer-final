package graphs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
/**
 * Class providing test cases for Graph implementation of our {@link Graph} class.
 *
 * @see Graph
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GraphTests {

    @Test
    @DisplayName("Testing that the graph correctly obtains the 3 paths with the least transfers")
    void testTransitPaths(){
        Graph graph = new Graph();
        List<String> p372 = new ArrayList<>();
        p372.add("A"); p372.add("C"); p372.add("D"); p372.add("E"); p372.add("I"); p372.add("H");
        List<Double> p372Time = new ArrayList<>();
        p372Time.add(2.0); p372Time.add(0.5); p372Time.add(1.0); p372Time.add(1.0); p372Time.add(3.0);
        List<String> p32 = new ArrayList<>();
        p32.add("A"); p32.add("B"); p32.add("H");
        List<Double> p32Time = new ArrayList<>();
        p32Time.add(1.0); p32Time.add(2.0);
        List<String> p556 = new ArrayList<>();
        p556.add("B"); p556.add("D"); p556.add("E"); p556.add("F"); p556.add("G");
        List<Double> p556Time = new ArrayList<>();
        p556Time.add(2.0); p556Time.add(1.0); p556Time.add(2.0); p556Time.add(1.0);
        List<String> p1Line = new ArrayList<>();
        p1Line.add("B"); p1Line.add("G");
        List<Double> p1LineTime = new ArrayList<>();
        p1LineTime.add(3.0);
        graph.addTransitRoute(p372, "372", p372Time);
        graph.addTransitRoute(p32, "32", p32Time);
        graph.addTransitRoute(p556, "556", p556Time);
        graph.addTransitRoute(p1Line, "1Line", p1LineTime);
        List<List<String>> actual = graph.getTransitPaths("A", "G");
        List<String> p1 = new ArrayList<>();
        p1.add("A"); p1.add("B"); p1.add("G"); p1.add("32"); p1.add("1Line");
        List<String> p2 = new ArrayList<>();
        p2.add("A"); p2.add("C"); p2.add("D"); p2.add("E"); p2.add("F"); p2.add("G"); p2.add("372"); p2.add("556");
        List<String> p3 = new ArrayList<>();
        p3.add("A"); p3.add("B"); p3.add("D"); p3.add("E"); p3.add("F"); p3.add("G"); p3.add("32"); p3.add("556");
        assertTrue(actual.contains(p1) && actual.contains(p2) && actual.contains(p3) && actual.size() == 3);
    }

    @Test
    @DisplayName("Testing the allPaths function to see if all paths are correctly returned. " +
            "This test can be used as a halfway point for getTransitPaths!")
    void testAllPaths(){
        Graph graph = new Graph();
        List<String> p372 = new ArrayList<>();
        p372.add("A"); p372.add("C"); p372.add("D"); p372.add("E"); p372.add("I"); p372.add("H");
        List<Double> p372Time = new ArrayList<>();
        p372Time.add(2.0); p372Time.add(0.5); p372Time.add(1.0); p372Time.add(1.0); p372Time.add(3.0);
        List<String> p32 = new ArrayList<>();
        p32.add("A"); p32.add("B"); p32.add("H");
        List<Double> p32Time = new ArrayList<>();
        p32Time.add(1.0); p32Time.add(2.0);
        List<String> p556 = new ArrayList<>();
        p556.add("B"); p556.add("D"); p556.add("E"); p556.add("F"); p556.add("G");
        List<Double> p556Time = new ArrayList<>();
        p556Time.add(2.0); p556Time.add(1.0); p556Time.add(2.0); p556Time.add(1.0);
        List<String> p1Line = new ArrayList<>();
        p1Line.add("B"); p1Line.add("G");
        List<Double> p1LineTime = new ArrayList<>();
        p1LineTime.add(3.0);
        graph.addTransitRoute(p372, "372", p372Time);
        graph.addTransitRoute(p32, "32", p32Time);
        graph.addTransitRoute(p556, "556", p556Time);
        graph.addTransitRoute(p1Line, "1Line", p1LineTime);
        List<List<String>> expected = new ArrayList<>();
        List<String> f = new ArrayList<>(); f.add("A"); f.add("B"); f.add("G"); expected.add(f);
        List<String> a = new ArrayList<>(); a.add("A"); a.add("C"); a.add("D"); a.add("B"); a.add("G"); expected.add(a);
        List<String> b = new ArrayList<>(); b.add("A"); b.add("C"); b.add("D"); b.add("E"); b.add("F"); b.add("G"); expected.add(b);
        List<String> c = new ArrayList<>(); c.add("A"); c.add("C"); c.add("D"); c.add("E"); c.add("F"); c.add("G"); expected.add(c);
        List<String> d = new ArrayList<>(); d.add("A"); d.add("B"); d.add("D"); d.add("E"); d.add("F"); d.add("G"); expected.add(d);
        List<String> e = new ArrayList<>(); e.add("A"); e.add("B"); e.add("D"); e.add("E"); e.add("F"); e.add("G"); expected.add(e);
        List<String> g = new ArrayList<>(); g.add("A"); g.add("B"); g.add("H"); g.add("I"); g.add("E"); g.add("F"); g.add("G"); expected.add(g);
        List<String> h = new ArrayList<>(); h.add("A"); h.add("C"); h.add("D"); h.add("E"); h.add("I"); h.add("H"); h.add("B"); h.add("G"); expected.add(h);
        List<String> i = new ArrayList<>(); i.add("A"); i.add("C"); i.add("D"); i.add("E"); i.add("I"); i.add("H"); i.add("B"); i.add("G"); expected.add(i);
        List<String> j = new ArrayList<>(); j.add("A"); j.add("C"); j.add("D"); j.add("B"); j.add("H"); j.add("I"); j.add("E"); j.add("F"); j.add("G"); expected.add(j);
        List<List<String>> actual = graph.allPaths("A", "G");
        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    @DisplayName("Testing tie-breaking for getTransitPaths is working correctly")
    void testTieBreaking(){
        Graph graph = new Graph();
        List<String> p372 = new ArrayList<>();
        p372.add("A"); p372.add("C"); p372.add("D"); p372.add("E"); p372.add("I"); p372.add("H");
        List<Double> p372Time = new ArrayList<>();
        p372Time.add(2.0); p372Time.add(0.5); p372Time.add(1.0); p372Time.add(1.0); p372Time.add(3.0);
        List<String> p32 = new ArrayList<>();
        p32.add("A"); p32.add("B"); p32.add("H");
        List<Double> p32Time = new ArrayList<>();
        p32Time.add(1.0); p32Time.add(2.0);
        List<String> p556 = new ArrayList<>();
        p556.add("B"); p556.add("D"); p556.add("E"); p556.add("F"); p556.add("G");
        List<Double> p556Time = new ArrayList<>();
        p556Time.add(2.0); p556Time.add(1.0); p556Time.add(2.0); p556Time.add(1.0);
        List<String> p1Line = new ArrayList<>();
        p1Line.add("B"); p1Line.add("G");
        List<Double> p1LineTime = new ArrayList<>();
        p1LineTime.add(3.0);
        graph.addTransitRoute(p372, "372", p372Time);
        graph.addTransitRoute(p32, "32", p32Time);
        graph.addTransitRoute(p556, "556", p556Time);
        graph.addTransitRoute(p1Line, "1Line", p1LineTime);
        List<List<String>> testing = graph.getTransitPaths("A", "F");
        List<List<String>> expected = new ArrayList<>();
        List<String> p1 = new ArrayList<>();
        p1.add("A"); p1.add("C"); p1.add("D"); p1.add("E"); p1.add("F"); p1.add("372"); p1.add("556");
        List<String> p2 = new ArrayList<>();
        p2.add("A"); p2.add("B"); p2.add("D"); p2.add("E"); p2.add("F"); p2.add("32"); p2.add("556");
        List<String> p3 = new ArrayList<>();
        p3.add("A"); p3.add("B"); p3.add("G"); p3.add("F"); p3.add("32"); p3.add("1Line"); p3.add("556");
        expected.add(p1); expected.add(p2); expected.add(p3);
        assertTrue(expected.equals(testing));
    }

    @Test
    @DisplayName("Testing that the graph correctly obtains the shortest path from start to destination with a coffee shop")
    void testCoffeeStops(){
        Graph graph = new Graph();
        List<String> p372 = new ArrayList<>();
        p372.add("A"); p372.add("C"); p372.add("D"); p372.add("E"); p372.add("I"); p372.add("H");
        List<Double> p372Time = new ArrayList<>();
        p372Time.add(2.0); p372Time.add(0.5); p372Time.add(1.0); p372Time.add(1.0); p372Time.add(3.0);
        List<String> p32 = new ArrayList<>();
        p32.add("A"); p32.add("B"); p32.add("H");
        List<Double> p32Time = new ArrayList<>();
        p32Time.add(1.0); p32Time.add(2.0);
        List<String> p556 = new ArrayList<>();
        p556.add("B"); p556.add("D"); p556.add("E"); p556.add("F"); p556.add("G");
        List<Double> p556Time = new ArrayList<>();
        p556Time.add(2.0); p556Time.add(1.0); p556Time.add(2.0); p556Time.add(1.0);
        List<String> p1Line = new ArrayList<>();
        p1Line.add("B"); p1Line.add("G");
        List<Double> p1LineTime = new ArrayList<>();
        p1LineTime.add(3.0);
        graph.addTransitRoute(p372, "372", p372Time);
        graph.addTransitRoute(p32, "32", p32Time);
        graph.addTransitRoute(p556, "556", p556Time);
        graph.addTransitRoute(p1Line, "1Line", p1LineTime);
        Set<String> coffeeStops = new HashSet<>();
        coffeeStops.add("C"); coffeeStops.add("I"); coffeeStops.add("H");
        List<String> actual = graph.getShortestCoffeePath("A", "G", coffeeStops);
        List<String> expected = new ArrayList<>();
        expected.add("A"); expected.add("C"); expected.add("D"); expected.add("E"); expected.add("F"); expected.add("G");
        expected.add("372"); expected.add("556");
        assertTrue(expected.equals(actual));
    }

    @Test
    @DisplayName("Testing the shortest paths method. This can be used as the halfway point for getShortestCoffeePath!")
    void testShortestPath(){
        Graph graph = new Graph();
        List<String> p372 = new ArrayList<>();
        p372.add("A"); p372.add("C"); p372.add("D"); p372.add("E"); p372.add("I"); p372.add("H");
        List<Double> p372Time = new ArrayList<>();
        p372Time.add(2.0); p372Time.add(0.5); p372Time.add(1.0); p372Time.add(1.0); p372Time.add(3.0);
        List<String> p32 = new ArrayList<>();
        p32.add("A"); p32.add("B"); p32.add("H");
        List<Double> p32Time = new ArrayList<>();
        p32Time.add(1.0); p32Time.add(2.0);
        List<String> p556 = new ArrayList<>();
        p556.add("B"); p556.add("D"); p556.add("E"); p556.add("F"); p556.add("G");
        List<Double> p556Time = new ArrayList<>();
        p556Time.add(2.0); p556Time.add(1.0); p556Time.add(2.0); p556Time.add(1.0);
        List<String> p1Line = new ArrayList<>();
        p1Line.add("B"); p1Line.add("G");
        List<Double> p1LineTime = new ArrayList<>();
        p1LineTime.add(3.0);
        graph.addTransitRoute(p372, "372", p372Time);
        graph.addTransitRoute(p32, "32", p32Time);
        graph.addTransitRoute(p556, "556", p556Time);
        graph.addTransitRoute(p1Line, "1Line", p1LineTime);
        List<String> expected = new ArrayList<>();
        expected.add("A"); expected.add("B"); expected.add("G");
        assertTrue(expected.equals(graph.shortestPath("A", "G")));
        List<String> secExpected = new ArrayList<>();
        secExpected.add("B"); secExpected.add("D"); secExpected.add("E"); secExpected.add("I");
        assertTrue(secExpected.equals(graph.shortestPath("B", "I")));
    }
}
