# Grammar
```
program
    function
    
function
    return_type IDENTIFIER LEFT_BRACE RIGHT_BRACE return_expression END SEMICOLON
    return_type IDENTIFIER LEFT_BRACE RIGHT_BRACE statement_list return_expression END SEMICOLON
    return_type IDENTIFIER LEFT_BRACE argument_list RIGHT_BRACE return_expression END SEMICOLON
    return_type IDENTIFIER LEFT_BRACE argument_list RIGHT_BRACE statement_list return_expression END SEMICOLON

argument
    variable_type IDENTIFIER
    variable_type LEFT_BRACKET RIGHT_BRACKET IDENTIFIER
    
argument_list
    argument
    argument_list COMMA argument

statement
    variable_type IDENTIFIER EQUAL expression SEMICOLON
    variable_type LEFT_BRACKET NUMBER RIGHT_BRACKET IDENTIFIER SEMICOLON
    IDENTIFIER EQUAL expression SEMICOLON
    IDENTIFIER LEFT_BRACKET NUMBER RIGHT_BRACKET EQUAL expression SEMICOLON
    IDENTIFIER LEFT_BRACKET expression RIGHT_BRACKET EQUAL expression SEMICOLON
    IDENTIFIER LEFT_BRACE RIGHT_BRACE SEMICOLON
    IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE SEMICOLON
    expression SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list END SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list return END SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list ELSE statement_list END SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list ELSE statement_list return END SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list return ELSE statement_list END SEMICOLON
    IF LEFT_BRACE condition RIGHT_BRACE statement_list return ELSE statement_list return END SEMICOLON
    WHILE LEFT_BRACE condition RIGHT_BRACE statement_list END SEMICOLON
    WHILE LEFT_BRACE condition RIGHT_BRACE statement_list return END SEMICOLON

statement_list
    statement
    statement_list statement

condition
    or_condition

or_condition
    and_condition
    and_condition OR OR and_condition

and_condition
    simple_condition
    simple_condition AND AND simple_condition

simple_condition
    LEFT_BRACE condition RIGHT_BRACE
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
    STR
    BOOLEAN
    
return_type
    INT
    FLOAT
    BOOLEAN
    STR
    INT LEFT_BRACKET RIGHT_BRACKET
    FLOAT LEFT_BRACKET RIGHT_BRACKET
    BOOLEAN LEFT_BRACKET RIGHT_BRACKET
    STR LEFT_BRACKET RIGHT_BRACKET
    VOID

return_expression
    RETURN expression SEMICOLON

expression
    additive_expression

expression_list
    expression
    expression_list COMMA expression 

additive_expression
    multiplicative_expression
    multiplicative_expression PLUS multiplicative_expression
    multiplicative_expression MINUS multiplicative_expression

multiplicative_expression
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
    IDENTIFIER LEFT_BRACKET expression RIGHT_BRACKET
    IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE
```