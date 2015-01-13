
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick leftStick;
	Joystick rightStick;
	
	
    public void robotInit() {
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, Constants.RIGHT_TALON_CHANNEL);
    	leftStick = new Joystick(Constants.LEFT_STICK_PORT);
    	rightStick = new Joystick(Constants.RIGHT_STICK_PORT);
    }

    public void autonomousPeriodic() {

    }


    public void teleopPeriodic() {
        
    }
    

    public void testPeriodic() {
    
    }
    
}
