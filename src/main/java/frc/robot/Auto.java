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
    String trajectorySerpentine = "output/Move.wpilib.json";
    Trajectory Move = new Trajectory();
    //String trajectoryJSON_MidtoBalance = "PLACEHOLDER";
    //Trajectory trajectoryMidtoBalance = new Trajectory();

    private final RamseteController m_ramseteController = new RamseteController();

  private Timer timer;

  private Field2d field;

  public void trajectoryInit(){
    try {
        Path trajectoryPath_Move = Filesystem.getDeployDirectory().toPath().resolve(trajectorySerpentine);
        Move = TrajectoryUtil.fromPathweaverJson(trajectoryPath_Move);

        //Path trajectoryPath_MidtoBalance = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON_MidtoBalance);
        //trajectoryMidtoBalance = TrajectoryUtil.fromPathweaverJson(trajectoryPath_MidtoBalance);
     } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectorySerpentine: " + trajectorySerpentine, ex.getStackTrace());
     }
    
      field = new Field2d();
      SmartDashboard.putData(field);
  
      field.getObject("traj").setTrajectory(Move);
}

public void autonomousStartup(){
    timer = new Timer();
    timer.start();

    drivetrain.resetOdometry(Move.getInitialPose());
}

public void runAutonomous(){
    drivetrain.updateOdometry();

    field.setRobotPose(drivetrain.getPose());

    if (timer.get()<Move.getTotalTimeSeconds()){
        var desiredPose = Move.sample(timer.get());
        var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
      
        drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
        System.out.println("Running A to Mid");

    } else {
        drivetrain.autoDrive(0,0);
        System.out.println("finished Auto");
    }

   /*  if (timer.get() < trajectorySerpentine.getTotalTimeSeconds()) {

      var desiredPose = trajectorySerpentine.sample(timer.get());
      var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
    
      drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
      System.out.println("Running A to Mid");

    } else if (Math.abs(limelight.limelightSteeringAlign(limelight.calculateLimelightAngle())) > limelight.limelightMinCommand) {
        double autoLeftDrive = -limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());
        double autoRightDrive = limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());

        drivetrain.autoDrive(autoLeftDrive, autoRightDrive);
        System.out.println("Limelight aligning");

    } else if (Math.abs(limelight.limelightSteeringAlign(limelight.calculateLimelightAngle())) <= limelight.limelightMinCommand && timer.get() < trajectoryMidtoBalance.getTotalTimeSeconds()) {
        var desiredPose = trajectoryMidtoBalance.sample(timer.get());
        var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
        drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
        System.out.println("Running Mid to Balance");

    } else {
        drivetrain.autoDrive(0, 0);
        System.out.println("Finished Auto");
    }*/
}

}