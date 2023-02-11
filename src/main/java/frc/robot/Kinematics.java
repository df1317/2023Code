package frc.robot;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.util.Properties;

import edu.wpi.first.wpilibj.Filesystem;

public class Kinematics {
    
    private Gyro g;
    public double x,y,dx,dy,vx,vy,ax,ay,angle,time,deltaTime;
    private Properties vals;
    private File propFile;
    
    public Kinematics(Gyro g){
        this.g = g;
        vals = new Properties();
        init();
    }

    public void init(){
        x = 300;
        y = 200;
        dx = 0;
        dy = 0;
        vx = 0;
        vy = 0;

         //read initial accelerations,angles,time
        ax = g.getAccelX();
        ay = g.getAccelY();

        angle = g.getGyroZ();

        //keyUpdate();

         deltaTime = System.currentTimeMillis() - time;
         time += deltaTime;

         propFile = new File(Filesystem.getDeployDirectory() + File.pathSeparator + "KinematicsVals.txt");
         System.out.println(propFile.setWritable(true,false));
 

        update();
    }

    public void update(){
        //read time
        deltaTime = System.currentTimeMillis() - time;
        time += deltaTime;
        //calculate displacement
        double rad  = Math.toRadians(angle + 90);
        double a = Math.sqrt(ax*ax + ay*ay);
        double accAngle = Math.atan2(ay, ax) + rad;
        System.out.println((accAngle)%6.28);
        ax = a*Math.sin(accAngle);
        ay = a*Math.cos(accAngle);
        dx = vx*deltaTime/1000 + 0.5*ax*deltaTime*deltaTime/1000/1000;
        dy = vy*deltaTime/1000 + 0.5*ay*deltaTime*deltaTime/1000/1000;

        //calculate absolute position
        x += dx;
        y += dy;

        //calculate velocity
        vx += ax*deltaTime/1000;
        vy += ay*deltaTime/1000;

        //read acceleration,angle
        ax = g.getAccelX();
        ay = g.getAccelY();
        angle = g.getGyroZ();
        
        /*keyUpdate();

        if(x < 0 || x > 54*12*2){
            vx = -vx;
            ax =0;
        }

        if(y < 0 || y > 27*12*2){
            vy = -vy;
            ay = 0;
        }*/
        //vals.setProperty("x", "" + x);
        //vals.setProperty("y", "" + y);
        vals.setProperty("theta", "" + angle);
        System.out.println("angle : "+vals.getProperty("theta"));
       
        try {
            vals.store(new FileWriter(propFile), null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*private void keyUpdate() {
        // System.out.println(ax + "  " + ay);
        
        ax = 0;
        ay = 0;
		if(Main.canv.keys.getValue(37)){
			ax=10;
		}
		
		if(Main.canv.keys.getValue(39) ){
			ax=-10;
		}
		
		if(Main.canv.keys.getValue(38)){
			ay=10;
		}
		
		if(Main.canv.keys.getValue(40)){
			ay=-10;
		}
        if(Main.canv.keys.getValue('A')){
            angle -= 5;
        }

        if(Main.canv.keys.getValue('S')){
            angle += 5;
        }
		
	}*/

    public double getX(){
        return x;
    };

    public double getY(){
        return y;
    };



}