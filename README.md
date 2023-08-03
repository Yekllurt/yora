View the examples/how-to-execute.md file to find a detailed instruction as how to execute the program

# How to build

1. Clone the repository `https://github.com/Yekllurt/yora.git`
2. Build using the command `mvn clean validate pacakge`
    1. The lexer `yora-lexer-<version>.jar` can be found in the folder `yora-lexer/src/target`
    2. The interpreter `yora-interpretr-<version>.jar` can be found in the folder `yora-interpretr/src/target`

# How to execute a program

1. Write a program and save it in a file, preferably a `.yora` file
2. Lex a program using the command `java -jar yora-lexer-<version>.jar <program-file> <output-file>`
3. Interpret a program using the command `java -jar yora-interpretr-<version>.jar <lexed-program>`
    1. The `<lexed-program>` is the `<output-file>` from the lexer