package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick mainStick;
	Joystick secondaryStick;
	Compressor compressor;
	
	double prevThrottleY;
	double prevThrottleX;
	double finalThrottleY;
	double finalThrottleX;
	
	public void robotInit() {
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL);
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	compressor = new Compressor();

    	prevThrottleY = 0;
    	prevThrottleX = 0;
    	
    	System.out.println("YOU ARE CONNECTED 'MON, IT'S BOBSLED TIME!");
    }

	public void autonomousPeriodic() {
		compressor.start();
		}

	public void teleopInit() {
		
	}
	public void teleopPeriodic() {
		compressor.start();
		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(mainStick.getX(), Constants.DEAD_ZONE);
    	
    	// Puts a speed curve on the X and Y values from the mainStick 
    	
    	//Drive code with acceleration limits and slow mode
    	
    	if (mainStick.getRawButton(3) == true) { //Slow Mode - speed limit and lower acceleration limit
    		//Speed limit
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.SLOW_SPEED_CURVE) * 0.2; //Speed limit (multiply by 0.2)
        	double throttleX = Utilities.speedCurve(mainStickX, Constants.SLOW_SPEED_CURVE) * 0.2; //Speed limit (multiply by 0.2)
        	
        	//Acceleration Limit
    		finalThrottleY = Utilities.accelLimit(Constants.SLOW_FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
        	finalThrottleX = Utilities.accelLimit(Constants.SLOW_TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
        	//prev variables are redefined after the code is executed in a cycle
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    		
    	} else { //Normal Acceleration Drive Mode
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.SPEED_CURVE);
        	double throttleX = Utilities.speedCurve(mainStickX, Constants.SPEED_CURVE);
        	
        	//Acceleration Limit
    		finalThrottleY = Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
        	finalThrottleX = Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
        	//prev (previous) variables are redefined after the code is executed in a cycle
        	prevThrottleY = finalThrottleY;
        	prevThrottleX = finalThrottleX;
        	
    	}    
    	
    	//Drive
    	driveTrain.drive(finalThrottleY, finalThrottleX);
    
    }
	
	public void disabledInit() {
		System.out.println("DISABLED");
	}
	
	public void disabledPeriodic() {
		compressor.stop();
	}

    public void testPeriodic() {
    		
    }
    
}
