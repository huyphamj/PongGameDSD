package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import client.ClientController;
import shared.GameEventListener;
import shared.IAction;

public class Action extends UnicastRemoteObject implements IAction {
	private GameEventListener listener;
	public IAction client = null;

	public Action() throws RemoteException {
	}

	public void setClient(IAction c) throws RemoteException {
		client = c;
	}

	public IAction getClient() throws RemoteException {
		return client;
	}

	public void send(String s) throws RemoteException {
		System.out.println(s);
	}


	public void clientMove(ClientController client) throws RemoteException{
		listener.onClientMove(client);
	}

	public void serverMove(ServerController server) throws RemoteException{
		listener.onServerMove(server);
	}

	@Override
	public void hello() throws RemoteException {
		listener.onGameStarted();
	}

	public void setGameEventListener(GameEventListener instance) throws RemoteException {
		this.listener = instance;
	}
}
