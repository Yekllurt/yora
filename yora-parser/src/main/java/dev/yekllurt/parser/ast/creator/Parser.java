package dev.yekllurt.parser.ast.creator;

import dev.yekllurt.api.DataType;
import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.ConditionOperator;
import dev.yekllurt.parser.ast.Configuration;
import dev.yekllurt.parser.ast.impl.*;
import dev.yekllurt.parser.ast.throwable.GrammarException;
import dev.yekllurt.parser.ast.throwable.ParseException;
import dev.yekllurt.parser.ast.throwable.ParserException;
import dev.yekllurt.parser.ast.throwable.UnsupportedTokenTypeException;
import dev.yekllurt.api.collection.SequencedCollection;
import dev.yekllurt.parser.token.Token;
import dev.yekllurt.parser.token.TokenType;
import dev.yekllurt.api.utility.ExceptionUtility;
import dev.yekllurt.parser.utility.ParserUtility;

import java.util.Objects;
import java.util.Set;

public class Parser {

    private static final Set<String> EXPR_PRIORITY_3 = Set.of(TokenType.PUNCTUATION_PLUS, TokenType.PUNCTUATION_MINUS);
    private static final Set<String> EXPR_PRIORITY_2 = Set.of(TokenType.PUNCTUATION_STAR, TokenType.PUNCTUATION_DIVIDE, TokenType.PUNCTUATION_PERCENT);
    private static final Set<String> EXPR_PRIORITY_1 = Set.of(TokenType.PUNCTUATION_CARET);

