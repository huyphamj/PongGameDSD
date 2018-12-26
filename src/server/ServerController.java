package server;

import java.io.Serializable;

public class ServerController implements Serializable {
	private String name = "Server";
	private int x, y;
	private int ballX, ballY;
	private int serverScore = 0;
	private int playerScore = 0;
	private String imessage = "";
	private String omessage = "";
	private boolean restart = false;

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public ServerController() {
		x = 50;
		y = 200;
		ballX = 380;
		ballY = 230;
	}

	public String getName() {
		return name;
	}

	public String getImessage() {
		return imessage;
	}

	public void setImessage(String imessage) {
		this.imessage = imessage;
	}

	public String getOmessage() {
		return omessage;
	}

	public void setOmessage(String omessage) {
		this.omessage = omessage;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getBallX() {
		return ballX;
	}

	public void setBallX(int ballx) {
		this.ballX = ballx;
	}

	public int getBallY() {
		return ballY;
	}

	public void setBallY(int bally) {
		this.ballY = bally;
	}

	public int getScoreS() {
		return serverScore;
	}

	public void setServerScore(int serverScore) {
		this.serverScore = serverScore;
	}

	public int getPlayerScore() {
		return playerScore;
	}

	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}
}
