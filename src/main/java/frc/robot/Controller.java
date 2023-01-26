package frc.robot;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import edu.wpi.first.wpilibj.GenericHID;

public class Controller extends GenericHID{
    private Button[] buttons;
    private int[] map;
    private Properties config;
    
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
    
    //Updates controller settings and button mappings to match the config file
    public void runConfig() throws FileNotFoundException, IOException {
        config = new Properties();
        config.load(new FileInputStream("src\\main\\java\\frc\\robot\\ControllerConfig.cfg"));
        Constants.sensitivity = Integer.parseInt(config.getProperty("sensitivity"));
        
        for(int i = 1; i < map.length; i++){
            map[i] = Integer.parseInt(config.getProperty("button" + i));
        }

        System.out.println("Map: " + Arrays.toString(map));
    }
    
    public boolean getButton(int id){
        return buttons[id].getOutput();
    }

    public boolean getButtonState(int id){
        return buttons[id].getState();
    }


    public void update(){
        for(int i = 1; i < buttons.length; i++){
            buttons[i].update();
        }
    }

    public boolean getButtonOutput(int id){
        return buttons[map[id]].getOutput();
    }
}