    private static final Set<String> VARIABLE_TYPES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_STRING);
    private static final Set<String> RETURN_TYPES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_STRING, TokenType.KEYWORD_VOID);
    private static final Set<String> RETURN_TYPES_WITH_VALUES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_STRING);
    private static final Set<String> RETURN_TYPES_WITHOUT_VALUES = Set.of(TokenType.KEYWORD_VOID);
    private static final Set<String> STATEMENT_START_TYPES = Set.of(TokenType.IDENTIFIER, TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_STRING, TokenType.KEYWORD_IF, TokenType.KEYWORD_WHILE);

    private final SequencedCollection<Token> tokens;
    private int tokenCursor = 0;

    public Parser(SequencedCollection<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        var program = parseProgram();
        ExceptionUtility.throwIf(isParseNotCompleted(),
                new ParserException(String.format("Didn't use all available tokens. Only used %s out of %s tokens", tokenCursor, tokens.size())));
        return program;
    }

    private ProgramNode parseProgram() {
        return ProgramNode.builder()
                .functions(parseFunctionList())
                .build();
    }

    private SequencedCollection<FunctionNode> parseFunctionList() {
        var functionList = new SequencedCollection<FunctionNode>();
        var function = parseFunction();
        while (Objects.nonNull(function)) {
            functionList.add(function);
            function = parseFunction();
        }
        return functionList;
    }

    private FunctionNode parseFunction() {
        if (isNextToken(RETURN_TYPES)) {
            var returnType = getCurrentTokenType();
            tokenCursor++;
            if (isNextToken(TokenType.IDENTIFIER)) {
                var identifier = getCurrentTokenValue();
                tokenCursor++;
                if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                    tokenCursor++;
                    var parameters = new SequencedCollection<ParameterNode>();
                    if (isNextToken(VARIABLE_TYPES)) {
                        parameters = parseParameterList();
                        ExceptionUtility.throwIf(parameters.size() > Configuration.MAX_FUNCTION_PARAMETERS,
                                new ParseException(String.format("Unable to parse the function '%s' as its function header expects %s however only %s are allowed", identifier, parameters.size(), Configuration.MAX_FUNCTION_PARAMETERS)));
                    }
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        var statements = new SequencedCollection<ASTNode>();
                        if (!isNextToken(TokenType.KEYWORD_RETURN)) {
                            statements = parseStatementList();
                        }
                        var returnExpression = parseReturnStatement();
                        ExceptionUtility.throwIf(RETURN_TYPES_WITH_VALUES.contains(returnType) && Objects.isNull(returnExpression),
                                new ParseException(String.format("Unable to parse the function '%s' as it expects to return a value of type '%s' however it does not return anything", identifier, returnType)));
                        ExceptionUtility.throwIf(RETURN_TYPES_WITHOUT_VALUES.contains(returnType) && Objects.nonNull(returnExpression),
                                new ParseException(String.format("Unable to parse the function '%s' as it expects to not return a value however it does return a value", identifier)));
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
                        }
                    }
                }
            }
        }
        return null;
    }

    private SequencedCollection<ParameterNode> parseParameterList() {
        var parameterList = new SequencedCollection<ParameterNode>();
        var parameter = parseParameter();
        if (Objects.nonNull(parameter)) {
            parameterList.add(parameter);
            while (isNextToken(TokenType.PUNCTUATION_COMMA)) {
                tokenCursor++;
                parameter = parseParameter();
                ExceptionUtility.throwIf(Objects.isNull(parameter),
                        new ParseException(String.format("Unable to parse a parameter list at token %s due to a parameter being expected after a ',' however non was provided", tokenCursor)));
                parameterList.add(parameter);
            }
        }
        return parameterList;
    }

    // Rule:
    //  variable_type IDENTIFIER
    private ParameterNode parseParameter() {
        if (isNextToken(VARIABLE_TYPES)) {
            var parameterType = getCurrentTokenType();
            tokenCursor++;
            if (isNextToken(TokenType.IDENTIFIER)) {
                var identifier = getCurrentTokenValue();
                tokenCursor++;
                return ParameterNode.builder()
                        .type(DataType.fromString(parameterType, false))
                        .identifier(identifier)
                        .build();
            }
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
                    throw new GrammarException("Failed to parse a statement");
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
        if (isNextToken(VARIABLE_TYPES)) {
            var variableType = getCurrentTokenType();
            tokenCursor++;
            // Rule: variable_type IDENTIFIER EQUAL expression SEMICOLON
            if (isNextToken(TokenType.IDENTIFIER)) {
                var identifier = getCurrentTokenValue();
                tokenCursor++;
                if (isNextToken(TokenType.PUNCTUATION_EQUAL)) {
                    tokenCursor++;
                    var expression = parseExpression();
                    // TODO: add better error output
                    if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                        tokenCursor++;
                        return VariableDeclarationNode.builder()
                                .type(DataType.fromString(variableType, false))
                                .identifier(identifier)
                                .value(expression)
                                .build();
                    }
                }
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
                        }
                    }
                }

            }
        } else if (isNextToken(TokenType.IDENTIFIER)) {
            var identifier = getCurrentTokenValue();
            tokenCursor++;
            // Rule: IDENTIFIER EQUAL expression SEMICOLON
            if (isNextToken(TokenType.PUNCTUATION_EQUAL)) {
                tokenCursor++;
                var expression = parseExpression();
                // TODO: add better error output
                if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                    tokenCursor++;
                    return AssignmentNode.builder()
                            .identifier(identifier)
                            .value(expression)
                            .build();
                }
            }
            // Rules:
            //  IDENTIFIER LEFT_BRACE RIGHT_BRACE SEMICOLON
            //  IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE SEMICOLON
            else if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                tokenCursor++;
                // It is a function call
                var arguments = parseExpressionList();
                // TODO: add better error output
                if (Objects.nonNull(arguments) && isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                    tokenCursor++;
                    if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                        tokenCursor++;
                        return FunctionCallNode.builder()
                                .functionIdentifier(identifier)
                                .arguments(arguments)
                                .build();
                    }
                }
            }
        }
        // Rules:
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list END SEMICOLON
        //  IF LEFT_BRACE condition RIGHT_BRACE statement_list return END SEMICOLON
        else if (isNextToken(TokenType.KEYWORD_IF)) {
            tokenCursor++;
            if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                tokenCursor++;
                var conditions = parseCondition();
                if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                    tokenCursor++;
                    var statements = parseStatementList();
                    var returnStatement = parseReturnStatement();
                    if (isNextToken(TokenType.KEYWORD_END)) {
                        tokenCursor++;
                        if (isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
                            tokenCursor++;
                            return IfBranchNode.builder()
                                    .condition(conditions)
                                    .statements(statements)
                                    .returnStatement(returnStatement)
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
        var left = parseExpression();
        if (isNextToken(TokenType.PUNCTUATION_EQUAL) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (==)
            var right = parseExpression();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.PUNCTUATION_EXCLAMATION_MARK) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (!=)
            var right = parseExpression();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.NOT_EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.PUNCTUATION_GREATER_THAN) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (>=)
            var right = parseExpression();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.GREATER_THAN_EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.PUNCTUATION_GREATER_THAN)) {
            tokenCursor++;
            var right = parseExpression();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.GREATER_THAN)
                    .build();
        }
        if (isNextToken(TokenType.PUNCTUATION_LESS_THAN) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (<=)
            var right = parseExpression();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.LESS_THAN_EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.PUNCTUATION_LESS_THAN)) {
            tokenCursor++;
            var right = parseExpression();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.LESS_THAN)
                    .build();
        }
        throw new GrammarException(String.format("Failed parsing a condition du to an unsupported token type %s", getCurrentTokenType()));
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
                ExceptionUtility.throwIf(Objects.isNull(expression),
                        new ParseException(String.format("Unable to parse an expression list at token %s due to an expression being expected after a ',' however non was provided", tokenCursor)));
            }
        }
        return ExpressionListNode.builder()
                .expressionList(expressionList)
                .build();
    }

    private ASTNode parseExpression() {
        return parseAddSubtractExpression();
    }

    // Rules:
    //  multiply_divide_expression
    //  multiply_divide_expression PLUS multiply_divide_expression
    //  multiply_divide_expression MINUS multiply_divide_expression
    private ASTNode parseAddSubtractExpression() {
        var expression = parseMultiplyDivideExpression();
        while (isParseNotCompleted() && EXPR_PRIORITY_3.contains(getCurrentTokenType())) {
            // Rule: multiply_divide_expression PLUS multiply_divide_expression
            if (isNextToken(TokenType.PUNCTUATION_PLUS)) {
                tokenCursor++;
                var right = parseMultiplyDivideExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_PLUS)
                        .build();
                continue;
            }
            // Rule: multiply_divide_expression MINUS multiply_divide_expression
            if (isNextToken(TokenType.PUNCTUATION_MINUS)) {
                tokenCursor++;
                var right = parseMultiplyDivideExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_MINUS)
                        .build();
                continue;
            }
        }
        // Rule: multiply_divide_expression
        return expression;
    }

    // Rules:
    //  power_expression
    //  power_expression STAR power_expression
    //  power_expression DIVIDE power_expression
    //  power_expression PERCENT power_expression
    private ASTNode parseMultiplyDivideExpression() {
        var expression = parsePowerExpression();
        while (isParseNotCompleted() && EXPR_PRIORITY_2.contains(getCurrentTokenType())) {
            // Rule: power_expression STAR power_expression
            if (isNextToken(TokenType.PUNCTUATION_STAR)) {
                tokenCursor++;
                var right = parsePowerExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_STAR)
                        .build();
                continue;
            }
            // Rule: power_expression DIVIDE power_expression
            if (isNextToken(TokenType.PUNCTUATION_DIVIDE)) {
                tokenCursor++;
                var right = parsePowerExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_DIVIDE)
                        .build();
                continue;
            }
            // Rule: power_expression PERCENT power_expression
            if (isNextToken(TokenType.PUNCTUATION_PERCENT)) {
                tokenCursor++;
                var right = parsePowerExpression();
                expression = BinaryExpressionNode.builder()
                        .left(expression)
                        .right(right)
                        .operator(TokenType.PUNCTUATION_PERCENT)
                        .build();
                continue;
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
    //  IDENTIFIER
    //  IDENTIFIER LEFT_BRACE expression_list RIGHT_BRACE
    private ASTNode parseAtom() {
        var token = getCurrentToken();
        switch (token.getType()) {
            case TokenType.DECIMAL_NUMBER, TokenType.STRING -> {
                tokenCursor++;
                return TermNode.builder().value(token.getValue()).type(TermNode.TermType.LITERAL).build();
            }
            case TokenType.PUNCTUATION_LEFT_BRACE -> {
                tokenCursor++;
                var expression = parseExpression();
                // TODO: add better error output
                if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                    tokenCursor++;
                    return expression;
                }
                return null;
            }
            case TokenType.IDENTIFIER -> {
                tokenCursor++;
                if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                    tokenCursor++;
                    // It is a function call
                    var arguments = parseExpressionList();
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        return FunctionCallNode.builder()
                                .functionIdentifier(token.getValue())
                                .arguments(arguments)
                                .build();
                    }
                    return null; // TODO: throw error instead?
                } else {
                    // It is a variable
                    return TermNode.builder().value(token.getValue()).type(TermNode.TermType.DYNAMIC).build();
                }
            }
            default ->
                    throw new UnsupportedTokenTypeException(String.format("Unable to parse an atom at token %s due to an unsupported token type '%s' being provided", tokenCursor, getCurrentTokenType()));

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
