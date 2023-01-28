package frc.robot;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;

/**An instance of a joystick or controller that maps its buttons based on associated config file */
public class Controller extends GenericHID{
    private Button[] buttons;
    private int[] map;
    private Properties config;
    
    /**
     * Construct an instance of a joystick or controller.
     *  @param port The port index on the Driver Station that the device is plugged into.
     * **/
    public Controller(int port){
        super(port);
        System.out.println(this.getButtonCount());
        //Wraps each button on the joystick with a Button object 
            //Note that button count starts at 1
        buttons = new Button[this.getButtonCount()+1];
        for(int i = 1; i < buttons.length; i++){
            buttons[i] = new Button(this, i);
        }
        
        //Configures intial button mapping
        map = new int[buttons.length];
        try {
            runConfig();
        } catch (FileNotFoundException e) { e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
    }
    
    /**Updates controller settings and button mappings to match its config file**/
    public void runConfig() throws FileNotFoundException, IOException {
        config = new Properties();
        config.load(new FileInputStream(Filesystem.getDeployDirectory() + "\\ControllerConfig" + this.getPort() + ".cfg"));
        Constants.sensitivity = Integer.parseInt(config.getProperty("sensitivity"));
        
        for(int i = 1; i < map.length; i++){
            map[Integer.parseInt(config.getProperty("button" + i))] = i;
        }

        System.out.println("Map: " + Arrays.toString(map));
    }
    
    /** 
     * Updates all buttons on the controller
    */
    public void update(){
        for(int i = 1; i < buttons.length; i++){
            buttons[i].update();
        }
    }

    /** 
     *This method returns true if the button is in an "on" toggle  (or false if in an "off" toggle) at the time that this method is being called.
     * @param id The function button number to be read (starting at 1) (will be mapped to a joystick button)
     * @return The toggle state of the button.
    */
    public boolean getButtonState(int id){
        return buttons[map[id]].getState();
    }

    /** 
     *This method returns true if the button is being held down at the time that this method is being called.
     * @param id The function button number to be read (starting at 1) (will be mapped to a joystick button)
     * @return The raw output of the button.
    */
    public boolean getButtonOutput(int id){
        return buttons[map[id]].getOutput();
    }

    /** 
     *This method returns true if the button is pressed exactly at the frame this method is called.
     * @param id The function button number to be read (starting at 1) (will be mapped to a joystick button)
     * @return true on press of the button.
    */
    public boolean onButtonPress(int id){
        return buttons[map[id]].onPress();
    }

    /** 
     *This method returns true if the button is released exactly at the frame this method is called.
     * @param id The function button number to be read (starting at 1) (will be mapped to a joystick button)
     * @return true on release of the button.
    */
    public boolean onButtonRelease(int id){
        return buttons[map[id]].onRelease();
    }
}