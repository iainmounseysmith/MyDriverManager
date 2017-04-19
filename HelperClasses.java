package com.selenium.environment;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
//import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static com.selenium.environment.MyDriverManager.aDriver;
import static com.selenium.redirect_tests.testRedirectionViaFoundListofUserAgentStrings.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Iain Mounsey-Smith on 22/10/2015.
 */
public class HelperClasses {
    private static String theServerURLReturned;
    //public static WebDriver aDriver = null;
    //private static String gridHostURL = "http://localhost:4444/wd/hub";
    private static Boolean isGridRunningDecision =null;
    public static String pathToResourcesTools="\\src\\test\\resources\\tools";
    public static String pathToSeleniumGridServerNodeBatchFiles="F:\\Iains Work Stuff\\testing\\Selenium\\Grid";
    public static String fireBugNumericVersion="2.0.18";
    public static JavascriptExecutor js;


    public static void outputText(String text) {
        System.out.println(text);
    }

    public static void println(String text) {
        System.out.println(text);
    }

    public static void debugLine() {
        System.out.println();
    }

    @Test //get capabilities and take snapshot if capability available
    public static void takeSnapShotold(WebDriver driver) {

        String screenShotPrefix = "CapturedScreenShot-"; //filename prefix
        String ScreenShotLocation = "C:\\temp\\screenshots";
        try {
            TakesScreenshot snapper = (TakesScreenshot) driver;
            if (((HasCapabilities) driver).getCapabilities().is(CapabilityType.TAKES_SCREENSHOT)) {//test takes_screenshot capability exists
                File tempcsreenshot = snapper.getScreenshotAs(OutputType.FILE);//creates temp file
                File screenshotsDirectory = new File(ScreenShotLocation);
                if (screenshotsDirectory.exists()){
                    /*nothing to see here ..move on*/
                }else{
                    screenshotsDirectory.mkdirs();//this creates the directory specified above
                };
                screenshotsDirectory.mkdirs();//this creates the directory specified above
                Long uniqueSystemTimeInMillis = System.currentTimeMillis();//create unique part of filename
                File theScreenShot = new File((screenshotsDirectory.toString()) + "\\" + screenShotPrefix + uniqueSystemTimeInMillis.toString() + (".png"));
                try {
                    FileUtils.moveFile(tempcsreenshot, theScreenShot);
                } catch (IOException e) {/*nothing to see here...move on*/}
                assertTrue("Asserting that the file is created...", theScreenShot.exists());

            } else {
                fail("Adriver does not support snapshots!!!");
            }
        }catch(ClassCastException e){//just incase the Adriver no longer supports casting
            fail("Adriver no longer supports casting!!!");
        }
    }
    @Test //if capability available,get capabilities and take snapshot in FILE format
    public static void takeSnapShotFILE(WebDriver driver) {

        String screenShotPrefix = "CapturedScreenShot-"; //filename prefix
        String ScreenShotLocation = "C:\\temp\\screenshots";
        try {
            TakesScreenshot snapper = (TakesScreenshot) driver;
            if (((HasCapabilities) driver).getCapabilities().is(CapabilityType.TAKES_SCREENSHOT)) {//test takes_screenshot capability exists
                File tempcsreenshot = snapper.getScreenshotAs(OutputType.FILE);//creates temp file
                File screenshotsDirectory = new File(ScreenShotLocation);
                //if (screenshotsDirectory.exists()){}else{};
                if (screenshotsDirectory.exists()){
                    /*nothing to see here ..move on*/
                }else{
                    System.out.println("Snapshot directory " + ScreenShotLocation + " does not exist so have created it");
                    screenshotsDirectory.mkdirs();//this creates the directory specified above if it does not already exist
                };
                Long uniqueSystemTimeInMillis = System.currentTimeMillis();//create unique part of filename
                File theScreenShot = new File((screenshotsDirectory.toString()) + "\\" + screenShotPrefix + uniqueSystemTimeInMillis.toString() + (".png"));
                try {
                    FileUtils.moveFile(tempcsreenshot, theScreenShot);
                } catch (IOException e) {/*nothing to see here...move on*/}
                assertTrue("Asserting that the file is created...", theScreenShot.exists());

            } else {
                fail("Adriver does not support snapshots!!!");
            }
        }catch(ClassCastException e){//just incase the Adriver no longer supports casting
            fail("Adriver no longer supports casting!!!");
        }
    }
    @Test //if capability available,get capabilities and take snapshot in BYTES format
    public static void takeSnapShotBYTES(WebDriver driver) {
        String screenShotPrefix = "CapturedScreenShot-"; //filename prefix
        String ScreenShotLocation = "C:\\temp\\screenshots";
        try {
            TakesScreenshot snapper = (TakesScreenshot) driver;
            if (((HasCapabilities) driver).getCapabilities().is(CapabilityType.TAKES_SCREENSHOT)) {//test takes_screenshot capability exists
                byte[] tempcsreenshotAsBYTES = snapper.getScreenshotAs(OutputType.BYTES);//creates temp file
                File screenshotsDirectory = new File(ScreenShotLocation);
                //if (screenshotsDirectory.exists()){}else{};
                if (screenshotsDirectory.exists()){
                    /*nothing to see here ..move on*/
                }else{
                    screenshotsDirectory.mkdirs();//this creates the directory specified above if it does not already exist
                    System.out.println("Snapshot directory " + ScreenShotLocation + " does not exist so have created it");
                };
                Long uniqueSystemTimeInMillis = System.currentTimeMillis();//create unique part of filename
                File theScreenShot = new File((screenshotsDirectory.toString()) + "\\" + screenShotPrefix + uniqueSystemTimeInMillis.toString() + (".png"));
                try {FileOutputStream osf = new FileOutputStream(theScreenShot);//need output stream as we're using BYTES format
                    osf.write(tempcsreenshotAsBYTES);
                    osf.flush();
                } catch (IOException e) {/*nothing to see here...move on*/}
                assertTrue("Asserting that the file is created...", theScreenShot.exists());

            } else {
                fail("Adriver does not support snapshots!!!");
            }
        }catch(ClassCastException e){//just incase the Adriver no longer supports casting
            fail("Adriver no longer supports casting!!!");
        }
    }
    @Test
    public static WebDriver customiseFireFox()  {
        //create path via user.dir and firebug filename
        String s = File.separator;
        //TODO detect if firebug is installed, and only install if not
        String extensionPath = System.getProperty("user.dir") +
                String.format("%ssrc%stest%sresources%stools%sfirebug%s%s",s,s,s,s,s,s,"firebug-" + fireBugNumericVersion + "-fx.xpi");
        System.out.println("Using FireBug from "+ extensionPath);
        FirefoxProfile profile = new FirefoxProfile(); //create new profile
        //profile.setEnableNativeEvents(true); //set setEnableNativeEvents to true...remmed out 11/4/17 as this now deprecated
        //stop firebug showing the first run screen by setting the last version
        //10/4/17profile.setPreference("extensions.firebug.currentVersion", fireBugNumericVersion);
        //if
        //profile.setPreference("general.useragent.override", "some UA string");
        //enable all firebug pages including NET
        //firebug preferences doco - http://getfirebug.com/wiki/index.php/Firebug_Preferences
        //10/4/17profile.setPreference("extensions.firebug.net.enableSites", true);
        // add the extension to firefox


        /* rem this out s causing issues...stil need this?...used to work ok
        try {

            profile.addExtension(new File(extensionPath));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        DesiredCapabilities capabilities = new DesiredCapabilities();//added this so that I could use full firefoxdriver constructor
        FirefoxBinary binary = new FirefoxBinary();//added this so that I could use full firefoxdriver constructor
        MyDriverManager.aDriver = new FirefoxDriver(binary,profile,capabilities);//added binary and capabiliites object so that I could test using them in constructor
        return MyDriverManager.aDriver;
        //driver.get("http://www.compendiumdev.co.uk/selenium/basic_html_form.html");
        //assertThat(driver.getTitle(), is("HTML Form Elements"));
    }
    @Test
    public static WebDriver customiseChrome(){
        String DeterminedLocationOfChromeDriverExecutable="";
        String UserDir = System.getProperty("user.dir"); //get user directory
        String FullPathToExecutableInResources = UserDir + pathToResourcesTools + "\\chromedriver\\chromedriver.exe";//add resources part of directory to string
        String FullPathToExecutableInFileSystem = "H:\\testing\\Chrome\\chromedriver.exe";//this is an alternate location for the driver
        File myfile = new File(FullPathToExecutableInResources);
        File myfile2 = new File(FullPathToExecutableInFileSystem);
        System.out.println(myfile.exists());
        if (myfile.exists() == true) {
            //System.out.println("it exists");
            DeterminedLocationOfChromeDriverExecutable = myfile.getAbsolutePath();
            //System.out.println("abs resources " + DeterminedLocationOfChromeDriverExecutable);
        } else {
            System.out.println("NOT  exists at " + myfile);
            if (myfile2.exists() == true) {
                //System.out.println("it exists at " + myfile2);
                DeterminedLocationOfChromeDriverExecutable = myfile2.getAbsolutePath();
                //System.out.println("abs h " + DeterminedLocationOfChromeDriverExecutable);
            }
        else{fail("A chromedriver.exe cannot be found either at " + FullPathToExecutableInResources + " or in " +FullPathToExecutableInFileSystem + "/n. Download it from " +
                    "https://sites.google.com/a/chromium.org/chromedriver/downloads" + "/n and place it in " +DeterminedLocationOfChromeDriverExecutable  );

            }
        }
       System.setProperty("webdriver.chrome.driver", DeterminedLocationOfChromeDriverExecutable); //set location
        System.out.println("Executing Google Chrome driver from " + DeterminedLocationOfChromeDriverExecutable);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-plugins");
        options.addArguments("disable-extensions");
        //options.setBinary("H:/testing/Chrome/"); //optionally chnage path to the binary
        options.addArguments("start-maximised"); //start chrome maximised
        //options.addExtensions(path to extension here);//add extensions here e.g. options.addExtensions(new File("/path/to/extension.crx"));
        //https://sites.google.com/a/chromium.org/chromedriver/extensions

        MyDriverManager.aDriver = new ChromeDriver(options);
        return MyDriverManager.aDriver;
    }
    @Test
    public static WebDriver customiseIE(){
        String UserDir = System.getProperty("user.dir"); //get user directory
        String FullPathToExecutableInResources = UserDir + pathToResourcesTools + "\\IE\\IEDriverServer.exe"; //add resources part of directory to string
        //System.out.println("IEPropertiesLocation " + IEPropertiesLocation);
        File myIEDriverServerExe = new File(FullPathToExecutableInResources);
        if (myIEDriverServerExe.exists() == true) {
                 }
        else{fail("A IEDriverServer.exe cannot be found at " + FullPathToExecutableInResources + "/n. Download it from " + "http://www.seleniumhq.org/download/ /n and place it in " +
                FullPathToExecutableInResources+ "\\IE\\");
        }
        System.setProperty("webdriver.ie.driver", FullPathToExecutableInResources); //set location
        MyDriverManager.aDriver = new InternetExplorerDriver();
        return MyDriverManager.aDriver;
    }
    @Test
    public static WebDriver customisePhantom(){
        String UserDir = System.getProperty("user.dir"); //get user directory
        String FullPathToExecutableInResources = UserDir + pathToResourcesTools + "\\phantom\\phantomjs.exe"; //add resources part of directory to string
        //System.out.println("IEPropertiesLocation " + IEPropertiesLocation);
        File myPhantomJSExe = new File(FullPathToExecutableInResources);
        if (myPhantomJSExe.exists() == true) {
        }
        else{fail("A PhantomJSExe.exe cannot be found.\n. Download it from \n http://phantomjs.org/download.html /n and place it in " + FullPathToExecutableInResources + "\\phantom\\");
        }
        System.setProperty("webdriver.ie.phantom", FullPathToExecutableInResources); //set location
        // String[] cli_args = new String[]{ "--proxy-type=none" };//added to fix 263 erros...didn't work
        //String[] cli_args = new String[]{ " --ignore-ssl-errors=true"};//added to fix 263 erros...didn't work
        File phantomFile = new File(FullPathToExecutableInResources );
        DesiredCapabilities  capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        //capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cli_args);//added to fix 263 erros...didn't work
        System.out.println(FullPathToExecutableInResources);
        capabilities.setCapability("phantomjs.binary.path", FullPathToExecutableInResources);
        if (System.getProperty("phantomjs.page.settings.userAgent")!=""){
            capabilities.setCapability("phantomjs.page.settings.userAgent", useThisUserAgent2);
        }
        MyDriverManager.aDriver = new PhantomJSDriver(capabilities);
       return MyDriverManager.aDriver;
        //create another method with alternate signature - accepts arguments specifying UA string....run customisePhantom()
    }

    @Test
    public static WebDriver customiseMarionette(){
        /*
        Marionette driver .09 works with selenium 2.x NOT 3.x.
        Marionette driver .10 works with selenium 3.x
         */
        String currentDir = System.getProperty("user.dir");
        String wiresExecutable = "geckodriver.exe";
        String wiresExecutable2 = "wires.exe";//not needed for later driver
        String marionetteDriverLocation = currentDir + "\\src\\test\\resources\\tools\\GeckoDriver\\";

        File myWiresExe = new File(marionetteDriverLocation+ wiresExecutable);
        if (myWiresExe.exists() == true) {
           System.setProperty("webdriver.gecko.driver", marionetteDriverLocation + wiresExecutable);
            System.setProperty("webdriver.firefox.marionette", marionetteDriverLocation + wiresExecutable);
            System.out.println("drivcer " + System.getProperty("webdriver.gecko.driver"));

        }
        else{fail("geckodriver.exe cannot be found.\nDownload it from \n https://github.com/mozilla/geckodriver/releases \n and place it in " + marionetteDriverLocation );
        }

        //When I wrote this(Aug2016) gecko needed to know where the firefox executable is
        String fireFoxExecutable = "firefox.exe";
        String fireFoxExecutableLocation = "C:\\Program Files (x86)\\Mozilla Firefox\\";
        File myFireFoxExe = new File(fireFoxExecutableLocation + fireFoxExecutable);
        if (myFireFoxExe.exists() == true) {
            System.setProperty("webdriver.firefox.bin", fireFoxExecutableLocation + fireFoxExecutable);
        }
        else{fail("FireFox.exe cannot be found.\nSearch for firefox.exe on your c drive \nand set string fireFoxExecutableLocation to that location");
        }

        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
        //11/4/17 - changed from marionettedriver to firefox driver
        MyDriverManager.aDriver = new FirefoxDriver();
        return MyDriverManager.aDriver;
    }
    @Test
    public static WebDriver  customiseEDGE(){
        System.out.println("entering customiseedge");
        DesiredCapabilities capability = DesiredCapabilities.edge();
        System.out.println("remote host name " + System.getProperty(MyDriverManager.REMOTE_BROWSER_NAME));
        capability.setBrowserName("MicrosoftEdge");
        capability.setPlatform(Platform.WIN10);
        try {
            MyDriverManager.aDriver = new RemoteWebDriver(new URL("http://192.168.1.67:4444/wd/hub"), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return MyDriverManager.aDriver;
    }
    @Test
    public  static Boolean testIfGridComponentIsRunning(String component) {
        String line;
        String pidInfo = "";
        Process process = null;
        try {//we're looking for a process with the title matching that of the grid component
            process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe /FI \"WINDOWTITLE eq Selenium Grid " + component + "\"");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((line = input.readLine()) != null) {
                pidInfo += line;
            }
            if (pidInfo.contains("cmd.exe")){//make a isGridRunningDecision based on presence of cmd.exe
                isGridRunningDecision =true;//yes have found the process running
            }else{
                isGridRunningDecision =false;}//no have not found the process running

            //System.out.println(isGridRunningDecision);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isGridRunningDecision;
    }
    @Test
    public static WebDriver customiseRemoteWeb(){
           // if (Objects.equals(MyDriverManager.remoteHostNameFoundAsProperty, MyDriverManager.remoteHostName.GRID.name().toString())) {
       // System.out.println("fix "+MyDriverManager.remoteHostNameFoundAsProperty);
        //System.out.println("fix "+MyDriverManager.remoteHostName.GRID.name().toString());
        if (MyDriverManager.remoteHostNameFoundAsProperty.equals(MyDriverManager.remoteHostName.GRID.name().toString())){
            //System.out.println("inside isGridRunningDecision");
            if (testIfGridComponentIsRunning("Server") == false) {//isGridRunningDecision is false therefore start the server component
               try {
                   Desktop.getDesktop().open(new File(pathToSeleniumGridServerNodeBatchFiles + "\\startgrid.bat"));
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
            //the reason I've remmed this node section out is that I haven't been able to get authentoication to a remote node to work (in terms of starting startnode.bat remotely
           /*if (testIfGridComponentIsRunning("Node") == false) {//isGridRunningDecision is false therefore start the node component
               //while (testIfGridComponentIsRunning("Server")==false)
               try {
                   Desktop.getDesktop().open(new File(pathToSeleniumGridServerNodeBatchFiles + "\\startnode.bat"));
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }*/
           try {//TODO some sort of wait until node loaded
               Thread.sleep(15000);//this is quite a long delay but it's here because the VM seems to slow down and the code runs before the Grid server is properly up
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }

       determineRemoteServerURL(MyDriverManager.remoteHostNameFoundAsProperty);
        if (System.getProperties().containsKey(MyDriverManager.REMOTE_BROWSER_NAME)) {//then we look for property key grid_browser"(might be set via maven switch)
            System.out.println("This system property has been passed through - REMOTE_BROWSER_NAME="+ System.getProperty(MyDriverManager.REMOTE_BROWSER_NAME));
            MyDriverManager.remoteHostedBrowserNameAsString = System.getProperty(MyDriverManager.REMOTE_BROWSER_NAME);//set driverFoundAsProperty as found property
                    }
        switch (MyDriverManager.remoteHostedBrowserNameAsString) {
            case ("GOOGLECHROME"):
                DesiredCapabilities chromeCapabilities =  DesiredCapabilities.chrome();//create a capabilities object for firefox
                //chromeCapabilities.chrome().setCapability("platform", Platform.WINDOWS);
                chromeCapabilities.setCapability("platform", Platform.WIN10);//set platform capability setting - Platform.ANY works.....WIN8 does not
                chromeCapabilities.setBrowserName("chrome");
                try {
                    MyDriverManager.aDriver = new RemoteWebDriver(new URL(theServerURLReturned), chromeCapabilities);
                } catch (MalformedURLException e) {e.printStackTrace();
                }
                break;
            case ("FIREFOX"):
                //System.out.println("inside firefox case");
                DesiredCapabilities firefoxCapabilities =  DesiredCapabilities.firefox();//create a capabilities object for firefox
                firefoxCapabilities.setCapability("platform", Platform.WINDOWS);
                //capabilities.setCapability("platform", Platform.WINDOWS);//set platform capability setting - Platform.ANY works.....WIN8 does not
                try {
                    MyDriverManager.aDriver = new RemoteWebDriver(new URL(theServerURLReturned), firefoxCapabilities);
                } catch (MalformedURLException e) {e.printStackTrace();
                }
                break;
            case ("IE"):
                DesiredCapabilities ieCapabilities =  DesiredCapabilities.internetExplorer();//create a capabilities object for firefox
                ieCapabilities.setCapability("platform", Platform.WINDOWS);
                //capabilities.setCapability("platform", Platform.WINDOWS);//set platform capability setting - Platform.ANY works.....WIN8 does not
                try {
                    MyDriverManager.aDriver = new RemoteWebDriver(new URL(theServerURLReturned), ieCapabilities);
                } catch (MalformedURLException e) {e.printStackTrace();
                }
                break;
            case ("EDGE"):
                DesiredCapabilities edgeCapabilities =  DesiredCapabilities.edge();//create a capabilities object for firefox
                edgeCapabilities.setCapability("platform", Platform.WIN10);
                edgeCapabilities.setBrowserName("MicrosoftEdge");
                //capabilities.setCapability("platform", Platform.WINDOWS);//set platform capability setting - Platform.ANY works.....WIN8 does not
                try {
                    MyDriverManager.aDriver = new RemoteWebDriver(new URL(theServerURLReturned), edgeCapabilities);
                } catch (MalformedURLException e) {e.printStackTrace();
                }
                break;
        }
        return MyDriverManager.aDriver;
    }
    public static WebElement getWebElementUsingJavascriptWhenClassNameNotUnique(String className,int occurenceOfClass) {
        //uisefull for finding elements by classname where ther are multiple that have the same classname - occurenceOfClass is the occurence number
        final JavascriptExecutor js = (JavascriptExecutor)aDriver;
        return (WebElement) js.executeScript("return document.querySelectorAll(\"" + className + "\")[ " + Integer.toString(occurenceOfClass) + "];");
    }

    public static String remediateCSSClassName(String className){//just in case the user forgets to put the . in front of the classname
        if (!String.valueOf(className.charAt(0)).equals(".")){//look for . at beginning of classname
            return "." + className;
        }else{
            return className;
        }
    }
    //got this from some guy on the internet - checks if something is a number...usefull as some cells are populated by alpha days - MTWTFSS
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    private static String determineRemoteServerURL(String remoteServerName) {
        //theServerURLReturned=remoteServerName;
        //System.out.println("theServerURLReturned is " + remoteServerName);
        switch (remoteServerName){
            case("GRID"):
                theServerURLReturned="http://192.168.1.67:4444/wd/hub";
                break;
            case("SAUCELABS"):
                theServerURLReturned="http://iainms:3bda9c6c-79c3-4819-b7d4-3ce319a19915@ondemand.saucelabs.com:80/wd/hub";
                break;
            default:
                System.out.println("determineRemoteServerURL default");
                theServerURLReturned="http://localhost:4444/wd/hub";
                break;
        }
                return theServerURLReturned;
    }

}