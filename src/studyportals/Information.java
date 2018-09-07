package studyportals;

import java.util.List;

public class Information {

    private List<Mistake> mistakes;
    private Integer totalTries;
    private Integer usedTries;

    public Information(List<Mistake> mistakes, Integer totalTries, Integer usedTries) {
        this.mistakes = mistakes;
        this.totalTries = totalTries;
        this.usedTries = usedTries;
    }

    public List<Mistake> getMistakes() {
        return mistakes;
    }

    public Integer getTotalTries() {
        return totalTries;
    }

    public Integer getUsedTries() {
        return usedTries;
    }
}
