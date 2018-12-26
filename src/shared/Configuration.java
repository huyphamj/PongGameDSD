package shared;

public final class Configuration {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 460;
	public static final String host = "127.0.0.1";
	public static final int port = 3333;
	public static final String domainName = "Pong";

	public static String getUrlConfiguration() {
		return "rmi://" + host + ":" + port + "/" + domainName;
	}

	public static final int WINSCORE = 10;
	public static final int VERTICAL_PADDING = 5;
	public static final int BALL_VELOCITY = 4;
	public static final int BALL_RADIUS = 45;
	public static final int BAR_WIDTH = 30;
	public static final int BAR_HEIGHT = 120;
}
