# How to execute a program

## In IntelliJ

1. Create a new application under configurations (recommended name: (1) Lexer) with the following
   configuration:
    - Module: yora-lexer
    - Main class: dev.yekllurt.lexer.Main
    - Program arguments: "./examples/programs/hello_world.yora" "./temp/program.out"
2. Create a new application under configurations (recommended name: (2) Interpreter) with the following configuration:
    - Module: yora-parser
    - Main class: dev.yekllurt.interpreter.Interpreter
    - Program arguments: "./temp/program.out"
3. Create a new compound under configurations (recommend name: Process) and add the applications in the following
   order: (1) Lexer, (2) Interpreter
4. Run the Process, this should sequentially execute the scanner, lexer and interpreter

Note: this requires the creation of a directory named 'temp' on the project root level <br>
Note: if you want to execute a different program you can do this by simply changing the second program argument of the
scanner