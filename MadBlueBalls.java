import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class MadBlueBalls extends JFrame {
	VectorObject ball;
	int diameter;
	Image dbImage;
	Image success;
	Image fail;
	Graphics dbGraphics;
	Vector velocity; // Unfortunately necessary
	Vector gravity;
	Vector wind;
	Vector targetLoc;
	Vector targetCenter;
	BufferedImage ballImage;
	BufferedImage Background;
	BufferedImage grass;
	BufferedImage target;
	ParticleSystem explosion;
	ParticleSystem deadBall;
	Random rand;
	boolean hasBounced;
	boolean hitTarget;
	
	public MadBlueBalls() throws IOException
	{
		// Set up the ball
		ball = new VectorObject(200,450, 650, 1.75);;
		diameter = 30;
		ballImage = ImageIO.read(new File("MadBlueBall.png"));
		
		// Set up the background
		Background = ImageIO.read(new File("Background.png"));
		grass = ImageIO.read(new File("grass.png"));
		
		// Set up end game images
		success = ImageIO.read(new File("success.png"));
		fail = ImageIO.read(new File("fail.png"));
		
		// Set up the target
		target = ImageIO.read(new File("target.png"));
		targetLoc = new Vector(950,500);
		targetCenter = new Vector(targetLoc.getX()+8,targetLoc.getY()+23);
		hitTarget = false;
		
		// Forces
		gravity = new Vector(0, 9.8);
		wind = new Vector();
		
		// Set up various simple things
		rand = new Random();
		hasBounced = false;
		
		
		// Set up the window
		setSize(1200,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		addMouseMotionListener(new MML());
		addMouseListener(new ML());
	}
	
	public void update()
	{
		if (!hasBounced)
		{
			// Only apply gravity if ball has been shot
			if (!ball.getVelocity().isZero())
			{
				ball.applyForce(gravity);
			}
			
			// Update the ball's location
			ball.updateLocation();
			
			// Get the location
			Vector location = ball.getLocation();
			int x = (int) location.getX();
			int y = (int) location.getY();
			
			// Detect a hit target
			Vector distFromCent = Vector.sub(targetCenter, location);
			if (distFromCent.magnitude()<25)
				hitTarget = true;
			
			
			// Bounce the ball if it hits the ground
			if (y>=600)
			{
				ball.setLocation(new Vector(x,599));
				ball.bounceY();
				ball.multVelocity(0.75);
				if (!hasBounced && !hitTarget) // Explosion
				{
					explosion = new ParticleSystem(x,y-10,50);
					explosion.create(); // Should put this directly in the particlesystem constructor
					deadBall = new ParticleSystem(x,y-10,10);
					deadBall.create();
					hasBounced = true;
				}
			}
		}
		
		if (hasBounced && !hitTarget) // Explosion already happened
		{
			explosion.update();
			deadBall.update();
		}
	}
	
	public void paint(Graphics g)
	{
		/*
		 * Double buffered graphics
		 * 
		 * This creates an offscreen image,
		 * draws everything on it,
		 * then replaces the image currently
		 * in the window with the new image.
		 * 
		 * This keeps things from creating
		 * trails, when they move.
		 */
		dbImage = createImage(1200,700); // Create new image
		dbGraphics = dbImage.getGraphics(); // get its graphics
		paintComponent(dbGraphics); // Actually draws the new image
		g.drawImage(dbImage, 0, 0, this); // puts the image into the window
	}
	
	public void paintComponent(Graphics g)
	{
		// Update the ball's location
		update();
		
		// Get ball's location
		Vector loc = ball.getLocation();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		
		// Set background
		g.drawImage(Background, 0, 0, this);
		
		// Draw grass
		for (int i=0; i<=1200; i=i+100)
			g.drawImage(grass, i, 600, this);
		
		// Draw the "slingshot"
		g.setColor(new Color(139,69,19,255));
		g.fillRect(198, 455, 20, 170);
		
		/*
		 * PUT THIS AS THE LAST THING DRAWN!
		 */
		// Draw the target
		
		int tX = (int) targetLoc.getX();
		int tY = (int) targetLoc.getY();
		g.drawImage(target, tX, tY, this);
		
		/*
		 * Draw the explosion
		 * 
		 * Both for loops do the same thing, but
		 * the particles are colored differently
		 */
		if(hasBounced)
		{
			// Brown particles
			ArrayList<Particle> particles = explosion.getParticles();
			for (Particle P: particles)
			{
				Vector expLoc = P.getLocation();
				int pX = (int) expLoc.getX();
				int pY = (int) expLoc.getY();
				g.setColor(new Color(139,69,19,255));
				g.fillOval(pX, pY, 10, 10);
				g.setColor(Color.BLACK);
				g.drawOval(pX, pY, 10, 10);
			}
			// Blue particles
			ArrayList<Particle> deadBallParts = deadBall.getParticles();
			for (Particle P: deadBallParts)
			{
				Vector expLoc = P.getLocation();
				int pX = (int) expLoc.getX();
				int pY = (int) expLoc.getY();
				g.setColor(Color.BLUE);
				g.fillOval(pX, pY, 10, 10);
				g.setColor(Color.BLACK);
				g.drawOval(pX, pY, 10, 10);
			}
		}
		else // If the ball either hasn't hit the ground, or hit the target
		{
			//Draw the ball
			g.drawImage(ballImage, x-x/diameter, y-y/diameter, this);
		}
		
		// Draw endgame images
		if (x > 1300 || (ball.getVelocity().isZero() && hitTarget)) // SECOND CONDITION MAY FAIL
			g.drawImage(success, 315, 200, this);
		else if (hasBounced && explosion.getParticles().size()==0)
		{
			g.drawImage(fail, 315, 200, this);
		}
		
		repaint();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		new MadBlueBalls();
	}
	
	private class ML implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
			// Properly scale & limit the velocity
			velocity.div(10);
			velocity.limit(10);
			
			// Set the ball's velocity
			ball.setVelocity(velocity);
		}
		
	}

	private class MML implements MouseMotionListener
	{
		/*
		 * Get the ball's location
		 * 
		 * This is important for tracking the ball's original location.
		 * Only used when the ball is on the 'slingshot', for creating
		 * the velocity vector, to shoot the ball.
		 */
		Vector oldLoc = ball.getLocation();
		
		@Override
		public void mouseDragged(MouseEvent mE) {
			// Get the mouse's location
			int x = mE.getX();
			int y = mE.getY();
			
			// Create a vector from the location
			Vector newLoc = new Vector(x,y);
			Vector diff = Vector.sub(newLoc, oldLoc);
			
			// This keeps the ball from moving too far from the slingshot
			diff.limit(100); 
			
			// Set the ball to the new location (animation purposes)
			newLoc = oldLoc.get();
			newLoc.add(diff);
			ball.setLocation(newLoc);
			
			/*
			 * Invert the velocity, so the ball shoots in the opposite
			 * direction from where it was dragged by the mouse
			 */
			diff.mult(-1);
			
			// Set the velocity variable (not the ball's velocity yet)
			velocity = diff;
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			
			// We don't need no stinkin' mouseMoved method!
		}
		
	}
}
