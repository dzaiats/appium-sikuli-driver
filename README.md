# Sikuppium
Open source project which allows to mix Appium and Sikuli under one framework and easy manipulate by content and recognition of images.
This is just a very nice helper in the automation testing to help test something that was left for manual visual testing.
Now You can check without any problem in Your mobile automation project that correct Logo appears or correct photo is showed etc.

### How to run
To run the sample test need to have pre-installed Maven 3 and Java 1.8 and need to execute next command from command line (example):
 - PLATFORM_NAME="android" APPIUM_VERSION="1.4.13" NAME="MyReaction" PLATFORM_VERSION="4.4" DEVICE="Nexus 5" APP="/Developing/Android/SignedApp/MyReaction.apk" mvn clean install

Please, specify correct path to the application which You can find in the folder "app" under the main parent folder of the project.

Current example is adapted for the devices with resolution of display: 768x1280, 1080x1920, 1200x1920.

### How to take a correct images
To take a correct screenshots need to use a tool SikuliX. You cannot take a screenshot directly from simulator screen. Only crop from the
screenshot provided by WebDriver API in class DriverScreen.java (line 22)

### Help and contact
Also You can find the application My Reaction on Play Store https://play.google.com/store/apps/details?id=com.denyszaiats.myreactions

If You have any questions, feel free to contact me by email: denys.zaiats@gmail.com