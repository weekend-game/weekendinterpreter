## WeekendInterpreter

**Programming language interpreter**

I take the [text editor](https://github.com/weekend-game/weekendtexteditor) and divide its central part into two parts: I leave the editor in the upper part, and in the lower part I place a pane where all the interpreter output will be displayed. Then I add a "Run" menu with "Run" and "Stop" items. I implement an interpreter that executes the program opened in the editor.

You can **launch the application**: from Eclipse, by double-clicking on the WeekendInterpreter.jar file, or, if it does not start, by double-clicking on the WeekendInterpreter.bat file. If the latter does not start the application, download and install Java 11 or newer and try the methods described above again.

### Language Description

Backus-Naur Form (BNF) is a convenient way to describe the syntax of a language. However, most popular textbooks on various programming languages ​​do not use it. Well, I guess I can do without it.

The language is called **Weekend Game Language**. The default file extension is **WGL**.

**Comment**

```
REM Sample program
```

**Variable**

A variable in the language is designated by a letter of the Latin alphabet. Thus, 26 variables can be used. Variables have an integer type.

Not very impressive? But you have to understand that this is only a test of interpreter development, knowledge of how it is generally done, a blank for future adaptation to specific needs, Hello world in writing interpreters.

A **string** is any sequence of characters enclosed in double quotes.

**Assignment, expression**

```
a = 7
b = 8 * (a + 5)
```

Supported operations: unary minus, +, -, *, /, % (modulo remainder), ^ (raising to a power). The priority of operations is traditional, but can be changed with parentheses.

**Output (to the output area)**

```
PRINT "a = ", a
```

Output with line feed

```
PRINTLN
PRINTLN "b = ", b
PRINTLN "b - 37 = ", b – 37
```

That is, after PRINT or PRINTLN, strings, variables, or expressions are listed separated by commas.

**Input**

```
INPUT "Specify the value of X: ", x
```

A value input window will appear on the screen, and you will need to enter a value. You can omit it, but it will be interpreted as entering 0. Of course, it would be good to enter the value directly in the interpreter's output area. But this complicates the code somewhat, and in this case, it is important that the implementation is as simple and easy to read as possible.

**Unconditional jump (may Edsger Wybe Dijkstra forgive me)**

```
GOTO 10
```

The number 10 is the program line **label**. To put a label in a line and thus indicate where to jump, write this number at the beginning of the line. For example:

```
10 PRINTLN "Label 10"
```

By the way, in the GOTO operator it is not necessary to use a constant as a label. It can be a variable or an arbitrary expression.

**Conditional jump or execution of one command by condition**

```
IF a > 5 THEN GOTO 20
```

It is not necessary to write GOTO, you can write any operator, but only one.

Comparison operators are supported: <, >, =, #.

**Loop**

```
FOR i = 3 TO 7
  operators
NEXT
```

**Subroutine**

```
1000
PRINTLN "This is subroutine 1000"
RETURN
```

Subroutines do not have names. They are identified by labels. They should be placed after the main program. The subroutine ends with the RETURN command.

**Calling a subroutine**

```
GOSUB 1000
```

**Ending a program**

```
END
```

You can write nothing at the end of a program, but subroutines may follow the text of the main program, and then you need to use END to separate them.

**Example of a program**

You can see the application of all the above-described constructions by running the CommandsDemo.wgl program (included in the repository).

### Results
I have implemented a language similar to either Fortran or the prehistoric Basic that our dads, grandmas, and some of us wrote in. As a test of writing interpreters, it is interesting. It can be useful as a template for adaptation to specific needs. As a programming language itself... well, you get the idea. I was interested in immersing myself in the distant past for a while. As if I were not writing a program, but taking part in testing a time machine.

**About the implementation**

I took [WeekendTextEditor](https://github.com/weekend-game/weekendtexteditor) as an application template, and the entire game.weekend.texteditor package, in which I did not even change the name of the main class. It is still called WeekendTextEditor. The interpreter itself is allocated to the game.weekend.interpreter package. The new menu item "Run" saves the currently edited file and calls the Runner.run() method, which opens the just saved file and begins interpreting it.

Well, for a more detailed description of interpretation, see here: [Weekend interpreter](https://weekend-game.github.io/weekendinterpreter.htm)

### It would be nice...
To number lines in the editor and highlight keywords in color.
