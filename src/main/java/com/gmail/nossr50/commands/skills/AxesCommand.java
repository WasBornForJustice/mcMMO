package com.gmail.nossr50.commands.skills;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;

import com.gmail.nossr50.commands.SkillCommand;
import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.datatypes.SkillType;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.util.Misc;

public class AxesCommand extends SkillCommand {
    AdvancedConfig advancedConfig = AdvancedConfig.getInstance();

    private String critChance;
    private String critChanceLucky;
    private String bonusDamage;
    private String impactDamage;
    private String greaterImpactDamage;
    private String skullSplitterLength;

    private int bonusDamageAxesBonusMax = advancedConfig.getBonusDamageAxesBonusMax();
    private int bonusDamageAxesMaxBonusLevel = advancedConfig.getBonusDamageAxesMaxBonusLevel();
    private double critMaxChance = advancedConfig.getAxesCriticalChance();
    private int critMaxBonusLevel = advancedConfig.getAxesCriticalMaxBonusLevel();
    private int greaterImpactIncreaseLevel = advancedConfig.getArmorImpactIncreaseLevel();
    private double greaterImpactBonusDamage = advancedConfig.getGreaterImpactBonusDamage();
    private int abilityLengthIncreaseLevel = advancedConfig.getAbilityLength();

    private boolean canSkullSplitter;
    private boolean canCritical;
    private boolean canBonusDamage;
    private boolean canImpact;
    private boolean canGreaterImpact;

    public AxesCommand() {
        super(SkillType.AXES);
    }

    @Override
    protected void dataCalculations() {
        DecimalFormat df = new DecimalFormat("0.0");
        float critChanceF;
        int skillCheck = Misc.skillCheck((int)skillValue, critMaxBonusLevel);

        //Armor Impact
        impactDamage = String.valueOf(1 + ((double) skillValue / (double) greaterImpactIncreaseLevel));
        //Skull Splitter
        skullSplitterLength = String.valueOf(2 + (int) ((double) skillValue / (double) abilityLengthIncreaseLevel));
        //Greater Impact
        greaterImpactDamage = String.valueOf(greaterImpactBonusDamage);
        //Critical Strikes
        if (skillValue >= critMaxBonusLevel) critChanceF = (float) critMaxChance;
        else critChanceF = (float) ((critMaxChance / critMaxBonusLevel) * skillCheck);
        critChance = df.format(critChanceF);
        if(critChanceF + critChanceF * 0.3333D >= 100D) critChanceLucky = df.format(100D);
        else critChanceLucky = df.format(critChanceF + critChanceF * 0.3333D);
        //Axe Mastery
        if (skillValue >= bonusDamageAxesMaxBonusLevel) bonusDamage = String.valueOf(bonusDamageAxesBonusMax);
        else bonusDamage = String.valueOf(skillValue / ((double) bonusDamageAxesMaxBonusLevel / (double) bonusDamageAxesBonusMax));
    }

    @Override
    protected void permissionsCheck() {
        canSkullSplitter = permInstance.skullSplitter(player);
        canCritical = permInstance.criticalHit(player);
        canBonusDamage = permInstance.axeBonus(player);
        canImpact = permInstance.impact(player);
        canGreaterImpact = permInstance.greaterImpact(player);
    }

    @Override
    protected boolean effectsHeaderPermissions() {
        return canSkullSplitter || canCritical || canBonusDamage || canImpact || canGreaterImpact;
    }

    @Override
    protected void effectsDisplay() {
        if (player.hasPermission("mcmmo.perks.lucky.axes")) {
            String perkPrefix = ChatColor.RED + "[mcMMO Perks] ";
            player.sendMessage(perkPrefix + LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Perks.lucky.name"), LocaleLoader.getString("Perks.lucky.desc", new Object[] { "Axes" }) }));
        }

        if (canSkullSplitter) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Axes.Effect.0"), LocaleLoader.getString("Axes.Effect.1") }));
        }

        if (canCritical) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Axes.Effect.2"), LocaleLoader.getString("Axes.Effect.3") }));
        }

        if (canBonusDamage) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Axes.Effect.4"), LocaleLoader.getString("Axes.Effect.5") }));
        }

        if (canImpact) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Axes.Effect.6"), LocaleLoader.getString("Axes.Effect.7") }));
        }

        if (canGreaterImpact) {
            player.sendMessage(LocaleLoader.getString("Effects.Template", new Object[] { LocaleLoader.getString("Axes.Effect.8"), LocaleLoader.getString("Axes.Effect.9") }));
        }
    }

    @Override
    protected boolean statsHeaderPermissions() {
        return canSkullSplitter || canCritical || canBonusDamage || canImpact || canGreaterImpact;
    }

    @Override
    protected void statsDisplay() {
        if (canBonusDamage) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template", new Object[] { LocaleLoader.getString("Axes.Ability.Bonus.0"), LocaleLoader.getString("Axes.Ability.Bonus.1", new Object[] {bonusDamage}) }));
        }

        if (canImpact) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template", new Object[] { LocaleLoader.getString("Axes.Ability.Bonus.2"), LocaleLoader.getString("Axes.Ability.Bonus.3", new Object[] {impactDamage}) }));
        }

        if (canGreaterImpact) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template", new Object[] { LocaleLoader.getString("Axes.Ability.Bonus.4"), LocaleLoader.getString("Axes.Ability.Bonus.5", new Object[] {greaterImpactDamage}) }));
        }

        if (canCritical){
            if (player.hasPermission("mcmmo.perks.lucky.axes"))
                player.sendMessage(LocaleLoader.getString("Axes.Combat.CritChance", new Object[] { critChance }) + LocaleLoader.getString("Perks.lucky.bonus", new Object[] { critChanceLucky }));
            else
                player.sendMessage(LocaleLoader.getString("Axes.Combat.CritChance", new Object[] { critChance }));
        }

        if (canSkullSplitter) {
            player.sendMessage(LocaleLoader.getString("Axes.Combat.SS.Length", new Object[] { skullSplitterLength }));
        }
    }
}
