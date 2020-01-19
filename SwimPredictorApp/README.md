# SwimPredictor application

## Running the app
You can find the .apk file of the application in the '_app/release/_' folder.  
We recmmend to launch the app with an Android Studio emulator. If you have no device installed, you can add a new one as described here: https://developer.android.com/studio/run/managing-avds#createavd
Run the emulator from the AVD Manager and simply drag and drop the .apk file to the emulator. AndroidStudio will then install the app on the emulated device.  
If you have noch Android device available, you can run an emulator in the [Genymotion Cloud](https://cloud.geny.io/). Here you need to register and then you get 60 minutes free trial without any local installation.

## Structure
The files in '.idea/', 'gradle/wrapper/' and all the gradle files can be ignored. These files are only necessary if the user wants to build the app himself. The interesting code lies in the 'app/src/main/java/org/swimpredictor/' folder. The app only has a main activity that is divided into three fragments: the home or prediction fragment, the stopwatch fragment and the database fragment. The functionality of these fragments is described in our paper.
