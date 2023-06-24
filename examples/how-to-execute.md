# How to execute a program
## In IntelliJ

1. Create a new application under configurations (recommended name: (1) Scanner) with the following
   configuration:
    - Module: yora-scanner
    - Main class: dev.yekllurt.scanner.Main
    - Program arguments: "./specification/token-definition.yora-scanner" "./examples/programs/print_1.yora" "
      ./temp/program.yora-scanner.out"
2. Create a new application under configurations (recommended name: (2) Lexer) with the following configuration:
    - Module: yora-lexer
    - Main class: dev.yekllurt.lexer.Main
    - Program arguments: "./specification/lexer-definition.yora-lexer" "./temp/program.yora-scanner.out" "
      ./temp/program.yora-lexer.out"
3. Create a new application under configurations (recommended name: (3) Interpreter) with the following configuration:
    - Module: yora-parser
    - Main class: dev.yekllurt.lexer.Main
    - Program arguments: "./temp/program.yora-lexer.out"
4. Create a new compound under configurations (recommend name: Process) and add the applications in the following
   order: (1) Scanner, (2) Lexer, (3) Interpreter
5. Run the Process, this should sequentially execute the scanner, lexer and interpreter

Note: this requires the creation of a directory named 'temp' on the project root level <br>
Note: if you want to execute a different program you can do this by simply changing the second program argument of the
scanner