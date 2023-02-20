package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class Claw {
    Controllers controllers = new Controllers();

    Compressor compressor = new Compressor(0, PneumaticsModuleType.REVPH);

    Solenoid leftSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 1);
    Solenoid rightSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 2);

    private boolean grabbing = false;

    public void grabCone() {
            leftSolenoid.set(true);
            rightSolenoid.set(true);
            grabbing = true;

    }

    public void grabCube() {
            leftSolenoid.set(true);
            rightSolenoid.set(false);
            grabbing = true;

    }

    public void releaseClaw() {
            leftSolenoid.set(false);
            rightSolenoid.set(false);
            grabbing = false;
    }

    public void runClawCommands() {
        //Use this code and private bool if using onPress
        if(grabbing){
            if(controllers.grabConeButton() || controllers.grabCubeButton()){
                releaseClaw();
            }
        }else{
            if(controllers.grabConeButton()){
                grabCone();
            }else if(controllers.grabCubeButton()){
                grabCube();
            }

        }

        //Use this code if using toggle states
        /**if(controllers.grabConeButton() == controllers.grabCubeButton()){
            resetClaw();   
        }else if(controllers.grabConeButton()){
            grabCone();
        }else if(controllers.grabCubeButton()){
            grabCube();
        }**/
    }
}
