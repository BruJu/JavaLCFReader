package random;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lecture {

	
	public List<Byte> mapToBytes(String chemin) {
		List<Byte> bytes;
		
		try {
			File file = new File(chemin);
			FileInputStream stream = new FileInputStream(file);
			
			bytes = new ArrayList<>();
			
			while (true) {
				int byteLu = stream.read();
				
				if (byteLu == -1)
					break;
				
				bytes.add((byte) byteLu);
			}
			
			stream.close();
			
			return bytes;
		} catch (IOException e) {
			return null;
		}
	}
}
