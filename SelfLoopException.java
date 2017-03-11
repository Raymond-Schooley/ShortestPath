/**
 * Exception thrown when some tries to create an edge that loops a vertex to itself.
 * @author Raymond Schooley, Walker Hertel, David Dean
 *
 */
public class SelfLoopException extends RuntimeException {
    
	private static final long serialVersionUID = 6793571402655375218L;

	public SelfLoopException(final String theString) {
    	super(theString);
    }
}
