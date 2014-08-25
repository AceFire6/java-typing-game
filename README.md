# Assignment 2 - Safety with Concurrency and Synchronization
#### Author: Jethro Muller
#### Student Number: MLLJET001
#### Date: 19 August 2014

## Description

A typing game where the player has to type the word and press enter before the word reaches the red rectangle area at the bottom of the screen.

The aim of the assignment was to explore concurrency and the synchronization and thread safety issues surrounding it.

## Instructions

1. Locate and move to the `src/` directory within `assignment-2`.
2. Run `make` to compile the source files.
3. Run `make run-default` to run the code with the `example_dict.txt` word dictionary file.
4. Run `make run FILE=<file_path>` to use a custom dictionary file.
5. Run `make run-no-file` to use the built-in word dictionary.

If you wish to change the total selection of words, or the number of words on the screen, 
go into the `makefile` and change:

```
CONCURRENT_WORDS = 4
TOTAL_WORDS = 25
```

When you have the game running. Press the `Start` button to start the game. You can push the 
`Pause` button to pause the game. Push the `Reset` button to reset the game to its start state.

The words will scroll down the screen. You need to type out the words and press `Enter`. 
You will get points depending on the words length.

After getting 25 words right, you will win the game. If you miss 10 words, you will lose.
