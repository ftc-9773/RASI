package org.firstinspires.ftc.teamcode.RASIV2;

import android.util.Log;

import import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by Vikesh on 2/20/2018.
 */

public class RasiExecutor {

    private boolean isnull;
    private int numberOfParams;
    private String LOG_TAG = "RasiExecutor";
    private RasiParserV2 rasiParser;
    private LinearOpModeCamera linearOpMode;
    private HashMap<String, String> hashMap;
    private TeamRasiCommands teamRasiCommands;
    private String methodString;
    private String lcString;
    private String type;
    private String command;
    private String ucString;
    private String[] parameters;
    private Object[] finalParameters;
    private StringBuilder stringBuilder;
    private HashMap<String, String[]> infoHashmap;
    private HashMap<String, Method> methodsHashMap;
    private boolean hasArguments;
    private Method method;

    public RasiExecutor(LinearOpMode linearOpMode, String filepath, String filename){
        this.linearOpMode = linearOpMode;
        teamRasiCommands = new TeamRasiCommands linearOpMode);
        rasiParser = new RasiParserV2(filepath, filename, linearOpMode);
        hashMap = new HashMap<String, String>();
        infoHashmap = new HashMap<String, String[]>();
        methodsHashMap = new HashMap<String, Method>();

        for(int x = 0; x < teamRasiCommands.getClass().getMethods().length; x++){ //runs for every method in the TeamRasiCommands Class
            //Log.d("RasiExecutor", Integer.toString(x));

            if linearOpMode.isStopRequested())
                break;

            if(teamRasiCommands.getClass().getMethods()[x].toString().contains("TeamRasiCommands.")){ //filters out the stuff that java puts there and hides.
                method = teamRasiCommands.getClass().getMethods()[x];
                methodString = method.toString();
                //Log.d("Rasi_MethodString1", methodString);
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
                //Log.d("RasiExecutor",methodString);
                String[] tempArray = methodString.split("\\(");
                tempArray = tempArray[0].split("\\."); //set ucstring to the name of the method. removes the remaining parenthesis and dots.
                ucString = tempArray[tempArray.length-1];
                //Log.d("rasiucstring", ucString);
                //Log.d("Rasi_MethodString2", methodString);
                if(methodString.charAt(methodString.length()-1)!= '(') {
                    //make a string of the arguments to the method
                    parameters = methodString.split("\\(");
                    parameters = parameters[1].split(",");
                    hasArguments = true;
                    //Log.d(LOG_TAG, "parameters initialized to actual parameters");
                    numberOfParams = parameters.length;
                    //Log.d("rasi_parameters", Arrays.asList(parameters).toString());
                }
                else{
                    hasArguments= false;
                    parameters = null;
                    //Log.i(LOG_TAG, "Parameters set to empty array");
                    numberOfParams = 0;
                }
                lcString = ucString.toLowerCase();
                //Log.d("rasilcString", lcString);
                hashMap.put(lcString, ucString);
                if(parameters != null)
                    Log.i("RasiExecutor", parameters.toString());
                //Log.i("RasiExecutor", ucString);
                infoHashmap.put(ucString, parameters);
                //Log.i(LOG_TAG, "added parameters array, length: " + Integer.toString(numberOfParams));
                methodsHashMap.put(ucString, method);
            }
        }
    }

    public void runRasi() {
        while ( linearOpMode.isStopRequested()) {
            command = rasiParser.getCommand();
            //Log.d("RasiCommand", hashMap.get(command.toLowerCase()));
            //Log.d("infohashmapout", infoHashmap.get(hashMap.get(command.toLowerCase())).toString());
            if (infoHashmap.get(hashMap.get(command.toLowerCase())) != null) {
                isnull = false;
            } else {
                isnull = true;
            }
            //Log.d("rasiisnull", Boolean.toString(isnull));
            if (!isnull) {
                finalParameters = new Object[infoHashmap.get(hashMap.get(command.toLowerCase())).length];
                for (int index = 0; index < finalParameters.length; index++) {
                    type = infoHashmap.get(hashMap.get(command.toLowerCase()))[index];
                    switch (type) {
                        case "int":
                            finalParameters[index] = Integer.valueOf(rasiParser.parameters[index+1]);
                            //Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Int" + rasiParser.parameters[index]);
                            break;
                        case "char":
                            finalParameters[index] = rasiParser.parameters[index+1].charAt(0);
                            //Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Char" + rasiParser.parameters[index]);
                            break;
                        case "long":
                            finalParameters[index] = Long.valueOf(rasiParser.parameters[index+1]);
                            //Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Long" + rasiParser.parameters[index]);
                            break;
                        case "float":
                            finalParameters[index] = Float.valueOf(rasiParser.parameters[index+1]);
                            //Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Float" + rasiParser.parameters[index]);
                            break;
                        case "double":
                            finalParameters[index] = Double.valueOf(rasiParser.parameters[index+1]);
                            //Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Double" + rasiParser.parameters[index]);
                            break;
                        case "java.lang.String":
                            finalParameters[index] = rasiParser.parameters[index+1];
                            //Log.i(LOG_TAG + "Parameter is String", rasiParser.parameters[index]);
                            break;
                        case "boolean":
                            finalParameters[index] = Boolean.valueOf(rasiParser.parameters[index+1]);
                            //Log.i(LOG_TAG + "parameter number" + Integer.toString(index) + ":", "Boolean" + rasiParser.parameters[index]);
                            break;
                    }
                }
            } else {
                finalParameters = null;
                //Log.d("finalparamsisnull", "true");
            }
            if(finalParameters != null)
              //  Log.d("rasifinalparams", Arrays.asList(finalParameters).toString());

            /*try {
                method = teamRasiCommands.getClass().getMethod(hashMap.get(command.toLowerCase()));

            } catch (NoSuchMethodException e) {
                Log.e("RasiExecutor", "NoSuchMethodException");
            }*/
            //Log.d("RasiExecutor", method.toString());
            try {
                methodsHashMap.get(hashMap.get(command.toLowerCase())).invoke(teamRasiCommands, finalParameters);
            } catch (IllegalAccessException e) {
                Log.e("rasiExecutor", "illegalAccessException");
            } catch (InvocationTargetException e) {
                Log.e("rasiExecutor", "InvocationTargetException");
            }
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
}
