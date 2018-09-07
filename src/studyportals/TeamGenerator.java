package studyportals;

import java.io.*;
import java.util.*;


public class TeamGenerator {

    public static Information generate(Integer amountOfTeams, Integer totalTries, File csvFile, String outputFileAbsolutePath) {

        Integer minAmountOfMistakes = Integer.MAX_VALUE;
        List<List<Person>> finalTeams = new ArrayList<>();
        List<Mistake> finalTeamsMistakes = new ArrayList<>();

        int tryCount;
        FileHandler fileHandler = new FileHandler(csvFile);
        Data data = fileHandler.getData(amountOfTeams);

        for(tryCount = 1; tryCount < totalTries; tryCount++) {
            data.shufflePeople();

            List<Person> notProcessedPeople = new ArrayList<>();
            List<List<Person>> teams = getTeams(data.getPeopleList(), data.getAmountOfTeams(), notProcessedPeople);

            distributeRestOfThePeopleIntoTeams(teams, notProcessedPeople);

            fixTeamsWithTooLittlePeople(teams, data.getPeoplePerTeam());
            fixTeamsWithTooManyPeople(teams, data.getPeoplePerTeam());

            List<Mistake> mistakes;
            try {
                mistakes = getMistakes(teams, data.getPeoplePerTeam(), data.getPossibleCriterias());
            } catch (Error error){
                continue;
            }

            if(mistakes.size() < minAmountOfMistakes){
                minAmountOfMistakes = mistakes.size();
                finalTeams = teams;
                finalTeamsMistakes = mistakes;
            }

            if(mistakes.isEmpty()){
                break;
            }
        }

        if(finalTeams.isEmpty()){
            throw new Error("Something has gone wrong! Try checking the data or changing the criteria around or amount of tries.");
        }
        fileHandler.writeToFile(outputFileAbsolutePath, finalTeams, Collections.max(data.getPeoplePerTeam()));
        return new Information(finalTeamsMistakes, totalTries, tryCount);
    }

    private static List<List<Person>> getTeams(List<Person> personList, Integer amountOfTeams, List<Person> notProcessedPeople){
        List<List<Person>> peopleBuckets = getPeopleBuckets(personList);
        List<Person> peopleInOrder = putPeopleInOrder(amountOfTeams, peopleBuckets, notProcessedPeople);
        List<List<Person>> teams = initiateTeams(amountOfTeams);
        putPeopleToTeams(teams, peopleInOrder);
        return teams;
    }

    private static List<List<Person>> getPeopleBuckets(List<Person> personList){
        List<List<Person>> peopleBuckets = new ArrayList<>();
        for(Person person: personList){
            boolean added = false;
            for(List<Person> bucket: peopleBuckets){
                if(!bucket.isEmpty() && person.isEqualCriteriaList(bucket.get(0).getCriteriaList())){
                    bucket.add(person);
                    added = true;
                    break;
                }
            }
            if(!added){
                List<Person> bucket = new ArrayList<>();
                bucket.add(person);
                peopleBuckets.add(bucket);
            }
        }
        return peopleBuckets;
    }

    private static List<List<Person>> initiateTeams(Integer amountOfTeams){
        List<List<Person>> teams = new ArrayList<>();
        for(int i = 0; i < amountOfTeams; i++) {
            teams.add(new ArrayList<>());
        }
        return teams;
    }

    private static void putPeopleToTeams(List<List<Person>> teams, List<Person> peopleInOrder){
        for(int i = 0; i < peopleInOrder.size();){
            for(List<Person> team: teams){
                if(i >= peopleInOrder.size()){
                    break;
                }
                team.add(peopleInOrder.get(i));
                i++;
            }
        }
    }

    private static List<Person> putPeopleInOrder(Integer amountOfTeams, List<List<Person>> peopleBuckets, List<Person> notProcessedPeople){
        List<Person> peopleInOrder = new ArrayList<>();
        for(List<Person> bucket: peopleBuckets){
            if(bucket.size() % amountOfTeams == 0){
                peopleInOrder.addAll(bucket);
            } else {
                Integer distributablePeople = bucket.size() / amountOfTeams * amountOfTeams;
                peopleInOrder.addAll(bucket.subList(0, distributablePeople));
                List<Person> notProcessedBucket = bucket.subList(distributablePeople, bucket.size());
                notProcessedPeople.addAll(notProcessedBucket);
            }
        }
        return peopleInOrder;
    }


