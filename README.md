## WeekendInterpreter

**Programming language interpreter**

I take the [text editor](https://github.com/weekend-game/weekendtexteditor) and divide its central part into two parts: I leave the editor in the upper part, and in the lower part I place a pane where all the interpreter output will be displayed. Then I add a "Run" menu with "Run" and "Stop" items. I implement an interpreter that executes the program opened in the editor.

### How to run the program

Download the repository to your computer. Everything you need for the program is located in the app folder. Navigate to the app folder and run the program by double-clicking the WeekendInterpreter.jar file or, if the program doesn't start, double-click the WeekendInterpreter.bat file. If the program doesn't start, download and install Java 11 or later and repeat the steps above.

### How to open a project in Eclipse

In Eclipse, select "Import..." from the "File" menu. In the window that opens, select "Existing projects into workspace." Navigate to the folder with the downloaded repository and click "Finish." The project will open in Eclipse. In the Package Explorer (on the left side of the screen), double-click the WeekendInterpreter.java file. The file will open for editing (in the center of the screen). Run the program by pressing Ctrl+F11 or using your preferred method for running programs in Eclipse.

### How to use the program

At the top of the application window, write a program in a language I've named Weekend Game Language (the default file extension is WGL). Run it by pressing F5 or clicking the green square button in the toolbar, or by selecting "Run" from the "Run" menu. If the program gets stuck in a loop, stop it by pressing the Escape key or the red square button in the toolbar, or by selecting "Stop" from the "Run" menu.

### Description of the programming language Weekend Game Language

The implementation description is located on the project page in the section: ["Weekend Game Language Description"](https://weekend-game.github.io/weekendinterpreter.htm#LangDescr).

You can also open the **CommandsDemo.wgl** file (included in the repository) and see examples of all the language constructs.

### How the program is written

Details of the program's implementation can be found on the [project page](https://weekend-game.github.io/weekendinterpreter.htm#ProgDescr).

### Results

An interpreter has been created for a language similar to either Fortran or the very old Basic used by our fathers, grandmothers, and some of us. It's interesting as a test version of an interpreter. It could be useful as a template for adapting it to specific needs.

### It would be nice...

It would be nice to add line numbers in the editor and highlight keywords in color.
