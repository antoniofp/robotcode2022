/** 
 * @author ToñoKUN JorgeMeco BrunoKUN
 * @Version 2.3
 * https://www.youtube.com/watch?v=dQw4w9WgXcQ
 * Java es bullshit
 * */ 

package frc.robot;
//Importing libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder.Type;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
// Creating objects for controllers
  private final XboxController m_driverController = new XboxController(0);
  private final XboxController m_driver2Controller = new XboxController(1);
//Creating objects for mechanims motors
  private CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushless);
  private CANSparkMax intake = new CANSparkMax (6, MotorType.kBrushless);
  private CANSparkMax feeder = new CANSparkMax (7, MotorType.kBrushless);
  private CANSparkMax leftClimber = new CANSparkMax (8, MotorType.kBrushless);
  private CANSparkMax rightClimber = new CANSparkMax (9, MotorType.kBrushless);
//Create chassis object
  Drivetrain drivetrain = new Drivetrain();
// Create encoder objects 
  RelativeEncoder rightClimbEncoder = rightClimber.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder leftClimbEncoder = leftClimber.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder shootEncoder = shooter.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder feederEncoder = feeder.getEncoder(Type.kHallSensor, 42);
// Create solenoid object
  private final DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  
  @Override
  public void robotInit() { //Class used when the robot is on
    CameraServer.startAutomaticCapture();
//keep intake up by default
    doubleSolenoid.set(Value.kForward);
    rightClimber.follow(leftClimber);
    leftClimber.stopMotor();
    rightClimber.stopMotor(); 
  }

  @Override
  public void robotPeriodic() { //Class used all time
    SmartDashboard.putNumber("Shooter Temp", shooter.getMotorTemperature());
    SmartDashboard.putNumber("Climber L", rightClimbEncoder.getPosition());
    SmartDashboard.putNumber("Intake Temp", intake.getMotorTemperature());
  }

  @Override
  public void autonomousInit() { //Class used when the autonomous period is initializated
  }

  @Override
  public void autonomousPeriodic() { //Class used during the autonomous period
  }

  @Override
  public void teleopInit() { //Class used when the teleoperated period is initializated
    drivetrain.brakeChassis();
    leftClimber.setIdleMode(IdleMode.kBrake);
    rightClimber.setIdleMode(IdleMode.kBrake);
  }

  @Override
  public void teleopPeriodic() { 
// Get values from triggers and add both to set multiplier for drivetrain speed
    double leftTriggerGas = m_driverController.getLeftTriggerAxis()*.5;
    double rightTriggerGas = m_driverController.getRightTriggerAxis()*.5;
    double totalGas = leftTriggerGas + rightTriggerGas;
// Take value of x and y coordinates from left joystick to control speed and rotation of drivetrain
    drivetrain.drive(m_driverController.getLeftX(), m_driverController.getLeftY(), totalGas );

// Compact if statement to asign speed values to each button
    double shooterSpeed = (m_driver2Controller.getLeftBumper()) ? 0.8 : 0;
    double intakeSpeed = (m_driver2Controller.getAButton()) ? 0.8 : 0;
    double reverseIntakeSpeed = (m_driver2Controller.getXButton()) ? -0.3 : 0;
    double feederSpeed = (m_driver2Controller.getBButton()) ? 0.7 : 0;
    double reverseFeederSpeed = (m_driver2Controller.getYButton()) ? -0.2 : 0;
    double climbUpSpeed = (m_driverController.getRightBumper()) ? 0.4 : 0;
    double climbDownSpeed = (m_driverController.getLeftBumper()) ? -0.8 : 0;

// Speeds for motors are set using the previous variables
    shooter.set(shooterSpeed);
    intake.set(intakeSpeed + reverseIntakeSpeed);
    feeder.set(feederSpeed + reverseFeederSpeed);
    leftClimber.set(climbUpSpeed + climbDownSpeed);
// Intake is lowered or elevated with D-Pad on controller
    if (m_driver2Controller.getPOV() == 0) {
    doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    } else if (m_driver2Controller.getPOV() == 180) {
      doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }

  @Override
  public void disabledInit(){
    drivetrain.coastChassis();
  }

}
