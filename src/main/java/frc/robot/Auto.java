package frc.robot;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;
import edu.wpi.first.wpilibj.Filesystem;
import java.io.IOException;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

import java.nio.file.Path;
import java.nio.file.*;

public class Auto {
    Drivetrain drivetrain = new Drivetrain();
    Limelight limelight = new Limelight();

    // trajectory setup
    String trajectoryJSON_AtoMid = "paths/Unnamed.wpilib.json";
    Trajectory trajectoryAtoMid = new Trajectory();
    String trajectoryJSON_MidtoBalance = "PLACEHOLDER";
    Trajectory trajectoryMidtoBalance = new Trajectory();

    private final RamseteController m_ramseteController = new RamseteController();

  private Timer timer;

  private Field2d field;

  public void trajectoryInit(){
    try {
        Path trajectoryPath_AtoMid = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON_AtoMid);
        trajectoryAtoMid = TrajectoryUtil.fromPathweaverJson(trajectoryPath_AtoMid);

        Path trajectoryPath_MidtoBalance = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON_MidtoBalance);
        trajectoryMidtoBalance = TrajectoryUtil.fromPathweaverJson(trajectoryPath_MidtoBalance);
     } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectoryAtoMid: " + trajectoryJSON_AtoMid, ex.getStackTrace());
     }
    
      field = new Field2d();
      SmartDashboard.putData(field);
  
      field.getObject("traj").setTrajectory(trajectoryAtoMid);
}

public void autonomousStartup(){
    timer = new Timer();
    timer.start();

    drivetrain.resetOdometry(trajectoryAtoMid.getInitialPose());
}

public void runAutonomous(){
    drivetrain.updateOdometry();

    field.setRobotPose(drivetrain.getPose());

    if (timer.get() < trajectoryAtoMid.getTotalTimeSeconds()) {

      var desiredPose = trajectoryAtoMid.sample(timer.get());
      var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
    
      drivetrain.drive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
      System.out.println("Running A to Mid");

    } else if (Math.abs(limelight.limelightSteeringAlign(limelight.calculateLimelightAngle())) > limelight.limelightMinCommand) {
        double autoLeftDrive = -limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());
        double autoRightDrive = limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());

        drivetrain.drive(autoLeftDrive, autoRightDrive);
        System.out.println("Limelight aligning");

    } else if (Math.abs(limelight.limelightSteeringAlign(limelight.calculateLimelightAngle())) <= limelight.limelightMinCommand && timer.get() < trajectoryMidtoBalance.getTotalTimeSeconds()) {
        var desiredPose = trajectoryMidtoBalance.sample(timer.get());
        var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
        System.out.println("Running Mid to Balance");

    } else {
        drivetrain.drive(0, 0);
        System.out.println("Finished Auto");
    }
}

}