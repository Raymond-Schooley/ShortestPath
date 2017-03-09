import java.util.*;
import java.io.*;

/**
 * Driver program that reads in a graph and prompts user for shortests paths in the graph.
 * (Intentionally without comments.  Read through the code to understand what it does.)
 */

public class FindPaths {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("USAGE: java Paths <vertex_file> <edge_file>");
			System.exit(1);
		}

		MyGraph g = readGraph(args[0],args[1]);

		@SuppressWarnings("resource")
		Scanner console = new Scanner(System.in);
		Collection<Vertex> v = g.vertices();
		Collection<Edge> e = g.edges();
        
		if (e.size() < 50) {
			System.out.println("Vertices are "+v);
			System.out.println("Edges are "+e);
		} else {
			System.out.println("Vertices size: " + v.size());
			System.out.println("Edges size: " + e.size());
		}
		
		while(true) {
			System.out.print("Start vertex? ");
			Vertex a = new Vertex(console.nextLine());
			if(!v.contains(a)) {
				System.out.println("no such vertex");
				System.exit(0);
			}
			
			System.out.print("Destination vertex? ");
			Vertex b = new Vertex(console.nextLine());
			if(!v.contains(b)) {
				System.out.println("no such vertex");
				System.exit(1);
			}
			
			// YOUR CODE HERE: call shortestPath and print
			// out the result
			Path shortestPath = g.shortestPath(a, b);
			System.out.println("Path is " + shortestPath.vertices);
			System.out.println("Weight is " + shortestPath.cost);
		}
	}

	public static MyGraph readGraph(String f1, String f2) {
		Scanner s = null;
		try {
			s = new Scanner(new File(f1));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: "+f1);
			System.exit(2);
		}

		Collection<Vertex> v = new ArrayList<Vertex>();
		while(s.hasNext()) {
			v.add(new Vertex(s.nextLine()));
		}
		try {
			s = new Scanner(new File(f2));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: "+f2);
			System.exit(2);
		}

		Collection<Edge> e = new ArrayList<Edge>();
		while(s.hasNext()) {
			try {
				Vertex a = new Vertex(s.nextLine());
				Vertex b = new Vertex(s.nextLine());
				String weightStr = s.nextLine();
				String[] tabSplit = weightStr.split("\t", -1);
				int w = Integer.parseInt(tabSplit[0]);
				String vertexDesc = "";
				if (tabSplit.length > 1) {
					vertexDesc = tabSplit[1];
				}
				Edge newedge = new Edge(a,b,w);
				newedge.description = vertexDesc;
				e.add(newedge);
			} catch (NoSuchElementException e2) {
				e2.printStackTrace();
				System.err.println("EDGE FILE FORMAT INCORRECT");
				System.exit(3);
			}
		}

		return new MyGraph(v,e);
	}
}
