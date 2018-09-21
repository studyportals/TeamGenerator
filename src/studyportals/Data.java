package studyportals;
import java.util.*;

public class Data {

    private List<Person> peopleList;
    private Integer amountOfTeams;
    private List<Criteria> possibleCriterias;
    private List<Integer> peoplePerTeam;

    public Data(List<Person> peopleList, Integer amountOfTeams){
        this.setPeopleList(peopleList);
        this.setAmountOfTeams(amountOfTeams);
        this.setPossibleCriterias(this.peopleList);
        this.setPeoplePerTeam(this.peopleList.size(), amountOfTeams);
        this.updateCriteriasPerTeam();
        this.shufflePeople();
    }

    private void setPeopleList(List<Person> peopleList){
        this.peopleList = peopleList;
    }

    private void setAmountOfTeams(Integer amountOfTeams){
        this.amountOfTeams = amountOfTeams;
    }

    private void setPossibleCriterias(List<Person> peopleList){

        Set<Criteria> uniqueCriteria = new HashSet<>();
        for(Person person: peopleList){
            uniqueCriteria.addAll(person.getCriteriaList());
        }
        this.possibleCriterias = new ArrayList<>(uniqueCriteria);
    }

    private void setPeoplePerTeam(Integer amountOfPeople, Integer amountOfTeams){
        this.peoplePerTeam = new ArrayList<>();
        if(amountOfPeople % amountOfTeams == 0){
            this.peoplePerTeam.add(amountOfPeople/amountOfTeams);
        } else {
            this.peoplePerTeam.add(amountOfPeople/amountOfTeams);
            this.peoplePerTeam.add(amountOfPeople/amountOfTeams + 1);
        }
    }

    public List<Person> getPeopleList(){
        return this.peopleList;
    }

    public Integer getAmountOfTeams(){
        return this.amountOfTeams;
    }

    public List<Criteria> getPossibleCriterias(){
        return this.possibleCriterias;
    }

    public List<Integer> getPeoplePerTeam(){
        return this.peoplePerTeam;
    }

    private void updateCriteriasPerTeam(){
        List<Criteria> allCriteria = new ArrayList<>();
        for(Person person: this.peopleList){
            allCriteria.addAll(person.getCriteriaList());
        }
        for(Criteria criteria: this.possibleCriterias){
            Integer occurrences = Collections.frequency(allCriteria, criteria);
            setCriteriaOccurrencePerTeam(criteria, occurrences);
        }
    }

    private void setCriteriaOccurrencePerTeam(Criteria criteria, Integer occurrences){
        for(Person person: this.peopleList){
            for(Criteria personCriteria: person.getCriteriaList()){
                if(personCriteria.equals(criteria)){
                    personCriteria.setOccurrencePerTeam(occurrences);
                }
            }
        }
    }

    public void shufflePeople(){
        Collections.shuffle(this.peopleList);
    }

}
