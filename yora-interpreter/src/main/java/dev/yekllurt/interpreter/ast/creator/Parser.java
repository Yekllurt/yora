package dev.yekllurt.interpreter.ast.creator;

import dev.yekllurt.api.DataType;
import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.api.errors.SemanticError;
import dev.yekllurt.api.errors.SyntaxError;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.api.utility.IdentifierValidatorUtility;
import dev.yekllurt.interpreter.ast.ASTNode;
import dev.yekllurt.interpreter.ast.ConditionOperator;
import dev.yekllurt.interpreter.ast.Configuration;
import dev.yekllurt.interpreter.ast.impl.*;
import dev.yekllurt.interpreter.token.Token;
import dev.yekllurt.interpreter.token.TokenType;
import dev.yekllurt.interpreter.utility.ParserUtility;

import java.util.Objects;
import java.util.Set;

public class Parser {

    private static final Set<String> STATEMENT_START_TYPES = Set.of(TokenType.IDENTIFIER, TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_STRING, TokenType.KEYWORD_IF, TokenType.KEYWORD_WHILE);

    private final SequencedCollection<Token> tokens;
    private int tokenCursor = 0;

    public Parser(SequencedCollection<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        var program = parseProgram();
        ExceptionUtility.throwExceptionIf(isParseNotCompleted(),
                SyntaxError.INVALID_PROGRAM, tokenCursor, tokens.size());
        return program;
    }

    private ProgramNode parseProgram() {
        return ProgramNode.builder()
                .functions(parseFunctionList())
                .build();
    }

    private SequencedCollection<FunctionNode> parseFunctionList() {
        var functionList = new SequencedCollection<FunctionNode>();
        var function = parseMethod();
        while (Objects.nonNull(function)) {
            functionList.add(function);
            function = parseMethod();
        }
        return functionList;
    }

