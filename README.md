RASI, for Robocracy Autonomous Scripting Interface is a system we developed for encoding autonomous programs in a file.

Instructions for use:

A RASI file must be created with the extension ".rasi". This file encodes the autonomous program.

To add a RASI command, simply add a method in the TeamRasiActions class. Typing the name of this method in the RASI file will call this method.

The opmode must be a LinearOpMode. In the opmode class, construct a RasiExecutor object. Pass the opmode to the RasiExecutor by typing "this" as the first argument.

In the runOpMode method, call RasiExecutor.runRasi()

RASI Syntax:

    Run a command by typing in the form:
    
        methodname, argument1, argument2, argument3

    Each command is separated by a newline

    RASI tags:

        Rasi has basic flow control through the use of tags. Tags precede a     command and determine whether or not it runs, The have the form:

    tag: command, argument1, argument2, argument3

Tags can be changed using the RasiExecutor.setTags, RasiExecutor.addTag, and RasiExecutor.removeTag methods.



