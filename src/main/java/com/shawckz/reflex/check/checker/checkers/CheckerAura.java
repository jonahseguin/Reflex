package com.shawckz.reflex.check.checker.checkers;

import com.shawckz.reflex.check.CheckType;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.check.checker.CheckerSimilarity;
import com.shawckz.reflex.player.ReflexPlayer;

public class CheckerAura extends Checker {

    public CheckerAura(CheckType check, ReflexPlayer player) {
        super(check, player);
    }

    @Override
    public CheckerSimilarity getSimilarityTo(Checker checker) {
        return null;
    }
}
