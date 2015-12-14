package io.sikuppium.driver;

import org.openqa.selenium.remote.DesiredCapabilities;

public class CapabilitiesFactory {

    protected static DesiredCapabilities capabilities;

    public static DesiredCapabilities getCapabilities() throws Exception {

        capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", System.getenv("PLATFORM_NAME"));
        capabilities.setCapability("appium-version", System.getenv("APPIUM_VERSION"));
        capabilities.setCapability("name", System.getenv("NAME"));
        capabilities.setCapability("platformVersion", System.getenv("PLATFORM_VERSION"));
        capabilities.setCapability("deviceName", System.getenv("DEVICE"));
        capabilities.setCapability("app", System.getenv("APP"));

        return capabilities;
    }

}
