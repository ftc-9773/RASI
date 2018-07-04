package org.ftcrobocracy.rasi;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.math.*;

/**
 * Created by Vikesh on 2/20/2018.
 */

public class TeamRasiCommands {
    LinearOpMode linearOpMode;
    Telemetry telemetry;
    public TeamRasiCommands(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        telemetry = linearOpMode.telemetry;
    }

    public void waitForStart(){
        linearOpMode.waitForStart();
    }

    public void TeleWrite(String caption, String message){
        linearOpMode.telemetry.addData(caption, message);
        linearOpMode.telemetry.update();
    }
    public void Wait(double timeInSeconds){
        long startTime = System.currentTimeMillis();
        while(startTime + Math.round(timeInSeconds*1000) > System.currentTimeMillis() && !linearOpMode.isStopRequested()){}
    }
}
