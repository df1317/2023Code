package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxRelativeEncoder;

public class Arm {
    private final CANSparkMax turretMotor = new CANSparkMax(5, MotorType.kBrushless);
    private final CANSparkMax axisMotor = new CANSparkMax(7, MotorType.kBrushless);
    private final CANSparkMax extensionMotor = new CANSparkMax(6, MotorType.kBrushless);

    private final RelativeEncoder turretEncoder = turretMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    private final RelativeEncoder axisEncoder = axisMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
    private final RelativeEncoder extensionEncoder = extensionMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor,
            42);

    private Controllers controllers;
    private Claw claw;
    private SparkMaxPIDController axisController;

    private final double kp = 0.3;
    private final double ki = 0.05;
    private final double kd = 0.1;

    private final double axisLoweringPower = 0.2;
    private final double axisRaisingPower = 0.35;
    private final double turretPower = 0.15;
    private final double axisDeadzone = 0.25;
    private final double extendPower = 1;
    private final double retractPower = -1;

    // temporary values, determine these w/testing
    private final double axisHighScorePosition = 90;
    private final double axisMidScorePosition = 60;
    private final double axisLowScorePosition = 30;

    private final double fullExtend = 100;
    private final double fullRetract = 0;

    private int i = 0;
    public boolean continueToTrajectory = false;

    public Arm(Controllers controllers, Claw claw) {
        this.controllers = controllers;
        this.claw = claw;
        axisController = axisMotor.getPIDController();
        axisController.setP(kp);
        axisController.setI(ki);
        axisController.setD(kd);
    }

    public void resetEncoders() {
        // restore factory defaults? reset encoders?
        // turretMotor.restoreFactoryDefaults();
        // axisMotor.restoreFactoryDefaults();
        // extensionMotor.restoreFactoryDefaults();
        turretEncoder.setPosition(0);
        axisEncoder.setPosition(0);
        extensionEncoder.setPosition(0);
        continueToTrajectory = false;
        i = 0;
    }

    public void temporaryEncoderTesting() {
        System.out.println("Extension: " + extensionEncoder.getPosition());
        System.out.println("Axis: " + axisEncoder.getPosition());
    }

    public void rotateTurret() {
        double turretDirection = (-controllers.getTurretRotation() > 0) ? 1 : -1;
        if (controllers.turretTrigger()) {
            turretMotor.set(turretDirection * turretPower);
        } else {
            turretMotor.set(0);
        }
    }

    public void rotateAxis() {
        double axisDirection = (-controllers.getAxisRotation() > 0) ? 1 : -1;
        double axisPower;

        if (Math.abs(controllers.getAxisRotation()) > axisDeadzone) {
            axisPower = (axisDirection > 0) ? axisLoweringPower : axisRaisingPower;
            axisMotor.set(axisDirection * axisPower);
        } else {
            axisMotor.set(0);
        }
    }

    public void extension() {
        if (controllers.extendButton() && !atFullExtension()) {
            extensionMotor.set(extendPower);
        } else if (controllers.retractButton() && !atFullRetraction()) {
            extensionMotor.set(retractPower);
        } else {
            extensionMotor.set(0);
        }
    }

    public boolean atFullExtension() {
        return extensionEncoder.getPosition() >= fullExtend;
    }

    public boolean atFullRetraction() {
        return extensionEncoder.getPosition() <= fullRetract;
    }

    public boolean atAxisPosition(double targetPosition) {
        return Math.abs(axisEncoder.getPosition() - targetPosition) <= axisDeadzone;
    }

    /**
     * Rotates to
     * 
     * @param targetPosition desired axis position IN # of ROTATIONS
     */
    public void rotateTo(double targetPosition) {
        axisController.setReference(targetPosition, CANSparkMax.ControlType.kPosition);
    }

    public void runArmCommands() {
        rotateTurret();
        rotateAxis();
        extension();
    }

    public void highScoreCube() {
        switch (i) {
            case 0:
            continueToTrajectory = false;
            rotateTo(axisHighScorePosition);
            if (atAxisPosition(axisHighScorePosition)) {
                i++;
            }
                break;
    
            case 1:
                extensionMotor.set(extendPower);
                if (atFullExtension()) {
                    extensionMotor.set(0);
                    i++;
                }
                break;
    
            case 2:
                claw.releaseClaw();
                if (!claw.grabbing()) {
                    continueToTrajectory = true;
                    i++;
                }
              break;

            case 3:
                extensionMotor.set(retractPower);
                if (atFullRetraction()) {
                    extensionMotor.set(0);
                    i++;
                }
                break;

            case 4:
                rotateTo(axisLowScorePosition);
                if (atAxisPosition(axisLowScorePosition)) {
                    i++;
                }
                break;
          }
    }
}
