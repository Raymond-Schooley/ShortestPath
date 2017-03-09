import java.util.*;

/**
 * A representation of a graph. Assumes that we do not have negative cost edges
 * in the graph.
 */
public class MyGraph implements Graph {
	// you will need some private fields to represent the graph
	// you are also likely to want some private helper methods

	private Map<Vertex, ArrayList<Edge>> adjacencyMap = new HashMap<Vertex, ArrayList<Edge>>();

	public MyGraph(Collection<Vertex> newVertices, Collection<Edge> newEdges) {
		for(Vertex newVertex: newVertices) {
			adjacencyMap.put(newVertex, new ArrayList<Edge>());
		}
		for (Edge newEdge: newEdges) {
			adjacencyMap.get(newEdge.getSource()).add(newEdge);
		}

		// Check for exceptions.
		checkEdgeExceptions();
	}

	/** Checking edge for exceptions. */
	private void checkEdgeExceptions() {
		Set<Vertex> setOfVertex = adjacencyMap.keySet();
		Object theList[] = setOfVertex.toArray();

		// Obtain the set in order to check if there are repeats.
		for (int i = 0 ; i < theList.length; i++) {
			ArrayList<Edge> edgeArrays = adjacencyMap.get(theList[i]);

			// Temp placeholder values.
			Vertex previousSrc    = new Vertex("Temp");
			Vertex previousDest   = new Vertex("Temp");
			int    previousWeight = 0;

			// Iterate through everything to check.
			for (int j = 0 ; j < edgeArrays.size() ; j++) {
				Edge edge = edgeArrays.get(j);

				// Check for null.
				if (edge == null) {
					throw new NullPointerException();
				}

				// Check Weight if 0 or negative.
				int edgeWeight = edge.getWeight();
				if (edgeWeight <= 0) {
					try {
						throw new Exception("Weight is negative or 0");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// Check for self loop.
				Vertex currentSrc  = edge.getSource();
				Vertex currentDest = edge.getDestination();
				if (currentSrc.equals(currentDest)) {
					try {
						throw new Exception("Edge loops to self");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// Check repeating destination goes to the same destination with a different weight.
				if (previousSrc.equals(currentDest) && previousDest.equals(currentDest)) {
					// Check if weight differs.
					if (edgeWeight != previousWeight) {
						try {
							throw new Exception("Repeated direction with different weight");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				// Assign previous values for checking due to being in order.
				previousSrc    = currentSrc;
				previousDest   = currentDest;
				previousWeight = edgeWeight;
			}
		}
	}


	/**
	 * Return the collection of vertices of this graph
	 * 
	 * @return the vertices as a collection (which is anything iterable)
	 */
	//@Override
	public Collection<Vertex> vertices() {
		return adjacencyMap.keySet();
	}

	/**
	 * Return the collection of edges of this graph
	 * 
	 * @return the edges as a collection (which is anything iterable)
	 */
	//@Override
	public Collection<Edge> edges() {
		List<Edge> edges = new ArrayList<Edge>();
		for(Map.Entry<Vertex, ArrayList<Edge>> entry: adjacencyMap.entrySet()) {
			for (Edge e : entry.getValue()) {
				edges.add(e);
			}
		}
		return edges;
	}

	/**
	 * Return a collection of vertices adjacent to a given vertex v. i.e., the
	 * set of all vertices w where edges v -> w exist in the graph. Return an
	 * empty collection if there are no adjacent vertices.
	 * 
	 * @param v
	 *            one of the vertices in the graph
	 * @return an iterable collection of vertices adjacent to v in the graph
	 * @throws IllegalArgumentException
	 *             if v does not exist.
	 */
	//@Override
	public Collection<Vertex> adjacentVertices(Vertex v) {
		List<Vertex> adjVertices = new ArrayList<Vertex>();
		if (adjacencyMap.containsKey(v)) {
			ArrayList<Edge> edges = adjacencyMap.get(v);
			for (Edge e : edges) {
				adjVertices.add(e.getDestination());
			}
		}
		return adjVertices;
	}

	/**
	 * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed
	 * graph. Assumes that we do not have negative cost edges in the graph.
	 * 
	 * @param a
	 *            one vertex
	 * @param b
	 *            another vertex
	 * @return cost of edge if there is a directed edge from a to b in the
	 *         graph, return -1 otherwise.
	 * @throws IllegalArgumentException
	 *             if a or b do not exist.
	 */
	//@Override
	public int edgeCost(Vertex a, Vertex b) {
		int ret = -1;
		if (adjacencyMap.containsKey(a)) {
			for (Edge edge : adjacencyMap.get(a)) {
				if (edge.getDestination().equals(b))
					ret = edge.getWeight();
					break;
			}
		}
		return ret;
	}

	/**
	 * Returns the shortest path from a to b in the graph, or null if there is
	 * no such path. Assumes all edge weights are nonnegative. Uses Dijkstra's
	 * algorithm.
	 * 
	 * @param a
	 *            the starting vertex
	 * @param b
	 *            the destination vertex
	 * @return a Path where the vertices indicate the path from a to b in order
	 *         and contains a (first) and b (last) and the cost is the cost of
	 *         the path. Returns null if b is not reachable from a.
	 * @throws IllegalArgumentException
	 *             if a or b does not exist.
	 */
	public Path shortestPath(Vertex a, Vertex b) {
		List<Vertex> shortestPath = new ArrayList<Vertex>();
		Path ret;

		// If there is no path, return null.
		if (a.equals(null) || b.equals(null)) {
			return null;
		}
		
		if (a.equals(b)) {
			shortestPath.add(a);
			ret = new Path(shortestPath, 0);
		
		} else {
			a.priority = 0;
			PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
			queue.add(a);
			
			while (!queue.isEmpty()) {
				Vertex currentV = queue.poll();
				currentV.seen = true;

				for (Edge e: adjacencyMap.get(currentV)) {
					
					Vertex nextV = null;
					for (Vertex v : adjacencyMap.keySet()) {
						if (v.equals(e.getDestination())) {
							nextV = v;
							break;
						}
					}
					if (!nextV.seen && currentV.priority + e.getWeight() < nextV.priority) {
						nextV.priority = currentV.priority + e.getWeight();
						nextV.next = currentV;
						queue.offer(nextV);
						nextV.connectorDesc = e.description;
						if (nextV.equals(b)) {
							b.connectorDesc = e.description;
							b.next = nextV.next;
							b.priority = nextV.priority;
						}
					}
				}
			}

			for (Vertex v = b; v != null; v = v.next) {
				shortestPath.add(v);
			}
			Collections.reverse(shortestPath);
			ret = new Path(shortestPath, b.priority);
			
			// reset temp vertex values to defaults
			for (Vertex v : adjacencyMap.keySet()) {
				v.resetTempVars();
			}
		}
		return ret;
	}

}
