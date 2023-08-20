# Language Basics

## Table of Contents

1. [Program Structure](#program-structure)
2. [Examples](#examples)
3. [Types, Values and Variables](#types-values-and-variables)
    1. [The Kinds of Types and Values](#the-kinds-of-types-and-values)
    2. [Primitive Types and Values](#primitive-types-and-values)
        1. [Integer Types, Values and Operations](#integer-types-values-and-operations)
        2. [Floating-Point Types, Values and Operations](#floating-point-types-values-and-operations)
        3. [`bool` Types, Values and Operations](#bool-types-values-and-operations)
        4. [`str` Types, Values and Operations](#str-types-values-and-operations)
    3. [Reference Types and Values](#reference-types-and-values)
        1. [`array` Types Values and Operators](#array-types-values-and-operators)
    4. [Variables](#variables)
        1. [Initial Values of Variables](#initial-values-of-variables)
        2. [Native Variables](#native-variables)
            1. [Constants](#constants)
            2. [System Variables](#system-variables)
4. [Statements](#statements)
    1. [Completion of Statements](#completion-of-statements)
    2. [The `if` Statement](#the-if-statement)
        1. [The `if-then` Statement](#the-if-then-statement)
        2. [The `if-then-else` Statement](#the-if-then-else-statement)
    3. [The `while` Statement](#the-while-statement)
5. [Names](#names)
    1. [Declaration](#declaration)
    2. [Names and Identifiers](#names-and-identifiers)
    3. [Scope of a Declaration](#scope-of-a-declaration)
6. [Methods](#methods)
    1. [Invoking](#invoking)
    2. [Native Functions](#native-functions)
        1. [I/O Functions](#io-functions)
        2. [Mathematical Functions](#mathematical-functions)
7. [Source](#source)

## Program Structure

Every program contains a method like the following that is called upon execution of the program.

```
void main()
    // The program code    
end;

// Other program code
```

## Examples

In the `examples/programs` directory as well as its subdirectories a list of example programs can be found demonstrating
certain functionalities.

## Types, Values and Variables

The Yora programming language is a statically an strongly typed language.

### The Kinds of Types and Values

The Yora programming language has primitive types as well as reference types.

### Primitive Types and Values

The `primitive` types are the `bool` type, `str` type and `numeric` types. Where the `numeric` types are `int`
and `float`.
The `int`
values are 64-bit signed two's complemented integers and the `float` values are 64-bit IEEE 754 binary 64 floating-point
numbers. <br>
Primitive values do not share their state with other primitive values.

#### Integer Types, Values and Operations

The value of the integer type is in the following range:

- For `int`, from -9223372036854775808 to 9223372036854775807 (inclusive)

The integer values operators:

- The comparison operators which result in a value of type `bool`:
    - The numerical comparison operators: `<`, `<=`, `>` and `>=`
    - The numerical equality operators: `==` and `!=`
- The numerical operators which result in a value of type `int`:
    - The power operator: `^`
    - The unary plus and minus operators: `+` and `-`
    - The multiplicative operators: `*`, `/` and `%`
    - The additive operators: `+` and `-`
- The string concatenation operator `+`

An integer operator can terminate the program for the following reasons:

- The multiplicative operators `/` or `%` have as right-hand operand a `0`

#### Floating-Point Types, Values and Operations

The floating-point values operators:

- The comparison operators which result in a value of type `bool`:
    - The numerical comparison operators: `<`, `<=`, `>` and `>=`
    - The numerical equality operators: `==` and `!=`
- The numerical operators which result in a value of type `float`:
    - The power operator: `^`
    - The unary plus and minus operators: `+` and `-`
    - The multiplicative operators: `*`, `/`, `%`
    - The additive operators: `+` and `-`
- The string concatenation operator `+`

A floating-point operator can terminate the program for the following reasons:

- The multiplicative operators `/` or `%` have as right-hand operand a `0`

#### `bool` Types, Values and Operations

The `bool` type represents a logical state with the value `true` or `false`.

The `bool` operators are:

- The relational operators `==` and `!=`
- The conditional-and and conditional-or operators `&&` and `||`
- The string concatenation operator `+`

Boolean expressions determine the control flow of the `if` and `while` statement. Furthermore, only `bool` expressions
can be used in control flow statements.

#### `str` Types, Values and Operations

The `str` type represents a character string in other words a text.

The `str` operators are:

- The relational operators `==` and `!=`
- The string concatenation operator `+`

### Reference Types and Values

The reference types are `array`.

#### `array` Types Values and Operators

The `array` type represents a container that holds a fixed number of single types. The length of the `array` as well as
data type is established upon its creation. <br>
Valid data types are the following:

- `int` where `0` is the default value
- `float` where `0` is the default value
- `bool` where `false` is the default value
- `str` where `""` (an empty string) is the default value

Declaring an array: `data-type[size] variable-name;` <br>
Accessing data: `variable-name[index]` Note that arrays start at index 0<br>
Setting a value: `variable-name[index] = data;`

### Variables

A variable is a storage location with an associated type that is either a primitive type or reference type. A variable
value can only be changed by an assignment.

A variable of a primitive type always holds a primitive value of that exact primitive type. <br>
A variable of type `str` holds a reference to an immutable string. <br>
A variable of type array holds a reference to and array. <br>

#### Initial Values of Variables

Every variable in a program must have a value before its value is used. For the types `int`, `float`, `bool` and `str`
it is the assigned value by the program. For the types `int[]`, `float[]`, `bool[]` and `str[]` it
is `0`, `0`, `false`, `""` respectively.

#### Native Variables

Note: The name is case-sensitive and only native variables are allowed to be all uppercase

##### Constants

A native constant is a pre-defined immutable variable holding a commonly used value.

| Name | Value        |
|------|--------------|
| PI   | 3.1415926535 |
| E    | 2.7182818284 |

##### System Variables

A system variable that holds a (dynamic) variable.

| Name          | Description                                                        |
|---------------|--------------------------------------------------------------------|
| OSARCH        | The operating system architecture on which the program is executed |
| OSNAME        | The operating system name on which the program is executed         |
| JAVAVERSION   | The java version                                                   |
| JAVAVMVERSION | The java vm version                                                |
| JAVAVMVENDOR  | The java vm vendor                                                 |

## Statements

### Completion of Statements

TODO return

### The `if` Statement

The `if` statement allows conditional execution of a statement or a conditional choice of two statements.

#### The `if-then` Statement

```
if(<expression>)
    <statements>
end;
```

The `if-then` statement is executed by first evaluating the expression that must evaluate to a `bool` type.

Based on the expression result, one of the following actions is performed:

- If the value is `true`, then the contained statement(s) is/are executed
- If the value is `false`, then no further action is taken

#### The `if-then-else` Statement

```
if(<expression>)
    <statements>
else
    <statements>
end;
```

The `if-then-else` statement is executed by first evaluating the expression that must evaluate to a `bool` type.

Based on the expression result, one of the following actions is performed:

- If the value is `true`, then the first contained statement(s) is/are executed
- If the value is `false`, then the second contained statement(s) is/are executed

### The `while` Statement

```
while(<expression>)
    <statements>
end;
```

The `while` statement executes an expression that must evaluate to a `bool` type and the contained statement(s) if the
expression is `true` until the expression is `false`.

## Names

Names are used to refer to entities declared in the program.

A declared entity is a method, argument of a method or local variable.

Every declaration that introduces has a scope which determines the part of the program from which the declared entity
can be accessed.

### Declaration

A declaration introduces an entity into a program and includes an identifier that can be used in a name to refer to this
entity.

A declared entity is one of the following:

- A method
- An argument of a method
- A local variable

### Names and Identifiers

A name is used to refer to an entity declared in a program and is a single identifier. An identifier is characterized by
the following `[a-zA-Z]+` structure, note that uppercase identifiers are reserved for the Yora programming language.

### Scope of a Declaration

The scope of a declaration is the region of the program within which the entity can be referred to using a name.

The scope of a member is the entire program.

The scope of a method argument is the entire declaration of the method.

The scope of a local variable are the following statements in its block as well as its sub blocks in a method
declaration.

## Methods

A function declares executable code that can be invoked from anywhere within the code (Note: recursion is not
officially supported), passing a fixed number of values as arguments and having a well-defined return type (maybe void).

<b>argument Structure</b><br>
Structure:`varaible-type identifier`<br>
Valid variable types: `int`, `float`, `bool`, `str`, `int[]`, `float[]`, `bool[]`, `str[]`<br>
Multiple arguments can be chained by dividend by dividing them with a `,`.

<b>Return types</b>: `int`, `float`, `bool`, `str`, `int[]`, `float[]`, `bool[]`, `str[]`, `void` where the return
type `void` signals that the function does not return any data.

```
<return-type> <identifier>(<arguments>)
    <statements>
end;
```

### Invoking

This is also valid for [native functions](#native-functions).

```
<identifier>(<arguments>);
```

### Native Functions

Native functions are provided by the programming language and can not be overwritten by the user. Furthermore, a user
function may not have the same name as a native function.

#### I/O Functions

##### println

Function: `println(<value>)` <br>
Description: The print function prints a given argument to the console <br>
Arguments:

* `value`
    * Datatype: any datatype
    * Description: the datatype that is to be printed to the console

##### readln

Function: `readln(<type>, <error-message>)` <br>
Description: Reads a value from the console <br>
Arguments:

* `type`
    * Datatype: str
    * Description: the datatype that is to be read from the console, supported data types: int, float, str
* `error-message`
    * Datatype: str
    * Description: the message that is printed to the console when the input value is invalid

#### Mathematical Functions

##### cos

Function: `cos(<value>)` <br>
Description: Calculates the cosine value of a value <br>
Arguments:

* `value`
    * Datatype: int or float
    * Description: the int or float from which the sin value should be calculated

##### sin

Function: `sin(<value>)` <br>
Description: Calculates the sine value of n value <br>
Arguments:

* `value`
    * Datatype: int or float
    * Description: the int or float from which the sin value should be calculated

##### sqrt

Function: `sqrt(<value>)` <br>
Description: Calculates the sqrt value <br>
Arguments:

* `value`
    * Datatype: int
    * Description: the int from which a sqrt should be calculated

##### randl

Function: `randl(<min>, <max>)` <br>
Description: Calculates a random int within the given bounds <br>
Arguments:

* `min`
    * Datatype: int
    * Description: the minimum bound (inclusive)
* `max`
    * Datatype: int
    * Description: the upper bound (inclusive)

## Source

Inspiration for this format is drawn by the Java
documentation (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html) and java
specification (https://docs.oracle.com/javase/specs/jls/se12/html/index.html).