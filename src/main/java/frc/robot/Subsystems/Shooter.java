package frc.robot.Subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private final CANSparkMax shooterMotor = new CANSparkMax(5, CANSparkMax.MotorType.kBrushless);

    public void shoot(){
        shooterMotor.set(0.85);
    }

    public void setDefaultSpeed(){
        shooterMotor.set(0);
    }

}
