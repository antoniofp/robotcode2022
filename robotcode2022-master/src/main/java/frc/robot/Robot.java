
package frc.robot;
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
  private final XboxController m_driverController = new XboxController(0);
  private final XboxController m_driver2Controller = new XboxController(1);
  private CANSparkMax m_frontleftMotor = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax m_backleftMotor = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax m_frontrightMotor = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax m_backrightMotor = new CANSparkMax(4, MotorType.kBrushless);
  private CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushless);
  private CANSparkMax intake = new CANSparkMax (6, MotorType.kBrushless);
  private CANSparkMax feeder = new CANSparkMax (7, MotorType.kBrushless);
  private CANSparkMax rightClimber = new CANSparkMax (8, MotorType.kBrushless);
  private CANSparkMax leftClimber = new CANSparkMax (9, MotorType.kBrushless);
  private final MotorControllerGroup leftMotors = new MotorControllerGroup(m_frontleftMotor, m_backleftMotor);
  private final MotorControllerGroup rightMotors = new MotorControllerGroup(m_frontrightMotor, m_backrightMotor);

  // Create object for drive train
  DifferentialDrive m_myRobot = new DifferentialDrive(rightMotors, leftMotors);

  // Create encoder objects 
  RelativeEncoder leftEncoder =  m_frontleftMotor.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder rightEncoder = m_frontrightMotor.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder rightClimbEncoder = rightClimber.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder shootEncoder = shooter.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder feederEncoder = feeder.getEncoder(Type.kHallSensor, 42);

  // Create solenoid object
  private final DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  
  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();

    //keep intake up by default
    doubleSolenoid.set(Value.kForward);
    leftClimber.follow(rightClimber);
    leftClimber.stopMotor();
    rightClimber.stopMotor(); 
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Drivetrain Encoder", leftEncoder.getPosition());
    SmartDashboard.putNumber("Right Drivetrain Encoder", rightEncoder.getPosition());
    SmartDashboard.putNumber("Shooter Temp", shooter.getMotorTemperature());
    SmartDashboard.putNumber("Intake Temp", intake.getMotorTemperature());
  }

  @Override
  public void autonomousInit() {
    // Set encoder positions to zero
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    shootEncoder.setPosition(0);
    feederEncoder.setPosition(0);
    m_backleftMotor.setIdleMode(IdleMode.kBrake);
    m_frontleftMotor.setIdleMode(IdleMode.kBrake);
    m_backrightMotor.setIdleMode(IdleMode.kBrake);
    m_frontrightMotor.setIdleMode(IdleMode.kBrake);
  }

  @Override
  public void autonomousPeriodic() {
    double distance = 1.5; //In meters
    double traveled = rightEncoder.getPosition() * 0.1524;
    double error = distance - traveled;

  }

  @Override
  public void teleopInit() {
    m_backleftMotor.setIdleMode(IdleMode.kBrake);
    m_frontleftMotor.setIdleMode(IdleMode.kBrake);
    m_backrightMotor.setIdleMode(IdleMode.kBrake);
    m_frontrightMotor.setIdleMode(IdleMode.kBrake);
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
    m_myRobot.arcadeDrive(m_driverController.getLeftX()*totalGas, m_driverController.getLeftY()*totalGas);

    if(m_driverController.getAButton()){
      intake.set(0.4);
    }
    else if(m_driverController.getBButton()){
      intake.set(-0.5);
    }
    else{
      intake.set(0);
    }

    // Intake is lowered or elevated with D-Pad
    if (m_driver2Controller.getPOV() == 0) {
      doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    } else if (m_driver2Controller.getPOV() == 180) {
      doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }
}
