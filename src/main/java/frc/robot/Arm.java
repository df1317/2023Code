package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Arm {
    private final CANSparkMax turretMotor = new CANSparkMax(5, MotorType.kBrushless);
    private final CANSparkMax axisMotor = new CANSparkMax(7, MotorType.kBrushless);
    private final CANSparkMax extensionMotor = new CANSparkMax(6, MotorType.kBrushless);

    Controllers controllers;

    public Arm(Controllers controllers){
        this.controllers = controllers;
    }

    private final double turretDeadzone = 0.25;
    private final double axisDeadzone = 0.25;
    private final double extensionSpeed = 0.50;

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

        if (Math.abs(controllers.getAxisRotation()) > axisDeadzone) {
            axisMotor.set(axisDirection * 0.25);
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
        rotateTurret();
        rotateAxis();
        extension();
    }
}
