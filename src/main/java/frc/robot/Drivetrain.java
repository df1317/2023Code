package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Drivetrain {
    private final WPI_VictorSPX frontLeftMotor = new WPI_VictorSPX(2);
    private final WPI_VictorSPX backLeftMotor = new WPI_VictorSPX(1);
    private final WPI_VictorSPX frontRightMotor = new WPI_VictorSPX(3);
    private final WPI_VictorSPX backRightMotor = new WPI_VictorSPX(4);

    // TEMPORARY SCORING MOTOR: REMOVE ME
    // private final WPI_VictorSPX scoringMotor = new WPI_VictorSPX(5);
    public static boolean balanced = false;
    public static boolean aligning = false;

    private static final double kTrackWidth = 0.6858; // meters
    private static final double kWheelRadius = 0.0762; // meters
    private static final double kEncoderResolution = 2048;
    private static final double kGearRatio = 1;

    private final MotorControllerGroup leftMotorGroup = new MotorControllerGroup(frontLeftMotor, backLeftMotor);
    private final MotorControllerGroup rightMotorGroup = new MotorControllerGroup(frontRightMotor, backRightMotor);
    private final DifferentialDrive robotDrive = new DifferentialDrive(leftMotorGroup, rightMotorGroup);

    private final PIDController m_leftPIDController = new PIDController(0.0000002855, 0, 0);
    private final PIDController m_rightPIDController = new PIDController(0.00000032183, 0, 0);

    // left was 01 right was 23
    private static final Encoder m_leftEncoder = new Encoder(0, 1);
    private static final Encoder m_rightEncoder = new Encoder(2, 3);

    DoubleSolenoid gearshiftSolenoid = new DoubleSolenoid(9, PneumaticsModuleType.REVPH, 2, 3);

    private final DifferentialDriveKinematics m_kinematics = new DifferentialDriveKinematics(kTrackWidth);

    private final DifferentialDriveOdometry m_odometry;

    // Gains are for example purposes only - must be determined for your own robot!
    private final SimpleMotorFeedforward m_feedforward = new SimpleMotorFeedforward(1.0442, 3.4783, 0.98103);

    private Gyro gyro = new Gyro();
    private Limelight limelight;
    private Controllers controllers;

    private final double driveDeadzone = 0.1;
    private final double slowForwardSpeed = -0.4;
    private final double autoBalanceMaxPower = 0.48;
    private final double autoBalanceMinPower = 0.2;

    public Drivetrain(Controllers controllers, Limelight limelight) {

        this.controllers = controllers;
        this.limelight = limelight;

        // left was true, right was false
        leftMotorGroup.setInverted(false);
        rightMotorGroup.setInverted(true);

        m_leftEncoder.setDistancePerPulse(-(2 * Math.PI * kWheelRadius / kEncoderResolution) * kGearRatio);
        m_rightEncoder.setDistancePerPulse(-(2 * Math.PI * kWheelRadius / kEncoderResolution) * kGearRatio);

        resetEncoders();

        m_odometry = new DifferentialDriveOdometry(
                Rotation2d.fromDegrees(gyro.getGyroYaw()), m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
    }

    public void setSpeeds(DifferentialDriveWheelSpeeds speeds) {
        final double leftFeedforward = m_feedforward.calculate(speeds.leftMetersPerSecond);
        final double rightFeedforward = m_feedforward.calculate(speeds.rightMetersPerSecond);

        final double leftOutput = m_leftPIDController.calculate(m_leftEncoder.getRate(), speeds.leftMetersPerSecond);
        final double rightOutput = m_rightPIDController.calculate(m_rightEncoder.getRate(),
                speeds.rightMetersPerSecond);
        leftMotorGroup.setVoltage(leftOutput + leftFeedforward);
        rightMotorGroup.setVoltage(rightOutput + rightFeedforward);
    }

    public void autoDrive(double xSpeed, double rot) {
        var wheelSpeeds = m_kinematics.toWheelSpeeds(new ChassisSpeeds(xSpeed, 0.0, rot));
        setSpeeds(wheelSpeeds);
    }

    public void updateOdometry() {
        m_odometry.update(
                Rotation2d.fromDegrees(gyro.getGyroYaw()), m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
    }

    public void resetOdometry(Pose2d pose) {
        m_odometry.resetPosition(Rotation2d.fromDegrees(gyro.getGyroYaw()), m_leftEncoder.getDistance(),
                m_rightEncoder.getDistance(), pose);
    }

    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    /**
     * Sets left and right motor groups to input speeds.
     * 
     * @param leftSpeed  Speed of left side motor group
     * @param rightSpeed Speed of right side motor group
     **/
    public void drive(double leftSpeed, double rightSpeed) {
        drive(leftSpeed, rightSpeed, 1);
    }

    /**
     * Sets left and right motor groups to input speeds, with a speed multiplier.
     * 
     * @param leftSpeed  Speed of left side motor group
     * @param rightSpeed Speed of right side motor group
     * @param speedMod   Speed multiplier / Max speed
     **/
    public void drive(double leftSpeed, double rightSpeed, double speedMod) {
        if (Math.abs(leftSpeed) < driveDeadzone) {
            leftSpeed = 0.0;
        }

        if (Math.abs(rightSpeed) < driveDeadzone) {
            rightSpeed = 0.0;
        }

        robotDrive.tankDrive(leftSpeed * speedMod, rightSpeed * speedMod);
    }

    /** Stops all driving. **/
    public void stop() {
        robotDrive.stopMotor();
    }

    /**
     * Sets drivetain to rotate at input direction and speed.
     * 
     * @param speedMod         Speed of rotation
     * @param counterclockwise True to rotate CCW, false to rotate CW
     **/
    public void startRotate(double speedMod, boolean counterclockwise) {
        if (counterclockwise) {
            drive(-speedMod, speedMod);
        } else {
            drive(speedMod, -speedMod);
        }
    }

    /**
     * Sets drivetrain to rotate at input direction and full speed.
     * 
     * @param counterclockwise True to rotate CCW, false to rotate CW
     **/
    public void startRotate(boolean counterclockwise) {
        startRotate(1, counterclockwise);
    }

    public double gyroDrive() {
        double power = -0.5 * gyro.gyroAdjust();

        balanced = false;

        if (power > 0) {
            power = Math.min(power, autoBalanceMaxPower);
            power = Math.max(power, autoBalanceMinPower);
        } else if (power < 0) {
            power = Math.min(power, -autoBalanceMinPower);
            power = Math.max(power, -autoBalanceMaxPower);
        } else {
            balanced = true;
        }
        return power;

    }

    public void driveTeleop() {
        double leftDrive;
        double rightDrive;

        if (controllers.getLimelightAutoAlign()) {
            // NOT TESTED after inverting drivetrain (changed sign though)
            leftDrive = -limelight.limelightSteeringAlign();
            rightDrive = limelight.limelightSteeringAlign();
            aligning = true;
            // System.out.println(limelight.calculateLimelightAngle());
        } else if (controllers.slowForwardDriveLeft() || controllers.slowForwardDriveRight()) {
            // sets to low gear
            gearshiftInit();
            leftDrive = slowForwardSpeed;
            rightDrive = slowForwardSpeed;
        } else if (controllers.gyroBalance1() || controllers.gyroBalance2()) {
            leftDrive = gyroDrive();
            rightDrive = gyroDrive();
            // System.out.println(gyroDrive());
        } else {
            aligning = false;
            leftDrive = -controllers.getLeftDrive();
            rightDrive = -controllers.getRightDrive();
        }

        drive(leftDrive, rightDrive);
    }

    public void gearshift() {
        // kForward is a lower, gentler gear, kReverse is a high gear (use kForward for
        // auto)
        if (controllers.gearshiftButtonLeft2() || controllers.gearshiftButtonRight2()) {
            gearshiftSolenoid.set(DoubleSolenoid.Value.kForward);
        } else if (controllers.downshiftButtonLeft2() || controllers.downshiftButtonRight2()) {
            gearshiftSolenoid.set(DoubleSolenoid.Value.kReverse);
        } else {
            gearshiftSolenoid.set(DoubleSolenoid.Value.kOff);
        }
    }

    public void gearshiftInit() {
        // init sets initial gear to kForward for autonomous/teleop driving
        gearshiftSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void resetEncoders() {
        m_leftEncoder.reset();
        m_rightEncoder.reset();
    }

    public double getLeftEncoder() {
        return m_leftEncoder.getDistance();
    }

    public double getRightEncoder() {
        return m_rightEncoder.getDistance();
    }
}