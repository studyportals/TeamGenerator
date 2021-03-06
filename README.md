# Team generator usage guide

Team generator is used to generate teams from a given list of people according to given **criteria**, distributing people who have the same criteria evenly throughout the teams.

- [**Expected input**](#expected-input)
- [**Application usage**](#application-usage)
- [**Output**](#output)
- [**How does criteria work?**](#how-does-criteria-work)

For running the application the first time:
- [**How to run the application?**](#how-to-run-the-application)
- [**How to create a CSV file?**](#how-to-create-a-csv-file)

## Expected input:

- **CSV** (comma or semi comma separated) file, with columns:
  - **Name** (mandatory)
  - **Joining** (mandatory) - values &#39;no&#39; and empty means these people should not be taken into the teams)
  - **Criteria** columns (if you want some criteria to be taken into consideration when dividing into teams)
- Quotes, commas and semi commas should **not** be used in the file.

![alt text](img/CSV_input_sample.png "CSV file input example")

_1 pic – CSV file input example_

## Application usage:

![alt text](img/app_sample.png "application")

_2 pic – application_

1. Select a csv file to be processed.
2. Change number of teams to the correct one (if needed) – default 2.
3. Change number of tries to make teams perfect (if needed) – default 500. The bigger the number – the more time it takes, but bigger chances to make teams perfect.
4. Change the name of the output file (if needed) – default same name as the input file, but added _\_teams_ in the end.
5. Render

After rendering the file an information popup will appear explaining what happened.

![alt text](img/app_info_sample.png "Information window example")

_3 pic – Information window example_

If the application did not manage to distribute people evenly it will explain what was not distributed evenly.

In the example:

1. Team 3 has 15 people with a criteria _not together,_ which has an empty value, even though after distributing evenly this team should only have 13 or 14 people with that specific criteria with that value.
2. Team 4 has multiple discrepancies:
  1. two people from South park (criteria: team with value South Park), even though after distributing evenly this team should only have 0 or 1 person from South Park.
  2. five people are females, even though after distributing evenly this team should have 6 or 7 females.
3. Team 5 has two people from Finance, even though after distributing evenly this team should only have 0 or 1 person from Finance.

500/500 tries have been used. That means that if better results are wanted – the number of tries should be increased. The system tries to get the best results. If it manages to distribute people evenly it stops processing and returns the result without using all the given tries.

## Output:

**CSV** file containing people divided into a selected number of teams, trying to distribute them according to their criteria. People who are not joining (according to the joining column) are not taken into teams.

![alt text](img/CSV_output_sample.png "CSV output example")

_4 pic – CSV output example_

## How does criteria work?

The application is trying to distribute people evenly throughout the teams according to their criteria. For example: trying to have teams with equal number of females and males.

If you don&#39;t want two people to be in the same team – assign a criteria that would have the same unique value only for those two people. For example in the _1 pic_ there are two people who have a unique value 5 for a criteria Not\_together. This means that the other four people have a Not\_together criteria of empty.

# How to run the application?

The application is a Java 11 executable file. That means that you have to have a Java 11 Runtime Environment. It needs to be installed on the machine only once.
Because Oracle doesn't provide Java 11 Runtime Environment as a downloadable file yet, we will install Java 11 Development Kit which allows to run and develop applications.
Please go here to download it:

https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html

![alt text](img/JDK_11_download.png "JDK 11 download page")

_5 pic – JDK 11 download page_

1. Accept the License Agreement
2. If you have windows – download the jdk-11.0.2\_windows-x64\_bin.exe
3. Install it by clicking next/accept all the time
4. After installation the application will have a Java runnable icon (_6 pic)_ – that means you can double click it and it will run

![alt text](img/java_icon_sample.png "Team Generator application icon")

_6 pic – Team Generator application icon_

# How to create a CSV file?

Create an excel file and save it as **CSV (Comma delimited) (\*.csv)**. Other formats are not guaranteed to work.

![alt text](img/save_as_CSV_sample.png "Save as CSV file")

_7 pic – Save as CSV file_
