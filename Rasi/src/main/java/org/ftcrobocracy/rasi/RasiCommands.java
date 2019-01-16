package org.firstinspires.ftc.teamcode.RASI.Rasi;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Logic.PIDdriveUtil;
import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;

/**
 * Class for defining functions that can be used in RASI. As of version 2.5, there is no way to
 * define functions inside a RASI file.
 *
 * //TODO Create detailed guides and documentation for RASI
 * For a detailed guide on RASI, please see RASI/guides
 *
 * The return type of the function does not matter, and it is best to leave it as void. As of version
 * 2.5, there is no way to store information in a RASI script, all variables should be contained and accessed
 * in this class.
 *
 * RASI is not case sensitive, therefore neither CreateRobot() and CrEaTeRoBot() will cause errors. Use whichever
 * is more readable. RASI has not been tested with overloaded functions, unknown errors may occur.
 *    todo: test this. should be pretty doable
 *
 *
 * Reserved function names and their uses:
 * - addTag      : Add the following argument as a tag for this programs execution
 * - endOpMode   : This command ends the LinearOpMode, as if the user pressed the stop button on the device
 * - removeTag   : This command removes a previously added tag from the possible tags during this programs execution.
 *      The tag can still be added back later.
 * Possible future function names. While they will cause not issues yet, they should not be used to prevent future incompatibilty
 * - addTags    : Works as addTag except that it adds all arguments as tags
 * - removeTags : Works as removeTag except that it removes all arguments from the Tag list
 * - end        : Terminates the execution of the RASI file
 *
 * Note: Rasi 2.5 is not fully backwards compatible with version 2.0. In particular, end has been replaced by endOpMode and semicolons
 * are not used as statement terminators. Statements must be written on one line, and end with a newline. Otherwise,
 * all other functions should work.
 *
 * NOTE: RASI 2.5 DOES NOT SUPPORT FUNCTIONS WITH STRING ARGUMENTS. THIS INCLUDES THE TAG MANIPULATION FUNCTIONS.
 * ----Please add documentation to the functions you create-----
 * @author cadence
 * @version 2.5
 * */

public class RasiCommands {
    private LinearOpMode opMode;
    private Telemetry telemetry;
    private FTCRobotV1 robert;
    private PIDdriveUtil driver;

    public RasiCommands(LinearOpMode o, FTCRobotV1 r){
        this.robert = r;
        this.opMode = o;
        this.telemetry = o.telemetry;
        this.driver = new PIDdriveUtil(robert, opMode);
    }

    public void driveQuick(double dist, double speed){
        driver.driveQuick(dist, speed);
    }

    public void driveEncoder(double dist, double speed){
        driver.driveDistStraight(dist, speed);
    }

    public void drive(double dist, double speed){
        driver.driveDistStraight(dist, speed);
    }

    public void turn(double angle){
        driver.turnToAngle(angle);
    }

    public void drop(){
        robert.lift.unLatchStopper();
        Wait(0.5);
        robert.lift.goToHangPos();
    }

    public void liftLow(){
        robert.lift.goToLowPos();
    }

    public void extendIntake(double dist){
        robert.intake.setPos(dist);
        opMode.telemetry.addLine("Wrote dist " + dist + " to arm");
        opMode.telemetry.update();
    }

    public void dropIntake(){
        robert.intake.setPos(5);
        Wait(1);
    }

    public void strafeTime(double foo, double spam){
        driver.strafeTime(foo, spam);
    }

    public void stop(){
        robert.stop();
        opMode.requestOpModeStop();
    }

    public void driveTime(double x, double y, double t){
        robert.drivebase.drive(x, y, t, false);
        Wait(1);
        robert.drivebase.stop();
    }

    public void write(String msg){
        telemetry.addLine(msg);
        telemetry.update();
    }

    /**
     * Stops executing for a duration of time
     * @param timeInSeconds The amount of seconds to pause execution
     * */
    public void Wait(double timeInSeconds){
        //System.out.println("Waiting for " + timeInSeconds);
        long startTime = System.currentTimeMillis();
        while(startTime + timeInSeconds*1000 > System.currentTimeMillis() && !opMode.isStopRequested()){
            continue;
        }
    }

}
