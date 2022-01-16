
public class RecommendItem {

    private Double value;
    private Integer index;

    public RecommendItem() {
        super();
    }

    public RecommendItem(Double value, Integer index) {
        super();
        this.value = value;
        this.index = index;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "RecommendItem: " + "similarity value=" + value + ", index=" + index;
    }
    
    
}
