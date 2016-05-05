/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.check.Violation;
import com.shawckz.reflex.check.checker.Checker;
import com.shawckz.reflex.check.checker.CheckerSimilarity;
import com.shawckz.reflex.player.ReflexPlayer;
import com.shawckz.reflex.util.ReflexCaller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.scheduler.BukkitRunnable;

public class ReflexInspection {

    public static void inspect(final ReflexPlayer player, final Violation violation, final ReflexCaller<ReflexInspectionResult> callback) {
        final Checker data = violation.getData();

        new BukkitRunnable(){
            @Override
            public void run() {
                //Finds (5 - make configurable?) records of most similarity
                Set<ConfirmedRecord> confirmedRecords = new HashSet<>();
                ConfirmedRecord mostSimilarPositive = null;
                ConfirmedRecord mostSimilarNegative = null;

                for(ConfirmedRecord cr : RecordCache.getData()) {
                    CheckerSimilarity similarity = data.getSimilarityTo(cr.getData());
                    if(similarity.isPassed()) {

                        if(cr.isResult()) {
                            if(mostSimilarPositive == null || mostSimilarPositive.getData().getSimilarityTo(data).getSimilarity() < similarity.getSimilarity()) {
                                mostSimilarPositive = cr;
                            }
                        }
                        else{
                            if(mostSimilarNegative == null || mostSimilarNegative.getData().getSimilarityTo(data).getSimilarity() < similarity.getSimilarity()) {
                                mostSimilarNegative = cr;
                            }
                        }

                        if(confirmedRecords.size() < 5) {
                            confirmedRecords.add(cr);
                        }
                        else{
                            Iterator<ConfirmedRecord> it = confirmedRecords.iterator();

                            loo:while(it.hasNext()) {
                                ConfirmedRecord next = it.next();
                                if(similarity.getSimilarity() > next.getData().getSimilarityTo(data).getSimilarity()) {
                                    confirmedRecords.remove(next);
                                    confirmedRecords.add(cr);
                                    break loo;
                                }
                            }
                        }
                    }
                }

                if(!confirmedRecords.isEmpty()) {
                    int positive = 0;
                    int negative = 0;
                    for(ConfirmedRecord record : confirmedRecords) {
                        if(record.isResult()) {
                            positive++;
                        }
                        else{
                            negative++;
                        }
                    }

                    if(positive == negative) {
                        //Staff assistance / log //TODO - we don't want to log too many times // check their previous inspections for this checktype and see how many they have with this outcome
                        callback.call(new ReflexInspectionResult(
                                new ReflexInspectionResult.Action[]{ReflexInspectionResult.Action.STAFF_APPROVAL, ReflexInspectionResult.Action.LOG},
                                        null, null,
                                        ReflexInspectionResult.Result.COULD_NOT_DECIDE
                                )
                        );
                    }
                    else if(positive > negative) {
                        //Autoban
                        ConfirmedRecord record = mostSimilarPositive != null ? mostSimilarPositive : confirmedRecords.stream().filter(ConfirmedRecord::isResult).findFirst().get();
                        callback.call(new ReflexInspectionResult(
                                        new ReflexInspectionResult.Action[]{ReflexInspectionResult.Action.AUTOBAN},
                                                (record.getData().getSimilarityTo(data)),
                                                record,
                                                ReflexInspectionResult.Result.POSITIVE
                                )
                        );
                    }
                    else if (negative > positive) {
                        //Log - ignore if too many outcomes that are similar for this player
                        ConfirmedRecord record = mostSimilarPositive != null ? mostSimilarPositive : confirmedRecords.stream().filter(ConfirmedRecord::isResult).findFirst().get();
                        callback.call(new ReflexInspectionResult(
                                        new ReflexInspectionResult.Action[]{ReflexInspectionResult.Action.LOG},
                                        (record.getData().getSimilarityTo(data)),
                                        record,
                                        ReflexInspectionResult.Result.NEGATIVE
                                )
                        );
                    }
                }
                else {
                    //Staff assistance
                    callback.call(new ReflexInspectionResult(new ReflexInspectionResult.Action[]{ReflexInspectionResult.Action.STAFF_APPROVAL}, null, null, ReflexInspectionResult.Result.NO_RECORDS_FOUND));
                }


            }
        }.runTaskAsynchronously(Reflex.getPlugin());

    }

}
