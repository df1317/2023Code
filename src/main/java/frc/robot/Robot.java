package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    public Controllers controllers;
    public Gyro gyro = new Gyro();
    public Limelight limelight;
    public Drivetrain drivetrain;
    public Auto auto;
    public Arm arm;
    public DataSender dataSender;
    public LED led;
    public Claw claw;

    @Override
    public void robotInit() {
        controllers = new Controllers();
        limelight = new Limelight();
        drivetrain = new Drivetrain(controllers, limelight);
        auto = new Auto(drivetrain, limelight);
        arm = new Arm(controllers);
        dataSender = new DataSender(drivetrain.getPose());
        led = new LED();
        claw = new Claw(controllers);

       dataSender.init();
       gyro.reset();
       drivetrain.resetEncoders();
       led.initLED();
    }

    @Override
    public void robotPeriodic() {
        drivetrain.updateOdometry();
        dataSender.update(drivetrain.getPose());
        led.runLED();
        controllers.update();
    }

    @Override
    public void autonomousInit() {
        auto.autonomousStartup();
    }

    @Override
    public void autonomousPeriodic() {
        auto.runAutonomous();

    }

    @Override
    public void teleopInit() {
        dataSender.init();
        // TODO: set all motors to 0 here, 3 second wait period between auto and teleop
        // for checking balance
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.drive(controllers.getLeftDrive(), controllers.getRightDrive(), 0.5);
        SmartDashboard.putNumber("X Pos", drivetrain.getPose().getX());
        SmartDashboard.putNumber("Y Pos", drivetrain.getPose().getY());
        SmartDashboard.putNumber("Gyro Z", gyro.getGyroYaw());
        SmartDashboard.putNumber("Gyro X", gyro.getGyroPitch());
        SmartDashboard.putNumber("Gyro Y", gyro.getGyroRoll());

        // Limelight testing
        // limelight.updateLimelightVariables();
        // SmartDashboard.putNumber("LL distance", limelight.calculateLimelightDistance());
        // testing only

        // TODO: run limelight alignment commands in teleop

        drivetrain.driveTeleop();

        gyro.resetButton(controllers.gyroResetButton());
        
        arm.runArmCommands();

        // claw.runClawCommands();
        claw.runClaw_Revised();
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