    private static void distributeRestOfThePeopleIntoTeams(List<List<Person>> teams, List<Person> notProcessedPeople){
        for(Person unprocessedPerson: notProcessedPeople){
            List<List<Person>> availableTeams = new ArrayList<>(teams);
            for(Criteria criteria: unprocessedPerson.getCriteriaList()){
                availableTeams = getAvailableTeams(availableTeams, criteria);
                if(availableTeams.size() == 1){
                    break;
                }
            }
            getSmallestTeam(availableTeams).add(unprocessedPerson);
        }
    }

    private static List<Person> getSmallestTeam(List<List<Person>> availableTeams){
        Integer minSize = Integer.MAX_VALUE;
        List<Person> availableTeam = null;
        for (List<Person> team : availableTeams) {
            if (team.size() < minSize) {
                minSize = team.size();
                availableTeam = team;
            }
        }
        return availableTeam;
    }

    private static List<List<Person>> getAvailableTeams(List<List<Person>> teams, Criteria criteria){
        List<List<Person>> availableTeams = new ArrayList<>();
        Integer minOccurences = Integer.MAX_VALUE;
        for(List<Person> team: teams){
            Integer occurences = getCriteriaOccurences(team, criteria);
            if(occurences < minOccurences){
                minOccurences = occurences;
            }
        }
        for(List<Person> team: teams){
            Integer occurences = getCriteriaOccurences(team, criteria);
            if(occurences.equals(minOccurences)){
                availableTeams.add(team);
            }
        }
        return availableTeams;
    }

    private static Integer getCriteriaOccurences(List<Person> team, Criteria criteria){
        Integer occurences = 0;
        for(Person person: team){
            if(person.getCriteriaList().contains(criteria)){
                occurences++;
            }
        }
        return occurences;
    }


    private static void fixTeamsWithTooLittlePeople(List<List<Person>> teams, List<Integer> peoplePerTeam){
        List<List<Person>> teamsWithTooLittlePeople = getTeamsWithTooLittlePeople(teams, peoplePerTeam);
        if(teamsWithTooLittlePeople.isEmpty()) {
            return;
        }
        for(List<Person> team: teamsWithTooLittlePeople){
            Integer peopleLacking = Collections.min(peoplePerTeam) - team.size();
            for(int i = 0; i < peopleLacking; i++){
                List<Criteria> criteriasWhichAreEnough = getCriteriasWhichAreEnough(team);
                List<List<Person>> teamsWithRemovablePeople = getTeamsWithRemovablePeople(teams, peoplePerTeam);
                Person person = getPersonFromTeamsWithNotCriteria(teamsWithRemovablePeople, criteriasWhichAreEnough);
                if(person != null) {
                    team.add(person);
                }
            }
        }
    }

    private static List<List<Person>> getTeamsWithTooLittlePeople(List<List<Person>> teams, List<Integer> peoplePerTeam){
        List<List<Person>> teamsWithTooLittlePeople = new ArrayList<>();
        for(List<Person> team: teams){
            if(team.size() < Collections.min(peoplePerTeam)){
                teamsWithTooLittlePeople.add(team);
            }
        }
        return teamsWithTooLittlePeople;
    }

