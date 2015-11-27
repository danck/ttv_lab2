package game;

import java.net.MalformedURLException;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.chord.service.ServiceException;

public class Joiner {

	public static void main(String[] args) throws InterruptedException {
		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		NotifyCallback cb = new GameCallback();	

		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		try {
			localURL = new URL(protocol + "://localhost:8081/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		URL bootstrapURL = null;
		try {
			bootstrapURL = new URL(protocol + "://localhost:8080/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		
		Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
		chord.setCallback(cb);
		
		try {
			chord.join(localURL, bootstrapURL);
		} catch (ServiceException e) {
			throw new RuntimeException("Could not join DHT", e);
		}
		
		while (true) {
			chord.broadcast(new ID(new byte[123]), true);
			Thread.sleep(2000);
		}
	}

}
