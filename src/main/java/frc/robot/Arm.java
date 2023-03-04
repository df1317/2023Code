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
    private final RelativeEncoder extensionEncoder = extensionMotor.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);

    private Controllers controllers;
    private SparkMaxPIDController axisController;

    private double axisTarget = 0;

    private final double kp = 0.67972;
    private final double ki = 0;
    private final double kd = 0;

    private final double axisLoweringPower = 0.2;
    private final double axisRaisingPower = 0.5;
    private final double turretPower = 0.15;
    private final double axisDeadzone = 0.25;
    private final double extendPower = 1;
    private final double retractPower = -1;
    private double turretTarget;

    public Arm(Controllers controllers){
        this.controllers = controllers;
    
        axisEncoder.setPosition(0);
        axisController = axisMotor.getPIDController();
        axisController.setP(kp);
        axisController.setI(ki);
        axisController.setD(kd);
    }

    public void resetEncoders() {
        // restore factory defaults? reset encoders?
        //turretMotor.restoreFactoryDefaults();
        //axisMotor.restoreFactoryDefaults();
        //extensionMotor.restoreFactoryDefaults();
        turretEncoder.setPosition(0);
        axisEncoder.setPosition(0);
        extensionEncoder.setPosition(0);
    }

    public void temporaryEncoderTesting() {
        // System.out.println("Extension: " + axisEncoder.getPosition());
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
        double axisPower = (axisDirection > 0) ? axisLoweringPower : axisRaisingPower;

        axisController.setP(0);

        if (Math.abs(controllers.getAxisRotation()) > axisDeadzone) {
            axisController.setReference(axisDirection*axisPower, CANSparkMax.ControlType.kDutyCycle);
        } else {
            axisController.setReference(0, CANSparkMax.ControlType.kDutyCycle);
        }
    }

    public void extension() {
        if (controllers.extendButton()) {
            extensionMotor.set(extendPower);
        } else if (controllers.retractButton()) {
            extensionMotor.set(retractPower);
        } else {
            extensionMotor.set(0);
        }
    }
    /**
     * Rotates to
     * @param targetPosition desired axis position IN # of ROTATIONS
     */
    public void axisTo(double targetPosition){

        axisController.setP(kp);
        axisController.setI(ki);
        axisController.setD(kd);

        axisTarget = targetPosition;
        axisController.setReference(targetPosition, CANSparkMax.ControlType.kPosition);
        System.out.println(axisMotor.getEncoder().getPosition());
    }

    public void turretTo(double targetPosition, double tolerance){
        turretTarget = targetPosition;
        turretMotor.set((Math.abs(targetPosition - turretEncoder.getPosition()) < tolerance) ? 0 : Math.signum(targetPosition - turretEncoder.getPosition())*turretPower);
    }

    public boolean turretWithin(double tolerance){
        return Math.abs(turretTarget - turretEncoder.getPosition()) < tolerance;
    }

    public boolean axisWithin(double tolerance){
        return Math.abs(axisTarget - axisEncoder.getPosition()) < tolerance; 
    }

    public void runArmCommands() {
        rotateTurret();
        rotateAxis();
        extension();
    }
}