package org.laeq.model.statistic;

public class Result{
    public Integer matched = 0;
    public Integer lonely = 0;

    public void addLonely(Integer value){
        lonely += value;
    }
    public void addMatched(Integer min) {
        matched += min;
    }

    public int getMatched() {
        return matched.intValue();
    }

    public int getLonely() {
        return lonely.intValue();
    }

    public double getPercent(){
        if(matched + lonely != 0){
            return ((double) matched) / (matched + lonely) * 100;
        } else  {
            return 100;
        }
    }
}
