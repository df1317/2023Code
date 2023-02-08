package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Encoder;

public class Robot extends TimedRobot {

    public Drivetrain drivetrain = new Drivetrain();
    public Controllers controllers = new Controllers();
    public Gyro gyro = new Gyro();
    public Limelight limelight = new Limelight();
    public Auto auto = new Auto();

    // temporary encoder setup
    private final Encoder leftEncoder = new Encoder(0, 1);
    private final Encoder rightEncoder = new Encoder(2, 3);

    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        gyro.gyro.reset();
        System.out.println(gyro.getGyroY());

        leftEncoder.reset();
        rightEncoder.reset();
        leftEncoder.setDistancePerPulse(2 * Math.PI * 3 / 360);
        rightEncoder.setDistancePerPulse(2 * Math.PI * 3 / 360);

        auto.trajectoryInit();
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void autonomousInit() {
        gyro.gyro.reset();
        auto.autonomousStartup(); 
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        SmartDashboard.putNumber("Gyro X", gyro.getGyroX());
        //System.out.println(gyro.gyro.getAngle());
        SmartDashboard.putNumber("Gyro Y", gyro.getGyroY());
        SmartDashboard.putBoolean("Auto Balance", controllers.autoBalanceXMode);
        /*if (controllers.autoBalanceXMode) {
            drivetrain.rotateDegrees();
        } else {*/

        //drivetrain.drive(controllers.getLeftDrive(), controllers.getRightDrive(), 1);
        
        // Limelight testing
        limelight.updateLimelightVariables();
        SmartDashboard.putNumber("LL distance", limelight.calculateLimelightDistance());

        // testing only 
        double leftDrive;
        double rightDrive;

        if (controllers.getLimelightAutoAlign()) {
            leftDrive = -limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());
            rightDrive = limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());
            System.out.println(limelight.calculateLimelightAngle());
        } else if (controllers.getAutoBalance()) {
            leftDrive = -gyro.gyroAdjust(gyro.getGyroY());
            rightDrive = -gyro.gyroAdjust(gyro.getGyroY());
        } else {
            leftDrive = controllers.getLeftDrive();
            rightDrive = controllers.getRightDrive();
        }

        drivetrain.drive(leftDrive, rightDrive);

        //System.out.println("gyro " + gyro.gyroAdjust(gyro.getGyroY()));
        //System.out.println(leftDrive + " " + rightDrive);

        double leftEncoderValue = leftEncoder.getDistance();
        System.out.println(leftEncoderValue);
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void simulationInit() {
    }

    @Override
    public void simulationPeriodic() {
    }
}
