package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;

public class LED {
private AddressableLED m_led;
  private AddressableLEDBuffer m_ledBuffer;
  // Store what the last hue of the first pixel is
  private int m_rainbowFirstPixelHue;
  private int t = 0;

  private int numLoops = 0;

  public void initLED() {
    m_led = new AddressableLED(9);

    // Reuse buffer
    // Default to a length of 60, start empty output
    // Length is expensive to set, so only set it once, then just update data
    m_ledBuffer = new AddressableLEDBuffer(300);
    m_led.setLength(m_ledBuffer.getLength());
    // Set the data
    m_led.setData(m_ledBuffer);
    m_led.start();
}

  public void rainbow() {
    // For every pixel
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      // Set the value
      m_ledBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
  }

  public void purpleSolid() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      m_ledBuffer.setRGB(i, 128, 0, 128);
   }
   
   m_led.setData(m_ledBuffer);
  }

  public void purpleFade() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      m_ledBuffer.setRGB(i, 64 + (int)(64*Math.cos(t*Math.PI/36)), 0, 64 + (int)(64*Math.cos(t*Math.PI/36)));
   }
   
   m_led.setData(m_ledBuffer);
  }

  public void purpleChaser(int number, int size){
      if(size > m_ledBuffer.getLength()/(number*2)){
        size = m_ledBuffer.getLength()/(number*2);
      }
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Sets the specified LED to the RGB values for red
      if((i + t) % (m_ledBuffer.getLength()/(number)) < size){
        m_ledBuffer.setRGB(i, 128, 0, 128);
      }else{
        m_ledBuffer.setRGB(i, 0, 0, 0);
      }
   }
   
   m_led.setData(m_ledBuffer);
  }

  public void greenSwitcher(){
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i,0,128*((i+t/10)%2),0);
    }
  }

  public void runLED() {
    if(Drivetrain.aligning){
      greenSwitcher();
    }else if(Drivetrain.balanced){
      rainbow();
    }else if(DriverStation.isAutonomousEnabled()){
      purpleFade();
    }else if(DriverStation.isTeleopEnabled()){
      purpleChaser(2, 50);
    }else{
      purpleSolid();
    }
    m_led.setData(m_ledBuffer);
    t++;
  }
}
