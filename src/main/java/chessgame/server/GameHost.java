package chessgame.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import chessgame.app.Game;
import chessgame.server.pings.*;

public class GameHost implements IClient{
	Client client;
	
	public GameHost(Game game) throws IOException{
		this.client = new Client();
		this.client.start();
		
		Network.register(client);
		this.client.connect(5000, "localhost", 54555);
		
		client.addListener(new Listener() {
		       public void received (Connection connection, Object object) {
		          if (object instanceof HashMap || object instanceof List || object instanceof PausePing || object instanceof NextMapPing || object instanceof FinishedPing || object instanceof P2WaitingPing) {
		             if (game.netHandler != null)
		            	 game.netHandler.handlePacket(object, game);
		             
		          }
		       }
		    });
	}
	
	public Client getClient(){
		return this.client;
	}
}
