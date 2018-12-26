package server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JFrame;

import client.ClientController;
import client.PongClient;
import shared.Ball;
import shared.Configuration;
import shared.GameEventListener;
import shared.IAction;

public class PongServer extends JFrame implements KeyListener, Runnable, WindowListener, GameEventListener {
	private Font font1 = new Font("TimesRoman", Font.BOLD, 90);
	private Font font2 = new Font("TimesRoman", Font.BOLD, 50);
	private Font font3 = new Font("TimesRoman", Font.BOLD, 32);
	private Font font4 = new Font("TimesRoman", Font.BOLD, 18);
	
	private static final int WIDTH = Configuration.WINDOW_WIDTH;
	private static final int HEIGHT = Configuration.WINDOW_HEIGHT;
	private int ballVelocity = Configuration.BALL_VELOCITY;
	private int barWidth = Configuration.BAR_WIDTH;
	private int barHeight = Configuration.BAR_HEIGHT;
	private int winScore = Configuration.WINSCORE;
	private int verticalPadding = Configuration.VERTICAL_PADDING;
	
	boolean check = true;
	private boolean restart = false;
	private boolean restartEnabled = false;
	private Graphics graphic;
	private String[] message;

	Ball movingBall;
	private Thread movingBallThread;
	private ServerController serverController;
	private ClientController clientController;
	private IAction client;
	private IAction server;

