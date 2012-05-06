package net.minecraft.src;

public class mod_OldDaysMobs extends mod_OldDays{
    public void load(){
        registerModule(this, 3);
        addProperty(1, "Old mob AI",          false, true,  "OldAI",            "");
        addProperty(2, "Animal panic",        true,  false, "AnimalsFlee",      "");
        addProperty(3, "Sheep eat grass",     true,  true,  "SheepEatGrass",    "");
        addProperty(4, "Spiders climb walls", true,  true,  "SpidersCanClimb",  "");
        addProperty(5, "Survival Test mobs",  false, false, "SurvivalTestMobs", "");
        addProperty(6, "Jumping mobs",        false, true,  "JumpingMobs",      "");
        addProperty(7, "Old skeleton fire",   false, false, "FastSkeletons",    "");
        addProperty(8, "Old mob health",      false, false, "OldHealth",        "");
        loadModuleProperties();
    }

    public void callback (int i){
        switch(i){
            case 1: setBool(net.minecraft.src.EntityLiving.class, "newai", !OldAI);
                    setBool(net.minecraft.src.EntityCreeper.class, "fixai", OldAI);
                    setBool(net.minecraft.src.EntitySkeleton.class, "fixai", OldAI);
                    setBool(net.minecraft.src.EntitySnowman.class, "fixai", OldAI);
                    setBool(net.minecraft.src.EntitySheep.class, "fixai", OldAI); break;
            case 2: setBool(net.minecraft.src.EntityAIPanic.class, "disablePanic", !AnimalsFlee);
                    setBool(net.minecraft.src.EntityCreature.class, "nopanic", !AnimalsFlee); break;
            case 3: setBool(net.minecraft.src.EntitySheep.class, "caneatgrass", SheepEatGrass); break;
            case 4: setBool(net.minecraft.src.EntitySpider.class, "canclimb", SpidersCanClimb); break;
            case 5: setBool(net.minecraft.src.EntityCreeper.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityCreature.class, "fastzombies", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityZombie.class, "burns", !SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySkeleton.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySpider.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySheep.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySheep.class, "hungry", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityPig.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityLiving.class, "survivaltest", SurvivalTestMobs); break;
            case 6: setBool(net.minecraft.src.EntityCreature.class, "jump", JumpingMobs); break;
            case 7: setBool(net.minecraft.src.EntitySkeleton.class, "fast", FastSkeletons); break;
            case 8: setBool(net.minecraft.src.EntitySheep.class, "oldhealth", OldHealth);
                    setBool(net.minecraft.src.EntitySpider.class, "oldhealth", OldHealth); break;
        }
    }

    public static boolean OldAI = true;
    public static boolean AnimalsFlee = false;
    public static boolean SheepEatGrass = true;
    public static boolean SpidersCanClimb = true;
    public static boolean SurvivalTestMobs;
    public static boolean JumpingMobs = true;
    public static boolean FastSkeletons;
    public static boolean OldHealth = true;
//Creepers see through blocks
}