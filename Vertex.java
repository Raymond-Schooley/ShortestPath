/**
 * Representation of a graph vertex
 */
public class Vertex implements Comparable<Object> {
	private String label;
	
	// values for calculating shortest path
	public boolean known;
	public Vertex prev;
	public int weight;
	
	//used only for movie location application
	public String connectorDesc = "";

	/**
	 * Construct a new vertex
	 * 
	 * @param label
	 *            the label attached to this vertex
	 */
	public Vertex(String label) {
		if (label == null)
			throw new IllegalArgumentException("null");
		this.label = label;
		resetTempVars();
	}
	
	public void resetTempVars() {
		known = false;
		prev = null;
		weight = Integer.MAX_VALUE;
	}

	/**
	 * Get a vertex label
	 * 
	 * @return the label attached to this vertex
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * A string representation of this object
	 * 
	 * @return the label attached to this vertex
	 */
	public String toString() {
		if (connectorDesc.isEmpty()) {
			return label;
		} else {
			return "\n\t<<<connected by " + connectorDesc + ">>>\n" + label;
		}
	}

	// auto-generated: hashes on label
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	// auto-generated: compares labels
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vertex other = (Vertex) obj;
		if (label == null) {
			return other.label == null;
		} else {
			return label.equals(other.label);
		}
	}

	public int compareTo(Object arg0) {
		return Integer.compare(weight, ((Vertex)arg0).weight);
	}

}
