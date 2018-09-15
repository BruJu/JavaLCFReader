package random;

public class BytePrinter {
	private static int id = 0;
	
	public static void printByte(byte octet, char prefixe) {
		System.out.print(prefixe + String.format("%02X", octet) + " ");
		
		id++;
		
		if (id == 16) {
			id = 0;
			System.out.println();
		}
	}
	
	
}
