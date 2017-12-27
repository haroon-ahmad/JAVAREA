# JAVAREA
JAVAREA - Android application designed to control electrical appliances via bluetooth.

In this app the user signs up, enters the map of his house(Rooms, Electrical Appliances, Type of applicances) and gives each room and electrical appliance a name by which we can identify the appliance. Then, there are two ways by which you can control your appliances:
1. User Interface
2. Speaking the room name, appliance name and the operation to be performed (On or off).

This App contains three major modules:

-Communication with firebase

-Communication with Bluetooth module

-Speech input and tokenizing of the speech input to valuable commands.

Communication with firebase:

We used firebase because it was a real time database and we could send and recieve data in real time.

Communication with bluetooth module:

For communication with the bluetooth module, all we needed was the mac address of the bluetooth module which we stored in the database when the map of the house was entered, a connection was opened with the bluetooth module and a string was sent to it and then parsed at the arduino side.

Speech Processing:

We used Google's speech-to-text API for the input of the string and then we did some natural language processing with which we mapped the entered string to the closest match in our dictionary. Also to minimize the error of Google's Speech Recognition API, we used the minimum edit distance algorithm.


