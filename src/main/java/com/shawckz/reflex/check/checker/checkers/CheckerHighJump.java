package com.shawckz.reflex.check.checker.checkers;

import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.check.checker.CheckerSimilarity;

public class CheckerHighJump extends Checker {

    public CheckerHighJump() {
        super(CheckType.HIGH_JUMP);
    }

    @Override
    public CheckerSimilarity getSimilarityTo(Checker checker) {
        return null;
    }
}