    private static void fixTeamsWithTooManyPeople(List<List<Person>> teams, List<Integer> peoplePerTeam){
        List<List<Person>> teamsWithTooManyPeople = getTeamsWithTooManyPeople(teams, peoplePerTeam);
        if(teamsWithTooManyPeople.isEmpty()){
            return;
        }
        for(List<Person> team: teamsWithTooManyPeople){
            Integer peopleTooMany = team.size() - Collections.max(peoplePerTeam);
            for(int i = 0; i < peopleTooMany; i++){
                List<List<Person>> teamsWithAddablePeople = getTeamsWithAddablePeople(teams, peoplePerTeam);
                for(Person person: team){
                    if(isRemovablePerson(team, person)){
                        List<Person> teamToAddTo = getTeamToAddTo(teamsWithAddablePeople, person);
                        if(teamToAddTo != null){
                            teamToAddTo.add(person);
                            team.remove(person);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static List<List<Person>> getTeamsWithTooManyPeople(List<List<Person>> teams, List<Integer> peoplePerTeam){
        List<List<Person>> teamsWithTooManyPeople = new ArrayList<>();
        for(List<Person> team: teams){
            if(team.size() > Collections.max(peoplePerTeam)){
                teamsWithTooManyPeople.add(team);
            }
        }
        return teamsWithTooManyPeople;
    }


    private static List<Person> getTeamToAddTo(List<List<Person>> teams, Person person){
        for(List<Person> team: teams){
            List<Criteria> criteriasWhichAreEnough = getCriteriasWhichAreEnough(team);
            if(Collections.disjoint(criteriasWhichAreEnough, person.getCriteriaList())){
                return team;
            }
        }
        return null;
    }

    private static List<Criteria> getCriteriasWhichAreEnough(List<Person> team){
        List<Criteria> criteriasWhichAreEnough = new ArrayList<>();
        List<Criteria> uniqueCriteria = getUniqueCriterias(team);

        for(Criteria criteria: uniqueCriteria) {
            Integer occurences = 0;
            for (Person person : team) {
                if (person.getCriteriaList().contains(criteria)){
                    occurences++;
                }
            }
            if(occurences >= Collections.max(criteria.getOccurrencePerTeam())){
                criteriasWhichAreEnough.add(criteria);
            }
        }
        return criteriasWhichAreEnough;
    }

    private static List<List<Person>> getTeamsWithRemovablePeople(List<List<Person>> teams, List<Integer> peoplePerTeam){
        List<List<Person>> teamsWithRemovablePeople = new ArrayList<>();
        for(List<Person> team: teams){
            if(team.size() > Collections.min(peoplePerTeam)){
                teamsWithRemovablePeople.add(team);
            }
        }
        return teamsWithRemovablePeople;
    }

    private static List<List<Person>> getTeamsWithAddablePeople(List<List<Person>> teams, List<Integer> peoplePerTeam){
        List<List<Person>> teamsWithAddablePeople = new ArrayList<>();
        for(List<Person> team: teams){
            if(team.size() < Collections.max(peoplePerTeam)){
                teamsWithAddablePeople.add(team);
            }
        }
        return teamsWithAddablePeople;
    }

    private static Person getPersonFromTeamsWithNotCriteria(List<List<Person>> teams, List<Criteria> notCriteria){
        for(List<Person> team: teams){
            for(Person person: team){
                if(Collections.disjoint(notCriteria, person.getCriteriaList()) && isRemovablePerson(team, person)){
                    team.remove(person);
                    return person;
                }
            }
        }
        return null;
    }

    private static Boolean isRemovablePerson(List<Person> team, Person removablePerson){
        for(Criteria criteria: removablePerson.getCriteriaList()) {
            Integer occurences = 0;
            for (Person person : team) {
                if (person.getCriteriaList().contains(criteria)){
                    occurences++;
                }
            }
            if(occurences <= Collections.min(criteria.getOccurrencePerTeam())){
                return false;
            }
        }
        return true;
    }

    private static List<Criteria> getUniqueCriterias(List<Person> team){
        Set<Criteria> uniqueCriteria = new HashSet<>();
        for(Person person: team){
            uniqueCriteria.addAll(person.getCriteriaList());
        }
        return new ArrayList<>(uniqueCriteria);
    }


    private static List<Mistake> getMistakes(List<List<Person>> teams, List<Integer> peoplePerTeam, List<Criteria> possibleCriterias){
        List<Mistake> mistakes = new ArrayList<>();
        for(int teamNumber = 0; teamNumber < teams.size(); teamNumber++){
            List<Person> team = teams.get(teamNumber);
            if(!peoplePerTeam.contains(team.size())){
                throw new Error("Amount of people per team is unproportional!");
            }
            for(Criteria criteria: possibleCriterias){
                Integer criteriaOccurences = getCriteriaOccurences(team, criteria);
                if(!criteria.getOccurrencePerTeam().contains(criteriaOccurences)){
                    mistakes.add(new Mistake(teamNumber, criteria, criteriaOccurences));
                }
            }
        }
        return mistakes;
    }
}

