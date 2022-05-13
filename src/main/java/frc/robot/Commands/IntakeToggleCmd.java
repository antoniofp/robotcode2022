package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.IntakeSubsystem;

public class IntakeToggleCmd  extends CommandBase{
    private final IntakeSubsystem intakeSubsystem;
    private final boolean open;

    public IntakeToggleCmd(IntakeSubsystem intakeSubsystem, boolean open){
        this.intakeSubsystem = intakeSubsystem;
        this.open = open;
        addRequirements(intakeSubsystem);
    }
    
    @Override
    public void initialize(){ //Runs once every time command is called
        System.out.println("Intake toggled!");
    }

    @Override
    public void execute(){
        intakeSubsystem.intakeSet(open);
    }

    @Override
    public void end(boolean interreupted){
    }

    @Override
    public boolean isFinished(){
            return false;
    }
}