    private FunctionNode parseMethod() {
        if (isNextToken(TokenType.METHOD_RETURN_TYPES)) {
            var returnTypeStr = getCurrentTokenType();
            tokenCursor++;
            boolean isArray = false;
            if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACKET)) {
                tokenCursor++;
                if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACKET)) {
                    tokenCursor++;
                    isArray = true;
                } else {
                    ExceptionUtility.throwException(SyntaxError.INVALID_METHOD_EXPECTED_ACTUAL,
                            tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACKET, getCurrentTokenType());
                }
            }
            var returnType = DataType.fromString(returnTypeStr, isArray);
            if (isNextToken(TokenType.IDENTIFIER)) {
                var identifier = getCurrentTokenValue();
                tokenCursor++;
                if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                    tokenCursor++;
                    var parameters = new SequencedCollection<ParameterNode>();
                    if (isNextToken(TokenType.VARIABLE_TYPES)) {
                        parameters = parseArgumentList();
                        ExceptionUtility.throwExceptionIf(parameters.size() > Configuration.MAX_METHOD_PARAMETERS,
                                SemanticError.INVALID_METHOD_DEFINITION_TOO_MANY_PARAMETERS,
                                identifier, parameters.size(), Configuration.MAX_METHOD_PARAMETERS);
                    }
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        var statements = new SequencedCollection<ASTNode>();
                        if (!isNextToken(TokenType.KEYWORD_RETURN)) {
                            statements = parseStatementList();
                        }
                        var returnExpression = parseReturnStatement();

                        ExceptionUtility.throwExceptionIf(TokenType.TYPES_WITH_VALUES.contains(returnTypeStr) && Objects.isNull(returnExpression),
                                SemanticError.INVALID_METHOD_EXPECTED_RETURN_STATEMENT,
                                identifier);
                        ExceptionUtility.throwExceptionIf(TokenType.TYPES_WITHOUT_VALUES.contains(returnTypeStr) && Objects.nonNull(returnExpression),
                                SemanticError.INVALID_METHOD_EXPECTED_NO_RETURN_STATEMENT,
                                identifier);

                        if (isNextToken(TokenType.KEYWORD_END)) {
                            tokenCursor++;
                            if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                                tokenCursor++;
                                return FunctionNode.builder()
                                        .returnType(returnType)
                                        .identifier(identifier)
                                        .parameters(parameters)
                                        .statements(statements)
                                        .returnStatement(returnExpression)
                                        .build();
                            }
                            ExceptionUtility.throwException(SyntaxError.INVALID_METHOD_EXPECTED_ACTUAL,
                                    tokenCursor, TokenType.PUNCTUATION_SEMICOLON, getCurrentTokenType());
                        }
                        ExceptionUtility.throwException(SyntaxError.INVALID_METHOD_EXPECTED_ACTUAL,
                                tokenCursor, TokenType.KEYWORD_END, getCurrentTokenType());
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_METHOD_EXPECTED_ACTUAL,
                            tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACE, getCurrentTokenType());
                }
                ExceptionUtility.throwException(SyntaxError.INVALID_METHOD_EXPECTED_ACTUAL,
                        tokenCursor, TokenType.PUNCTUATION_LEFT_BRACE, getCurrentTokenType());
            }
            ExceptionUtility.throwException(SyntaxError.INVALID_METHOD_NO_IDENTIFIER,
                    tokenCursor);
        }
        return null;
    }

    private SequencedCollection<ParameterNode> parseArgumentList() {
        var parameterList = new SequencedCollection<ParameterNode>();
        var parameter = parseArgument();
        if (Objects.nonNull(parameter)) {
            parameterList.add(parameter);
            while (isNextToken(TokenType.PUNCTUATION_COMMA)) {
                tokenCursor++;
                parameter = parseArgument();
                ExceptionUtility.throwExceptionIf(Objects.isNull(parameter),
                        SyntaxError.INVALID_METHOD_ARGUMENT_LIST_MISSING_ARGUMENT_AFTER_COMMA,
                        tokenCursor);
                parameterList.add(parameter);
            }
        }
        return parameterList;
    }

    // Rule:
    //  variable_type IDENTIFIER
    //  variable_type LEFT_BRACKET RIGHT_BRACKET IDENTIFIER
    private ParameterNode parseArgument() {
        if (isNextToken(TokenType.VARIABLE_TYPES)) {
            var parameterType = getCurrentTokenType();
            tokenCursor++;
            if (isNextToken(TokenType.IDENTIFIER)) {
                var identifier = getCurrentTokenValue();
                tokenCursor++;
                return ParameterNode.builder()
                        .type(DataType.fromString(parameterType, false))
                        .identifier(identifier)
                        .build();
            } else if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACKET)) {
                tokenCursor++;
                if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACKET)) {
                    tokenCursor++;
                    if (isNextToken(TokenType.IDENTIFIER)) {
                        var identifier = getCurrentTokenValue();
                        tokenCursor++;
                        return ParameterNode.builder()
                                .type(DataType.fromString(parameterType, true))
                                .identifier(identifier)
                                .build();
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_ARGUMENT_NO_IDENTIFIER,
                            tokenCursor);
                }
                ExceptionUtility.throwException(SyntaxError.INVALID_ARGUMENT_EXPECTED_ACTUAL,
                        tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACKET, getCurrentTokenType());
            }
            ExceptionUtility.throwException(SyntaxError.INVALID_ARGUMENT_EXPECTED_ONE_OF_ACTUAL,
                    tokenCursor, TokenType.IDENTIFIER + ", " + TokenType.PUNCTUATION_LEFT_BRACKET, getCurrentTokenType(), getCurrentTokenValue());
        }
        return null;
    }

    private SequencedCollection<ASTNode> parseStatementList() {
        var statementList = new SequencedCollection<ASTNode>();
        var statemenet = isNextToken(STATEMENT_START_TYPES) ? parseStatement() : null;
        if (Objects.nonNull(statemenet)) {
            statementList.add(statemenet);
            while (isNextToken(STATEMENT_START_TYPES)) {
                statemenet = parseStatement();
                if (Objects.isNull(statemenet)) {
                    ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_NULL,
                            tokenCursor);
                }
                statementList.add(statemenet);
            }
        }
        return statementList;
    }

    // Rules:
    //  variable_type IDENTIFIER EQUAL expression SEMICOLON
    //  variable_type LEFT_BRACKET NUMBER RIGHT_BRACKET IDENTIFIER SEMICOLON
    //  IDENTIFIER EQUAL expression SEMICOLON
    //  expression SEMICOLON
    private ASTNode parseStatement() {
        // Rules:
        //  variable_type IDENTIFIER EQUAL expression SEMICOLON
        //  variable_type LEFT_BRACKET NUMBER RIGHT_BRACKET IDENTIFIER SEMICOLON
        if (isNextToken(TokenType.VARIABLE_TYPES)) {
            var variableType = getCurrentTokenType();
            tokenCursor++;
            // Rule: variable_type IDENTIFIER EQUAL expression SEMICOLON
            if (isNextToken(TokenType.IDENTIFIER)) {
                var identifier = getCurrentTokenValue();

                IdentifierValidatorUtility.performValidationIsValidUserVariableWhenDeclaring(tokenCursor, identifier);

                tokenCursor++;
                if (isNextToken(TokenType.PUNCTUATION_EQUAL)) {
                    tokenCursor++;
                    var expression = parseExpression();
                    if (Objects.nonNull(expression)) {
                        if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                            tokenCursor++;
                            return VariableDeclarationNode.builder()
                                    .type(DataType.fromString(variableType, false))
                                    .identifier(identifier)
                                    .value(expression)
                                    .build();
                        }
                        ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                                tokenCursor, TokenType.PUNCTUATION_SEMICOLON, getCurrentTokenType());
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_EXPRESSION_BUT_FOUND_NONE,
                            tokenCursor);
                }
                ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                        tokenCursor, TokenType.PUNCTUATION_EQUAL, getCurrentTokenType());

                // Rule: variable_type LEFT_BRACKET NUMBER RIGHT_BRACKET IDENTIFIER SEMICOLON
            } else if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACKET)) {
                tokenCursor++;
                if (isNextToken(TokenType.DECIMAL_NUMBER)) {
                    var value = ParserUtility.parseInt(getCurrentTokenValue());
                    tokenCursor++;
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACKET)) {
                        tokenCursor++;
                        if (isNextToken(TokenType.IDENTIFIER)) {
                            var identifier = getCurrentTokenValue();

                            IdentifierValidatorUtility.performValidationIsValidUserVariableWhenDeclaring(tokenCursor, identifier);

                            tokenCursor++;
                            if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                                tokenCursor++;
                                var dataType = DataType.fromString(variableType, true);
                                return VariableDeclarationNode.builder()
                                        .type(dataType)
                                        .identifier(identifier)
                                        .value(TermNode.builder()
                                                .value(DataType.createArray(dataType, value))
                                                .type(TermNode.TermType.LITERAL)
                                                .build())
                                        .build();
                            }
                            ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                                    tokenCursor, TokenType.PUNCTUATION_SEMICOLON, getCurrentTokenType());
                        }
                        ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_ATTEMPTING_TO_DECLARE_VARIABLE_BUT_FOUND_NO_IDENTIFIER,
                                tokenCursor, getCurrentTokenType());
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                            tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACKET, getCurrentTokenType());
                }
                ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_ATTEMPTING_TO_DECLARE_ARRAY_BUT_FOUND_NO_SIZE,
                        tokenCursor, getCurrentTokenType());
            }
            // TODO: throw error?
        } else if (isNextToken(TokenType.IDENTIFIER)) {
            var identifier = getCurrentTokenValue();

            IdentifierValidatorUtility.performValidationIsValidUserVariableWhenAssigning(tokenCursor, identifier);

            tokenCursor++;
            // Rule: IDENTIFIER EQUAL expression SEMICOLON
            if (isNextToken(TokenType.PUNCTUATION_EQUAL)) {
                tokenCursor++;
                var expression = parseExpression();
                if (Objects.nonNull(expression)) {
                    if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                        tokenCursor++;
                        return AssignmentNode.builder()
                                .identifier(identifier)
                                .value(expression)
                                .build();
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                            tokenCursor, TokenType.PUNCTUATION_SEMICOLON, getCurrentTokenType());
                }
                ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_EXPRESSION_BUT_FOUND_NONE,
                        tokenCursor);
            }
            // Rule:
            //  IDENTIFIER LEFT_BRACKET NUMBER RIGHT_BRACKET EQUAL expression SEMICOLON
            else if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACKET)) {
                tokenCursor++;
                if (isNextToken(TokenType.DECIMAL_NUMBER)) {
                    var index = ParserUtility.parseInt(getCurrentTokenValue());
                    tokenCursor++;
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACKET)) {
                        tokenCursor++;
                        if (isNextToken(TokenType.PUNCTUATION_EQUAL)) {
                            tokenCursor++;
                            var expression = parseExpression();
                            if (Objects.nonNull(expression)) {
                                if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                                    tokenCursor++;
                                    return AssignmentNode.builder()
                                            .identifier(identifier)
                                            .index(TermNode.builder()
                                                    .value(index)
                                                    .type(TermNode.TermType.LITERAL)
                                                    .build())
                                            .value(expression)
                                            .build();
                                }
                                ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                                        tokenCursor, TokenType.PUNCTUATION_SEMICOLON, getCurrentTokenType());
                            }
                            ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_EXPRESSION_BUT_FOUND_NONE,
                                    tokenCursor);
                        }
                        ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                                tokenCursor, TokenType.PUNCTUATION_EQUAL, getCurrentTokenType());
                    }
                } else {
                    var indexExpression = parseExpression();
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACKET)) {
                        tokenCursor++;
                        if (isNextToken(TokenType.PUNCTUATION_EQUAL)) {
                            tokenCursor++;
                            var expression = parseExpression();
                            if (Objects.nonNull(expression)) {
                                if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                                    tokenCursor++;
                                    return AssignmentNode.builder()
                                            .identifier(identifier)
                                            .index(indexExpression)
                                            .value(expression)
                                            .build();
                                }
                                ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                                        tokenCursor, TokenType.PUNCTUATION_SEMICOLON, getCurrentTokenType());
                            }
                            ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_EXPRESSION_BUT_FOUND_NONE,
                                    tokenCursor);
                        }
                        ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                                tokenCursor, TokenType.PUNCTUATION_EQUAL, getCurrentTokenType());
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                            tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACKET, getCurrentTokenType());
                }
            }
            // Rules:
            //  IDENTIFIER LEFT_BRACE RIGHT_BRACE SEMICOLON
            //  IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE SEMICOLON
            else if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                tokenCursor++;
                // It is a function call
                var arguments = isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)
                        ? ExpressionListNode.builder().expressionList(new SequencedCollection<>()).build()
                        : parseExpressionList();
                if (Objects.nonNull(arguments)) {
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                            tokenCursor++;
                            return FunctionCallNode.builder()
                                    .functionIdentifier(identifier)
                                    .arguments(arguments)
                                    .build();
                        }
                        ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                                tokenCursor, TokenType.PUNCTUATION_SEMICOLON, getCurrentTokenType());
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_STATEMENT_EXPECTED_ACTUAL,
                            tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACE, getCurrentTokenType());
                }
                // TODO: throw error
            }
            // TODO: throw error?
        }
        // Rules:
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list END SEMICOLON
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list return END SEMICOLON
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list ELSE statement_list END SEMICOLON
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list ELSE statement_list return END SEMICOLON
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list return ELSE statement_list END SEMICOLON
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list return ELSE statement_list return END SEMICOLON
        else if (isNextToken(TokenType.KEYWORD_IF)) {
            tokenCursor++;
            if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                tokenCursor++;
                var conditions = parseCondition();
                if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                    tokenCursor++;
                    var statementsThen = parseStatementList();
                    var returnStatementThen = parseReturnStatement();
                    var statementsElse = new SequencedCollection<ASTNode>();
                    ReturnNode returnStatementElse = null;
                    if (isNextToken(TokenType.KEYWORD_ELSE)) {
                        tokenCursor++;
                        statementsElse = parseStatementList();
                        returnStatementElse = parseReturnStatement();
                    }
                    if (isNextToken(TokenType.KEYWORD_END)) {
                        tokenCursor++;
                        if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                            tokenCursor++;
                            return IfBranchNode.builder()
                                    .condition(conditions)
                                    .statementsThen(statementsThen)
                                    .returnStatementThen(returnStatementThen)
                                    .statementsElse(statementsElse)
                                    .returnStatementElse(returnStatementElse)
                                    .build();
                        }
                    }
                }
            }
        }
        // Rules:
        //  WHILE LEFT_BRACE condition RIGHT_BRACE statement_list END SEMICOLON
        //  WHILE LEFT_BRACE condition RIGHT_BRACE statement_list return END SEMICOLON
        else if (isNextToken(TokenType.KEYWORD_WHILE)) {
            tokenCursor++;
            if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                tokenCursor++;
                var conditions = parseCondition();
                if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                    tokenCursor++;
                    var statements = parseStatementList();
                    if (isNextToken(TokenType.KEYWORD_END)) {
                        tokenCursor++;
                        if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                            tokenCursor++;
                            return WhileBranchNode.builder()
                                    .condition(conditions)
                                    .statements(statements)
                                    .build();
                        }
                    }
                }
            }
        }
        // Rule: expression SEMICOLON
        else {
            var expression = parseExpression();
            if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                tokenCursor++;
                return expression;
            }
        }
        return null;
    }

    // Rule:
    //  RETURN expression SEMICOLON
    private ReturnNode parseReturnStatement() {
        if (isNextToken(TokenType.KEYWORD_RETURN)) {
            tokenCursor++;
            var expression = parseExpression();
            if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                tokenCursor++;
                return ReturnNode.builder()
                        .value(expression)
                        .build();
            }
        }
        return null;
    }

    private ConditionNode parseCondition() {
        return parseOrCondition();
    }

    // Rules:
    //  and_condition
    //  and_condition OR OR and_condition
    private ConditionNode parseOrCondition() {
        var left = parseAndCondition();
        if (isParseNotCompleted() && isNextToken(TokenType.PUNCTUATION_OR) && isNextNextToken(TokenType.PUNCTUATION_OR)) {
            tokenCursor += 2;   // Adding two as we are checking two values (||)
            var right = parseAndCondition();
            return LogicConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.OR)
                    .build();
        }
        return left;
    }

    // Rules:
    //  simple_condition
    //  simple_condition AND AND simple_condition
    private ConditionNode parseAndCondition() {
        var left = parseSimpleCondition();
        if (isParseNotCompleted() && isNextToken(TokenType.PUNCTUATION_AND) && isNextNextToken(TokenType.PUNCTUATION_AND)) {
            tokenCursor += 2;   // Adding two as we are checking two values (&&)
            var right = parseSimpleCondition();
            return LogicConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.AND)
                    .build();
        }
        return left;
    }

    // Rule:
    //  LEFT_BRACE condition RIGHT_BRACE
    //  expression comparison_operator expression
    private ConditionNode parseSimpleCondition() {
        if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
            tokenCursor++;
            var condition = parseCondition();

            ExceptionUtility.throwExceptionIf(Objects.isNull(condition),
                    SyntaxError.INVALID_CONDITION_NULL,
                    tokenCursor);

            if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                tokenCursor++;
                return condition;
            }
            ExceptionUtility.throwException(SyntaxError.INVALID_CONDITION_EXPECTED_ACTUAL,
                    tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACE, getCurrentTokenType());
            return null;
        } else {
            var left = parseExpression();
            if (isNextToken(TokenType.PUNCTUATION_EQUAL) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
                tokenCursor += 2;   // Adding two as we are checking two values (==)
                var right = parseExpression();
                return ComparisonConditionNode.builder()
                        .left(left)
                        .right(right)
                        .operator(ConditionOperator.EQUAL)
                        .build();
            }
            if (isNextToken(TokenType.PUNCTUATION_EXCLAMATION_MARK) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
                tokenCursor += 2;   // Adding two as we are checking two values (!=)
                var right = parseExpression();
                return ComparisonConditionNode.builder()
                        .left(left)
                        .right(right)
                        .operator(ConditionOperator.NOT_EQUAL)
                        .build();
            }
            if (isNextToken(TokenType.PUNCTUATION_GREATER_THAN) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
                tokenCursor += 2;   // Adding two as we are checking two values (>=)
                var right = parseExpression();
                return ComparisonConditionNode.builder()
                        .left(left)
                        .right(right)
                        .operator(ConditionOperator.GREATER_THAN_EQUAL)
                        .build();
            }
            if (isNextToken(TokenType.PUNCTUATION_GREATER_THAN)) {
                tokenCursor++;
                var right = parseExpression();
                return ComparisonConditionNode.builder()
                        .left(left)
                        .right(right)
                        .operator(ConditionOperator.GREATER_THAN)
                        .build();
            }
            if (isNextToken(TokenType.PUNCTUATION_LESS_THAN) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
                tokenCursor += 2;   // Adding two as we are checking two values (<=)
                var right = parseExpression();
                return ComparisonConditionNode.builder()
                        .left(left)
                        .right(right)
                        .operator(ConditionOperator.LESS_THAN_EQUAL)
                        .build();
            }
            if (isNextToken(TokenType.PUNCTUATION_LESS_THAN)) {
                tokenCursor++;
                var right = parseExpression();
                return ComparisonConditionNode.builder()
                        .left(left)
                        .right(right)
                        .operator(ConditionOperator.LESS_THAN)
                        .build();
            }
            ExceptionUtility.throwException(SyntaxError.INVALID_CONDITION_UNSUPPORTED_OPERATOR,
                    tokenCursor, getCurrentTokenType());
            return null;
        }
    }

    // Rules:
    //  expression
    //  expression_list COMMA expression
    private ExpressionListNode parseExpressionList() {
        var expressionList = new SequencedCollection<ASTNode>();
        var expression = parseExpression();
        while (Objects.nonNull(expression)) {
            expressionList.add(expression);
            expression = null;
            if (isNextToken(TokenType.PUNCTUATION_COMMA)) {
                tokenCursor++;
                expression = parseExpression();
                ExceptionUtility.throwExceptionIf(Objects.isNull(expression),
                        SyntaxError.INVALID_EXPRESSION_LIST_MISSING_EXPRESSION_AFTER_COMMA,
                        tokenCursor);
            }
        }
        return ExpressionListNode.builder()
                .expressionList(expressionList)
                .build();
    }

    private ASTNode parseExpression() {
        return parseAdditiveExpression();
    }

    // Rules:
    //  multiplicative_expression
    //  multiplicative_expression PLUS multiplicative_expression
    //  multiplicative_expression MINUS multiplicative_expression
    private ASTNode parseAdditiveExpression() {
        var expression = parseMultiplicativeExpression();
        while (isParseNotCompleted() && TokenType.ADDITIVE_TYPES.contains(getCurrentTokenType())) {
            // Rule: multiplicative_expression PLUS multiplicative_expression
            if (isNextToken(TokenType.PUNCTUATION_PLUS)) {
                tokenCursor++;
                var right = parseMultiplicativeExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_PLUS)
                        .build();
            }
            // Rule: multiplicative_expression MINUS multiplicative_expression
            else if (isNextToken(TokenType.PUNCTUATION_MINUS)) {
                tokenCursor++;
                var right = parseMultiplicativeExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_MINUS)
                        .build();
            }
        }
        // Rule: multiplicative_expression
        return expression;
    }

    // Rules:
    //  power_expression
    //  power_expression STAR power_expression
    //  power_expression DIVIDE power_expression
    //  power_expression PERCENT power_expression
    private ASTNode parseMultiplicativeExpression() {
        var expression = parsePowerExpression();
        while (isParseNotCompleted() && TokenType.MULTIPLICATIVE_TYPES.contains(getCurrentTokenType())) {
            // Rule: power_expression STAR power_expression
            if (isNextToken(TokenType.PUNCTUATION_STAR)) {
                tokenCursor++;
                var right = parsePowerExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_STAR)
                        .build();
            }
            // Rule: power_expression DIVIDE power_expression
            else if (isNextToken(TokenType.PUNCTUATION_DIVIDE)) {
                tokenCursor++;
                var right = parsePowerExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_DIVIDE)
                        .build();
            }
            // Rule: power_expression PERCENT power_expression
            else if (isNextToken(TokenType.PUNCTUATION_PERCENT)) {
                tokenCursor++;
                var right = parsePowerExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_PERCENT)
                        .build();
            }
        }
        // Rule: power_expression
        return expression;
    }

    // Rules:
    //  PLUS power_expression
    //  MINUS power_expression
    //  atom
    //  atom CARET power_expression
    private ASTNode parsePowerExpression() {
        var token = getCurrentToken();
        // Rule: PLUS power_expression
        if (isNextToken(TokenType.PUNCTUATION_PLUS)) {
            tokenCursor++;
            var powerExpression = parsePowerExpression();
            return UnaryExpressionNode.builder()
                    .node(powerExpression)
                    .operator(token.getType())
                    .build();

        }
        // Rule: MINUS power_expression
        if (isNextToken(TokenType.PUNCTUATION_MINUS)) {
            tokenCursor++;
            var powerExpression = parsePowerExpression();
            return UnaryExpressionNode.builder()
                    .node(powerExpression)
                    .operator(token.getType())
                    .build();
        }
        var atom = parseAtom();
        // Rule: atom CARET power_expression
        if (isNextToken(TokenType.PUNCTUATION_CARET)) {
            var operator = getCurrentToken();
            tokenCursor++;
            var powerExpression = parsePowerExpression();
            return BinaryExpressionNode.builder()
                    .left(atom)
                    .right(powerExpression)
                    .operator(operator.getType())
                    .build();
        }
        // Rule: atom
        return atom;
    }

    // Rules:
    //  NUMBER
    //  STRING
    //  TRUE
    //  FALSE
    //  IDENTIFIER
    //  IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE
    //  IDENTIFIER LEFT_BRACKET expression RIGHT_BRACKET
    private ASTNode parseAtom() {
        var token = getCurrentToken();
        switch (token.getType()) {
            case TokenType.DECIMAL_NUMBER, TokenType.STRING -> {
                tokenCursor++;
                // TODO: maybe perform analysis to check if it actually is a number or string
                return TermNode.builder().value(token.getValue()).type(TermNode.TermType.LITERAL).build();
            }
            case TokenType.KEYWORD_TRUE -> {
                tokenCursor++;
                return TermNode.builder().value(true).type(TermNode.TermType.LITERAL).build();
            }
            case TokenType.KEYWORD_FALSE -> {
                tokenCursor++;
                return TermNode.builder().value(false).type(TermNode.TermType.LITERAL).build();
            }
            case TokenType.PUNCTUATION_LEFT_BRACE -> {
                tokenCursor++;
                var expression = parseExpression();
                if (Objects.nonNull(expression)) {
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        return expression;
                    } else {
                        ExceptionUtility.throwException(SyntaxError.INVALID_ATOM_EXPECTED_ACTUAL,
                                tokenCursor, TokenType.PUNCTUATION_RIGHT_BRACE, getCurrentTokenType());
                        return null;
                    }
                } else {
                    ExceptionUtility.throwException(SyntaxError.INVALID_ATOM_NO_EXPRESSION);
                    return null;
                }
            }
            case TokenType.IDENTIFIER -> {
                tokenCursor++;
                var identifier = token.getValue();
                if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                    tokenCursor++;
                    // It is a function call
                    var arguments = parseExpressionList();
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        return FunctionCallNode.builder()
                                .functionIdentifier(identifier)
                                .arguments(arguments)
                                .build();
                    } else {
                        ExceptionUtility.throwException(SyntaxError.INVALID_METHOD_CALL,
                                tokenCursor, identifier);
                        return null;
                    }
                } else if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACKET)) {
                    tokenCursor++;
                    // Resolving an array value
                    var indexExpression = parseExpression();
                    if (Objects.isNull(indexExpression)) {
                        ExceptionUtility.throwException(SyntaxError.INVALID_ARRAY_INDEX_EXPRESSION,
                                tokenCursor);
                        return null;
                    }
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACKET)) {
                        tokenCursor++;
                        return TermNode.builder()
                                .value(identifier)
                                .index(indexExpression)
                                .type(TermNode.TermType.DYNAMIC)
                                .build();
                    }
                    ExceptionUtility.throwException(SyntaxError.INVALID_ARRAY_READ_VALUE_CALL,
                            tokenCursor);
                    return null;
                } else {
                    // It is a variable
                    return TermNode.builder().value(identifier).type(TermNode.TermType.DYNAMIC).build();
                }
            }
            default -> {
                ExceptionUtility.throwException(SyntaxError.INVALID_ATOM_UNSUPPORTED_TOKEN_TYPE, tokenCursor, getCurrentTokenType());
                return null;
            }
        }
    }

    // ========================
    // === Helper functions ===
    // ========================
    private boolean isParseCompleted() {
        return tokenCursor == tokens.size();
    }

    private boolean isParseNotCompleted() {
        return !isParseCompleted();
    }

    private Token getCurrentToken() {
        return tokens.get(tokenCursor);
    }

    private String getCurrentTokenType() {
        return getCurrentToken().getType();
    }

    private String getCurrentTokenValue() {
        return getCurrentToken().getValue();
    }

    private boolean isNextToken(String type) {
        return isParseNotCompleted() && getCurrentToken().getType().equals(type);
    }

    private boolean isNextToken(Set<String> types) {
        return isParseNotCompleted() && types.contains(getCurrentToken().getType());
    }

    private boolean isNextNextToken(String type) {
        return tokenCursor + 1 < tokens.size() && tokens.get(tokenCursor + 1).getType().equals(type);
    }

}
