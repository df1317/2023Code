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

    private final double axisHighScorePosition = -79;
    private final double axisMidScorePosition = -72;
    private final double axisLowScorePosition = -32;
    private final double axisCollectionPosition = -11;

    private final double extensionFull = 447;
    private final double extensionMid = 100;
    private final double extensionLow = 0;
    private final double extensionNone = 0;

    private final double axisPositionDeadzone = 2;
    private final double extensionPositionDeadzone = 10;

    private double axisScorePosition;
    private double extensionPosition;

    private double turretDirection;
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

    public double axisEncoderGet() {
        return axisEncoder.getPosition();
    }

    public void temporaryEncoderTesting() {
        System.out.println("Extension: " + extensionEncoder.getPosition());
        System.out.println("Axis: " + axisEncoder.getPosition());
    }

    public void rotateTurret() {
        double turretDirection = controllers.povTurret();
        
        if (controllers.turretTrigger()) {
            turretMotor.set(turretDirection * turretPower);
        } else {
            turretMotor.set(0);
        }
    }

    // TODO: move deadzone logic to controllers
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

    // manual extension bounded, keep this method for drivers
    public void extension() {
        // remove for competition
        if (controllers.turretTrigger()) {
            if (controllers.extendButton()) {
                extensionMotor.set(extendPower);
            } else if (controllers.retractButton()) {
                extensionMotor.set(retractPower);
                if (atExtensionPosition(extensionNone)) {
                    extensionEncoder.setPosition(0);
                }
            } else {
                extensionMotor.set(0);
            }
        } else {
            if (controllers.extendButton() && !atExtensionPosition(extensionFull)) {
                extensionMotor.set(extendPower);
            } else if (controllers.retractButton() && !atExtensionPosition(extensionNone)) {
                extensionMotor.set(retractPower);
            } else {
                extensionMotor.set(0);
        }
    }
}

    public boolean atAxisPosition(double targetPosition) {
        return Math.abs(axisEncoder.getPosition() - targetPosition) <= axisPositionDeadzone;
    }

    public boolean atExtensionPosition(double targetExtension) {
        return Math.abs(extensionEncoder.getPosition() - targetExtension) <= extensionPositionDeadzone;
    }

    /**
     * Rotates to
     * 
     * @param targetPosition desired axis position IN # of ROTATIONS
     */
    public void rotateTo(double targetPosition) {
        axisController.setReference(targetPosition, CANSparkMax.ControlType.kPosition);
    }

    public void extendTo(double targetExtension) {
        double extensionDirection = (extensionEncoder.getPosition() - targetExtension > 0) ? 1 : -1;
        if (!atExtensionPosition(targetExtension)) {
            extensionMotor.set(extensionDirection * -extendPower);
        } else {
            extensionMotor.set(0);
        }
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
                extendTo(extensionFull);
                if (atExtensionPosition(extensionFull)) {
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
                extendTo(extensionNone);
                if (atExtensionPosition(extensionNone)) {
                    extensionMotor.set(0);
                    i++;
                }
                break;

            case 4:
                rotateTo(0);
                if (atAxisPosition(0)) {
                    i++;
                }
                break;
          }
    }

    public void scoreAlign() {
        if (controllers.midScoreAlignButton() || controllers.lowScoreAlignButton()) {
            if (controllers.midScoreAlignButton()) {
                axisScorePosition = axisMidScorePosition;
                extensionPosition = extensionMid;
            } else if (controllers.lowScoreAlignButton()) {
                axisScorePosition = axisLowScorePosition;
                extensionPosition = extensionLow;
            } 
            rotateTo(axisScorePosition);
            extendTo(extensionPosition);
        }
    }
}
