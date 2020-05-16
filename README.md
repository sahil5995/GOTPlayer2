Game Of Three - Player 2
========================

* To start the player, go to file StartPlayer.java and run it.
* it will start the application on port 8081 which is defined in application.yml file.
* Now it will try to connect to Player 1 to start the game.
* If player 1 is up and running, it will connect and start taking inout from user.
* If player 1 is not up, it will retry to connect to that 5 times. If after 5 times still it is not able to connect,
it will show the error message and exit the application.


How to Play the game:
--------------------
* When the application starts, player will be asked if he wants to manually enter some number 
or wants the auto generated number.
* Player can provide the input by using options 1 or 2.
* If player chooses 1, an auto number will be generated and game will start.
* If player chooses 2, then he can provide the number of his choice and game will start then.
* After this, both players will be sending and receiving numbers automatically. They can see it on console/Terminal.
* At the end, result will shown to both the players, who has won or lost.
