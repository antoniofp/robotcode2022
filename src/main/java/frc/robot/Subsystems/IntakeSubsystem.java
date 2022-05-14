package frc.robot.Subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class IntakeSubsystem extends SubsystemBase {
    private final DoubleSolenoid cylinders = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    private final CANSparkMax intakeMotor = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax feeder = new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);

    public IntakeSubsystem(){  //Done when robot is turned on
        cylinders.set(Value.kForward);
    }

    public void intakeSet(boolean open){  //Toggle when buttons are pressed
        if(open){cylinders.set(Value.kReverse);}
        else{cylinders.set(Value.kForward);}
    }

    public void collectBalls(){ //Collect with motor
        intakeMotor.set(0.4);
    }

    public void returnBall(){
        intakeMotor.set(-0.2);
    }

    public void setOff(){ //Default intake motor state
        intakeMotor.set(0); 
    }

    public void feed(){
        feeder.set(0.5);
    }

    public void throwUp(){
        feeder.set(-0.25);
    }

    public void noFeed(){
        feeder.set(0);
    }

}
