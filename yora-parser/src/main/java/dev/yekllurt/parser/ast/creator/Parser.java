package dev.yekllurt.parser.ast.creator;

import dev.yekllurt.parser.ast.ASTNode;
import dev.yekllurt.parser.ast.impl.*;
import dev.yekllurt.parser.ast.throwable.exception.GrammarException;
import dev.yekllurt.parser.ast.throwable.exception.UnsupportedTokenTypeException;
import dev.yekllurt.parser.ast.throwable.exception.ParserException;
import dev.yekllurt.parser.collection.SequencedCollection;
import dev.yekllurt.parser.token.Token;
import dev.yekllurt.parser.token.TokenType;

import java.util.List;
import java.util.Objects;

public class Parser {

    private final SequencedCollection<Token> tokens;
    private int tokenCursor = 0;

    public Parser(SequencedCollection<Token> tokens) {
        this.tokens = tokens;
    }

    public ASTNode parse() {
        var result = parseProgram();
        if (isParseNotCompleted()) {
            throw new ParserException("Didn't use all available tokens");
        }
        return result;
    }

    // == Parse methods ==

    /**
     * Handle the programm grammar production rule
     */
    private ASTNode parseProgram() {
        return ProgramNode.builder()
                .functions(parseMemberList())
                .build();
    }

    /**
     * Handle the member grammar production rule
     */
    private SequencedCollection<ASTNode> parseMemberList() {
        var members = new SequencedCollection<ASTNode>();
        var member = parseMember();
        while (member != null) {
            members.add(member);
            member = parseMember();
        }
        return members;
    }

