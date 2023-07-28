//Wenzhao Pan
//May 29, 2022
//Pong GUI Assignment

//Ball class creates the game ball
//Extends rectangle in order to make collisions simpler

import java.awt.*;

public class Ball extends Rectangle{

	public int xVelocity = 5; //x speed of the ball
	public int yVelocity = (int)(Math.random()*10-5); //randomly creates y speed of the ball
	public static final int BALL_DIAMETER = 20; //size of ball

	//creates ball at given location with given dimensions
	public Ball(int x, int y){
		super(x, y, BALL_DIAMETER, BALL_DIAMETER);
		int rand = (int)(Math.random()*100+1);
		if(rand%2 == 0) xVelocity*=-1;
	}


	//called whenever the movement of the ball changes in the y-direction (up/down)
	public void setXDirection(int xDirection){
		xVelocity = xDirection;
	}


	//called frequently from both Paddle class and GamePanel class
	//updates the current location of the ball
	public void move(){
		x = x + xVelocity;
		y += yVelocity;
	}

	//called frequently from the GamePanel class
	//draws the current location of the ball to the screen
	public void draw(Graphics g){
		g.setColor(Color.white);
		g.fillOval(x, y, BALL_DIAMETER, BALL_DIAMETER);
	}
}


