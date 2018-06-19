package org.firstinspires.ftc.teamcode.infrastructure;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pranavb on 9/23/16.
 */

public class FileRW {
    private static boolean DEBUG = false;
    private static final String TAG = "ftc9773: FileRW";
    File file = null;
    FileReader fileReader = null;
    FileWriter fileWriter = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;
    String fileName;

    public FileRW(String fileName, boolean write){
        this.fileName = fileName;
        try{
            this.file = new File(fileName);
            if(write) {
                if (DEBUG) {
                    Log.e(TAG, "Created a write file "+this.fileName);
                }
                file.createNewFile();
                this.fileWriter = new FileWriter(fileName);
            } else if(!write){
                if (DEBUG) {
                    Log.e(TAG, "Created a read file "+this.fileName);
                }
                this.fileReader = new FileReader(fileName);
            }
        }
        catch (IOException e){
            Log.e(TAG, "An Exception was caught while opening", e);
        }
        if (write) {
            this.bufferedWriter = new BufferedWriter(fileWriter);
        } else if(!write){
            this.bufferedReader = new BufferedReader(fileReader);
        }
    }

    public void fileWrite(String data){
        try{
            bufferedWriter.write(data);
            bufferedWriter.newLine();
        }
        catch(IOException e){
            Log.e(TAG, "An Exception was caught while writing", e);
        }
        if (DEBUG) {
            Log.e(TAG, "wrote into file "+this.fileName);
        }
    }

    public String getNextLine(){
        String data = null;
        try{
            data = bufferedReader.readLine();
        }
        catch(FileNotFoundException ex) {
            Log.e(TAG, String.format(
                    "Unable to open file '%s'", fileName));
        }
        catch (IOException e){
            Log.e(TAG, "An IOException was caught while next line", e);
        }
        if (DEBUG) {
            Log.e(TAG, "read from file "+this.fileName);
        }
        return data;
    }

    public void close(){
        if (DEBUG) {
            Log.e(TAG, "close file "+this.fileName);
        }
        try {
            if(bufferedWriter!=null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            if(bufferedReader!=null)
                bufferedReader.close();
            rmIfZeroSize(); // Delete the file if size is '0';  this happens when we press "init"
            // button repititively without pressing "play".
        } catch (IOException e) {
            Log.e(TAG, "An IOException was caught while closeing", e);
        }
    }

    public void rmIfZeroSize() {
        try {
            if (file.length() <= 0) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTimeStampedFileName(String fileName) {
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String newFileName = fileName.concat(timestamp);
        return (newFileName);
    }
}
