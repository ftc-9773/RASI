package org.firstinspires.ftc.teamcode.RASIV2;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by vikesh on 12/26/17.
 */

public class RasiParserV2 {
    private String TAG = "TeamRasiCommands";

    private LinearOpMode linearOpMode;

    private File rasiFile;                  //File object for the rasi file
    private Scanner fileScanner;            //Scanner object to read the file line by line
    private StringBuilder commandBuilder;   //StringBuilder object for miscellaneous manipulation

    private String currentCommand;          //The String that will contain the current command
    public String[] parameters;            //The String array that contains the parameters
    private String returnString;            //The String which contains the index to be
    private String Tag;
    private String[] TAGS = new String[0];
    private String[] reservedCommands = {"end", "changetags"};
    private boolean shouldExecute = false;
    private boolean isReservedCommand;

    public RasiParserV2(String filepath, String filename, LinearOpMode linearOpMode){

        this.linearOpMode = linearOpMode;
        //Make sure file extension is rasi
        Log.i(TAG, filepath+filename);
        Log.i(TAG, filename.split("\\.")[1].toLowerCase());
        if(filename.split("\\.")[1].toLowerCase().equals("rasi")){
            rasiFile = new File(filepath + filename);
            Log.i(TAG,filepath+filename);
            try {
                fileScanner = new Scanner(rasiFile);
            }
            catch(FileNotFoundException e){
                Log.e(TAG, "FileNotFoundException");
            }
        }
    }

    private void loadNextCommand(){
        currentCommand = fileScanner.nextLine();
        commandBuilder = new StringBuilder(currentCommand);

        int index = 0;
        while(index < commandBuilder.length()){
            if(commandBuilder.charAt(index) == ' '){
                commandBuilder.deleteCharAt(index);
            }
            else{
                index++;
            }
        }
        if(currentCommand.split(":").length>1) {
            Tag = currentCommand.split(":")[0];
        }
        else{
            Tag = "";
        }
        if(Tag != "") {
            parameters = currentCommand.split(":")[1].split(",");
        }
        else{
            parameters = currentCommand.split(",");
        }
        shouldExecute = false;
        if ((Arrays.asList(TAGS).contains(Tag) || Tag.length() == 0) && !Arrays.asList(reservedCommands).contains(parameters[0])) {
            shouldExecute = true;
            isReservedCommand = false;
        } else if (Arrays.asList(reservedCommands).contains(parameters[0])) {
            shouldExecute = false;
            isReservedCommand = true;
        }

        if(isReservedCommand){
            runReservedCommand(parameters[0]);
        }
    }
    public String getCommand(){
        loadNextCommand();
        while(!shouldExecute && linearOpMode.opModeIsActive()) {
            loadNextCommand();
        }
        return parameters[0];
    }
    public String getParam(int paramNumber){
        return parameters[paramNumber];
    }

    public void runReservedCommand(String command){
        switch (command){
            case "changetags":
                TAGS = new String[parameters.length-1];
                for(int n = 0; n < TAGS.length; n++){
                    TAGS[n] = parameters[n+1];
                }
                break;

            case "end":
                linearOpMode.requestOpModeStop();
                while(linearOpMode.opModeIsActive()){}
                break;
        }
    }
    public void setTags(String rasiTags[]){
        TAGS = rasiTags;
    }

    public void addTag(String rasiTag){
        int y;
        String[] tempTags = TAGS;
        TAGS = new String[tempTags.length+1];
        for(y = 0; y<TAGS.length; y++){
            if(y+1<TAGS.length){
                TAGS[y] = tempTags[y];
            }
            else{
                TAGS[y] = rasiTag;
            }
        }
    }
    public void removeTag(String rasiTag){
        int y;
        String[] tempTags = TAGS;
        if(Arrays.asList(tempTags).contains(rasiTag)){
            TAGS = new String[tempTags.length-1];
            for(y = 0; y<tempTags.length; y++){
                if(!tempTags[y].equals(rasiTag)){
                    TAGS[y] = tempTags[y];
                }
            }
        }
    }
}
