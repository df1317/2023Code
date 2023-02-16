package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain {
    private final WPI_VictorSPX frontLeftMotor = new WPI_VictorSPX(9);
    private final WPI_VictorSPX backLeftMotor = new WPI_VictorSPX(5);
    private final WPI_VictorSPX frontRightMotor = new WPI_VictorSPX(2);
    private final WPI_VictorSPX backRightMotor = new WPI_VictorSPX(3);

    // TEMPORARY SCORING MOTOR: REMOVE ME 
    public final WPI_VictorSPX scoringMotor = new WPI_VictorSPX(10);

    public static final double kMaxSpeed = 4; // meters per second
    public static final double kMaxAngularSpeed = 2 * Math.PI * 2; // one rotation per second
    public static final double kTrackWidth = 0.381 * 2; // meters
    public static final double kWheelRadius = 0.0762; // meters
    public static final double kEncoderResolution = 250;

    private final MotorControllerGroup leftMotorGroup = new MotorControllerGroup(frontLeftMotor, backLeftMotor);
    private final MotorControllerGroup rightMotorGroup = new MotorControllerGroup(frontRightMotor, backRightMotor);
    private final DifferentialDrive robotDrive = new DifferentialDrive(leftMotorGroup, rightMotorGroup);
    
    private final PIDController m_leftPIDController = new PIDController(1, 0, 0);
    private final PIDController m_rightPIDController = new PIDController(1, 0, 0);
    
    public static final Encoder m_leftEncoder = new Encoder(0, 1);
    public static final Encoder m_rightEncoder = new Encoder(2, 3);
    
    private final DifferentialDriveKinematics m_kinematics =
        new DifferentialDriveKinematics(kTrackWidth);
  
    private final DifferentialDriveOdometry m_odometry;
  
    // Gains are for example purposes only - must be determined for your own robot!
    private final SimpleMotorFeedforward m_feedforward = new SimpleMotorFeedforward(1, 3.1527, 1.5513);

    Gyro gyro = new Gyro();
    Limelight limelight = new Limelight();
    Controllers controllers = new Controllers();

    public Drivetrain() {
        // make sure to reset gyro when we start auto and when robot init please!!!
        leftMotorGroup.setInverted(true);
        rightMotorGroup.setInverted(false);
        double distancePerPulse = 2.0 * Math.PI * kWheelRadius / kEncoderResolution;
        System.out.println("distance per pulse: " + distancePerPulse);
        m_leftEncoder.setDistancePerPulse(2 * Math.PI * kWheelRadius / kEncoderResolution);
        m_rightEncoder.setDistancePerPulse(2 * Math.PI * kWheelRadius / kEncoderResolution);

        m_leftEncoder.reset();
        m_rightEncoder.reset();

        m_odometry =
            new DifferentialDriveOdometry(
            // maybe negate m_gyro.getAngle()? atleast for NavX apparently
            Rotation2d.fromDegrees(gyro.gyro.getAngle()), m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
    }

    public void setSpeeds(DifferentialDriveWheelSpeeds speeds) {
        final double leftFeedforward = m_feedforward.calculate(speeds.leftMetersPerSecond);
        final double rightFeedforward = m_feedforward.calculate(speeds.rightMetersPerSecond);
    
        final double leftOutput =
            m_leftPIDController.calculate(m_leftEncoder.getRate(), speeds.leftMetersPerSecond);
        final double rightOutput =
            m_rightPIDController.calculate(m_rightEncoder.getRate(), speeds.rightMetersPerSecond);
        leftMotorGroup.setVoltage(leftOutput + leftFeedforward);
        rightMotorGroup.setVoltage(rightOutput + rightFeedforward);
      }

      public void autoDrive(double xSpeed, double rot) {
        var wheelSpeeds = m_kinematics.toWheelSpeeds(new ChassisSpeeds(xSpeed, 0.0, rot));
        setSpeeds(wheelSpeeds);
      }

      public void updateOdometry() {
        m_odometry.update(
          Rotation2d.fromDegrees(gyro.gyro.getAngle()), m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
      }
      
      public void resetOdometry(Pose2d pose) {
        m_odometry.resetPosition(
          Rotation2d.fromDegrees(gyro.gyro.getAngle()), m_leftEncoder.getDistance(), m_rightEncoder.getDistance(), pose);
      }
      public Pose2d getPose() {
        return m_odometry.getPoseMeters();
      }
    /**Sets left and right motor groups to input speeds.
     * @param leftSpeed     Speed of left side motor group
     * @param rightSpeed    Speed of right side motor group
    **/
    public void drive(double leftSpeed, double rightSpeed) {
       drive(leftSpeed, rightSpeed, 1);
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

      /**Sets drivetain to rotate at input direction and full speed. 
     * @param counterclockwise True to rotate CCW, false to rotate CW
     * **/
    public void startRotate(boolean counterclockwise){
        startRotate(1, counterclockwise);
    };
    
    public void driveDistance(){

    }
/* 
    public void rotateDegrees(){
        robotDrive.tankDrive(gyro.getGyroX(), gyro.getGyroX());
    } */

    public void driveAutoLimelight() {
        double leftDrive = -limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());
        double rightDrive = limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());

        drive(leftDrive, rightDrive);
    }

    public void driveTeleop() {
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

        drive(leftDrive, rightDrive);
    }
}