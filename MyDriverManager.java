package com.selenium.environment;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.selenium.environment.HelperClasses.*;

/**'
 * Created by Iain Mounsey-Smith on 24/11/2015.
 * Production versionn
 */
public class MyDriverManager {
    public static WebDriver aDriver = null;//create variable Adriver of type Webdriver...must be static as accessed in beforeclass..//create as public so can access from other classes
    public static String driverFoundAsProperty = "";
    public static remoteHostName remoteHostServerName = null;
    public static final String MY_DRIVER = "MY_DRIVER";
    public static final String GRID_BROWSER="GRID_BROWSER";
    public static final String REMOTE_HOST_NAME="REMOTE_HOST_NAME";
    public static final String REMOTE_BROWSER_NAME="REMOTE_BROWSER_NAME";
    public static String remoteHostedBrowserNameAsString ="";
    public static String  remoteHostNameFoundAsProperty="";
    public static String remoteHostBrowserNameFoundAsProperty="";
    public static driverOrBrowserName useThisDriver = null;
    public static String theBrowserYouAreRunningIs = "";
    private static driverOrBrowserName remoteBrowserName;
    public enum driverOrBrowserName {FIREFOX, GOOGLECHROME, REMOTEWEB, IE, HTMLUNIT, GRID,PHANTOM,MARIONETTE,EDGE};
    public enum remoteHostName{GRID,SAUCELABS};
public static WebDriverWait wait;
    //the set method is used to set local browser name,remote host (if used) or remote browsername(if used) via CODE rather then Run Configuration/CI/Maven, we're setting system properties that get method can use
    //Method signature for a remotely hosted driver
    public static void set(driverOrBrowserName driverChoice,remoteHostName remoteHost,driverOrBrowserName remoteBrowser) {
        System.out.println("The Set method has been passed parameters...");
        useThisDriver = driverChoice;//local browser
        remoteHostServerName = remoteHost;// set this to null if only running local

        remoteBrowserName=remoteBrowser;

        //used for passing through property from MVN commandline
        //e.g. mvn test -DMY_DRIVER=FIREFOX
        String browserNameAsString = useThisDriver.name();

        System.setProperty(MY_DRIVER, browserNameAsString);
        System.setProperty(REMOTE_HOST_NAME, remoteHostServerName.name());
        System.setProperty(REMOTE_BROWSER_NAME, remoteBrowserName.name());

        System.out.println("The set method has passed through useThisDriver as \t\t\t\t" + useThisDriver + "\t\t\tand set as system property MY_DRIVER");
        System.out.println("The set method has passed through remoteHostServerName as \t\t" + remoteHostServerName+ "\t\t\t\tand set as system property REMOTE_HOST_NAME");
        System.out.println("The set method has passed through remoteBrowserName as \t\t\t" + remoteBrowserName+ "\t\tand set as system property REMOTE_BROWSER_NAME");

        //e.g. mvn test -DMY_DRIVER=GRID -DGRID_BROWSER=GOOGLECHROME
        if (remoteHostServerName !=null) {//this handles instances where we pass null in to set() method for gridBrowser
            remoteHostNameFoundAsProperty = remoteHostServerName.name();
        }
        if (remoteBrowserName !=null) {//this handles instances where we pass null in to set() method for gridBrowser
            remoteHostedBrowserNameAsString = remoteBrowserName.name();
        }
        //System.setProperty(REMOTE_BROWSER_NAME, remoteHostedBrowserNameAsString);

        if (aDriver != null) {//if a driver is already loaded then quit it
            aDriver.quit();
            aDriver = null;
        }
    }
    //Method signature for using a local driver only
    public static void set(driverOrBrowserName driverChoice){
        useThisDriver = driverChoice;//local browser;// set this to null if only running local
        System.out.println("The set method has passed through \t\t\t\t" + useThisDriver + "\t\t\tand set as system property MY_DRIVER");
        String browserNameAsString = useThisDriver.name();
        System.setProperty(MY_DRIVER, browserNameAsString);
        if (aDriver != null) {//if a driver is already loaded then quit it
            aDriver.quit();
            aDriver = null;
        }
    }
    //usethisdriver is set in ther SET method
    public static WebDriver get()  {
        if (useThisDriver == null) { //if we haven't had a browser name passed thru from set method ie usethisdriver is NULL ...so must be from maven commandline/IDE run configuration
            System.out.println("Set method not run - straight into the get method");
            if (System.getProperties().containsKey(MY_DRIVER)) {//then we look for property key "mydriver"(might be set via maven switch)
                System.out.println("Maven or Run Configuration has passed through this system property - MY_DRIVER \t\t\t\t" + System.getProperty(MY_DRIVER));
                driverFoundAsProperty = System.getProperty(MY_DRIVER);//set driverFoundAsProperty as found property
            }
            if (System.getProperties().containsKey(REMOTE_HOST_NAME)) {//then we look for property key "mydriver"(might be set via maven switch)
                System.out.println("Maven or Run Configuration has passed through this system property - REMOTE_HOST_NAME \t\t" + System.getProperty(REMOTE_HOST_NAME));
                remoteHostNameFoundAsProperty = System.getProperty(REMOTE_HOST_NAME);//set driverFoundAsProperty as found property
            }
            if (System.getProperties().containsKey(REMOTE_BROWSER_NAME)) {//then we look for property key "mydriver"(might be set via maven switch)
                System.out.println("Maven or Run Configuration has passed through this system property - REMOTE_BROWSER_NAME  \t" + System.getProperty(REMOTE_BROWSER_NAME));
                remoteHostBrowserNameFoundAsProperty = System.getProperty(REMOTE_BROWSER_NAME);//set driverFoundAsProperty as found property
            }

            switch (driverFoundAsProperty) {//and we set useThisDriver as per property
                case "FIREFOX":
                    System.out.println("This system property has been passed through - MY_DRIVER=FIREFOX ");
                    useThisDriver = driverOrBrowserName.FIREFOX;
                    break;
                case "GOOGLECHROME":
                    //System.out.println("GET:This system property has been passed through - MY_DRIVER=GOOGLECHROME ");
                    useThisDriver = driverOrBrowserName.GOOGLECHROME;
                    break;
                case "IE":
                    //System.out.println("GET:This system property has been passed through - MY_DRIVER=IE");
                    useThisDriver = driverOrBrowserName.IE;
                    break;
                case "REMOTEWEB":
                    //System.out.println("GET:This system property has been passed through - MY_DRIVER=REMOTEWEB");
                    //we now exit and customer the remote browser - capabilities etc etc
                    useThisDriver = driverOrBrowserName.REMOTEWEB; //presently only getcurrentbrowser uses this reference - consider fixing getcurrrentbrowser
                    customiseRemoteWeb();
                    break;
                case "HTMLUNIT":
                    //System.out.println("GET:This system property has been passed through - MY_DRIVER=HTMLUNIT");
                    useThisDriver = driverOrBrowserName.HTMLUNIT;
                    break;
                case "PHANTOM":
                    //System.out.println("GET:This system property has been passed through - MY_DRIVER=PHANTOM ");
                    useThisDriver = driverOrBrowserName.PHANTOM;
                    break;
                case "MARIONETTE":
                    //System.out.println("GET:This system property has been passed through - MY_DRIVER=MARIONETTE ");
                    useThisDriver = driverOrBrowserName.MARIONETTE;
                    break;
                case "EDGE":
                    //System.out.println("GET:This system property has been passed through - MY_DRIVER=EDGE ");
                    useThisDriver = driverOrBrowserName.EDGE;
                    System.out.println("usethisdriver = " + useThisDriver);
                    break;
                default:
                    System.out.println("No driver specified so using default driver - FIREFOX");
                    useThisDriver = driverOrBrowserName.FIREFOX;
            }
        }
        //if we haven't yet set aDriver to a driverOrBrowserName via set method - we're therefore expecting to get driverOrBrowserName via run configuration or CI
        if (aDriver == null) {
                switch (useThisDriver.name()) {
                case "FIREFOX":
                    //http://www.seleniumhq.org/docs/03_webdriver.jsp#firefox-driver
                    //System.out.println("The set method takes us straight to the FIREFOX browser");
                    customiseFireFox();//set profile with firebug
                    break;
                case "GOOGLECHROME":
                    //https://code.google.com/p/selenium/wiki/ChromeDriver
                    //System.out.println("The set method takes us straight to the Chrome browser");
                    customiseChrome();//disable extensions/plugins;start maximised
                    break;
                case "IE":
                    //http://www.seleniumhq.org/docs/03_webdriver.jsp#internet-explorer-driver
                    //System.out.println("The set method takes us straight to the Internet Explorer browser");
                    customiseIE();
                    break;
                case "HTMLUNIT"://htmlunit broken at moment
                    //http://www.seleniumhq.org/docs/03_webdriver.jsp#htmlunit-driver
                    //System.out.println("The set method takes us straight to the HTMLUnit browser");
                    aDriver = new HtmlUnitDriver(true);//javascript turned on
                    break;
                case "PHANTOM":
                    //https://github.com/detro/ghostdriver
                    //System.out.println("The set method takes us straight to the PhantomJS browser");
                    customisePhantom();//javascript turned on
                    //running sucessfully as at 27/11/15 using <groupId>com.codeborne</groupId><version>1.2.1</version>
                    break;
                case "REMOTEWEB":
                    //https://code.google.com/p/selenium/wiki/Grid2
                    //System.out.println("The set method takes us straight to the RemoteWeb");
                    customiseRemoteWeb();
                    break;
                case "MARIONETTE":
                    //https://code.google.com/p/selenium/wiki/Grid2
                    //System.out.println("The set method takes us straight to the GECKO-Marionette");
                    customiseMarionette();
                    break;
                case "EDGE":
                    customiseEDGE();
                    break;
            }
                   }
        return  aDriver;//we return the requested aDriver - instantiated with correct driver
    }
    public static WebDriver get(String goToThisURL )  {
        get();//we run the get method to set useThisDriver and instantiate aDriver based on (useThisDriver.name())
        aDriver.get(goToThisURL);
        try{
            System.out.println("Maximising window using get method...");
            //aDriver.manage().window().maximize();//this isn't working now...10/04/17

        }catch(UnsupportedCommandException e){
            System.out.println("Remote Driver does not support maximise");
        }
        System.out.println("\nEnd of MyDriverManager screen logging\n===================================================" );
        return aDriver;//we return this method-url parameter
    }
    //.use this to detect the currently running browser and make decisions as to what to do in the test based on the decision
    public static  String getCurrentBrowser() {//checking ALL of the drivers to see which browser is launched
        if (useThisDriver.toString()== driverOrBrowserName.REMOTEWEB.toString()){
            switch (remoteHostedBrowserNameAsString) {
                case "FIREFOX":
                    theBrowserYouAreRunningIs = driverOrBrowserName.FIREFOX.name();
                    break;
                case "GOOGLECHROME":
                    theBrowserYouAreRunningIs = driverOrBrowserName.GOOGLECHROME.name();
                    break;
                case "IE":
                    theBrowserYouAreRunningIs = driverOrBrowserName.IE.name();
                    break;
                case "PHANTOM":
                    theBrowserYouAreRunningIs = driverOrBrowserName.PHANTOM.name();
                    break;
                case "MARIONETTE":
                    theBrowserYouAreRunningIs = driverOrBrowserName.MARIONETTE.name();
                    break;
                case "EDGE":
                    theBrowserYouAreRunningIs = driverOrBrowserName.EDGE.name();
                    break;
            }
        }else{//else if driver is other then remotedriver/grid THEN report browser launched
            //System.out.println("inside else bit");
            switch (useThisDriver.toString()) {
                case "FIREFOX":
                    theBrowserYouAreRunningIs = driverOrBrowserName.FIREFOX.name();
                    break;
                case "GOOGLECHROME":
                    theBrowserYouAreRunningIs = driverOrBrowserName.GOOGLECHROME.name();
                    break;
                case "IE":
                    theBrowserYouAreRunningIs = driverOrBrowserName.IE.name();
                    break;
                case "PHANTOM":
                    theBrowserYouAreRunningIs = driverOrBrowserName.PHANTOM.name();
                    break;
                case "MARIONETTE":
                    theBrowserYouAreRunningIs = driverOrBrowserName.MARIONETTE.name();
                    break;
                case "EDGE":
                    theBrowserYouAreRunningIs = driverOrBrowserName.EDGE.name();
                    break;
            }
        }
        System.out.println("The browser that getCurrentBrowser() has detected is = " + theBrowserYouAreRunningIs);
        return theBrowserYouAreRunningIs;}

    public static boolean waitForJStoLoad(WebDriverWait wait) {
        //the following sourced from stackexchange
        final JavascriptExecutor js = (JavascriptExecutor)aDriver;
        // wait for jQuery to load
        System.out.println("Executing waitForJStoLoad - waiting for jquery to be active and document.readyState to be complete...complete");
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver aDriver) {
                try {//jquery has finished when active=0
                    return ((Long)js.executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    return true;
                }
            }
        };
        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return js.executeScript("return document.readyState").toString().equals("complete");
                //complete=The document and all sub-resources have finished loading. The state indicates that the load event has been fired.
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }
}