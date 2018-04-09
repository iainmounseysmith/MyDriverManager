package com.selenium.environment;

import io.appium.java_client.service.local.AppiumDriverLocalService;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static com.selenium.environment.HelperClasses.*;
import static java.lang.Thread.sleep;

/**
 * Created by Iain Mounsey-Smith on 30/11/2017.
 */
public class AppiumHelpers {
    //crate a default emulator batch file
    public static void populateEmulatorBatchFile() throws IOException {
        createTextFileAndAddLines(pathToAppiumFiles + "\\emulat"+ defaultAVD +".bat",
                "title " + defaultAVD,"c:","cd C:\\Users\\Owner\\.android\\avd","F:",
                "cd " + pathToAndroidSdkFiles + "\\tools",
                "emulator -avd " + defaultAVD + "-Iain","adb shell input keyevent 82");
    }
    //create batch file for loading node js appium
    public static void populateLoadNoadeAppiumBatchFile() throws IOException {
        createTextFileAndAddLines(pathToAppiumFiles + "\\loadNodeAppium.bat","Title node appium.js"
                ,"c:","cd \"" + pathToLocalAppiumBinFiles + "\""
                , "node appium.js -a localhost -p 4723 -U \"emulator-5554\" --chromedriver-executable " + UserDir + pathToResourcesTools + "\\chromedriver\\chromedriver.exe" + " --nodeconfig \"" + pathToGridFiles + "\\defaultnodeappium.json\"");
       /* , "node appium.js -a localhost -p 4723 -U \"emulator-5554\" --chromedriver-executable " + UserDir + pathToResourcesTools + "\\chromedriver\\chromedriver.exe" + " --nodeconfig \"" + pathToGridFiles + "\\defaultnodeappium.json\"");*/
    }
    //starts the appium node js server
    public static void loadNodeAppiumJSserverold(){
        try {
            Desktop.getDesktop().open(new File("F:\\Iains Work Stuff\\testing\\Selenium\\Appium\\loadNodeAppium.bat"));
            //use this to be consistent with starthub treatment..below
            //runSomethingViaProcesBuilder("cmd.exe", "/c",pathToAppiumFiles,"\\loadNodeAppium.bat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadNodeAppiumJSserver(){
        if (testIfCmdHostedComponentIsRunning("node appium.js") == false){
            try {
                Desktop.getDesktop().open(new File(pathToAppiumFiles + "\\loadNodeAppium.bat"));
                //replace this with runSomethingViaProcesBuilder blah blah..below
                //runSomethingViaProcesBuilder("cmd.exe", "/c",pathToGridFiles,"\\startgridHub.bat");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public static void ifEmulatorNotRunningStartDefaultOne() throws IOException{
          if (execAtCmdPromptADBdevicesAndReturnDeviceID("adb devices").equals("")) {
            System.out.println("I haven't detected an emulator so I will start a default one....." + execAtCmdPromptADBdevicesAndReturnDeviceID("adb devices") + "....that emulator is " + defaultAVD);
            String   theFullPathToTheEmulatBatchFile=pathToAppiumFiles + "\\emulat\"+ defaultAVD +\".bat";
            populateEmulatorBatchFile();
            runSomethingViaProcesBuilder("cmd.exe","/C",pathToAppiumFiles,"\\emulat.bat");
            try {
                sleep(35000); //yes I know that no-one likes sleep statements...it's just that I needed this to give enough time for the emulator to start
                System.out.println("Sleeping to give enough time for emulator to start");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //the following inspired by and courtesy of A K Sahu - http://aksahu.blogspot.co.nz/search/label/Android
            try {
                String[] commandBootComplete = new String[] { pathToAndroidSdkFiles + "\\platform-tools\\adb", "shell", "getprop", "dev.bootcomplete" };
                Process processWaitFor = new ProcessBuilder(commandBootComplete).start();
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(processWaitFor.getInputStream()));
                while (!inputStream.readLine().replaceAll("\\p{Cntrl}&&[^\\r\\n\\t]]\",", "").equals("1")) {
                    //System.out.println("Waiting for dev.bootcomplete=1 - Success");
                    processWaitFor.waitFor(2, TimeUnit.SECONDS);
                    processWaitFor = new ProcessBuilder(commandBootComplete).start();
                    inputStream = new BufferedReader(new InputStreamReader(processWaitFor.getInputStream()));
                }
                System.out.println("Finished waiting for dev.bootcomplete=1 - Success");
                String[] commandBootAnim = new String[] { pathToAndroidSdkFiles + "\\platform-tools\\adb", "shell", "getprop", "init.svc.bootanim" };
                processWaitFor = new ProcessBuilder(commandBootAnim).start();
                inputStream = new BufferedReader(new InputStreamReader(processWaitFor.getInputStream()));

                // wait till the property returns 'stopped'
                while (!inputStream.readLine().equals("stopped")) {
                    //System.out.println("Waiting for init.svc.bootanim=stopped");
                    processWaitFor.waitFor(2, TimeUnit.SECONDS);
                    processWaitFor = new ProcessBuilder(commandBootAnim).start();
                    inputStream = new BufferedReader(new InputStreamReader(processWaitFor.getInputStream()));
                }
                System.out.println("Finished waiting for init.svc.bootanim=stopped - Success");
                System.out.println("Emulator is ready to use!");
            } catch (Exception e) {
                e.printStackTrace();
            }}else{System.out.println("Emulator already running - nothing to do here");}
}}


