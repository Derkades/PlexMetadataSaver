# Compiling from source
1. Clone [Plex4J](https://github.com/Derkades/Plex4J) and run `mvn install`
2. Clone this project and run `mvn package shade:shade`

# Usage
1. Install java
2. Put the jar file in an empty folder on same server/vm/jail your Plex Media Server is running on.
3. Run the jar file using `java -jar plex-metadata-saver-*.jar`
4. The program will create a configuration file (`config.yml`) in the directory. Edit the configuration file and run the program again.

Note
* It will take a long time for the program to finish processing all tracks if you have a large music library. Make sure the user you are running the jar file from has permission to write to the files in your library, to avoid having to run the program for a second time.