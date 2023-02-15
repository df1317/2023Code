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

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathConstraints;

import java.nio.file.Path;
import java.nio.file.*;

public class Auto {
    Drivetrain drivetrain = new Drivetrain();
    Limelight limelight = new Limelight();
    Gyro gyro = new Gyro();

    // trajectory setup
    PathPlannerTrajectory SimpleCurve6 = PathPlanner.loadPath("SimpleCurve6", new PathConstraints(1.5, 1));
    PathPlannerTrajectory SecondCurve1 = PathPlanner.loadPath("SecondCurve1", new PathConstraints(1.0, 1));

    private final RamseteController m_ramseteController = new RamseteController();

    public boolean finishedFirstTrajectory;
    public boolean finishedAligning;

    private Timer timer;
    public double subtractTime;

    private Field2d field;

  public void trajectoryInit(){
   /* try {
        //Path trajectoryPath_Move = Filesystem.getDeployDirectory().toPath().resolve(trajectorySerpentine);
        //Move = TrajectoryUtil.fromPathweaverJson(trajectoryPath_Move);

        //Path trajectoryPath_MidtoBalance = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON_MidtoBalance);
        //trajectoryMidtoBalance = TrajectoryUtil.fromPathweaverJson(trajectoryPath_MidtoBalance);
     } catch (IOException ex) {
       // DriverStation.reportError("Unable to open trajectorySerpentine: " + trajectorySerpentine, ex.getStackTrace());
     }
    
      field = new Field2d();
      SmartDashboard.putData(field);
  */
      //field.getObject("traj").setTrajectory(Move);
}

public void autonomousStartup() {
    gyro.gyro.reset();
    drivetrain.m_leftEncoder.reset();
    drivetrain.m_rightEncoder.reset();

    // timer for first trajectory
    timer = new Timer();
    timer.start();

    finishedFirstTrajectory = false;
    finishedAligning = false;

    drivetrain.resetOdometry(SimpleCurve6.getInitialPose());
}

public void runAutonomous() {
    drivetrain.updateOdometry();
    limelight.updateLimelightVariables();
    //System.out.println(limelight.limelightTV);

   // field.setRobotPose(drivetrain.getPose());

    if (timer.get() < SimpleCurve6.getTotalTimeSeconds()) {
        var desiredPose = SimpleCurve6.sample(timer.get());
        var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
      
        drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
        // System.out.println(refChassisSpeeds);

    } else if (!finishedAligning && !limelight.limelightInAlignment() && limelight.validLimelightTarget()) {

        // System.out.println("align " + limelight.limelightInAlignment());
        // System.out.println("valid " + limelight.validLimelightTarget());

        drivetrain.driveAutoLimelight();
        // System.out.println("Limelight auto aligning!!.");
        finishedFirstTrajectory = true;
        //timerScoring.start();

    } else if (timer.get() < (2.5 + SimpleCurve6.getTotalTimeSeconds())) {
        finishedAligning = true;
        drivetrain.scoringMotor.set(0.5);

    } else if (timer.get() == (3 + SimpleCurve6.getTotalTimeSeconds())) {
        finishedAligning = true;

        drivetrain.scoringMotor.set(0);
        //timer2.start();

        drivetrain.resetOdometry(SecondCurve1.getInitialPose());

        subtractTime = timer.get();
        System.out.println(subtractTime);

    } else if (timer.get() < (SecondCurve1.getTotalTimeSeconds() + subtractTime)) {
        drivetrain.scoringMotor.set(0);

        var desiredPose = SecondCurve1.sample(timer.get() - subtractTime);
        var refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
      
        drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
        // System.out.println(refChassisSpeeds);

    } else {
        drivetrain.autoDrive(0, 0);
        System.out.println("FINISHED AUTO");
    }

    /*if (finishedFirstTrajectory) {
        timerScoring.start();
    }*/


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