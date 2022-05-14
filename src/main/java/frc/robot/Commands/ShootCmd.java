package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Shooter;

public class ShootCmd extends CommandBase{
    public final Shooter shooter;

    public ShootCmd(Shooter shooter){
        this.shooter = shooter;
    }

    @Override
    public void initialize(){
    }

    @Override
    public void execute(){
        shooter.shoot();
    }

    @Override
    public void end(boolean interrupted){
        shooter.setDefaultSpeed();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