	public PongServer() {
		serverController = new ServerController();
		clientController = new ClientController();
		this.setTitle("Server");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		movingBall = new Ball(serverController.getBallX(), serverController.getBallY(), ballVelocity, ballVelocity,
				Configuration.BALL_RADIUS, WIDTH, HEIGHT);
		addKeyListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			server = new Action();
			LocateRegistry.createRegistry(Configuration.port);
			Naming.bind(Configuration.getUrlConfiguration(), server);
			server.setGameEventListener(this);
			System.out.println("Waiting for connection...");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void startGame() {
		try {
			client = server.getClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		boolean notchecked = true;
		movingBallThread = new Thread(movingBall);
		while (true) {
			if (serverController.getPlayerScore() >= winScore
					|| serverController.getScoreS() >= winScore && restart == false) {

				if (serverController.getScoreS() > serverController.getPlayerScore()) {
					serverController.setOmessage("Won               Loss-Play Again: Press any key || Exit: Esc|N");
					serverController.setImessage("Won               Loss-Play again? ");
					restart = true;
				} else {
					serverController.setImessage("Loss              Won-Play Again: Press any key || Exit: Esc|N");
					serverController.setOmessage("Loss              Won-Play Again: Press any key || Exit: Esc|N");
					restart = true;
				}
				movingBallThread.suspend();
			}
			if (clientController.ok && notchecked) {
				serverController.setImessage("");
				movingBallThread.start();
				notchecked = false;
			}
			updateBall();
			// ObjectInputStream getObj = new
			// ObjectInputStream(clientSoc.getInputStream());
			// playerC = (PlayerClient) getObj.readObject();
			// getObj = null;
			// ObjectOutputStream sendObj = new
			// ObjectOutputStream(clientSoc.getOutputStream());
			// sendObj.writeObject(playerS);
			// sendObj = null;
			if (restartEnabled) {
				if (clientController.restart) {
					serverController.setPlayerScore(0);
					serverController.setServerScore(0);
					serverController.setOmessage("");
					serverController.setImessage("");
					restart = false;
					serverController.setRestart(false);
					serverController.setBallX(380);
					serverController.setBallY(230);
					movingBall.setX(380);
					movingBall.setY(230);
					movingBallThread.resume();
					restartEnabled = false;
				}
			}
			try {
				client.serverMove(serverController);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}
	}

	private Image createImage() {
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphic = bufferedImage.createGraphics();
		graphic.setColor(new Color(15, 9, 9));
		graphic.fillRect(0, 0, WIDTH, HEIGHT);
		graphic.setColor(Color.white);
		graphic.fillRect(WIDTH / 2 - 5, 0, 5, HEIGHT);
		graphic.fillRect(WIDTH / 2 + 5, 0, 5, HEIGHT);
		graphic.setFont(font1);
		graphic.setColor(new Color(228, 38, 36));
		graphic.drawString("" + serverController.getScoreS(), WIDTH / 2 - 60, 120);
		graphic.drawString("" + serverController.getPlayerScore(), WIDTH / 2 + 15, 120);
		graphic.setFont(font3);
		graphic.setColor(Color.white);
		graphic.drawString(serverController.getName(), WIDTH / 10, HEIGHT - 20);
		graphic.drawString(clientController.getName(), 600, HEIGHT - 20);
		graphic.setColor(new Color(57, 181, 74));
		graphic.fillRect(serverController.getX(), serverController.getY(), barWidth, barHeight);
		graphic.setColor(new Color(57, 181, 74));
		graphic.fillRect(clientController.getX(), clientController.getY(), barWidth, barHeight);
		graphic.setColor(new Color(255, 255, 255));
		graphic.fillOval(serverController.getBallX(), serverController.getBallY(), 45, 45);
		graphic.setColor(new Color(228, 38, 36));
		graphic.fillOval(serverController.getBallX() + 5, serverController.getBallY() + 5, 45 - 10, 45 - 10);
		message = serverController.getImessage().split("-");
		graphic.setFont(font2);
		graphic.setColor(Color.white);
		if (message.length != 0) {
			graphic.drawString(message[0], WIDTH / 4 - 31, HEIGHT / 2 + 38);
			if (message.length > 1) {
				if (message[1].length() > 6) {
					graphic.setFont(font4);
					graphic.setColor(new Color(228, 38, 36));
					graphic.drawString(message[1], WIDTH / 4 - 31, HEIGHT / 2 + 100);
				}
			}
		}
		return bufferedImage;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(createImage(), 0, 0, this);
	}

	public void updateBall() {
		checkCol();
		serverController.setBallX(movingBall.getX());
		serverController.setBallY(movingBall.getY());

	}

	public void playerUP() {
		if (serverController.getY() - verticalPadding > barHeight / 2 - 10) {
			serverController.setY(serverController.getY() - verticalPadding);
			try {
				client.serverMove(serverController);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void playerDOWN() {
		if (serverController.getY() + verticalPadding < HEIGHT - barHeight - 30) {
			serverController.setY(serverController.getY() + verticalPadding);
			try {
				client.serverMove(serverController);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkCol() {
		if (serverController.getBallX() < clientController.getX()
				&& serverController.getBallX() > serverController.getX()) {
			check = true;
		}
		if (serverController.getBallX() > clientController.getX() && check) {
			serverController.setServerScore(serverController.getScoreS() + 1);
			check = false;
		} else if (serverController.getBallX() <= serverController.getX() && check) {
			serverController.setPlayerScore(serverController.getPlayerScore() + 1);
			check = false;
		}
		if (movingBall.getX() <= serverController.getX() + barWidth
				&& movingBall.getY() + movingBall.getRadius() >= serverController.getY()
				&& movingBall.getY() <= serverController.getY() + barHeight) {
			movingBall.setX(serverController.getX() + barWidth);
			serverController.setBallX(serverController.getX() + barWidth);
			movingBall.setXv(movingBall.getXv() * -1);
		}
		if (movingBall.getX() + movingBall.getRadius() >= clientController.getX()
				&& movingBall.getY() + movingBall.getRadius() >= clientController.getY()
				&& movingBall.getY() <= clientController.getY() + barHeight) {
			movingBall.setX(clientController.getX() - movingBall.getRadius());
			serverController.setBallX(clientController.getX() - movingBall.getRadius());
			movingBall.setXv(movingBall.getXv() * -1);
		}

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int keycode = arg0.getKeyCode();
		if (keycode == KeyEvent.VK_UP) {
			playerUP();
			repaint();
		}
		if (keycode == KeyEvent.VK_DOWN) {
			playerDOWN();
			repaint();
		}
		if (restart == true) {
			restartEnabled = true;
			serverController.setRestart(true);
		}
		if (keycode == KeyEvent.VK_N || keycode == KeyEvent.VK_ESCAPE && restart == true) {
			this.setVisible(false);
			System.exit(EXIT_ON_CLOSE);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(1);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		System.exit(1);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void onGameStarted() {
		this.startGame();
		try {
			client.hello();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onServerMove(ServerController server) {

	}

	@Override
	public void onClientMove(ClientController client) {
		this.clientController = client;
		repaint();
	}
}
