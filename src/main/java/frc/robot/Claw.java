package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class Claw {
    Controllers controllers = new Controllers();

    Compressor compressor = new Compressor(0, PneumaticsModuleType.REVPH);

    Solenoid leftSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 1);
    Solenoid rightSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 2);

    public void grabCone() {
        if (controllers.grabConeButton()) {
            leftSolenoid.set(true);
            rightSolenoid.set(true);
        }
    }

    public void grabCube() {
        if (controllers.grabCubeButton()) {
            leftSolenoid.set(true);
            rightSolenoid.set(false);
        }
    }

    public void releaseClaw() {
        if (controllers.realeaseClawButton()) {
            leftSolenoid.set(false);
            rightSolenoid.set(false);
        }
    }

    public void resetClaw() {
        leftSolenoid.set(false);
        rightSolenoid.set(false);
    }

    public void runClawCommands() {
        grabCone();
        grabCube();
        releaseClaw();
    }
}
