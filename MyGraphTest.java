import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;



public class MyGraphTest {

	private MyGraph myTestGraph;
	
	@Before
	public void setUp() throws FileNotFoundException {
		Scanner in = null;
		try {
			    in = new Scanner(new File("testVertex1.txt"));
		} catch(FileNotFoundException e) {
			System.err.println("File not found for vertex");
			System.exit(2);
		}
		
		Collection<Vertex> v = new ArrayList<Vertex>();
		while (in.hasNext()) 
			v.add(new Vertex(in.next()));
		
		try {
			in = new Scanner(new File("testEdge1.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("File not found for edges.");
			System.exit(2);;
		}
		
		Collection<Edge> e = new ArrayList<Edge>();
		while (in.hasNext()) {
			try {
				Vertex v1 = new Vertex(in.next());
				Vertex v2 = new Vertex(in.next());
				int weight = in.nextInt();
				e.add(new Edge(v1, v2, weight));
			} catch (NoSuchElementException exc) {
				System.err.println("Bad File Format");
				System.exit(3);
			}
		}
		myTestGraph = new MyGraph(v, e);
	}
/**
 * Test to make sure the graph is created and that it is a graph.
 */
	@Test
	public void testMyGraph() {
		assertNotNull(myTestGraph);
		assertTrue(myTestGraph instanceof Graph);
	}

	/**
	 * Test to make sure the correct vertices are stored in the graph
	 */
	@Test
	public void testVertices1() {
		assertEquals("testVertices1 Failed", "[A, B, C, D, E, F, G]"
				, myTestGraph.vertices().toString());;
	}

	@Test
	public void testEdges1() {
		assertEquals("testEdges1 has failed", "[<A, B, 2>, <A, D, 5>, <A, C, 4>"
				+ ", <B, C, 1>, <B, D, 4>, <B, E, 3>, <C, E, 1>, "
				+ "<D, F, 1>, <G, E, 7>]", myTestGraph.edges().toString());
	}

	/**
	 * Test the most basic path consisting of 2 nodes.
	 */
	@Test
	public void testShortestPath1() {
		Vertex v1 = new Vertex("A");
		Vertex v2 = new Vertex("B");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertEquals("SP has failed in 2 node example",
				2, shortestPath.cost);
	}

	/**
	 * Test that the graph can choose to travel a less direct path because it 
	 * discovers there is less weight.
	 */
	@Test
	public void testShortestPath() {
		Vertex v1 = new Vertex("A");
		Vertex v2 = new Vertex("C");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertEquals("SP has fail for more stops less weight scenario",
				3, shortestPath.cost);
	}
	/**
	 * Test to make sure graph will return null when ask to reach an unreachable
	 * node.
	 */
	@Test
	public void testNullPath1() {
		Vertex v1 = new Vertex("C");
		Vertex v2 = new Vertex("A");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertNull(shortestPath);	
	}
	/**
	 * Test a slightly more complicated unreachable node.
	 */
	@Test
	public void testNullPath2() {
		Vertex v1 = new Vertex("A");
		Vertex v2 = new Vertex("G");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertNull(shortestPath);	
	}

}
