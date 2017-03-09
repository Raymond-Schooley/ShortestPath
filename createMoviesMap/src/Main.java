import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This program parses the locations.list file (or part of it) 
 * from imdb.com into a map readable by the main program. 
 * It groups movies by shared filming location.
 * The file was downloaded from ftp://ftp.fu-berlin.de/pub/misc/movies/database/
 */
public class Main {

	static Map<String, ArrayList<String>> locmap = new HashMap<String, ArrayList<String>>();
	
	public static void main(String[] args) throws IOException {
		
		File infile = new File("locations.list");

		BufferedReader bufRdr = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"ISO-8859-1"));
		String line = null;
		
		FileWriter vertexWriter = new FileWriter("movies-vertex.txt");
		
		while((line = bufRdr.readLine()) != null) {
			if(line.contains("\t")) {
				String[] tabSplit = line.split("\t", -1);
				String movie = tabSplit[0];
				
				//remove any episode numbers in {}
				movie = movie.replaceAll("\\{.*?\\} ?", "");
				String location;
				
				//remove unnecessary locations details in () 
				if (tabSplit[tabSplit.length-1].contains("(")) {
					location = tabSplit[tabSplit.length-2];
				} else {
					location = tabSplit[tabSplit.length-1];
				}
				if (!locmap.containsKey(location)) {
					locmap.put(location, new ArrayList<String>());
				}
				
				boolean duplicate = false;
				for (String existingMovie : locmap.get(location)) {
					if (movie.equals(existingMovie)) {
						duplicate = true;
						break;
					}
				}
				if (!duplicate) {
					locmap.get(location).add(movie);
					vertexWriter.write(movie + "\n");
				}
			}
		}
		
		FileWriter edgeWriter = new FileWriter("movies-edge.txt");
		
		for (String location : locmap.keySet()) {
			ArrayList<String> movies_in_loc = locmap.get(location);
			for (String movie1 : movies_in_loc) {
				for (String movie2 : movies_in_loc) {
					if (movie1.equals(movie2)) continue;
					edgeWriter.write(movie1 + "\n");
					edgeWriter.write(movie2 + "\n");
					edgeWriter.write("1\t" + location + "\n");
				}
			}
		}
		edgeWriter.flush();
		vertexWriter.flush();
		edgeWriter.close();
		vertexWriter.close();
		bufRdr.close();
	}

}
