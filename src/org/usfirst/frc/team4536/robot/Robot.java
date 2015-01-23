package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick mainStick;
	Joystick secondaryStick;
	Compressor compressor;
	
	double currentForwardThrottle;
	double currentTurnThrottle;
	double prevForwardThrottle;
	double prevTurnThrottle;
	double finalForwardThrottle;
	double finalTurnThrottle;
	
	public void robotInit() {
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL);
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	compressor = new Compressor();
    	currentForwardThrottle = 0;
    	currentTurnThrottle = 0;
    	prevForwardThrottle = 0;
    	prevTurnThrottle = 0;
    	finalForwardThrottle = 0;
    	finalTurnThrottle = 0;
    	
    	System.out.println("YOU ARE CONNECTED 'MON, IT'S BOBSLED TIME!");
    }

	public void autonomousPeriodic() {
		compressor.start();
		}

	public void teleopPeriodic() {
		compressor.start();
		
		if (secondaryStick.getRawButton(3)) {
			compressor.stop();
		}
		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(mainStick.getX(), Constants.DEAD_ZONE);
    	
    	// Puts a speed curve on the X and Y values from the mainStick 
    	mainStickY = Utilities.speedCurve(mainStick.getY(), Constants.SPEED_CURVE);
    	mainStickX = Utilities.speedCurve(mainStick.getX(), Constants.SPEED_CURVE);
    	
    	// Sets throttle values based on mainStick X and Y values (with dead zone).
    	double forwardThrottle = mainStickY;
    	double turnThrottle = mainStickX;
    	
    	driveTrain.drive(forwardThrottle, turnThrottle);
    	
    	//Acceleration Limit
    	double currentForwardThrottle = forwardThrottle;
    	double currentTurnThrottle = turnThrottle;
    	
    	double throttleForwardDiff = currentForwardThrottle - prevForwardThrottle;
    	double throttleTurnDiff = currentForwardThrottle - prevTurnThrottle;
    	
    	if (throttleForwardDiff > Constants.FORWARD_ACCELERATION_LIMIT) {
    		finalForwardThrottle = prevForwardThrottle + Constants.FORWARD_ACCELERATION_LIMIT;
    	} else {
    		finalForwardThrottle = currentForwardThrottle;
    	}
    	
    	if (throttleTurnDiff > Constants.TURN_ACCELERATION_LIMIT) {
    		finalTurnThrottle = prevTurnThrottle + Constants.TURN_ACCELERATION_LIMIT;
    	} else {
    		finalTurnThrottle = currentTurnThrottle;
    	}
    	
    	driveTrain.drive(finalForwardThrottle, finalTurnThrottle);
    	
    	double prevForwardThrottle = currentForwardThrottle;
    	double prevTurnThrottle = currentTurnThrottle;
    	    	
    	
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
