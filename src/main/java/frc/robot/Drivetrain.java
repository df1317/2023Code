package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain {
    private final WPI_VictorSPX frontLeftMotor = new WPI_VictorSPX(9);
    private final WPI_VictorSPX backLeftMotor = new WPI_VictorSPX(5);
    private final WPI_VictorSPX frontRightMotor = new WPI_VictorSPX(2);
    private final WPI_VictorSPX backRightMotor = new WPI_VictorSPX(3);

    private final MotorControllerGroup leftMotorGroup = new MotorControllerGroup(frontLeftMotor, backLeftMotor);
    private final MotorControllerGroup rightMotorGroup = new MotorControllerGroup(frontRightMotor, backRightMotor);
    private final DifferentialDrive robotDrive = new DifferentialDrive(leftMotorGroup, rightMotorGroup);


    public Drivetrain() {
        leftMotorGroup.setInverted(true);
        rightMotorGroup.setInverted(false);
    }

    /**Sets left and right motor groups to input speeds.
     * @param leftSpeed     Speed of left side motor group
     * @param rightSpeed    Speed of right side motor group
    **/
    public void drive(double leftSpeed, double rightSpeed) {
        robotDrive.tankDrive(leftSpeed, rightSpeed);
    }
    
     /**Sets left and right motor groups to input speeds, with a speed multiplier.
     * @param leftSpeed     Speed of left side motor group
     * @param rightSpeed    Speed of right side motor group
     * @param speedMod      Speed multiplier / Max speed
    **/
    public void drive(double leftSpeed, double rightSpeed, double speedMod){
        robotDrive.tankDrive(leftSpeed*speedMod, rightSpeed*speedMod);
    }

    /**Stops all driving.**/
    public void stop(){
        robotDrive.stopMotor();
    }

    /**Sets drivetain to rotate at input direction and speed. 
     * @param speedMod  Speed of rotation
     * @param counterclockwise True to rotate CCW, false to rotate CW
     * **/
    public void startRotate(double speedMod, boolean counterclockwise){
        if(counterclockwise){
            drive(-speedMod,speedMod);
        }else{
            drive(speedMod,-speedMod);
        }
    }
    
    public void driveDistance(){

    }

    public void rotateDegrees(){

    }
}
