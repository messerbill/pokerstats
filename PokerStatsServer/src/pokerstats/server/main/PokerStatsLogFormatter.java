package pokerstats.server.main;

import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import pokerstats.server.gui.Lobby;

public class PokerStatsLogFormatter extends Formatter {
	    //
	    // Create a DateFormat to format the logger timestamp.
	    //
	    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
	 
	    public String format(LogRecord record) {
	        StringBuilder builder = new StringBuilder(1000);
	        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
	        builder.append("[").append(record.getSourceClassName()).append(".");
	        builder.append(record.getSourceMethodName()).append("] - ");
	        builder.append("[").append(record.getLevel()).append("] - ");
	        builder.append(formatMessage(record));
	        builder.append("\n");
	        if(Lobby.getUiSetup()) {
	        	Lobby.addLineToLog("-- "+formatMessage(record));
	        	Lobby.scroll.getViewport().setViewPosition(new Point(0,Lobby.textArea.getDocument().getLength()));
	        }
	        return builder.toString();
	    }
	 
	    public String getHead(Handler h) {
	        return super.getHead(h);
	    }
	 
	    public String getTail(Handler h) {
	        return super.getTail(h);
	    }
	
}
