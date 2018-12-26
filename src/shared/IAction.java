package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientController;
import server.ServerController;

public interface IAction extends Remote {

	public void send(String msg) throws RemoteException;

	public void setClient(IAction c) throws RemoteException;

	public IAction getClient() throws RemoteException;

	public void clientMove(ClientController client) throws RemoteException;

	public void serverMove(ServerController server) throws RemoteException;

	public void hello() throws RemoteException;

	public void setGameEventListener(GameEventListener instance) throws RemoteException;
}