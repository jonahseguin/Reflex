/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.reflex.inspect;

import com.shawckz.reflex.Reflex;
import com.shawckz.reflex.bridge.CheckType;
import com.shawckz.reflex.core.configuration.RLang;
import com.shawckz.reflex.core.configuration.ReflexLang;
import com.shawckz.reflex.core.player.ReflexPlayer;
import com.shawckz.reflex.util.Alert;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;

/**
 * Created by Jonah Seguin on 5/9/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class InspectHandler {

    public static void train(final ReflexPlayer player, final CheckType checkType, final int period, NeuronOutput expectedOutput) {
        RInspectData inspectData = new RInspectData(period);
        RTrainer trainer = new RTrainer(checkType, period, inspectData, expectedOutput) {
            @Override
            public void onFinish() {
                ReflexNetwork network = RNetworkCache.getNetwork(checkType);

                for(int i = 0; i < Reflex.getInstance().getReflexConfig().getTrainingCreateNeurons(); i++) {

                }

                FancyMessage fancyMessage = new FancyMessage("");
                fancyMessage.then(RLang.format(ReflexLang.TRAIN_FINISH, player.getName(), checkType.getName()))
                        .tooltip(ChatColor.YELLOW + "Click to see results")
                        .command("/reflex trainresult " );
                Alert.staffMsg(fancyMessage);
            }
        };

        trainer.start();
        Alert.staffMsg(RLang.format(ReflexLang.TRAIN_START, player.getName(), period+"", checkType.getName()));

    }

    public static void test(final ReflexPlayer player, final CheckType checkType) {
        int period = Reflex.getInstance().getReflexConfig().getInspectPeriod(checkType);
        RInspectData inspectData = new RInspectData(period);

        RTester tester = new RTester(checkType, period, inspectData) {
            @Override
            public void onFinish(RInspectData data) {
                Alert.staffMsg(RLang.format(ReflexLang.INSPECT_FINISH, player.getName(), checkType.getName()));
            }
        };

        tester.start();
        Alert.staffMsg(RLang.format(ReflexLang.INSPECT_START, player.getName(), period+"", checkType.getName()));

    }

}
