# Language Basics

## Variables

| Datatype | Description                                                              |
|----------|--------------------------------------------------------------------------|
| int      | The int data type is a 64-bit two's complement integer                   |
| float    | The float data type is a double-precision 64-vit IEEE 754 floating point |
| boolean  | The boolean data type has two possible values: true and false            |
| string   | The string data type is a list of characters                             |                                            |   |   |   |

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

The if-then statement is a basic control flow statement that tells your programm to execute a certain section of code
only if a particular expression evaluates to true. <br>
Example:

```
if(<condition>):
    <statements>
end:
```

### The while Statement

The while statement continually executes a block of statements while a particular condition is true.

```
while(<condition>):
    <statements>
end:
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

# Source

Inspiration for this format is drawn by the Java
documentation (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/index.html)