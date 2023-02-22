package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Claw {
    Controllers controllers;
    Compressor compressor = new Compressor(9, PneumaticsModuleType.REVPH);

    // TODO: add gearshift
    DoubleSolenoid leftSolenoid = new DoubleSolenoid(9, PneumaticsModuleType.REVPH, 1, 0);
    DoubleSolenoid rightSolenoid = new DoubleSolenoid(9, PneumaticsModuleType.REVPH, 4, 5);

    private boolean grabbing = false;

    public Claw(Controllers controllers){
        this.controllers = controllers;
    }

    public void grabCone() {
            leftSolenoid.set(DoubleSolenoid.Value.kReverse);
            rightSolenoid.set(DoubleSolenoid.Value.kReverse);
            grabbing = true;

    }

    public void grabCube() {
            leftSolenoid.set(DoubleSolenoid.Value.kReverse);
            rightSolenoid.set(DoubleSolenoid.Value.kForward);
            grabbing = true;

    }

    public void releaseClaw() {
            leftSolenoid.set(DoubleSolenoid.Value.kForward);
            rightSolenoid.set(DoubleSolenoid.Value.kForward);
            grabbing = false;
    }

    public void runClawCommands() {
        if (controllers.grabCubeButton()) {
            grabCube();
        } else if (controllers.grabConeButton()) {
            grabCone();
        } else if (controllers.releaseButton()) {
            releaseClaw();
        } else {
            rightSolenoid.set(Value.kOff);
            leftSolenoid.set(Value.kOff);
        }
    }
}
