package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxRelativeEncoder;

public class Arm {
    private final CANSparkMax turretMotor = new CANSparkMax(5, MotorType.kBrushless);
    private final CANSparkMax axisMotor = new CANSparkMax(7, MotorType.kBrushless);
    private final CANSparkMax extensionMotor = new CANSparkMax(6, MotorType.kBrushless);

    private final RelativeEncoder turretEncoder = turretMotor.getEncoder(SparkMaxRelativeEncoder.Type.kQuadrature, 4096);
    private final RelativeEncoder axisEncoder = axisMotor.getEncoder(SparkMaxRelativeEncoder.Type.kQuadrature, 4096);
    private final RelativeEncoder extensionEncoder = extensionMotor.getEncoder(SparkMaxRelativeEncoder.Type.kQuadrature, 4096);

    Controllers controllers;

    public Arm(Controllers controllers){
        this.controllers = controllers;
    }

    private final double turretDeadzone = 0.25;
    private final double axisDeadzone = 0.25;
    private final double extensionSpeed = 0.50;

    public void resetEncoders() {
        // restore factory defaults? reset encoders?
        turretMotor.restoreFactoryDefaults();
        axisMotor.restoreFactoryDefaults();
        extensionMotor.restoreFactoryDefaults();
    }

    public void temporaryEncoderTesting() {
        System.out.println("Extension: " + extensionEncoder.getPosition());
    }

    public void rotateTurret() {
        double rotateDirection = (controllers.getTurretRotation() > 0) ? 1 : -1;

        if (Math.abs(controllers.getTurretRotation()) > turretDeadzone) {
            turretMotor.set(rotateDirection * 0.25);
        } else {
            turretMotor.set(0);
        }
    }

    public void rotateAxis() {
        double axisDirection = (controllers.getAxisRotation() > 0) ? 1 : -1;
        double axisPower;

        if (Math.abs(controllers.getAxisRotation()) > axisDeadzone) {
            axisPower = (axisDirection > 0) ? 0.5 : 0.25;
            axisMotor.set(axisDirection * axisPower);
        } else {
            axisMotor.set(0);
        }
    }

    public void extension() {
        if (controllers.extendButton()) {
            extensionMotor.set(extensionSpeed);
        } else if (controllers.retractButton()) {
            extensionMotor.set(-extensionSpeed);
        } else {
            extensionMotor.set(0);
        }
    }

    public void runArmCommands() {
        // temporarily disabling turret for student testing
        // rotateTurret();
        rotateAxis();
        extension();
    }
}
