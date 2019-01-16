package org.firstinspires.ftc.teamcode.RASI.Rasi;

//
// import android.support.annotation.NonNull;
// import android.util.
//
// import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
// import org.firstinspires.ftc.robotcore.external.Telemetry;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * This class acts as a lexer for RASI files to be used by RasiInterpreter
 * In general, this class should not be used on its own.
 * If you would like File input and Output, see FileRW or built in java utilities
 *
 * For more detail on RASI itself, see RasiCommands and RASI/guides/*
 * TODO: create guides and documentation
 *
 * @author vikesh cadence
 * @version 2.5
 */
public class RasiLexer {
    //Tag to use for telemetry
    private String TAG = "RasiCommands:";
    private LinearOpMode opMode;

    //Toggle for whether to send stuff to debug
    private static boolean DEBUG = true;

    //File io utilities
    private File rasiFile;                  //File object for the rasi file
    private Scanner fileScanner;            //Scanner object to read the file line by line
    public boolean fileEnded = false;

    //Objects to be returned to RasiInterpreter and Utilities for building them
    private StringBuilder commandBuilder;   //StringBuilder object for miscellaneous manipulation
    private String currentCommand;          //The String that will contain the current command
    public String[] parameters;            //The String array that contains the parameters

    //Utilites for managing tags and reserved commands.
    private String Tag;
    private String[] TAGS = new String[0];
    private String[] reservedCommands = {"changetags", "addtag", "removetag", "endOpMode"};
    private boolean shouldExecute = false;
    private boolean isReservedCommand;

    //LinearOpMode
    /**
     * Initialise lexer to a file located at filepath, with name filename
     *
     * @param filepath Path to rasi file
     * @param filename name of rasi file, including the extension .rasi
     * @param opmode LinearOpMode
     * */
    public RasiLexer(String filepath, String filename, LinearOpMode opmode){
        opMode = opmode;
        //Make sure file extension is rasi
        if(filename.split("\\.")[1].toLowerCase().equals("rasi")){
            rasiFile = new File(filepath + filename);

            try {
                fileScanner = new Scanner(rasiFile);
            } catch(FileNotFoundException e){
                 Log.e(TAG, "File " + rasiFile + " not found.", e);
            }
      } else {

      }
    }

    /**
     * Gets the next command from the file and stores it in currentCommand
     * If it is reserved, it runs it.
     * */
    private void loadNextCommand(){
        //TODO: this currently might end the file whenever a blankline is found. also can use scanner.hasNextLine
        try {
        currentCommand = fileScanner.nextLine();
        }
        catch (NoSuchElementException e) {
            fileEnded = true;
            return;
        }

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

        if(currentCommand.split(":").length>1) {Tag = currentCommand.split(":")[0];}
        else{Tag = "";}

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
    /**
     * Return the next command in file
     * */
    public String getCommand(){
        if (fileEnded)
            return null;
        loadNextCommand();
        while(!shouldExecute && !fileEnded) {
            loadNextCommand();
        }
        if (fileEnded){
            return null;
        }
        return parameters[0];
    }
    public String getParam(int paramNumber){
        return parameters[paramNumber];
    }

    /**
     * Run a reserved command
     * */
    public void runReservedCommand(String command){
        switch (command){
            case "changetags":
                TAGS = new String[parameters.length-1];
                for(int n = 0; n < TAGS.length; n++){
                    TAGS[n] = parameters[n+1];
                }
                break;
            case "addtag":
                addTag(parameters[1]);
                break;
            case "removetag":
                removeTag(parameters[1]);
                break;
            case "endOpMode":
                opMode.requestOpModeStop();
                while(opMode.opModeIsActive()){}
                break;
            case "end":
                fileEnded = true;
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
    public void debug(String msg){

    }
    public void debug(String msg, Exception e){

    }
}
