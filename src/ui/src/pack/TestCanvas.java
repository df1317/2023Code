import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

public class TestCanvas extends Canvas {

	private BufferedImage buffer = null;
	private Graphics screen;

	private Image bot;
	private Image field;

	private int WIDTH;
	private int HEIGHT;

	private double x, y;

	private URLConnection ftp;
	private BufferedInputStream propReader;
	private String[] temp = "[#Wed Dec 14 19:56:49 EST 2022\nx, 300\ny, 200\ntheta, 0]".split("=");

	public TestCanvas(int w, int h) {
		WIDTH = w;
		HEIGHT = h;
		setSize(WIDTH, HEIGHT);

		try {
			ftp = new URL("ftp://roborio-1317-frc.local/home/lvuser/deploy/KinematicsVals.txt").openConnection();
			propReader = new BufferedInputStream(ftp.getInputStream());

			bot = ImageIO.read(new File("src\\pack\\bot.png"));
			field = ImageIO.read(new File("src\\pack\\2023-field.png"));
			propReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setVisible(true);
	}

	public TestCanvas(GraphicsConfiguration config) {
		super(config);
	}

	public void paint(Graphics window) {
		screen.drawImage(field, 0, 0, null);
		String raw;
		try {
			ftp = new URL("ftp://roborio-1317-frc.local/home/lvuser/deploy/KinematicsVals.txt").openConnection();
			propReader = new BufferedInputStream(ftp.getInputStream());
			raw = new String(propReader.readAllBytes());
			if (raw.length() > 0) {
				temp = raw.split("=");
			}
			propReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		x = Double.parseDouble(temp[1].split("\n")[0]);
		y = Double.parseDouble(temp[2].split("\n")[0]);

		BufferedImage rotated = rotateImageByDegrees((BufferedImage) bot, Double.parseDouble(temp[3]));
		screen.drawImage(rotated, (int) x - rotated.getWidth() / 2, (int) y - rotated.getHeight() / 2, null);

		window.drawImage(buffer, 0, 0, null);
	}

	public void construct() {
		buffer = (BufferedImage) (this.createImage(WIDTH, HEIGHT));
		screen = buffer.createGraphics();
		requestFocusInWindow();
	}

	public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		int x = w / 2;
		int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, this);
		g2d.dispose();

		return rotated;
	}
}
