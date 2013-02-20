package ircclient.gui;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class UserD {
	private static String nick;
	
	public static void setNick(String n) {
		nick=n;
		System.out.println("setNick");
	}
	
	public String getNick() {
		return(nick);
	}

	public static void saveInfo() {
		try {
			FileOutputStream userSaveFile = new FileOutputStream("user.dat");
			ObjectOutputStream os = new ObjectOutputStream(userSaveFile);
			
			os.writeObject(nick);
			os.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadInfo() {
		try {
			FileInputStream userSaveFile = new FileInputStream("user.dat");
			ObjectInputStream is = new ObjectInputStream(userSaveFile);
			
			nick=(String) is.readObject();
			is.close();
			System.out.println("works"); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}