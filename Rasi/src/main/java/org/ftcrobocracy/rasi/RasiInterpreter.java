package org.firstinspires.ftc.teamcode.RASI.Rasi;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotDrivers.FTCRobotV1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Main way to run RASI files from inside java
 *
 * construct with
 * RasiInterpreter rasi = new RasiInterpreter("Path/to/file", "filename.rasi", this);
 * (this is a LinearOpMode instance)
 * call rasi.preproccess() to run the file.
 * */
public class RasiInterpreter {

    private boolean  paramsAreNull;
    private int numberOfParams;
    private String LOG_TAG = "RasiInterpreter";
    private RasiLexer rasiParser;
    private LinearOpMode linearOpMode;
    private HashMap<String, String> hashMap;
    public RasiCommands rasiCommands;
    private String methodString;
    private String lcString;
    private String type;
    private String command;
    private String mixedCaseString;
    private String[] parameters;
    private Object[] finalParameters;
    private StringBuilder stringBuilder;
    private HashMap<String, String[]> infoHashmap;
    private HashMap<String, Method> methodsHashMap;
    private boolean hasArguments;
    private Method method;

    private ArrayList<Method> methodQueue = new ArrayList<>();
    private ArrayList<Object[]> paramQueue = new ArrayList<>();

    public RasiInterpreter(String filepath, String filename, LinearOpMode opmode, FTCRobotV1 r){
        this.linearOpMode = opmode;
        rasiCommands = new RasiCommands(opmode, r);
        rasiParser = new RasiLexer(filepath, filename, linearOpMode);
        hashMap = new HashMap<String, String>();
        infoHashmap = new HashMap<String, String[]>();
        methodsHashMap = new HashMap<String, Method>();


        for(int x = 0; x < rasiCommands.getClass().getMethods().length; x++){ //runs for every method in the TeamRasiCommands Class
            if(rasiCommands.getClass().getMethods()[x].toString().contains("RasiCommands.")){ //filters out the stuff that java puts there and hides.
                method = rasiCommands.getClass().getMethods()[x];
                methodString = method.toString();
                stringBuilder = new StringBuilder(methodString); //StringBuilder to format the method text to be more usable.
                int index = 0;
                //remove spaces and close parenthesis:
                while(index < stringBuilder.length()){
                    if(stringBuilder.charAt(index) == ' ' || stringBuilder.charAt(index) == ')'){
                        stringBuilder.deleteCharAt(index);
                    }
                    else{
                        index++;
                    }
                }
                methodString = stringBuilder.toString();
                String[] tempArray = methodString.split("\\(");
                tempArray = tempArray[0].split("\\."); //set mixedCaseString to the name of the method. removes the remaining parenthesis and dots.
                mixedCaseString = tempArray[tempArray.length-1];

                if(methodString.charAt(methodString.length()-1)!= '(') {
                    //make a string of the arguments to the method
                    parameters = methodString.split("\\(");
                    parameters = parameters[1].split(",");
                    hasArguments = true;
                    numberOfParams = parameters.length;
                }
                else{
                    hasArguments= false;
                    parameters = null;
                    numberOfParams = 0;
                }
                lcString = mixedCaseString.toLowerCase();
                hashMap.put(lcString, mixedCaseString);
                infoHashmap.put(mixedCaseString, parameters);
                methodsHashMap.put(mixedCaseString, method);
            }
        }
    }

    public void preproccess() {
        command = rasiParser.getCommand();
        while (!rasiParser.fileEnded && !linearOpMode.isStopRequested()) {
            if (infoHashmap.get(hashMap.get(command.toLowerCase())) != null) {
                paramsAreNull = false;
            } else {
                paramsAreNull = true;
            }
            if (!paramsAreNull) {
                finalParameters = new Object[infoHashmap.get(hashMap.get(command.toLowerCase())).length];
                for (int index = 0; index < finalParameters.length; index++) {
                    type = infoHashmap.get(hashMap.get(command.toLowerCase()))[index];
                    switch (type) {
                        case "int":
                            finalParameters[index] = Integer.valueOf(rasiParser.parameters[index+1]);
                            break;
                        case "char":
                            finalParameters[index] = rasiParser.parameters[index+1].charAt(0);
                            break;
                        case "long":
                            finalParameters[index] = Long.valueOf(rasiParser.parameters[index+1]);
                            break;
                        case "float":
                            finalParameters[index] = Float.valueOf(rasiParser.parameters[index+1]);
                            break;
                        case "double":
                            finalParameters[index] = Double.valueOf(rasiParser.parameters[index+1]);
                            break;
                        case "java.lang.String":
                            finalParameters[index] = rasiParser.parameters[index+1];
                            break;
                        case "boolean":
                            finalParameters[index] = Boolean.valueOf(rasiParser.parameters[index+1]);
                            break;
                    }
                }
            } else {
                finalParameters = null;
            }
            if(finalParameters != null){
                try {
                    String command_lower = command.toLowerCase();
                    String hash = hashMap.get(command_lower);
                    Method method = methodsHashMap.get(hash);
                    appendMethod(method, finalParameters);
                    //method.invoke(rasiCommands, finalParameters);
                } catch (NullPointerException e){
                }
            } else {
                try {
                    String command_lower = command.toLowerCase();
                    String hash = hashMap.get(command_lower);
                    Method method = methodsHashMap.get(hash);
                    appendMethod(method, finalParameters);
                } catch (NullPointerException e) {
                }
            }
            command = rasiParser.getCommand();

        }
    }
    public void runRasi(){
        preproccess();
        run();
    }
    public void run(){
        Method currmethod;
        Object[] param;
        while (!methodQueue.isEmpty() && !paramQueue.isEmpty()){
            currmethod = methodQueue.get(0);
            param      = paramQueue.get(0);
            try {
                currmethod.invoke(rasiCommands, param);
            }catch (Exception e){
                Log.e(LOG_TAG, "Exception occured in Run", e);
            }
            methodQueue.remove(0);
            paramQueue.remove(0);
        }
    }

    public void setTags(String[] Tags){ //sets the rasi tags
        rasiParser.setTags(Tags);
    }
    public void addTag(String tag){ //adds a rasi tag
        rasiParser.addTag(tag);
    }
    public void removeTag(String tag){ // removes a rasi tag
        rasiParser.removeTag(tag);
    }

    private void appendMethod(Method m, Object[] p){
        methodQueue.add(m);
        paramQueue.add(p);
    }
}
