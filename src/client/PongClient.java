package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import server.Action;
import server.ServerController;
import shared.Configuration;
import shared.GameEventListener;
import shared.IAction;

public class PongClient extends JFrame implements KeyListener, Runnable, WindowListener, GameEventListener {
	private Font sFont = new Font("TimesRoman", Font.BOLD, 90);
	private Font mFont = new Font("TimesRoman", Font.BOLD, 50);
	private Font nFont = new Font("TimesRoman", Font.BOLD, 32);
	private Font rFont = new Font("TimesRoman", Font.BOLD, 18);
	private static final String TITLE = "Client";
	private static final int WIDTH = Configuration.WINDOW_WIDTH;
	private static final int HEIGHT = Configuration.WINDOW_HEIGHT;
	private int barWidth = Configuration.BAR_WIDTH;
	private int barHeight = Configuration.BAR_HEIGHT;
	private int verticalPadding = Configuration.VERTICAL_PADDING;
	private Graphics g;

	private ServerController serverController;
	private ClientController clientController;
	private IAction server, client;

	private String[] message;

	public PongClient() {
		serverController = new ServerController();
		clientController = new ClientController();
		this.setTitle(TITLE);
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		addKeyListener(this);
	}

	@Override
	public void run() {
		try {
			server = (IAction) Naming.lookup(Configuration.getUrlConfiguration());
			client = new Action();
			server.setClient(client);
			client.setGameEventListener(this);
			server.hello();
			System.out.println("Connected to server...");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void startGame() {
		while (true) {
			repaint();
		}
	}

	private Image createImage() {

		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = bufferedImage.createGraphics();
		g.setColor(new Color(15, 9, 9));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.white);
		g.fillRect(WIDTH / 2 - 5, 0, 5, HEIGHT);
		g.fillRect(WIDTH / 2 + 5, 0, 5, HEIGHT);
		g.setColor(new Color(228, 38, 36));
		g.setFont(sFont);
		g.drawString("" + serverController.getScoreS(), WIDTH / 2 - 60, 120);
		g.drawString("" + serverController.getPlayerScore(), WIDTH / 2 + 15, 120);
		g.setFont(nFont);
		g.setColor(Color.white);
		g.drawString(serverController.getName(), WIDTH / 10, HEIGHT - 20);
		g.drawString(clientController.getName(), 600, HEIGHT - 20);
		g.setColor(new Color(57, 181, 74));
		g.fillRect(serverController.getX(), serverController.getY(), barWidth, barHeight);
		g.setColor(new Color(57, 181, 74));
		g.fillRect(clientController.getX(), clientController.getY(), barWidth, barHeight);
		g.setColor(new Color(255, 255, 255));
		g.fillOval(serverController.getBallX(), serverController.getBallY(), 45, 45);
		g.setColor(new Color(228, 38, 36));
		g.fillOval(serverController.getBallX() + 5, serverController.getBallY() + 5, 45 - 10, 45 - 10);
		message = serverController.getImessage().split("-");
		g.setFont(mFont);
		g.setColor(Color.white);
		if (message.length != 0) {
			g.drawString(message[0], WIDTH / 4 - 31, HEIGHT / 2 + 38);
			if (message.length > 1) {
				if (message[1].length() > 6) {
					g.setFont(rFont);
					g.setColor(new Color(228, 38, 36));
					g.drawString(message[1], WIDTH / 4 - 31, HEIGHT / 2 + 100);
				}
			}
		}
		return bufferedImage;
	}

	public void paint(Graphics g) {
		g.drawImage(createImage(), 0, 0, this);
		clientController.ok = true;
	}

	public void playerUP() {
		if (clientController.getY() - verticalPadding > barHeight / 2 - 10) {
			clientController.setY(clientController.getY() - verticalPadding);
		}
		try {
			server.clientMove(clientController);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void playerDOWN() {
		if (clientController.getY() + verticalPadding < HEIGHT - barHeight - 30) {
			clientController.setY(clientController.getY() + verticalPadding);
		}
		try {
			server.clientMove(clientController);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int keycode = arg0.getKeyCode();
		if (keycode == KeyEvent.VK_UP) {
			playerUP();
			repaint();
			try {
				server.serverMove(serverController);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		if (keycode == KeyEvent.VK_DOWN) {
			playerDOWN();
			repaint();
		}
		// if (serverController.isRestart()) {
		// clientController.restart = true;
		// reset = true;
		// }
		if (keycode == KeyEvent.VK_ESCAPE || keycode == KeyEvent.VK_N && serverController.isRestart()) {
			this.setVisible(false);
			System.exit(EXIT_ON_CLOSE);
		}
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
	}

	@SuppressWarnings("deprecation")
	@Override
	public void windowClosing(WindowEvent arg0) {
		Thread.currentThread().stop();
		this.setVisible(false);
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
	}

	@Override
	public void onServerMove(ServerController server) {
		serverController = server;
		repaint();
	}

	@Override
	public void onClientMove(ClientController client) {
		// TODO Auto-generated method stub

	}
}
