package studyportals;

public class Mistake {

    private Integer teamNumber;
    private Criteria criteria;
    private Integer criteriaOccurrences;

    public Mistake(Integer teamNumber, Criteria criteria, Integer criteriaOccurrences) {
        this.teamNumber = teamNumber;
        this.criteria = criteria;
        this.criteriaOccurrences = criteriaOccurrences;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Integer getCriteriaOccurrences() {
        return criteriaOccurrences;
    }

}
