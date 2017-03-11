import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;



public class TestGraph {

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
	public void testMyGraph1() {
		assertNotNull(myTestGraph);
		assertTrue(myTestGraph instanceof Graph);
	}
	
	/**
	 * Test creating a graph with redundant information.
	 */
	@Test
	public void testMyGraph2() {
		ArrayList<Vertex> verts = new ArrayList<>();
		ArrayList<Edge> edges = new ArrayList<>();
		Vertex v1 = new Vertex("test");
		Vertex v2 = new Vertex("TEST");
		Edge e1 = new Edge(v1, v2, 8);
		Edge e2 = new Edge(v1, v2, 8);
		verts.add(v1);
		verts.add(v2);
		verts.add(v1);
		edges.add(e1);
		edges.add(e2);
		edges.add(e1);
		MyGraph testGraph = new MyGraph(verts, edges);
		assertEquals("[TEST, test]", testGraph.vertices().toString());
		assertEquals("[<test, TEST, 8>]", testGraph.edges().toString());
		
	}
	
	
	/**
	 * Check that the we do not create a graph that has edges that refer to a
	 * vertex that is not in the graph.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCheckVertexExceptions() {
		Vertex v1 = new Vertex("A");
		Vertex v2 = new Vertex("I");
		Vertex v3 = new Vertex("B");
		Edge goodEdge= new Edge(v1, v2, 7);
		Edge badEdge = new Edge(v1, v3, 8);
		ArrayList<Vertex> verts = new ArrayList<>();
		verts.add(v1);
		verts.add(v2);
		ArrayList<Edge> edges = new ArrayList<>();
		edges.add(goodEdge);
		edges.add(badEdge);
		MyGraph testGraph = new MyGraph(verts, edges);
	}
	
	/**
	 * Check to make sure you can't create a graph with an edge weight that is 0
	 */
	@Test(expected = BadWeightException.class)
	public void testCheckExceptions1() {
		ArrayList<Vertex> testVert = new ArrayList<>();
		ArrayList<Edge> testEdge = new ArrayList<>();
		Vertex v1 = new Vertex("test");
		Vertex v2 = new Vertex("TEST");
		Edge e1 = new Edge(v1, v2, 0);
		testVert.add(v1);
		testVert.add(v2);
		testEdge.add(e1);
		MyGraph testGraph = new MyGraph(testVert, testEdge);
	}
	
	/**
	 * Check to make sure graph will not allow someone to create a graph with an
	 * edge that represents a vertex looping back to itself.
	 */
	@Test(expected = SelfLoopException.class)
	public void testCheckExceptions2() {
		ArrayList<Vertex> testVert = new ArrayList<>();
		ArrayList<Edge> testEdge = new ArrayList<>();
		Vertex v1 = new Vertex("test");
		Edge e1 = new Edge(v1, v1, 7);
		testVert.add(v1);
		testEdge.add(e1);
		MyGraph testGraph = new MyGraph(testVert, testEdge);
	}
	
	/**
	 * Check to make sure the graph doesn't allow inserting a duplicate edge with
	 * a new weight.
	 */
	@Test(expected = DifferentWeightException.class)
	public void testCheckException3() {
		ArrayList<Vertex> testVert = new ArrayList<>();
		ArrayList<Edge> testEdge = new ArrayList<>();
		Vertex v1 = new Vertex("test");
		Vertex v2 = new Vertex("TEST");
		Edge e1 = new Edge(v1, v2, 7);
		Edge e2 = new Edge(v1, v2, 10);
		testVert.add(v1);
		testVert.add(v2);
		testEdge.add(e1);
		testEdge.add(e2);
		
		MyGraph testGraph = new MyGraph(testVert, testEdge);
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
		assertEquals("testEdges1 has failed", "[<A, D, 5>, <A, C, 4>, <A, B, 2>"
				+ ", <B, D, 4>, <B, C, 1>, <B, E, 3>, <C, E, 1>, "
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
		assertEquals("SP has failed in 2 node example for cost",
				2, shortestPath.cost);
		assertEquals("SP has failed in 2 node example for path", "[A, B]"
				, shortestPath.vertices.toString());
	}

	/**
	 * Test that the graph can choose to travel a less direct path because it 
	 * discovers there is less weight.
	 */
	@Test
	public void testShortestPath2() {
		Vertex v1 = new Vertex("A");
		Vertex v2 = new Vertex("C");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertEquals("SP has fail for more stops less weight scenario for cost",
				3, shortestPath.cost);
		assertEquals("SP has fail for more stops less weight scenario for path",
				"[A, B, C]", shortestPath.vertices.toString());
	}
	
	/**
	 * Test that the graph can choose to travel a less direct path because it 
	 * discovers there is less weight.
	 */
	@Test
	public void testShortestPath3() {
		Vertex v1 = new Vertex("A");
		Vertex v2 = new Vertex("A");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertEquals("SP has fail for more stops less weight scenario for cost",
				0, shortestPath.cost);
		assertEquals("SP has fail for more stops less weight scenario for path",
				"[A]", shortestPath.vertices.toString());
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
	
	/**
	 * Test to make sure graph returns null when the source vertex is not in the 
	 * graph.
	 */
	@Test
	public void testNullPath3() {
		Vertex v1 = new Vertex("L");
		Vertex v2 = new Vertex("B");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertNull(shortestPath);
	}
	
	/**
	 * Test to make sure graph returns null when the destination vertex is not in the 
	 * graph.
	 */
	@Test
	public void testNullPath4() {
		Vertex v1 = new Vertex("B");
		Vertex v2 = new Vertex("L");
		Path shortestPath = myTestGraph.shortestPath(v1, v2);
		assertNull(shortestPath);
	}

}
