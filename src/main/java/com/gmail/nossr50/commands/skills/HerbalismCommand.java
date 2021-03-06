package com.gmail.nossr50.commands.skills;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;

import com.gmail.nossr50.commands.SkillCommand;
import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.datatypes.SkillType;
import com.gmail.nossr50.locale.LocaleLoader;

public class HerbalismCommand extends SkillCommand {
    AdvancedConfig advancedConfig = AdvancedConfig.getInstance();

    private String greenTerraLength;
    private String greenThumbChance;
    private String greenThumbChanceLucky;
    private String greenThumbStage;
    private String farmersDietRank;
    private String doubleDropChance;
    private String doubleDropChanceLucky;

    private int abilityLengthIncreaseLevel = advancedConfig.getAbilityLength();
    private int farmersDietRankChange = advancedConfig.getFarmerDietRankChange();
    private int farmersDietMaxLevel = farmersDietRankChange * 5;
    private int greenThumbStageChange = advancedConfig.getGreenThumbStageChange();
    private int greenThumbStageMaxLevel = greenThumbStageChange * 4;
    private double greenThumbMaxBonus = advancedConfig.getGreenThumbChanceMax();
    private int greenThumbMaxLevel = advancedConfig.getGreenThumbMaxLevel();
    private double doubleDropsMaxBonus = advancedConfig.getHerbalismDoubleDropsChanceMax();
    private int doubleDropsMaxLevel = advancedConfig.getHerbalismDoubleDropsMaxLevel();

    private boolean canGreenTerra;
    private boolean canGreenThumbWheat;
    private boolean canGreenThumbBlocks;
    private boolean canFarmersDiet;
    private boolean canDoubleDrop;
    private boolean doubleDropsDisabled;

    public HerbalismCommand() {
        super(SkillType.HERBALISM);
    }

    @Override
    protected void dataCalculations() {
        DecimalFormat df = new DecimalFormat("0.0");
        float greenThumbChanceF;
        float doubleDropChanceF;
        greenTerraLength = String.valueOf(2 + (int) ((double) skillValue / (double) abilityLengthIncreaseLevel));
        //FARMERS DIET
        if(skillValue >= farmersDietMaxLevel) farmersDietRank = "5";
        else farmersDietRank = String.valueOf((int) ((double) skillValue / (double) farmersDietRankChange));
        //GREEN THUMB
        if(skillValue >= greenThumbStageMaxLevel) greenThumbStage = "4";
        else greenThumbStage = String.valueOf((int) ((double) skillValue / (double) greenThumbStageChange));

        if(skillValue >= greenThumbMaxLevel) greenThumbChanceF = (float) (greenThumbMaxBonus);
        else greenThumbChanceF = (float) ((greenThumbMaxBonus / greenThumbMaxLevel) * skillValue);
        greenThumbChance = df.format(greenThumbChanceF);
        if(greenThumbChanceF + greenThumbChanceF * 0.3333D >= 100D) greenThumbChanceLucky = df.format(100D);
        else greenThumbChanceLucky = df.format(greenThumbChanceF + greenThumbChanceF * 0.3333D);
        //DOUBLE DROPS
        if(skillValue >= doubleDropsMaxLevel) doubleDropChanceF = (float) (doubleDropsMaxBonus);
        else doubleDropChanceF = (float) ((doubleDropsMaxBonus / doubleDropsMaxLevel) * skillValue);
        doubleDropChance = df.format(doubleDropChanceF);
        if(doubleDropChanceF + doubleDropChanceF * 0.3333D >= 100D) doubleDropChanceLucky = df.format(100D);
        else doubleDropChanceLucky = df.format(doubleDropChanceF + doubleDropChanceF * 0.3333D);
    }

    @Override
    protected void permissionsCheck() {
        Config configInstance = Config.getInstance();

        canGreenTerra = permInstance.greenTerra(player);
        canGreenThumbWheat = permInstance.greenThumbWheat(player);
        canGreenThumbBlocks = permInstance.greenThumbBlocks(player);
        canFarmersDiet = permInstance.farmersDiet(player);
        canDoubleDrop = permInstance.herbalismDoubleDrops(player);
        doubleDropsDisabled = configInstance.herbalismDoubleDropsDisabled();
    }

    @Override
    protected boolean effectsHeaderPermissions() {
        return canGreenTerra || (canDoubleDrop && !doubleDropsDisabled) || canFarmersDiet || canGreenThumbBlocks || canGreenThumbWheat;
    }

    @Override
    protected void effectsDisplay() {
        if (player.hasPermission("mcmmo.perks.lucky.herbalism")) {
            String perkPrefix = ChatColor.RED + "[mcMMO Perks] ";
            player.sendMessage(perkPrefix + LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Perks.lucky.name"), LocaleLoader.getString("Perks.lucky.desc", new Object[] { "Herbalism" }) }));
        }

        if (canGreenTerra) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Herbalism.Effect.0"), LocaleLoader.getString("Herbalism.Effect.1") }));
        }

        if (canGreenThumbWheat) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Herbalism.Effect.2"), LocaleLoader.getString("Herbalism.Effect.3") }));
        }

        if (canGreenThumbBlocks) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Herbalism.Effect.4"), LocaleLoader.getString("Herbalism.Effect.5") }));
        }

        if (canFarmersDiet) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Herbalism.Effect.6"), LocaleLoader.getString("Herbalism.Effect.7") }));
        }

        if (canDoubleDrop && !doubleDropsDisabled) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Herbalism.Effect.8"), LocaleLoader.getString("Herbalism.Effect.9") }));
        }
    }

    @Override
    protected boolean statsHeaderPermissions() {
        return canGreenTerra || (canDoubleDrop && !doubleDropsDisabled) || canFarmersDiet || canGreenThumbBlocks || canGreenThumbWheat;
    }

    @Override
    protected void statsDisplay() {
        if (canGreenTerra) {
            player.sendMessage(LocaleLoader.getString("Herbalism.Ability.GTe.Length", new Object[] { greenTerraLength }));
        }

        if (canGreenThumbBlocks || canGreenThumbWheat) {
            if (player.hasPermission("mcmmo.perks.lucky.herbalism"))
                player.sendMessage(LocaleLoader.getString("Herbalism.Ability.GTh.Chance", new Object[] { greenThumbChance }) + LocaleLoader.getString("Perks.lucky.bonus", new Object[] { greenThumbChanceLucky }));
            else
                player.sendMessage(LocaleLoader.getString("Herbalism.Ability.GTh.Chance", new Object[] { greenThumbChance }));
        }

        if (canGreenThumbWheat) {
            player.sendMessage(LocaleLoader.getString("Herbalism.Ability.GTh.Stage", new Object[] { greenThumbStage }));
        }

        if (canFarmersDiet) {
            player.sendMessage(LocaleLoader.getString("Herbalism.Ability.FD", new Object[] { farmersDietRank } ));
        }

        if (canDoubleDrop && !doubleDropsDisabled) {
            if (player.hasPermission("mcmmo.perks.lucky.herbalism"))
                player.sendMessage(LocaleLoader.getString("Herbalism.Ability.DoubleDropChance", new Object[] { doubleDropChance }) + LocaleLoader.getString("Perks.lucky.bonus", new Object[] { doubleDropChanceLucky }));
            else
                player.sendMessage(LocaleLoader.getString("Herbalism.Ability.DoubleDropChance", new Object[] { doubleDropChance }));
        }
    }
}
