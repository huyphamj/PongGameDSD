package shared;

import client.ClientController;
import server.ServerController;

public interface GameEventListener {
	public void onGameStarted();

	public void onServerMove(ServerController server);

	public void onClientMove(ClientController client);
}
