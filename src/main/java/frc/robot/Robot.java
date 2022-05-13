
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
  RobotContainer container;

  @Override
  public void robotPeriodic(){
    CommandScheduler.getInstance().run();
  }

  @Override
  public void robotInit() { 
    container = new RobotContainer();
  }

  @Override
  public void autonomousInit() { 
    container.getAutonomousCommand().schedule();
  }

  @Override
  public void autonomousPeriodic() {
    //CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() { 
    if (container.getAutonomousCommand() != null) {
      container.getAutonomousCommand().cancel();
    }
  }

  @Override
  public void teleopPeriodic() { 
  }

  @Override
  public void disabledInit(){
  }
}