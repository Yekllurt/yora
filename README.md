# How to open in IntelliJ IDEA

1. Clone the repository `https://github.com/Yekllurt/yora.git`
2. Start IntelliJ IDEA and choose the directory

# How to build

1. Clone the repository `https://github.com/Yekllurt/yora.git`
2. Build using the command `mvn clean validate pacakge`
    1. The lexer `yora-lexer-<version>.jar` can be found in the folder `yora-lexer/src/target`
    2. The interpreter `yora-interpretr-<version>.jar` can be found in the folder `yora-interpretr/src/target`

# How to execute a program

## Via command line

1. Write a program and save it in a file, preferably a `.yora` file
2. Lex a program using the command `java -jar yora-lexer-<version>.jar <program-file> <output-file>`
3. Interpret a program using the command `java -jar yora-interpretr-<version>.jar <lexed-program>`
    1. The `<lexed-program>` is the `<output-file>` from the lexer

Note: this requires you to build the program

## Via IntelliJ IDEA

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

Note: this requires the project to be opened with IntelliJ IDEA <br>
Note: this requires the creation of a directory named 'temp' on the project root level <br>
Note: if you want to execute a different program you can do this by simply changing the second program argument of the
scanner