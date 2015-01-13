
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick mainStick;
	Joystick secondaryStick;
	
    public void robotInit() {
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, Constants.RIGHT_TALON_CHANNEL);
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    }

    public void autonomousPeriodic() {
  
    }


    public void teleopPeriodic() {
    	double forwardThrottle = mainStick.getY();
    	double turnThrottle = mainStick.getX();
    	
    	driveTrain.drive(forwardThrottle, turnThrottle);
    }
    

    public void testPeriodic() {
    
    }
    
}
