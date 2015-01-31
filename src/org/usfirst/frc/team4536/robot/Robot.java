package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;

public class Robot extends IterativeRobot {
	DriveTrain driveTrain;	
	Joystick mainStick;
	Joystick secondaryStick;	
	DigitalInput limitSwitch1;
	DigitalInput hallEffectSensor;
	Compressor compressor;
	
	public void robotInit() {
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL);
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	
    	compressor = new Compressor();
    	
    	limitSwitch1 = new DigitalInput(3);
    	hallEffectSensor = new DigitalInput(0);
    }

	public void autonomousPeriodic() {
		compressor.start();
    }

	public void teleopPeriodic() {
		compressor.start();
		
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
    	
    	SmartDashboard.putBoolean("Limit_Switch_Value", !limitSwitch1.get());
    	SmartDashboard.putBoolean("Hall_Effect_Sensor_Value", !hallEffectSensor.get());
    }
	
	public void disabledInit() {
		System.out.println("DIABLED");
	}
	
	public void disabledPeriodic() {
		compressor.stop();
	}
    
    public void testPeriodic() {
    		
    }
    
}
