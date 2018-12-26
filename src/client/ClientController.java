package client;

import java.io.Serializable;

public class ClientController implements Serializable {
	private String name = "Client";
	private int x, y;
	public boolean ok = false;
	public boolean restart = false;

	public ClientController() {
		x = 740;
		y = 210;
	}

	public String getName() {
		return this.name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
