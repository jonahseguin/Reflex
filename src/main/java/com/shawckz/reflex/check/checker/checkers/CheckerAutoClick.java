package com.shawckz.reflex.check.checker.checkers;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.check.checker.CheckerData;
import com.shawckz.reflex.check.checker.CheckerSimilarity;
import com.shawckz.reflex.database.mongo.annotations.MongoColumn;
import com.shawckz.reflex.player.ReflexPlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckerAutoClick extends Checker {

    @CheckerData
    @MongoColumn(name = "clicksPerSecond")
    private double[] clicksPerSecond = new double[]{0,0,0,0};// We will be keeping track of the past 4 seconds worth of cps

    public CheckerAutoClick(CheckType check, ReflexPlayer player) {
        super(check, player);
    }

    @Override
    public CheckerSimilarity getSimilarityTo(Checker checker) {
        if(checker instanceof CheckerAutoClick) {
            CheckerAutoClick test = (CheckerAutoClick) checker;

            double a = difference(test.getClicksPerSecond()[3], clicksPerSecond[3]);
            double b = difference(test.getClicksPerSecond()[2], clicksPerSecond[2]);
            double c = difference(test.getClicksPerSecond()[1], clicksPerSecond[1]);
            double d = difference(test.getClicksPerSecond()[0], clicksPerSecond[0]);

            //the closer to 0, the more similar

            double score = sub(a, 25) + sub(b, 25) + sub(c, 25) + sub(d, 25);
            double maxScore = 100;

            double result = score / maxScore;

            return new CheckerSimilarity(result, (result >= Reflex.getReflexConfig().getPassLevelAutoClick()));
        }
        else{
            throw new RuntimeException("Checker is not of same type");
        }
    }

    private double sub(double a, int percent) {
        double ret = percent - a;
        if(ret < 0) {
            ret = 0;
        }
        return ret;
    }

    private double difference(double a, double b) {
        double ret = (a > b ? a - b : b - a);
        if(ret <= 0) {
            ret = 1;
        }
        return ret;
    }

}
