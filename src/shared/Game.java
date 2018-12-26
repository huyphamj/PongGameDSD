package shared;

import java.util.Scanner;

import client.PongClient;
import server.PongServer;

public class Game {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input 0 to play as server, 1 to play as client: ");
		try {
			int player = scanner.nextInt();
			if (player == 0) {
				PongServer myServer = new PongServer();
				Thread myServerT = new Thread(myServer);
				myServerT.start();
			} else if (player == 1) {
				PongClient myClient = new PongClient();
				Thread myClientT = new Thread(myClient);
				myClientT.start();
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
