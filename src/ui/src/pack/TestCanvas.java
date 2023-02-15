import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import java.awt.GraphicsConfiguration;
import java.awt.Image;

public class TestCanvas extends Canvas {
	
	private BufferedImage buffer = null;
	private Graphics screen;

	private Image bot;
	private Image field;
	
	private int WIDTH;
	private int HEIGHT;
	
	private double x,y;

	private URLConnection ftp;
	private BufferedInputStream propReader;
	private String[] temp = "[#Wed Dec 14 19:56:49 EST 2022\nx, 300\ny, 200\ntheta, 0]".split("=");
	double t;

	public TestCanvas(int w, int h) {
		WIDTH = w;
		HEIGHT = h;
		setSize(WIDTH, HEIGHT);
		t=0;
		//String path = "";

		try {
			ftp = new URL("ftp://roborio-1317-frc.local/home/lvuser/deploy/KinematicsVals.txt").openConnection();
			propReader = new BufferedInputStream( ftp.getInputStream());
			File baseFile = new File("src\\pack\\bot.png");

			/*System.out.println(baseFile.getAbsolutePath());
			String[] temp = baseFile.getAbsolutePath().split(File.pathSeparator);
			int len = temp.length;
			temp[len -1] = "";
			temp[len -4] = "main";
			temp[len -2] = "KinematicsVals.txt";
			temp[len -3] = "deploy";

			for(int i = 0; i < len -2;i++){
				path += temp[i] + File.pathSeparator;
			}
			path += temp[len -2];
			System.out.println(path);*/


			bot = ImageIO.read(baseFile);
			field = ImageIO.read(new File("src\\pack\\2023-field.png"));
			propReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    setVisible(true);
	}

	public TestCanvas(GraphicsConfiguration config) {
		super(config);
		// TODO Auto-generated constructor stub
	}
	
    
	public void paint(Graphics window) {
		screen.drawImage(field,0,0,null);
		String raw;
		try {
			ftp = new URL("ftp://roborio-1317-frc.local/home/lvuser/deploy/KinematicsVals.txt").openConnection();
			propReader = new BufferedInputStream( ftp.getInputStream());
			raw = new String(propReader.readAllBytes());
			if(raw.length() > 0){
				temp  = raw.split("=");
			}
			System.out.println(Arrays.toString(temp));
			propReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		x = Double.parseDouble(temp[1].split("\n")[0]);
		y = Double.parseDouble(temp[2].split("\n")[0]);

		BufferedImage rotated = rotateImageByDegrees((BufferedImage)bot, Double.parseDouble(temp[3]));
		screen.drawImage(rotated, (int)x-rotated.getWidth()/2, (int)y-rotated.getHeight()/2, null);
		
		window.drawImage(buffer, 0, 0, null);
	}
	
	public void construct(){
		buffer = (BufferedImage)(this.createImage(WIDTH,HEIGHT));
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
