import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends JFrame {

	private static final int WIDTH = 54 * 12 * 2;
	private static final int HEIGHT = 27 * 12 * 2;
	private static TestCanvas canv;
	private static final double maxFPS = 2 * 60.052;
	private static final int sleepMill = (int) Math.floor(1000.0 / maxFPS);
	private static final int sleepNano = (int) ((1000.0 / maxFPS - Math.floor(1000.0 / maxFPS)) * 1000000);

	public Main() {
		super("Field Tracker");

		setSize(WIDTH + 15, HEIGHT + 40);

		canv = new TestCanvas(WIDTH, HEIGHT);
		getContentPane().add(canv);

		getContentPane().setBackground(Color.BLACK);

		setFocusable(true);

		setExtendedState(JFrame.NORMAL);
		setMaximizedBounds(getBounds());
		setUndecorated(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Image noCursor = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(noCursor, new Point(0, 0), "none"));

		setVisible(true);

		canv.construct();
	}

	public static void main(String[] args) {
		Main main = new Main();
		while (true) {

			try {
				Thread.sleep(sleepMill, sleepNano);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			canv.paint(canv.getGraphics());
		}
	}

}
