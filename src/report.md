# Concurrency & Synchronization
### Assignment 2 - Report

#### Jethro Muller
#### MLLJET001
#### August 2014

## Description

This assignment was designed to test one's understanding of concurrent data access and 
synchronization. Using the Model-View-Controller(MVC) Pattern was also a requirement.

The assignment was in the form of a typing game. Words were taken from a dictionary and displayed
 on the screen. The words scroll down the screen and the player must enter the word correctly 
 before it reaches the red section at the bottom of the screen. If the player succeeds, 
 the player is awarded points based on the word's length. If the player fails to 'catch' the 
 word, a counter of missed words is increased and once the player has missed 10 words, 
 the player loses.

## Additional Classes

During the writing of my code, I added two new classes. The WordThread class and the 
WordController class.

The WordThread class takes a WordRecord object as a parameter during it's instantiation. It 
handles the dropping of the word. It also checks the current game state to mae sure the game 
hasn't ended.

The WordController class is a class that is passed as an parameter to most of the other class 
files. The WordController class is used to perform actions based on the game state as well as to 
create the WordThread objects when the game is started.

### Changed Classes

As well as adding two additional classes, I have made changes to the existing classes.

In the Score class, I added synchronization to any events that weren't entirely atomic and 
changed the integer variables to AtomicInteger so as to not need to synchronize their getters and
 setters.
 
I added code to the `run()` method in the WordPanel class that updates and repaints the panel.

I changed the way the `fallingSpeed` of a WordRecord instance is set to have fewer repetitions of
 the same code. I also overrode the default `equals()` method to allow for my WordRecord 
 comparator to work correctly.
 
I rearranged the code in all the provided classes because I enjoy a consistent style in my code. 
I also rearranged the code to make it easier to pass the WordController the required object 
references.

## Required Concurrency Features

I used the `AtomicInteger` class, the `volatile` variable modifier and the `synchronized` method 
modifier.

I used the `AtomicInteger` class to allow for atomic actions and a reduced number of synchronized
 methods.
 
I used `volatile` for booleans that were accessed by more than one Thread object.

I used `synchronized` for methods that are used to poll variables or update them.

## Code Concurrency Methodology

The way I tried to ensure thread safety was by using atomic data classes and by using 
synchronized methods where concurrency was necessary. This allows for the same manipulation of 
data without the risk of race conditions.
 
 I didn't have any need for explicit thread synchronization.
  
 To resolve any liveness issues I used synchronized methods and made sure there were no cases 
 that could cause deadlock and none of the threads that access shared data are greedy and will 
 cause starvation.
 
## Testing Concurrency

I tested the `AtomicInteger`'s used by the getters and setters in the `Score` class by setting 
all the WordRecords to have the same falling speed so they all reached the bottom at the same time.

The incrementing and getting worked properly even under these conditions.

Numerous playtests were done as well and no anomolies were detected (this doesn't mean there 
aren't any problems).

## MVC Conformity

As I understand it, my code conforms to the MVC pattern for the most part.

The data is changed by the controller and the view updates drawing data from the model.

The `WordPanel` class is the 'view' in my code. It repaints itself, 
each time drawing on the model for data.

The `Score`, `WordDictionary` and `WordRecord` classes are the models. They get updated by the 
controller and the view polls them for data when it updates.

The `WordThread` and `WordController` classes are both controllers of sort. They facilitate the 
changing 
of the model.

## Additional Features

1. If there are two of the same word on the screen and the player enters the correct text, 
the word that is closest to the bottom of the screen are removed first, 
and only one of the words is deleted.

2. A counter of the number of words the player has typed incorrectly so they can more accurately 
track their performance.
