//Wenzhao Pan
//May 29, 2022
//Pong GUI Assignment

/* Paddle class defines behaviours for the left paddle
 * extension of rectangle because paddle is shaped in rectangle
 */ 
import java.awt.*;
import java.awt.event.*;

public class Paddle2 extends Rectangle{

	public int yVelocity;
	public final int SPEED = 5; //movement speed of paddle
	public static final int RECT_LENGTH = 70; //length of paddle
	public static final int RECT_WIDTH = 10; //width of paddle
	public int score; //keeps track of score
	public String s = ""; //is updated with score in order to conveniently display it on the game

	//constructor creates paddle at given location with given dimensions
	public Paddle2(int x, int y){
		super(x, y, RECT_WIDTH, RECT_LENGTH);
		score = 0;
	}

	//called from GamePanel when any keyboard input is detected
	//updates the direction of the paddle based on user input
	//if the keyboard input isn't any of the options (w, s), then nothing happens
	public void keyPressed(KeyEvent e){
		if(e.getKeyChar() == 'w'){
			setYDirection(SPEED*-1);
			move();
		}

		if(e.getKeyChar() == 's'){
			setYDirection(SPEED);
			move();
		}
	}

	//called from GamePanel when any key is released (no longer being pressed down)
	//Makes the paddle stop moving in that direction
	public void keyReleased(KeyEvent e){
		if(e.getKeyChar() == 'w'){
			setYDirection(0);
			move();
		}

		if(e.getKeyChar() == 's'){
			setYDirection(0);
			move();
		}
	}

	//called whenever the movement of the paddle changes in the y-direction (up/down)
	public void setYDirection(int yDirection){
		yVelocity = yDirection;
	}

	//called frequently from both Paddle2 class and GamePanel class
	//updates the current location of the paddle
	public void move(){
		y = y + yVelocity;
	}

	//called frequently from the GamePanel class
	//draws the current location of the ball, paddle, and score to the screen
	public void draw(Graphics g){
		g.setColor(Color.white);
		g.fillRect(x, y, RECT_WIDTH, RECT_LENGTH);
		s = "" + this.score;
		g.drawString(s, GamePanel.GAME_WIDTH/2+45, 50);
	}
	

}