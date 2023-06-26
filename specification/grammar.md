# Grammar
```
program
    function
    
function
    return_type IDENTIFIER LEFT_BRACE RIGHT_BRACE return_expression END SEMICOLON
    return_type IDENTIFIER LEFT_BRACE RIGHT_BRACE statement_list return_expression END SEMICOLON
    return_type IDENTIFIER LEFT_BRACE parameter_list RIGHT_BRACE return_expression END SEMICOLON
    return_type IDENTIFIER LEFT_BRACE parameter_list RIGHT_BRACE statement_list return_expression END SEMICOLON

parameter
    variable_type IDENTIFIER
    
parameter_list
    parameter
    parameter_list COMMA parameter

statement
    variable_type IDENTIFIER EQUAL expression SEMICOLON
    IDENTIFIER EQUAL expression SEMICOLON
    IDENTIFIER LEFT_BRACE RIGHT_BRACE SEMICOLON
    IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE SEMICOLON
    expression SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list END SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list return END SEMICOLON
    WHILE LEFT_BRACE condition RIGHT_BRACE statement_list END SEMICOLON
    WHILE LEFT_BRACE condition RIGHT_BRACE statement_list return END SEMICOLON

statement_list
    statement
    statement_list statement

condition
    expression comparison_operator expression

comparison_operator
    EQUAL EQUAL
    EXCLAMATION_MARK EQUAL
    GREATER_THAN
    GREATER_THAN EQUAL
    LESS_THAN
    LESS_THAN EQUAL

variable_type
    INT
    FLOAT
    BOOLEAN
    CHAR
    
return_type
    INT
    FLOAT
    BOOLEAN
    CHAR
    VOID

return_expression
    RETURN expression SEMICOLON

expression
    add_substract_expression

expression_list
    expression
    expression_list COMMA expression 

add_substract_expression
    multiply_divide_expression
    multiply_divide_expression PLUS multiply_divide_expression
    multiply_divide_expression MINUS multiply_divide_expression

multiply_divide_expression
    power_expression
    power_expression STAR power_expression
    power_expression DIVIDE power_expression
    power_expression PERCENT power_expression

power_expression
    PLUS power_expression
    MINUS power_expression
    atom
    atom CARET power_expression

atom
    STRING
    NUMBER
    LEFT_BRACE expression RIGHT_BRACE
    IDENTIFIER
    IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE
```