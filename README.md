# Tree FX

Forked: https://gitlab.com/lucaguada/treefx

A modernized version of an old JavaFX2.1 example.

I found this old example of an animated tree made with JavaFX2.1, and I was curious to check if it was compilable with new OpenJFX and OpenJDK.
Just a little refactor later (set maven build, removed old weird builders, added some lambdas here and there, etc.) I was able to run it.

I just found an issue with the MediaPlayer, the sound file starts, but then stops immediately afterwards.

Here the original post: https://docs.oracle.com/javafx/2/animations/basics.htm


### Run

`$ mvn clean compile javafx:jlink jpackage:jpackage`

Access target/dist folder and execute the TreeFX-1.0.0.exe

