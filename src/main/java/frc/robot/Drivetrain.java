package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder.Type;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain {
  //Creating objects for chassis motors
  private CANSparkMax m_frontleftMotor = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax m_backleftMotor = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax m_frontrightMotor = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax m_backrightMotor = new CANSparkMax(4, MotorType.kBrushless);
  //Grouping Left chassis motors and right chassis motors
  private final MotorControllerGroup leftMotors = new MotorControllerGroup(m_frontleftMotor, m_backleftMotor);
  private final MotorControllerGroup rightMotors = new MotorControllerGroup(m_frontrightMotor, m_backrightMotor);
  // Create object for drive train
  DifferentialDrive m_myRobot = new DifferentialDrive(rightMotors, leftMotors);

  //Encoders
  RelativeEncoder leftEncoder =  m_frontleftMotor.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder rightEncoder = m_frontrightMotor.getEncoder(Type.kHallSensor, 42);

  public void brakeChassis(){
    m_backleftMotor.setIdleMode(IdleMode.kBrake);
    m_frontleftMotor.setIdleMode(IdleMode.kBrake);
    m_backrightMotor.setIdleMode(IdleMode.kBrake);
    m_frontrightMotor.setIdleMode(IdleMode.kBrake);
  }

  public void coastChassis(){
    m_backleftMotor.setIdleMode(IdleMode.kCoast);
    m_frontleftMotor.setIdleMode(IdleMode.kCoast);
    m_backrightMotor.setIdleMode(IdleMode.kCoast);
    m_frontrightMotor.setIdleMode(IdleMode.kCoast);
  }

  public void drive(double x, double y, double gas){
    m_myRobot.arcadeDrive(x * gas, y * gas);
  }

  public double rotateTo(double actualAngle, double desiredAngle){
    double angle = 180;

    return angle;
  }

  public void auto(){

  }

}
