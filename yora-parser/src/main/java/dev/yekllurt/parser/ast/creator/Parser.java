package dev.yekllurt.parser.ast.creator;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.ConditionOperator;
import dev.yekllurt.parser.ast.impl.*;
import dev.yekllurt.parser.ast.throwable.exception.GrammarException;
import dev.yekllurt.parser.ast.throwable.exception.ParserException;
import dev.yekllurt.parser.ast.throwable.exception.UnsupportedTokenTypeException;
import dev.yekllurt.parser.collection.SequencedCollection;
import dev.yekllurt.parser.token.Token;
import dev.yekllurt.parser.token.TokenType;

import java.util.Objects;
import java.util.Set;

public class Parser {

    private static final Set<String> EXPR_PRIORITY_3 = Set.of(TokenType.PUNCTUATION_PLUS, TokenType.PUNCTUATION_MINUS);
    private static final Set<String> EXPR_PRIORITY_2 = Set.of(TokenType.PUNCTUATION_STAR, TokenType.PUNCTUATION_DIVIDE);
    private static final Set<String> EXPR_PRIORITY_1 = Set.of(TokenType.PUNCTUATION_CARET);

    private static final Set<String> VARIABLE_TYPES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_CHAR);
    private static final Set<String> RETURN_TYPES = Set.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_CHAR, TokenType.KEYWORD_VOID);
    private static final Set<String> STATEMENT_START_TYPES = Set.of(TokenType.IDENTIFIER, TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_CHAR, TokenType.KEYWORD_IF, TokenType.KEYWORD_WHILE);

    private final SequencedCollection<Token> tokens;
    private int tokenCursor = 0;

    public Parser(SequencedCollection<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        var program = parseProgram();
        if (isParseNotCompleted()) {
            throw new ParserException(String.format("Didn't use all available tokens. Only used %s out of %s tokens", tokenCursor, tokens.size()));
        }
        return program;
    }

    private ProgramNode parseProgram() {
        return ProgramNode.builder()
                .functions(parseFunctionList())
                .build();
    }

    // FILE DIVIDER --- TEMP
    private SequencedCollection<FunctionNode> parseFunctionList() {
        var functionList = new SequencedCollection<FunctionNode>();
        var function = parseFunction();
        while (Objects.nonNull(function)) {
            functionList.add(function);
            function = parseFunction();
            // TODO: add here better error handling
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
                    }
                    if (isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        var statements = new SequencedCollection<ASTNode>();
                        if (!isNextToken(TokenType.KEYWORD_RETURN)) {
                            statements = parseStatementList();
                        }
                        ReturnNode returnExpression = null;
                        if (isNextToken(TokenType.KEYWORD_RETURN)) {
                            returnExpression = parseReturn();
                        }
                        // TODO: add here to check if only a return expression exits if the function return type is not void
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
                if (Objects.isNull(parameter)) {
                    throw new GrammarException("Failed to parse a parameter");
                }
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
                        .type(parameterType)
                        .identifier(identifier)
                        .build();
            }
        }
        return null;
    }

    private SequencedCollection<ASTNode> parseStatementList() {
        var statementList = new SequencedCollection<ASTNode>();
        var statemenet = parseStatement();
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
    //  IDENTIFIER EQUAL expression SEMICOLON
    //  expression SEMICOLON
    private ASTNode parseStatement() {
        // Rule: variable_type IDENTIFIER EQUAL expression SEMICOLON
        if (isNextToken(VARIABLE_TYPES)) {
            var variableType = getCurrentTokenType();
            tokenCursor++;
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
                                .type(variableType)
                                .identifier(identifier)
                                .value(expression)
                                .build();
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
        //  IF LEFT_BRACE condition_list RIGHT_BRACE statement_list END SEMICOLON
        //  IF LEFT_BRACE condition_list RIGHT_BRACE statement_list return END SEMICOLON
        else if (isNextToken(TokenType.KEYWORD_IF)) {
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
                            return IfBranchNode.builder()
                                    .condition(conditions)
                                    .statements(statements)
                                    .build();
                        }
                    }
                }
            }
        }
        // Rules:
        //  WHILE LEFT_BRACE condition_list RIGHT_BRACE statement_list END SEMICOLON
        //  WHILE LEFT_BRACE condition_list RIGHT_BRACE statement_list return END SEMICOLON
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
    private ReturnNode parseReturn() {
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
        var left = parseAtom();
        if (isNextToken(TokenType.PUNCTUATION_EQUAL) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (==)
            var right = parseAtom();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.PUNCTUATION_EXCLAMATION_MARK) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (!=)
            var right = parseAtom();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.NOT_EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.GREATER_THAN) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (>=)
            var right = parseAtom();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.GREATER_THAN_EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.GREATER_THAN)) {
            tokenCursor++;
            var right = parseAtom();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.GREATER_THAN)
                    .build();
        }
        if (isNextToken(TokenType.LESS_THAN) && isNextNextToken(TokenType.PUNCTUATION_EQUAL)) {
            tokenCursor += 2;   // Adding two as we are checking two values (<=)
            var right = parseAtom();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.LESS_THAN_EQUAL)
                    .build();
        }
        if (isNextToken(TokenType.LESS_THAN)) {
            tokenCursor++;
            var right = parseAtom();
            return ConditionNode.builder()
                    .left(left)
                    .right(right)
                    .operator(ConditionOperator.LESS_THAN)
                    .build();
        }
        return null;
    }

    // FILE DIVIDER --- BOTTOM PART OF GRAMMAR

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
                if (Objects.isNull(expression)) {
                    throw new GrammarException("Failed parsing an expression due to there being no expression after a ','");
                }
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
        if (TokenType.DECIMAL_NUMBER.equals(token.getType())) {
            tokenCursor++;
            return TermNode.builder().value(token.getValue()).type(TermNode.TermType.LITERAL).build();
        }
        if (TokenType.PUNCTUATION_LEFT_BRACE.equals(token.getType())) {
            tokenCursor++;
            var expression = parseExpression();
            // TODO: add better error output
            if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                tokenCursor++;
                return expression;
            }
            return null;
        }
        if (TokenType.IDENTIFIER.equals(token.getType())) {
            tokenCursor++;
            if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
                tokenCursor++;
                // It is a function call
                var arguments = parseExpressionList();
                // TODO: add better error output
                if (Objects.nonNull(arguments) && isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                    tokenCursor++;
                    return FunctionCallNode.builder()
                            .functionIdentifier(token.getValue())
                            .arguments(arguments)
                            .build();
                }
                return null;
            } else {
                // It is a variable
                return TermNode.builder().value(token.getValue()).type(TermNode.TermType.DYNAMIC).build();
            }
        }
        throw new UnsupportedTokenTypeException(String.format("Failed parsing an atom due to a non supported token type '%s'", token.getType()));
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

    /*private Token getNextToken() {
        tokenCursor++;
        return getCurrentToken();
    }

    private String getNextTokenType() {
        return getNextToken().getType();
    }*/

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
