# Battle-of-Immortals---Online-Players

This tool is built using JavaFx 2.0.

Prerequisites:
1. Java 8.
2. Scene Builder installation.
3. Eclipse with JavaFx installed.
4. Apache Commons IO dependency.
5. SQL server jdbc dependency.
6. MySQL jdbc dependency.

Edit the config file and make necessary changes to read the log path and set connectivity to MySQL and SQL server database.

Approach:
This tool uses similar approach of tail Linux command. It reads the logs from the files, parses the required logs based on regular expressions and fetches the necessary details from MySQL database.

It keeps track of online players in SQL server onlinePlayer table.

Any suggestions would be appreciated.
