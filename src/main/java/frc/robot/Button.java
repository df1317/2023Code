package frc.robot;
import edu.wpi.first.wpilibj.GenericHID;

/**A wrapper for a joystick or controller button that supports toggle, press and release detection, and sensitivity adjustment */
public class Button{
    
    /**Joystick/controller associated with button**/
    private GenericHID owner;
    /**Button number on joystick/controller**/
    private int id;
    
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
    
    /**Updates the button state and outputs**/
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

    /** 
     *This method returns true if the button is in an "on" toggle  (or false if in an "off" toggle) at the time that this method is being called.
     * @return The toggle state of the button.
    */
    public boolean getState(){
        return state;
    }

    /** 
     *This method returns true if the button is being held down at the time that this method is being called.
     * @return The raw output of the button.
    */
    public boolean getOutput(){
        return output;
    }

    /** 
     *This method returns true if the button is pressed exactly at the frame this method is called.
     * @return true on press of the button.
    */
    public boolean onPress(){
        return onPress;
    }

    /** 
     *This method returns true if the button is released exactly at the frame this method is called.
     * @return true on release of the button.
    */
    public boolean onRelease(){
        return onRelease;
    }
}
