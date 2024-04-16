package me.ehsanmna.menumine.utils.skills;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.stat.StatModifier;
import dev.aurelium.auraskills.api.stat.Stats;
import dev.aurelium.auraskills.api.user.SkillsUser;

import java.util.UUID;

public class AuraSkillsManager {


    public static boolean hasSkillLevel(UUID id,String skill,int level){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        return level >= user.getSkillLevel(Skills.valueOf(skill));
    }

    public static void addSkillExp(UUID id,String skill,int exp){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        user.addSkillXp(Skills.valueOf(skill), exp);
    }

    public static void addSkillRawExp(UUID id,String skill,int exp){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        user.addSkillXpRaw(Skills.valueOf(skill), exp);
    }

    public static void setSkillExp(UUID id,String skill,int exp){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        user.setSkillXp(Skills.valueOf(skill), exp);
    }

    public static void setStatsModifier(UUID id,String modifier,String stats,int value){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        user.addStatModifier(new StatModifier(modifier, Stats.valueOf(stats), value));
    }

    public static void removeStatsModifier(UUID id,String modifier){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        user.removeStatModifier(modifier);
    }

    public static boolean hasMana(UUID id,int mana){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        return mana >= user.getMana();
    }

    public static void setMana(UUID id,int mana){
        AuraSkillsApi auraSkills = AuraSkillsApi.get();
        SkillsUser user = auraSkills.getUser(id);
        user.setMana(mana);
    }

}
