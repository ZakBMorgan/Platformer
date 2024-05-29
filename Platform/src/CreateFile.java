import java.io.File;

public class CreateFile {
	public static void main(String[] args) {
		try {
			File file = new File("distance.txt");
			if(file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (java.io.IOException e) {
			System.out.println("An error occured.");
			e.printStackTrace();
		}
	}
}