    private ASTNode parseMember() {
        if (isParseCompleted()) {
            return null;
        }

        var token = getCurrentToken();
        if (List.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_CHAR, TokenType.KEYWORD_VOID).contains(token.getType())) {
            tokenCursor++;
            if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.IDENTIFIER)) {
                var tokenIdentifier = getCurrentToken();
                tokenCursor++;
                if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_LEFT_BRACE)) {
                    tokenCursor++;
                    SequencedCollection<ASTNode> parameters = null;
                    if (isParseNotCompleted() && List.of(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_CHAR, TokenType.KEYWORD_VOID).contains(getCurrentTokenType())) {
                        parameters = parseParameterList();
                    }
                    if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_RIGHT_BRACE)) {
                        tokenCursor++;
                        SequencedCollection<ASTNode> statements = null;
                        if (isParseNotCompleted() && !getCurrentTokenType().equals(TokenType.KEYWORD_RETURN)) {
                            statements = parseStatementList();
                        }
                        if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.KEYWORD_RETURN)) {
                            tokenCursor++;
                            var returnExpr = parseReturn();
                            if (Objects.nonNull(returnExpr) && isParseNotCompleted() && getCurrentTokenType().equals(TokenType.KEYWORD_END)) {
                                tokenCursor++;
                                if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_SEMICOLON)) {
                                    tokenCursor++;
                                    return FunctionNode.builder()
                                            .identifier(tokenIdentifier.getValue())
                                            .returnType(token.getType())
                                            .parameters(parameters)
                                            .statements(statements)
                                            .returnStatement(returnExpr)
                                            .build();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private SequencedCollection<ASTNode> parseParameterList() {
        var parameters = new SequencedCollection<ASTNode>();
        var parameter = parseParameter();
        if (Objects.nonNull(parameter)) {
            parameters.add(parameter);
            while (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_COMMA)) {
                tokenCursor++;
                parameter = parseParameter();
                if (Objects.nonNull(parameter)) {
                    parameters.add(parameter);
                } else {
                    return null;
                }
            }
        }
        return parameters;
    }

    private ASTNode parseParameter() {
        if (isParseCompleted()) {
            return null;
        }
        var token = getCurrentToken();
        if (token.getType().matches("INT|FLOAT|BOOLEAN|CHAR|VOID")) {
            tokenCursor++;
            if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.IDENTIFIER)) {
                var tokenIdentifier = getCurrentToken();
                tokenCursor++;
                return ParameterNode.builder()
                        .type(token.getType())
                        .identifier(tokenIdentifier.getValue()).build();
            }
        }
        return null;
    }

    private SequencedCollection<ASTNode> parseStatementList() {
        var statements = new SequencedCollection<ASTNode>();
        var statement = parseStatement();
        if (Objects.nonNull(statement)) {
            statements.add(statement);
            while (isParseNotCompleted() && getCurrentTokenType().matches("IDENTIFIER|INT|FLOAT|BOOLEAN|CHAR|VOID")) {
                statement = parseStatement();
                if (Objects.nonNull(statement)) {
                    statements.add(statement);
                } else {
                    return null;
                }
            }
        }
        return statements;
    }

    private ASTNode parseStatement() {
        if (isParseCompleted()) {
            return null;
        }

        var token = getCurrentToken();
        switch (token.getType()) {
            case TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_BOOLEAN, TokenType.KEYWORD_CHAR, TokenType.KEYWORD_VOID -> {
                tokenCursor++;
                if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.IDENTIFIER)) {
                    var tokenIdentifier = getCurrentToken();
                    tokenCursor++;
                    if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_EQUAL)) {
                        tokenCursor++;
                        ASTNode expression = parseExpression();
                        if (Objects.nonNull(expression) && isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_SEMICOLON)) {
                            tokenCursor++;
                            return VariableDeclarationNode.builder()
                                    .type(token.getType())
                                    .identifier(tokenIdentifier.getValue())
                                    .value(expression)
                                    .build();
                        }
                    }
                }
            }
            case TokenType.IDENTIFIER -> {
                tokenCursor++;
                if (isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_EQUAL)) {
                    tokenCursor++;
                    ASTNode expression = parseExpression();
                    if (Objects.nonNull(expression)
                            && isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_SEMICOLON)) {
                        tokenCursor++;
                        return AssignmentNode.builder()
                                .identifier(token.getType())
                                .value(expression)
                                .build();
                    }
                }
            }
            default -> {
                // expression SEMICOLON - case
                ASTNode expression = parseExpression();
                if (Objects.nonNull(expression)
                        && isParseNotCompleted() && getCurrentTokenType().equals(TokenType.PUNCTUATION_SEMICOLON)) {
                    tokenCursor++;
                    return expression;
                }
            }
        }
        return null;
    }

    private ASTNode parseReturn() {
        var expression = parseExpression();
        if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_SEMICOLON)) {
            tokenCursor++;
            return ReturnNode.builder().value(expression).build();
        }
        return null;
    }

    private ASTNode parseTerm() {
        if (isParseCompleted()) {
            return null;
        }

        var token = getCurrentToken();
        switch (token.getType()) {
            case TokenType.DECIMAL_NUMBER -> {
                tokenCursor++;
                return TermNode.builder().value(token.getValue()).build();
            }
            case TokenType.PUNCTUATION_LEFT_BRACE -> {
                tokenCursor++;
                return parseBracketExpression();
            }
            case TokenType.IDENTIFIER -> {
                tokenCursor++;
                return parseIdentifierExpression(token);
            }
            default ->
                    throw new UnsupportedTokenTypeException(String.format("The token type '%s' is not supported", token.getType()));
        }
    }

    private ASTNode parseBracketExpression() {
        var expression = parseExpression();
        if (Objects.nonNull(expression) && isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE)) {
            return expression;
        }
        return null;
    }

    private ASTNode parseIdentifierExpression(Token identifierToken) {
        if (isNextToken(TokenType.PUNCTUATION_LEFT_BRACE)) {
            // It's a function call
            return parseFunctionCall(identifierToken);
        } else {
            // It's a variable
            return TermNode.builder().value(identifierToken.getValue()).build();
        }
    }

    private ASTNode parseFunctionCall(Token functionNameToken) {
        var expressionList = parseExpressionList();
        if (Objects.nonNull(expressionList) && isNextToken(TokenType.PUNCTUATION_RIGHT_BRACE) && getNextToken().getType().equals(TokenType.PUNCTUATION_SEMICOLON)) {
            tokenCursor++;
            return FunctionCallNode.builder()
                    .functionIdentifier(functionNameToken.getValue())
                    .arguments(expressionList)
                    .build();
        }
        return null;
    }

    private ASTNode parseExpression() {
        var left = parseTerm();
        if (Objects.nonNull(left) && isParseNotCompleted()) {
            String operator = getCurrentTokenType();
            if (List.of(TokenType.PUNCTUATION_PLUS, TokenType.PUNCTUATION_MINUS, TokenType.PUNCTUATION_STAR, TokenType.PUNCTUATION_DIVIDE).contains(operator)) {
                tokenCursor++;
                return parseOperatorExpression(left, operator);
            }
            return left;
        } else if (isParseNotCompleted() && isNextToken(TokenType.PUNCTUATION_MINUS)) {
            tokenCursor++;
            return parseTerm();
        }
        throw new GrammarException("Unknown grammar production rule for expression");
    }

    private ASTNode parseOperatorExpression(ASTNode left, String operator) {
        var right = parseExpression();
        if (Objects.nonNull(right)) {
            return ExpressionNode.builder().left(left).right(right).operator(operator).build();
        }
        return null;
    }

    private ASTNode parseExpressionList() {
        var expressionList = new SequencedCollection<ASTNode>();
        var expression = parseExpression();
        while (Objects.nonNull(expression)) {
            expressionList.add(expression);
            if (isNextToken(TokenType.PUNCTUATION_COMMA)) {
                tokenCursor++;
                expression = parseExpression();
                if (Objects.isNull(expression)) {
                    throw new GrammarException("Could not find a production");
                }
            }
        }
        return ExpressionListNode.builder().expressionList(expressionList).build();
    }

    // == Helper methods ==

    /**
     * Check if all tokens have been parsed
     */
    private boolean isParseCompleted() {
        return tokenCursor == tokens.size();
    }

    /**
     * Check if not all tokens have been parsed
     */
    private boolean isParseNotCompleted() {
        return !isParseCompleted();
    }

    /**
     * Get the current token
     */
    private Token getCurrentToken() {
        return tokens.get(tokenCursor);
    }

    /**
     * Get the current token type
     */
    private String getCurrentTokenType() {
        return getCurrentToken().getType();
    }

    /**
     * Get the next token and increment the cursor
     */
    private Token getNextToken() {
        var token = getCurrentToken();
        tokenCursor++;
        return token;
    }

    /**
     * Check if the next token matches a given type
     */
    private boolean isNextToken(String type) {
        return isParseNotCompleted() && getCurrentToken().getType().equals(type);
    }

}
