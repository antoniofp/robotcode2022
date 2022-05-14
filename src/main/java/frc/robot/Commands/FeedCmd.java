package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.IntakeSubsystem;

public class FeedCmd extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;
    private boolean feed;

    public FeedCmd(IntakeSubsystem intakeSubsystem, boolean feed){
        this.intakeSubsystem = intakeSubsystem;
        this.feed = feed;
        addRequirements(intakeSubsystem);
    }
    @Override
    public void initialize(){
    }

    @Override
    public void execute(){
        if(feed){intakeSubsystem.feed();}
        else{intakeSubsystem.throwUp();}
    }

    @Override
    public void end(boolean interreupted){
        intakeSubsystem.noFeed();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
    
}
