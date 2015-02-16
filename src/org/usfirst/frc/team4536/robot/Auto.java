package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Auto { 
	DriveTrain autoDriveTrain;
	Elevator autoElevator;
	Tipper tipper;
	
	
	public Auto(DriveTrain autoConstructerDriveTrain, Elevator elevator){		
		autoDriveTrain = autoConstructerDriveTrain;
		autoElevator = elevator;
		tipper = new Tipper(Constants.RIGHT_TIPPER_SOLENOID_CHANNEL, Constants.LEFT_TIPPER_SOLENOID_CHANNEL);
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
		
		if (autoTime < 1.8){
			autoDriveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	public void driveBackwardWithRecyclingContainer(double autoTime){
		if( !tipper.isExtended() && (tipper.timeExtended() < 1) 
				&& autoElevator.getDesiredHeight() != Constants.TOP_LIMIT_SWITCH_HEIGHT) {
	    		tipper.extend();}
		
	    else if (tipper.timeExtended() > 1){
	    		autoElevator.setDesiredHeight(Constants.TOP_LIMIT_SWITCH_HEIGHT);
	    					
		}	    	
	    if (autoTime > 3 && tipper.isExtended()){
	    		tipper.retract();			
	    }
		
	    else if (autoTime > 4 && autoTime < 8 && !tipper.isExtended()){
			autoDriveTrain.driveStraight(-0.3, 0, Constants.AUTO_FORWARD_FULL_SPEED_TIME);
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
