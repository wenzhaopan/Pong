//Wenzhao Pan
//May 29, 2022
//Pong GUI Assignment

/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Extends JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener{

	//dimensions of window
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 500;

	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	
	//Instance variables such as paddles, check if end game condition is met, and ball
	public Paddle paddleRight;
	public Paddle2 paddleLeft;
	public Ball ball;
	public boolean endGame = false;
	
	//Makes the game panel
	public GamePanel(){
		paddleRight = new Paddle(GAME_WIDTH-20, GAME_HEIGHT/2 - Paddle.RECT_LENGTH/2);//right paddle
		paddleLeft = new Paddle2(10, GAME_HEIGHT/2 - Paddle2.RECT_LENGTH/2);//left paddle
		ball = new Ball(GAME_WIDTH/2, GAME_HEIGHT/2);//game ball
		this.setFocusable(true); //make everything in this class appear on the screen
		this.addKeyListener(this); //start listening for keyboard input

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		//make this class run at the same time as other classes
		gameThread = new Thread(this);
		gameThread.start();
	}

	//updates what appears on the window
	public void paint(Graphics g){
		image = createImage(GAME_WIDTH, GAME_HEIGHT); //draw off screen
		graphics = image.getGraphics();
		draw(graphics);//update the positions of everything on the screen 
		g.drawImage(image, 0, 0, this); //move the image on the screen

	}

	//call the draw methods in each class to update positions as things move
	public void draw(Graphics g){
		Font font = new Font("Segoe UI", Font.PLAIN, 30);
        g.setFont(font);
        g.setColor(Color.white);
		paddleRight.draw(g);
		paddleLeft.draw(g);
		ball.draw(g);
		
		//if end condition has been met, checks which paddle won, and then prints out corresponding winner and loser messages
		if(endGame) {
			if(paddleLeft.score < paddleRight.score) {
				g.drawString("WINNER!", GAME_WIDTH/4-60, 200);
				g.drawString("LOSER", 3*GAME_WIDTH/4-55, 200);
			}
			else {
				g.drawString("LOSER", GAME_WIDTH/4-55, 200);
				g.drawString("WINNER!", 3*GAME_WIDTH/4-60, 200);
			}
		}
	}

	//call the move methods in other classes to update positions
	public void move(){
		paddleRight.move();
		paddleLeft.move();
		ball.move();
	}
	
	//Plays the tennis hitting sound effect
	public void soundEffect() {
		try{
			File myObj = new File("Tennis.wav");
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(myObj);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception e){
			System.out.println("Oops, something went wrong");
			System.out.println(e);
		}
	}
	
	//Plays the ding sound effect for scoring
	public void soundEffect1() {
		try{
			File myObj = new File("ding.wav");
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(myObj);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception e){
			System.out.println("Oops, something went wrong");
			System.out.println(e);
		}
	}
	
	//Plays the ping pong effect for hitting the edge of the racket
	public void soundEffect2() {
		try{
			File myObj = new File("Ping Pong.wav");
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(myObj);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception e){
			System.out.println("Oops, something went wrong");
			System.out.println(e);
		}
	}

	//handles all collision detection
	public void checkCollision(){

		//force ball hits one side, updates score and puts ball in the center
		if(ball.x + Ball.BALL_DIAMETER >= GAME_WIDTH) {
			ball.y = GAME_HEIGHT/2;
			ball.x = GAME_WIDTH/2;

			paddleRight.score++;
			
			//checks for end condition
			if(paddleRight.score == 5) {
				ball.yVelocity = 0;
				ball.xVelocity = 0;
				endGame = true;
			}
			//resets ball settings to random y velocity and original x velocity
			else {
				ball.yVelocity = (int)(Math.random()*10-5);
				ball.xVelocity = -3;
			}
			soundEffect1();
		}
		
		//if ball hits one side, updates score and puts ball in the center
		if(ball.x < 0) {
			ball.y = GAME_HEIGHT/2;
			ball.x = GAME_WIDTH/2;

			paddleLeft.score++;
			
			//checks for end condition
			if(paddleLeft.score == 5) {
				ball.yVelocity = 0;
				ball.xVelocity = 0;
				endGame = true;
			}
			//resets ball settings to random y velocity and original x velocity
			else {
				ball.yVelocity = (int)(Math.random()*10-5);
				ball.xVelocity = 3;
			}
			soundEffect1();
		}
		
		//forces the ball to remain on screen (bounces off side walls)
		if(ball.y < 0) {
			ball.y = 0;
			ball.yVelocity *= -1;
		}
		
		if(ball.y + Ball.BALL_DIAMETER >= GAME_HEIGHT) {
			ball.y = GAME_HEIGHT - Ball.BALL_DIAMETER;
			ball.yVelocity *= -1;
		}
		
		//forces paddles to remain on screen
		if(paddleRight.y + Paddle.RECT_LENGTH >= GAME_HEIGHT) {
			paddleRight.y = GAME_HEIGHT - Paddle.RECT_LENGTH;
		}
		if(paddleRight.y < 0) {
			paddleRight.y = 0;
		}
		if(paddleLeft.y + Paddle2.RECT_LENGTH >= GAME_HEIGHT) {
			paddleLeft.y = GAME_HEIGHT - Paddle2.RECT_LENGTH;
		}
		if(paddleLeft.y < 0) {
			paddleLeft.y = 0;
		}
		
		
		//pre-emptively checks collision between ball and paddle (avoids problems later on with paddle top & bottom side collision method)
		if(ball.x+Ball.BALL_DIAMETER + ball.xVelocity >= paddleRight.x && ball.x + Ball.BALL_DIAMETER <= paddleRight.x
				 && ball.y + Ball.BALL_DIAMETER >= paddleRight.y && paddleRight.y + Paddle.RECT_LENGTH >= ball.y) {
			ball.x = paddleRight.x - Ball.BALL_DIAMETER;
			if(ball.xVelocity < 20) ball.xVelocity++;
			ball.xVelocity*=-1;
			if(paddleRight.yVelocity!=0) {
				ball.yVelocity = (ball.yVelocity + paddleRight.yVelocity)/2;
			}
			soundEffect();
		}
		if(ball.x >= paddleLeft.x + Paddle2.RECT_WIDTH && ball.x + ball.xVelocity  < paddleLeft.x + Paddle2.RECT_WIDTH
				 && ball.y + Ball.BALL_DIAMETER >= paddleLeft.y && paddleLeft.y + Paddle2.RECT_LENGTH >= ball.y) {
			ball.x = paddleLeft.x + Paddle2.RECT_WIDTH;
			if(ball.xVelocity > -20) ball.xVelocity--;
			ball.xVelocity*=-1;
			if(paddleLeft.yVelocity!=0) {
				ball.yVelocity = (ball.yVelocity + paddleLeft.yVelocity)/2;
			}
			soundEffect();
		}
		
		//checks if ball hits the top & bottom sides of the paddles (makes sure that the ball won't get stuck inside the paddle by shooting it out at a much higher velocity)
		//also plays different sound effect to make sure user knows that the ball hit the edge of the paddle
		if(ball.x < paddleRight.x + Paddle.RECT_WIDTH && ball.x + Ball.BALL_DIAMETER > paddleRight.x) {
			if(ball.y + Ball.BALL_DIAMETER > paddleRight.y && ball.y < paddleRight.y + Paddle.RECT_LENGTH) {
				ball.yVelocity=paddleRight.yVelocity*2;
				soundEffect2();
			}
		}
		if(ball.x + Ball.BALL_DIAMETER > paddleLeft.x && ball.x < paddleLeft.x + Paddle.RECT_WIDTH) {
			if(ball.y + Ball.BALL_DIAMETER > paddleLeft.y && ball.y < paddleLeft.y + Paddle.RECT_LENGTH) {
				ball.yVelocity=paddleLeft.yVelocity*2;
				soundEffect2();
			}
		}
	}

	//makes the game continue running
	public void run(){
		//pauses for short intervals between method calls to update screen
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		long now;

		//infinite game loop
		while(true){
			now = System.nanoTime();
			delta = delta + (now-lastTime)/ns;
			lastTime = now;

			//only move objects around and update screen if enough time has passed
			if(delta >= 1){
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}

	//if a key is pressed
	public void keyPressed(KeyEvent e){
		paddleRight.keyPressed(e);
		paddleLeft.keyPressed(e);
	}

	//if a key is released
	public void keyReleased(KeyEvent e){
		paddleRight.keyReleased(e);
		paddleLeft.keyReleased(e);
	}

	//Must be here because it is required to be overridden by the KeyListener interface
	public void keyTyped(KeyEvent e){

	}
}

