package frc.robot.Commands;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.Drivetrain;

public class DriveJoystickCmd extends CommandBase {
    private final Drivetrain drivetrainSubsystem;
    private final Supplier <Double> getX, getY, triggerLeft, triggerRight;

    public DriveJoystickCmd(Drivetrain drivetrainSubsystem, Supplier<Double> getX, Supplier<Double> getY, Supplier<Double> triggerLeft, Supplier<Double> triggerRight){  //Constructor, runs only when robot is turned on
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.getX = getX;
        this.getY = getY;
        this.triggerLeft = triggerLeft;
        this.triggerRight = triggerRight;
        addRequirements(drivetrainSubsystem);  //Avoids conflicts of several commands using the same subsystem simoultaneously
    }

    @Override
    public void initialize(){ //Runs once every time command is called
        System.out.println("Arcade drive init");
        drivetrainSubsystem.brakeChassis();
    }

    @Override
    public void execute(){
        drivetrainSubsystem.drive(-getX.get(), -getY.get(), triggerLeft.get(), triggerRight.get());
    }

    @Override
    public void end(boolean interreupted){
        System.out.println("Arcade drive end");
        drivetrainSubsystem.coastChassis();
    }

    @Override
    public boolean isFinished(){
            return false;
    }
    
}
