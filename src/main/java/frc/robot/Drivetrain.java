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

    public void drive(double joyL, double joyR) {
        if (Math.abs(joyL) > DEADZONE || Math.abs(joyR) > DEADZONE) {
            if (halfsiesL) {
                robotDrive.tankDrive(joyL.getY() * SPEED_MOD * 0.5, joyR.getY() * SPEED_MOD);
            } else if (halfsiesR) {
                robotDrive.tankDrive(joyL.getY() * SPEED_MOD, joyR.getY() * SPEED_MOD * 0.5);
            } else {
                robotDrive.tankDrive(joyL.getY() * SPEED_MOD, joyR.getY() * SPEED_MOD);
            }
        } else {
            robotDrive.tankDrive(0, 0);
        }
    }



}
