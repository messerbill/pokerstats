package pokerstats.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class LoadPokerStarsFiles {
	
	public File maindir = new File("C:/Users/Messerbill_win/AppData/Local/PokerStars.EU/HandHistory/messerbill");
	public File[] files = maindir.listFiles();
	public long[] tabletimes;
	public String[] tablenames;
	public Vector<String> tableStrings = new Vector<String>();

	public LoadPokerStarsFiles(){
		this.loadFiles();
		for (String s : tablenames){
			this.parseTableFile(s);
		}
	}
	
	public void loadFiles(){
		this.maindir = new File("C:/Users/Messerbill_win/AppData/Local/PokerStars.EU/HandHistory/messerbill");
        this.files = maindir.listFiles();
        
        tabletimes = new long[files.length];
        tablenames = new String[files.length];
        
        for (int i = 0; i < files.length; i++){
        	tabletimes[i] = files[i].lastModified();
        }
        
        for (int i = 0; i < files.length; i++){
        	tablenames[i] = files[i].getName();
        }
	}
	
	public void parseTableFile(String txtfile){
		try {
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new FileReader("C:/Users/Messerbill_win/AppData/Local/PokerStars.EU/HandHistory/messerbill/"+txtfile));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				this.tableStrings.add(zeile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
