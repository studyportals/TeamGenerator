package studyportals;

import java.io.*;
import java.util.*;

public class FileHandler {

    private static final List<String> POSSIBLE_SCV_SPLITTERS = Arrays.asList(",", ";");

    private File csvFile;
    private String cvsSplitBy;

    public FileHandler(File csvFile){
        this.csvFile = csvFile;
        try (BufferedReader br = new BufferedReader(new FileReader(this.csvFile))) {
            String line = br.readLine();
            this.cvsSplitBy = getSCVSplitter(line);
        } catch (IOException e) {
            throw new Error("There is no such file: " + this.csvFile.getAbsolutePath());
        }
    }

    public Data getData(Integer amountOfTeams){
        Data data;

        try (BufferedReader br = new BufferedReader(new FileReader(this.csvFile))) {
            String line = br.readLine();
            String[] person = line.split(this.cvsSplitBy);

            checkIfCSVColumnNamingCorrect(person);
            Map<String, Integer> criteriaMapInCSV = getCriteriaMapInCSV(person);

            List<Person> peopleList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                person = line.split("\\" + this.cvsSplitBy, -1);
                String joiningColumnValue = person[1].trim().toLowerCase();
                Boolean isJoining = !joiningColumnValue.isEmpty() && !joiningColumnValue.equals("no");
                if(isJoining) {
                    peopleList.add(getPersonData(person, criteriaMapInCSV, amountOfTeams));
                }
            }

            data = new Data(peopleList, amountOfTeams);

        } catch (IOException e) {
            throw new Error("There is no such file: " + this.csvFile.getAbsolutePath());
        }
        return data;
    }

    public void writeToFile(String outputFileAbsolutePath, List<List<Person>> teams, Integer maxPeoplePerTeam){
        try(FileWriter writer = new FileWriter(outputFileAbsolutePath)) {

            List<String> teamNames = new ArrayList<>();
            for(int teamNr = 0; teamNr < teams.size(); teamNr++){
                teamNames.add("Team_" + (teamNr + 1));
            }
            CSVUtils.writeLine(writer, teamNames, this.cvsSplitBy.toCharArray()[0]);

            for(int line = 0; line < maxPeoplePerTeam; line++){
                List<String> peopleNamesPerLine = new ArrayList<>();
                for(List<Person> team: teams){
                    peopleNamesPerLine.add(team.size() > line ? team.get(line).getName() : "");
                }
                CSVUtils.writeLine(writer, peopleNamesPerLine, this.cvsSplitBy.toCharArray()[0]);
            }

            writer.flush();
        } catch (IOException e) {
            throw new Error("The application cannot access the file:\n" + outputFileAbsolutePath + "\nPlease close the file before trying to write to it");
        }
    }

    private static Person getPersonData(String[] person, Map<String, Integer> criteriaMapInCSV, Integer amountOfTeams){
        List<Criteria> criterias = new ArrayList<>();
        for (Map.Entry<String, Integer> criteriaMap : criteriaMapInCSV.entrySet()){
            criterias.add(new Criteria(criteriaMap.getKey(), person[criteriaMap.getValue()], amountOfTeams));
        }
        return new Person(person[0].trim(), criterias);
    }

    private static void checkIfCSVColumnNamingCorrect(String[] person){
        if(person.length < 2){
            throw  new Error("There have to be at least two columns set: name and joining");
        }

        String firstColumnName = person[0].trim().toLowerCase();
        if(!firstColumnName.contains("name")){
            throw new Error("The first column of a file must be: name\nIn uploaded file it is: " + person[0].trim());
        }

        String secondColumnName = person[1].trim().toLowerCase();
        if(!secondColumnName.contains("joining")){
            throw new Error("The second column of a file must be: joining\nIn uploaded file it is: " + person[1].trim());
        }
    }

    private static Map<String, Integer> getCriteriaMapInCSV(String[] person){
        Map<String, Integer> criteriaMapInCSV = new HashMap<>();
        for(int i = 2; i < person.length; i++){
            String value = person[i].trim().toLowerCase();
            criteriaMapInCSV.put(value, i);
        }
        return criteriaMapInCSV;
    }

    private String getSCVSplitter(String line){
        for(String splitter: POSSIBLE_SCV_SPLITTERS) {
            String[] columns = line.split(splitter);
            if(columns.length >= 2){
                return splitter;
            }
        }
        return POSSIBLE_SCV_SPLITTERS.get(0);
    }
}
