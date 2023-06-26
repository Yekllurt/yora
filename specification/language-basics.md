# Language Basics

## Table of Contents

1. [Program Structure](#program-structure)
2. [Variables](#variables)
    1. [Basic Data Types](#basic-data-types)
    2. [Arrays](#arrays)
3. [Operators](#operators)
    1. [Simple Assignment Operators](#simple-assignment-operator)
    2. [Arithmetic Operators](#arithmetic-operators)
    3. [Unary Operators](#unary-operators)
    4. [Equality and Relational Operators](#equality-and-relational-operators)
    5. [Conditional Operators](#conditional-operators)
4. [Control flow statements](#control-flow-statements)
    1. [The if-then Statement](#the-if-then-statement)
    2. [The if-then-else Statement](#the-if-then-else-statement)
    3. [The while Statement](#the-while-statement)
5. [Functions](#functions)
6. [Native functions](#native-functions)
    1. [I/O Functions](#io-functions)
    2. [Mathematical Functions](#mathematical-functions)
7. [Source](#source)

## Program Structure

## Variables

### Basic Data Types

| Datatype | Description                                                              | Note              |
|----------|--------------------------------------------------------------------------|-------------------|
| int      | The int data type is a 64-bit two's complement integer                   |                   |
| float    | The float data type is a double-precision 64-vit IEEE 754 floating point |                   |
| boolean  | The boolean data type has two possible values: true and false            | Not yet supported |
| string   | The string data type is a list of characters                             |                   |

### Arrays

An arrays is a container that holds a fixed number of values of a single data type.

## Operators

### Simple Assignment Operator

| Operator | Description                |
|----------|----------------------------|
| =        | Simple Assignment operator |

### Arithmetic Operators

| Operator | Description             |
|----------|-------------------------|
| +        | Additive operator       |
| -        | Subtraction operator    |
| *        | Multiplication operator |
| /        | Division operator       |
| ^        | Power operator          |

### Unary Operators

| Operator | Description                                    |
|----------|------------------------------------------------|
| +        | Unary plus operator; indicates positive values |
| -        | Unary minus operator; negates an expression    |

### Equality and Relational Operators

| Operator | Description              |
|----------|--------------------------|
| ==       | Equal to                 |
| !=       | Not equal to             |
| &#62;    | Greater than             |
| &#62;=   | Greater than or equal to |
| <        | Less than                |
| <=       | Less than or equal to    |

### Conditional Operators

| Operator     | Description     | Note              |
|--------------|-----------------|-------------------|
| &&           | Conditional-AND | Not yet supported |
| &#124;&#124; | Conditional-OR  | Not yet supported |

## Control Flow Statements

### The if-then Statement

The if-then statement is a basic control flow statement that tells your program to execute a certain section of code
only if a particular expression evaluates to true. <br>
Example:

```
if(<condition>)
    <statements>
    <return-statement>
end;
```

### The if-then-else Statement

The if-then-else statement is similar to the if-then statement a basic control flow statement with the difference that
if the given condition is false, it can execute an alternativ set of statements.

```
if(<condition>)
    <statements>
    <return-statement>
else:
    <statements>
    <return-statement>
end;
```

Note: Not yet supported

### The while Statement

The while statement continually executes a block of statements while a particular condition is true.

```
while(<condition>)
    <statements>
end;
```

## Functions

### Definition

A function declares executable code that can be invoked from anywhere within the code (Note: recursion is not
supported), passing a fixed number of values as arguments and having a well-defined return type (maybe void).

```
<return-type> <identifier>(<parameters>)
    <statements>
    <return-statement>
end;
```

### Invoking

This is also valid for [native functions](#native-functions).

```
<identifier>(<parameters>);
```

## Native Functions

Native functions are provided by the programming language and can not be overwritten by the user. Furthermore, a user
function may not have the same name as a native function.

### I/O Functions

#### print

Function: `print(<value>)` <br>
Description: The print function prints a given argument to the console <br>
Parameters:

* `value`
    * Datatype: any datatype
    * Description: the datatype that is to be printed to the console

### Mathematical Functions

#### cos

Function: `cos(<value>)` <br>
Description: Calculates the cosine value of a value <br>
Parameters:

* `value`
    * Datatype: int or float
    * Description: the int or float from which the sin value should be calculated

#### sin

Function: `sin(<value>)` <br>
Description: Calculates the sine value of n value <br>
Parameters:

* `value`
    * Datatype: int or float
    * Description: the int or float from which the sin value should be calculated

#### sqrt

Function: `sqrt(<value>)` <br>
Description: Calculates the sqrt value <br>
Parameters:

* `value`
    * Datatype: int
    * Description: the int from which a sqrt should be calculated

#### randl

Function: `randl(<min>, <max>)` <br>
Description: Calculates a random int within the given bounds <br>
Parameters:

* `min`
    * Datatype: int
    * Description: the minimum bound (inclusive)
* `max`
    * Datatype: int
    * Description: the upper bound (inclusive)

## Source

Inspiration for this format is drawn by the Java
documentation (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html) and java
specification (https://docs.oracle.com/javase/specs/jls/se12/html/index.html)