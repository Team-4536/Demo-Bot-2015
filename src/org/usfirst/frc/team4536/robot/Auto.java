package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Auto { 
	DriveTrain autoDriveTrain;
	
	public Auto(DriveTrain autoConstructerDriveTrain){		
		autoDriveTrain = autoConstructerDriveTrain;		
	
	}
	
	public static double autoNumber(){

		SendableChooser autoChooser;
		autoChooser = new SendableChooser();
		autoChooser.addObject("Drive Forward", 1);
		autoChooser.addObject("Drive Forward pushing Recycling Container", 2);
		autoChooser.addObject("Drive Forward pushing Tote", 3);
		autoChooser.addObject("Two Tote Auto", 4);
		autoChooser.addObject("Two Recycling Container Auto", 5);
		autoChooser.addDefault("Do Nothing", 6);
		SmartDashboard.putData("Auto_Chooser_Thing" , autoChooser); 
		
		//Returns the number of the auto selected auto on the SmartDashboard
		return autoChooser.getSelected().hashCode();
	}
	
	public void doNothing(){
		autoDriveTrain.drive(0, 0);
	}

	public void driveForward(double autoTime){
		
		if (autoTime < 1){
			autoDriveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	public void driveForwardWithRecyclingContainer(double autoTime){
		if (autoTime < 3){
			autoDriveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	public void driveForwardPushingTote (double autoTime){
		if ( autoTime < 3){

			autoDriveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	public void twoTote (double autoTime){
	
	}
	public void twoRecyclingContainers(double autoTime){
		
	}
	
}
