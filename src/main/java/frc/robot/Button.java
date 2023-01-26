package frc.robot;
import edu.wpi.first.wpilibj.GenericHID;

public class Button{
    
    /**Joystick/controller associated with button**/
    private GenericHID owner;
    /**Button number on joystick/controller**/
    private int id;
    
    //
    /**Buffer variable for raw input**/
    private boolean provisionalOutput;
    /**Time of last change to input**/
    private long lastChange;
    
    /**Whether button is being held or not**/
    private boolean output;
    /**Toggle state**/
    private boolean state;
    /**True on press frame**/
    private boolean onPress;
    /**True on release frame **/
    private boolean onRelease;

    public Button(GenericHID owner, int id){
        this.id = id;
        this.owner = owner;
        this.output = false;
        this.state = false;
        this.lastChange = System.currentTimeMillis();
    }
    
    /** **/
    public void update(){
        onPress = false;
        onRelease = false;
        boolean input = owner.getRawButton(id);
        
        //Updates buffer var to the current raw input
        if(input != provisionalOutput){
            provisionalOutput = input;
            lastChange = System.currentTimeMillis();
        }

        long timeSinceChange = System.currentTimeMillis() - lastChange;

        //If buffer has been constant for long enough, pass the value on to the output
        if(timeSinceChange*Constants.sensitivity > 1000){
           if(provisionalOutput != output){
                output = provisionalOutput;
           if(output){
                state = !state; 
                onPress = true;
                /*System.out.println(state);*/
            }else{
                onRelease = true;
            }
            lastChange = System.currentTimeMillis();
            }
        }
    }

    public boolean getState(){
        return state;
    }

    public boolean getOutput(){
        return output;
    }

    public boolean onPress(){
        return onPress;
    }

    public boolean onRelease(){
        return onRelease;
    }
}
