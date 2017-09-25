# JAVAREA
JAVAREA - Android Application used to control Electrical appliances through bluetooth
In this app the user signs up, Enters the map of his house(Rooms, Electrical appliances, type of applicances) and gives each room and electrical appliance a name by which we can identify the appliance. Then there were two ways by which you could control your appliances by the User Interface or you could speak the room name,appliance name and what operation you wanted to do on it (On or off).

This is app contains three major modules:
-Communication with firebase
-Communication with Bluetooth module
-Speech input and proccesing the speech inputs to commands.

Communication with firebase:
We used firebase because it was a real time database and we can send and recieve data in real time.

Communication with bluetooth module:
For communication with the bluetooth module, all we needed was the mac address of the bluetooth module which we stored in the database when the map of the house was entered, a connection was opened with the bluetooth module and a string was sent to it and then parsed at the arduino side.

Speech Processing:
We used google's speech to text api for the input of the string and then we did some natural language processing with which we mapped the entered string with our dictionary. Also to minimize the error of google's api we used the minimum edit distance algorithm.


