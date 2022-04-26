package frc.robot;
public class Autos extends Robot{
     public void auto1() {
        // Calculate average distance of both drivetrain sides
    double distance = (leftEncoder.getPosition() + rightEncoder.getPosition())/2;
    if (distance<10) {
      m_myRobot.arcadeDrive(0.3, 0);
    }
    else {
  // accelerate shooter for 10 rotations to reach full speed
      while (shootEncoder.getPosition()<10 ) {
        shooter.set(0.8);
      }
  // keep shooter spinning and activate feeder for 5 rotations
      while (feederEncoder.getPosition()<5) {
      shooter.set(0.8);
      feeder.set(.7);
      } 
    }
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    shootEncoder.setPosition(0);
  // once robot has shoot, move out of tarmac doing 5 rotations
    if (distance<5){
      m_myRobot.arcadeDrive(0.3, 0);
    }
  }
}