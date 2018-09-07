package studyportals;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private String name;
    private List<Criteria> criteriaList;

    public Person(String name, List<Criteria> criteriaList){
        this.setName(name);
        this.setCriteriaList(criteriaList);
    }

    private void setName(String name){
        this.name = name;
    }

    public void setCriteriaList(List<Criteria> criteriaList){
        this.criteriaList = new ArrayList<>(criteriaList);
    }

    public String getName(){
        return this.name;
    }

    public List<Criteria> getCriteriaList(){
        return this.criteriaList;
    }

    public boolean isEqualCriteriaList(List<Criteria> criteriaList){
        return this.criteriaList.size() == criteriaList.size() && this.criteriaList.containsAll(criteriaList);
    }
}
