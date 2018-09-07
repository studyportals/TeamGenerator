package studyportals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Criteria {

    private String name;
    private String value;
    private Integer amountOfTeams;
    private List<Integer> occurrencePerTeam;


    public Criteria(String name, String value, Integer amountOfTeams){
        this.setName(name);
        this.setValue(value);
        this.setAmountOfTeams(amountOfTeams);
    }


    private void setName(String name){
        this.name = name;
    }

    private void setValue(String value){
        this.value = value;
    }

    private void setAmountOfTeams(Integer amountOfTeams){
        this.amountOfTeams = amountOfTeams;
    }

    public void setOccurrencePerTeam(Integer occurences){
        this.occurrencePerTeam = new ArrayList<>();
        if(occurences % this.amountOfTeams == 0){
            this.occurrencePerTeam.add(occurences/this.amountOfTeams);
        } else {
            this.occurrencePerTeam.add(occurences/this.amountOfTeams);
            this.occurrencePerTeam.add(occurences/this.amountOfTeams + 1);
        }
    }


    public List<Integer> getOccurrencePerTeam(){
        return this.occurrencePerTeam;
    }

    public String getName(){
        return this.name;
    }

    public String getValue(){
        return this.value;
    }

    public String getStringifiedOccurrencesPerTeam(){
        if(this.occurrencePerTeam.size() == 2){
            return this.occurrencePerTeam.get(0) + "-" + this.occurrencePerTeam.get(1);
        }
        return this.occurrencePerTeam.get(0).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof  Criteria)) {
            return false;
        }

        final Criteria other = (Criteria) obj;
        return Objects.equals(this.name, other.name) && Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
