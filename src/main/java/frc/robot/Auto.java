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

    String trajectoryJSON = "paths/Unnamed.wpilib.json";
    Trajectory trajectory = new Trajectory();

    private final RamseteController m_ramseteController = new RamseteController();

  private Timer timer;

  private Field2d field;

  public void trajectoryInit(){
    try {
        Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
        trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
     } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
     }
    
      field = new Field2d();
      SmartDashboard.putData(field);
  
      field.getObject("traj").setTrajectory(trajectory);
}

public void autonomousStartup(){
    timer = new Timer();
    timer.start();

    drivetrain.resetOdometry(trajectory.getInitialPose());

}

public void runAutonomous(){
    drivetrain.updateOdometry();

    field.setRobotPose(drivetrain.getPose());

    if (timer.get() < trajectory.getTotalTimeSeconds()) {
      
      var desiredPose =trajectory.sample(timer.get());


      var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

    
      drivetrain.drive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
    } else {
      drivetrain.drive(0, 0);
    }

}

    
}
