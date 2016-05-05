package com.shawckz.reflex.check.checker.checkers;

import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.check.checker.CheckerSimilarity;

public class CheckerFastBow extends Checker{

    public CheckerFastBow() {
        super(CheckType.FAST_BOW);
    }

    @Override
    public CheckerSimilarity getSimilarityTo(Checker checker) {
        return null;
    }
}
