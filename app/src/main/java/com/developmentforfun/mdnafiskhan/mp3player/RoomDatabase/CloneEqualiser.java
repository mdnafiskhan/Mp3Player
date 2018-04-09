package com.developmentforfun.mdnafiskhan.mp3player.RoomDatabase;

import com.developmentforfun.mdnafiskhan.mp3player.Models.EqualizerBands;

/**
 * Created by mdnafiskhan on 19/03/2018.
 */

public class CloneEqualiser {

    public static EqualiserEntity CloneFromEqualiserBandToEntity(EqualizerBands equalizerBands)
    {
        EqualiserEntity equaliserEntity = new EqualiserEntity();
        equaliserEntity.setBand0(equalizerBands.getBand0());
        equaliserEntity.setBand1(equalizerBands.getBand1());
        equaliserEntity.setBand2(equalizerBands.getBand2());
        equaliserEntity.setBand3(equalizerBands.getBand3());
        equaliserEntity.setBand4(equalizerBands.getBand4());
        equaliserEntity.setBassBoost(equalizerBands.getBassBoost());
        equaliserEntity.setVisualizer(equalizerBands.getVisualizer());
        equaliserEntity.setId(1);
        return equaliserEntity;
    }

    public static EqualizerBands CloneFromEqualiserEntityToBand(EqualiserEntity equaliserEntity)
    {
        EqualizerBands equalizerBands = new EqualizerBands();
        equalizerBands.setBand0(equaliserEntity.getBand0());
        equalizerBands.setBand1(equaliserEntity.getBand1());
        equalizerBands.setBand2(equaliserEntity.getBand2());
        equalizerBands.setBand3(equaliserEntity.getBand3());
        equalizerBands.setBand4(equaliserEntity.getBand4());
        equalizerBands.setBassBoost(equaliserEntity.getBassBoost());
        equalizerBands.setVisualizer(equaliserEntity.getVisualizer());
        return equalizerBands;
    }


}
