package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;

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
    private PIDController axisController;

    private final double kp = 0.3;
    private final double ki = 0.05;
    private final double kd = 0.1;

    private final double axisLoweringPower = 0.2;
    private final double axisRaisingPower = 0.5;
    private final double turretPower = 0.25;
    private final double axisDeadzone = 0.25;
    private final double extendPower = 0.5;
    private final double retractPower = -0.25;

    public Arm(Controllers controllers){
        this.controllers = controllers;
        axisController = new PIDController(kp, ki, kd);
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
        double turretDirection = (controllers.getTurretRotation() > 0) ? 1 : -1;
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
        if (controllers.extendButton()) {
            extensionMotor.set(extendPower);
        } else if (controllers.retractButton()) {
            extensionMotor.set(retractPower);
        } else {
            extensionMotor.set(0);
        }
    }

    public void rotateTo(double targetPosition){
        axisMotor.set(axisController.calculate(axisEncoder.getPosition(), targetPosition));
    }

    public void runArmCommands() {
        rotateTurret();
        rotateAxis();
        extension();
    }
}
