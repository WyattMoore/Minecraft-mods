package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.*;
import net.minecraft.src.nbxlite.*;
import net.minecraft.src.nbxlite.blocks.*;
import net.minecraft.src.nbxlite.format.SaveConverterMcRegion;
import net.minecraft.src.nbxlite.indev.*;
import net.minecraft.src.nbxlite.indev.McLevelImporter;
import java.util.zip.*;

public class ODNBXlite extends OldDaysModule{
    public ODNBXlite(mod_OldDays c){
        super(c, 8, "NBXlite");
        new OldDaysPropertyInt(this,   1, 0,     5,     "Gen", 5).setUseNames().setGUIRefresh().disableLoading();
        new OldDaysPropertyInt(this,   2, 0,     0,     "MapTheme", 3).setUseNames().disableLoading();
        new OldDaysPropertyInt(this,   3, 0,     4,     "BetaFeatures", FEATURES_JUNGLE).setUseNames().disableLoading();
        new OldDaysPropertyInt(this,   4, 0,     5,     "ReleaseFeatures", FEATURES_14).setUseNames().disableLoading();
        new OldDaysPropertyBool(this,  5, false, false, "GenerateNewOres").disableLoading();
        new OldDaysPropertyInt(this,   6, 32,    0,     "SurrGroundHeight", -999, 256).setField().disableLoading();
        new OldDaysPropertyInt(this,   7, 1,     0,     "SurrGroundType", 1, 256).setField().disableLoading();
        new OldDaysPropertyInt(this,   8, 31,    0,     "SurrWaterHeight", -999, 256).setField().disableLoading();
        new OldDaysPropertyInt(this,   9, 9,     0,     "SurrWaterType", 8, 11).disableLoading();
        new OldDaysPropertyRGB(this,   10,0,     0,     "SkyColor").disableLoading();
        new OldDaysPropertyRGB(this,   11,0,     0,     "FogColor").disableLoading();
        new OldDaysPropertyRGB(this,   12,0,     0,     "CloudColor").disableLoading();
        new OldDaysPropertyInt(this,   13,-1,    -1,    "SkyBrightness", -1, 16).setField().disableLoading();
        new OldDaysPropertyFloat(this, 14,128F,  128F,  "CloudHeight", -999.0F, 999.0F).disableLoading();
        new OldDaysPropertyCond(this,  15,1,     2,     "LeavesDecay");
        new OldDaysPropertyBool(this,  16,true,  false, "OldSpawning");
        new OldDaysPropertyCond(this,  17,1,     0,     "OldHoes");
        new OldDaysPropertyBool(this,  18,false, false, "TexturedClouds");
        new OldDaysPropertyCond(this,  19,1,     0,     "OpaqueFlatClouds");
        new OldDaysPropertyCond2(this, 20,1,     0,     "ClassicLight", 2);
        new OldDaysPropertyCond(this,  21,1,     0,     "BedrockFog");
        new OldDaysPropertyCond2(this, 22,-1,    0,     "Sunset", 2);
        new OldDaysPropertyCond(this,  23,1,     0,     "SunriseAtNorth");
        new OldDaysPropertyCond(this,  24,1,     0,     "OldStars");
        new OldDaysPropertyBool(this,  25,true,  false, "OldNetherFog");
        new OldDaysPropertyBool(this,  26,true,  true,  "ShowGUI");
        replaceBlocks();
        registerGears();
        terrfx = new TextureTerrainPngFX();
        bedrockfx = new TextureTerrainPngFX();
        waterfx = new TextureTerrainPngFX();
        lavafx = new TextureTerrainPngFX();
        GuiSelectWorld.nbxlite = true;
        WorldInfo.useNBXlite = true;
        set(RenderGlobal.class, "nbxlite", true);
        Minecraft.getMinecraft().worldClass = WorldSSP2.class;
        set(ItemRenderer.class, "olddays", true);
    }

    public void callback(int i){
        switch(i){
            case 1: setGen(0);
                    setInWorldInfo("mapGen", Generator);
                    setInWorldInfo("mapGenExtra", MapFeatures); break;
            case 2: setInWorldInfo("mapTheme", MapTheme); break;
            case 3: setGen(1);
                    setInWorldInfo("mapGenExtra", MapFeatures); break;
            case 4: setGen(2);
                    setInWorldInfo("mapGenExtra", MapFeatures); break;
            case 5: setInWorldInfo("newOres", GenerateNewOres); break;
            case 6: setInWorldInfo("surrgroundheight", SurrGroundHeight); setTextureFX(); break;
            case 7: if (Block.blocksList[SurrGroundType] == null){
                        SurrGroundType = Block.bedrock.blockID;
                    }
                    setInWorldInfo("surrgroundtype", SurrGroundType); setTextureFX(); break;
            case 8: setInWorldInfo("surrwaterheight", SurrWaterHeight); setTextureFX(); break;
            case 9: setInWorldInfo("surrwatertype", SurrWaterType); setTextureFX(); break;
            case 10:setInWorldInfo("skycolor", SkyColor); break;
            case 11:setInWorldInfo("fogcolor", FogColor); break;
            case 12:setInWorldInfo("cloudcolor", CloudColor); break;
            case 13:setInWorldInfo("skybrightness", SkyBrightness); break;
            case 14:setInWorldInfo("cloudheight", CloudHeight); break;
            case 15:((BlockLeaves2)Block.blocksList[Block.leaves.blockID]).setDecay(LeavesDecay); break;
            case 16:set(EntityAnimal.class, "despawn", OldSpawning && Generator<GEN_NEWBIOMES);
                    set(EntityWolf.class, "despawn", OldSpawning && Generator<GEN_NEWBIOMES); break;
            case 17:set(ItemHoe2.class, "oldhoes", OldHoes); break;
            case 18:set(RenderGlobal2.class, "texClouds", TexturedClouds); break;
            case 19:set(RenderGlobal2.class, "opaqueFlatClouds", OpaqueFlatClouds); break;
            case 20:setLighting(ClassicLight, i); break;
            case 21:set(EntityRenderer.class, "voidFog", BedrockFog); break;
            case 22:set(RenderGlobal2.class, "sunriseColors", Sunset >= 1);
                    set(EntityRenderer.class, "sunriseFog", Sunset >= 2);
            case 23:set(EntityRenderer.class, "sunriseAtNorth", SunriseAtNorth);
                    set(RenderGlobal2.class, "sunriseAtNorth", SunriseAtNorth); break;
            case 24:setOldStars(OldStars); break;
            case 25:set(EntityRenderer.class, "oldNetherFog", OldNetherFog); break;
        }
        if (!renderersAdded && RenderManager.instance!=null){
            addRenderer(EntityGhast.class, new RenderGhast2());//Disable ghast shading with classic light
            addRenderer(EntitySheep.class, new RenderSheep2(new ModelSheep2(), new ModelSheep1(), 0.7F));
            addRenderer(EntityPainting.class, new RenderPainting2());
            addRenderer(EntityMooshroom.class, new RenderMooshroom2(new ModelCow(), 0.7F));
            addRenderer(EntityItem.class, new RenderItem2());
            addRenderer(EntityEnderman.class, new RenderEnderman2());
            addRenderer(EntityPlayer.class, new RenderPlayer2());
            addRenderer(EntityMinecart.class, new RenderMinecart2());
            addRenderer(EntityItemFrame.class, new RenderItemFrame2());
        }
    }

    public static int Gen;
    public static int BetaFeatures;
    public static int ReleaseFeatures;
    public static boolean GenerateNewOres=true;
    public static int SkyColor;
    public static int FogColor;
    public static int CloudColor;
    public static int SkyBrightness;
    public static float CloudHeight;
    public static boolean LeavesDecay;
    public static boolean OldSpawning;
    public static boolean OldHoes;
    public static boolean TexturedClouds;
    public static boolean OpaqueFlatClouds;
    public static int ClassicLight = 0;
    public static boolean BedrockFog;
    public static int Sunset = -1;
    public static boolean SunriseAtNorth;
    public static boolean OldStars;
    public static boolean OldNetherFog = true;
    public static boolean ShowGUI = true;

    public static boolean LeavesDecay(){
        return Generator>GEN_BIOMELESS || MapFeatures!=FEATURES_INFDEV0420;
    }

    public static boolean OldHoes(){
        return Generator==GEN_BIOMELESS || (Generator==GEN_OLDBIOMES && MapFeatures<=FEATURES_BETA15);
    }

    public static boolean OpaqueFlatClouds(){
        return Generator==GEN_BIOMELESS && MapFeatures>FEATURES_ALPHA11201;
    }

    public static int ClassicLight(){
        return Generator<GEN_NEWBIOMES ? 1 : 0;
    }

    public static boolean BedrockFog(){
        return Generator>=GEN_NEWBIOMES;
    }

    public static int Sunset(){
        if (Generator >= GEN_NEWBIOMES){
            return 2;
        }
        if (Generator <= GEN_BIOMELESS || MapFeatures == FEATURES_SKY){
            return 0;
        }
        return 1;
    }

    public static boolean SunriseAtNorth(){
        return Generator<GEN_NEWBIOMES || MapFeatures==FEATURES_BETA181;
    }

    public static boolean OldStars(){
        return Generator<GEN_NEWBIOMES || MapFeatures<FEATURES_13;
    }

    public void setOldStars(boolean b){
        if (Minecraft.getMinecraft().renderGlobal != null){
            if (Minecraft.getMinecraft().renderGlobal instanceof RenderGlobal2){
                ((RenderGlobal2)Minecraft.getMinecraft().renderGlobal).setStars(b);
            }
        }
    }

    public void setLighting(int i, int i2){
        if (i >= 2 && !Minecraft.getMinecraft().enableSP){
            i = 1;
        }
        set(EntityRenderer.class, "classicLight", i > 0);
        set(RenderGhast2.class, "bright", i > 0);
        Minecraft.oldlighting = i > 1;
        oldLightEngine = i > 1;
        if (i > 1 || ((OldDaysPropertyCond2)getPropertyById(i2)).value < 0){
            reload();
        }
    }

    public String[] getAdditionalPackageData(){
        return new String[]{getGenName(Generator, MapFeatures, SnowCovered),
                            ""+IndevSpawnX,
                            ""+IndevSpawnY,
                            ""+IndevSpawnZ,
                            ""+IndevWidthX,
                            ""+IndevHeight,
                            ""+IndevWidthZ};
    }

    public void readAdditionalPackageData(String[] data){
        Generator = getGen(data[0], 0);
        MapFeatures = getGen(data[0], 1);
        SnowCovered = getGen(data[0], 2) > 0;
        int i = 1;
        IndevSpawnX = Integer.parseInt(data[i++]);
        IndevSpawnY = Integer.parseInt(data[i++]);
        IndevSpawnZ = Integer.parseInt(data[i++]);
        IndevWidthX = Integer.parseInt(data[i++]);
        IndevHeight = Integer.parseInt(data[i++]);
        IndevWidthZ = Integer.parseInt(data[i++]);
        SetGenerator();
    }

    public void onLoadingSP(String par1Str, String par2Str){
        if (saveLoader.isOldMapFormat(par1Str) && saveLoader.getWorldInfo(par1Str).getSaveVersion() != 19132){
            convertMapFormatOld(par1Str, par2Str);
        }
    }

    private void convertMapFormatOld(String s, String s1)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mod_OldDays.getMinecraft().enableSP){
            mc.loadingScreen.resetProgressAndMessage("Converting World to Scaevolus' McRegion");
            mc.loadingScreen.resetProgresAndWorkingMessage("This may take a while :)");
        }
        saveLoader.convertMapFormat(s, mc.loadingScreen);
//         mc.startWorldSSP(s, s1, new WorldSettings(0L, EnumGameType.SURVIVAL, true, false, WorldType.DEFAULT));
    }

    public static void setGen(int i){
        if (isFinite()){
            return;
        }
        if (Gen == 0){
            MapFeatures = FEATURES_INFDEV0227;
            Generator = GEN_BIOMELESS;
        }else if (Gen == 1){
            MapFeatures = FEATURES_INFDEV0420;
            Generator = GEN_BIOMELESS;
        }else if (Gen == 2){
            MapFeatures = FEATURES_INFDEV0608;
            Generator = GEN_BIOMELESS;
        }else if (Gen == 3){
            MapFeatures = FEATURES_ALPHA11201;
            Generator = GEN_BIOMELESS;
        }else if (Gen == 4){
            MapFeatures = BetaFeatures;
            Generator = GEN_OLDBIOMES;
        }else if (Gen == 5){
            MapFeatures = ReleaseFeatures;
            Generator = GEN_NEWBIOMES;
        }
        refreshProperties();
        if (mod_OldDays.getMinecraft().theWorld != null){
            SetGenerator(mod_OldDays.getMinecraft().theWorld, Generator, MapFeatures, MapTheme, IndevMapType, SnowCovered, GenerateNewOres);
            if ((i == 0 && (Gen == 0 || Gen > 3)) || (i == 1 && (BetaFeatures >= 5 || BetaFeatures == 3 || BetaFeatures == 0)) || i == 2){
                reload();
            }
        }
    }

    public static void refreshProperties(){
        if (Generator == GEN_NEWBIOMES){
            Gen = 5;
            ReleaseFeatures = MapFeatures;
        }else if (Generator == GEN_OLDBIOMES){
            Gen = 4;
            BetaFeatures = MapFeatures;
        }else if (MapFeatures == FEATURES_ALPHA11201){
            Gen = 3;
        }else if (MapFeatures == FEATURES_INFDEV0608){
            Gen = 2;
        }else if (MapFeatures == FEATURES_INFDEV0420){
            Gen = 1;
        }else if (MapFeatures == FEATURES_INFDEV0227){
            Gen = 0;
        }
        for (int i = 1; i <= 14; i++){
            mod_OldDays.getModuleById(8).getPropertyById(i).updateValue();
        }
        for (int i = 1; i <= 5; i++){
            mod_OldDays.getModuleById(8).getPropertyById(i).disabled = isFinite() ? 5 : 0;
        }
        for (int i = 6; i <= 9; i++){
            mod_OldDays.getModuleById(8).getPropertyById(i).disabled = isFinite() ? 0 : 6;
        }
        mod_OldDays.getModuleById(8).getPropertyById(2).disabled = Generator == GEN_BIOMELESS ? 0 : 7;
        mod_OldDays.getModuleById(8).getPropertyById(3).disabled = isFinite() ? 5 : (Generator == GEN_OLDBIOMES ? 0 : 8);
        mod_OldDays.getModuleById(8).getPropertyById(4).disabled = isFinite() ? 5 : (Generator == GEN_NEWBIOMES ? 0 : 9);
    }

    private void registerGears(){
        int index = mod_OldDays.getFreeTextureIndex();
        int index2 = mod_OldDays.getFreeTextureIndex();
        Block gear = new BlockGear(gearId, index, index2);
        gear.setHardness(0.5F);
        gear.setBlockName("gear");
        
        gear.disableStats();
//         ModLoader.addName(gear, "Gear");
        Block.blocksList[gearId] = gear;
        new ItemBlock(gearId - 256);
        Item.itemsList[gearId].setItemName("gear");
        Block.blocksList[gearId].initializeBlock();
        mod_OldDays.getMinecraft().renderEngine.registerTextureFX(new TextureGearFX(index, 0));
        mod_OldDays.getMinecraft().renderEngine.registerTextureFX(new TextureGearFX(index2, 1));
        gearRenderID = 36;
    }

    public static int getSkyLightInBounds(int par2){
        int sky = 15;
        if (par2<SurrWaterHeight){
            if (Block.blocksList[SurrWaterType].blockMaterial!=Material.lava){
                sky-=3*(SurrWaterHeight-par2);
            }else{
                sky = 0;
            }
        }
        if (par2<SurrGroundHeight){
            sky = 0;
        }
        if (sky<0){
            sky = 0;
        }
        return sky;
    }

    public static int getBlockLightInBounds(int par2){
        int block = 0;
        if (par2>=SurrGroundHeight && SurrWaterHeight>=SurrGroundHeight){
            if (par2<SurrWaterHeight){
                block = Block.lightValue[SurrWaterType];
            }else{
                block = Block.lightValue[SurrWaterType]-(par2-SurrWaterHeight)-1;
            }
        }
        if (block<0){
            block = 0;
        }
        return block;
    }

    public static int getSkyLightInBounds2(int par2){
        int sky = 15 - (Minecraft.getMinecraft().theWorld == null ? 0 : Minecraft.getMinecraft().theWorld.skylightSubtracted);
        if (par2<SurrWaterHeight){
            if (Block.blocksList[SurrWaterType].blockMaterial!=Material.lava){
                sky-=3*(SurrWaterHeight-par2);
            }else{
                sky = 0;
            }
        }
        if (par2<SurrGroundHeight){
            sky = 0;
        }
        if (sky<0){
            sky = 0;
        }
        return sky;
    }

    public static int getLightInBounds(int par2){
        return getSkyLightInBounds(par2) << 20 | getBlockLightInBounds(par2) << 4;
    }

    public static int getLightInBounds2(int par2){
        return Math.max(getSkyLightInBounds2(par2), getBlockLightInBounds(par2));
    }

    public static int getLightInBounds(EnumSkyBlock block, int par2){
        return block == EnumSkyBlock.Sky ? getSkyLightInBounds2(par2) : getBlockLightInBounds(par2);
    }

    public static float getLightFloat(int par2){
        Minecraft mc = Minecraft.getMinecraft();
        return mc.theWorld.provider.lightBrightnessTable[getLightInBounds2(par2)];
    }

    public static int getBlockIdInBounds(int par2){
        if (par2<SurrGroundHeight-1){
            return Block.bedrock.blockID;
        }
        if (par2<SurrGroundHeight){
            if (MapFeatures==FEATURES_CLASSIC){
                return Block.bedrock.blockID;
            }
            if ((par2<SurrWaterHeight || SurrWaterType==Block.lavaStill.blockID) && SurrGroundType==Block.grass.blockID){
                return Block.dirt.blockID;
            }
            return SurrGroundType;
        }
        if (par2<SurrWaterHeight){
            return SurrWaterType;
        }
        return 0;
    }

    public static String getGenName(int gen, int feats, boolean snow){
        StringBuilder result = new StringBuilder();
        if (gen==GEN_BIOMELESS){
            result.append("nobiomes/");
            if (feats==FEATURES_ALPHA11201){
                result.append("alpha");
                if (snow){
                    result.append("/snow");
                }
            }else if (feats==FEATURES_INDEV){
                result.append("indev");
            }else if (feats==FEATURES_CLASSIC){
                result.append("classic");
            }else{
                result.append("infdev");
                if (feats==FEATURES_INFDEV0608){
                    result.append("0608");
                }else if (feats==FEATURES_INFDEV0420){
                    result.append("0420");
                }else if (feats==FEATURES_INFDEV0227){
                    result.append("0227");
                }
            }
        }else if (gen==GEN_OLDBIOMES){
            result.append("oldbiomes/");
            if (feats==FEATURES_ALPHA120){
                result.append("halloween");
            }else if (feats==FEATURES_SKY){
                result.append("sky");
            }else{
                result.append("beta1");
                if (feats==FEATURES_BETA12){
                    result.append("2");
                }else if (feats==FEATURES_BETA14){
                    result.append("4");
                }else if (feats==FEATURES_BETA15){
                    result.append("5");
                }else if (feats==FEATURES_BETA173){
                    result.append("73");
                }else if (feats==FEATURES_JUNGLE){
                    result.append("73/jungle");
                }
            }
        }else if (gen==GEN_NEWBIOMES){
            result.append("newbiomes/");
            if (feats==FEATURES_BETA181){
                result.append("beta181");
            }else if (feats==FEATURES_10){
                result.append("10");
            }else if (feats==FEATURES_11){
                result.append("11");
            }else if (feats==FEATURES_12){
                result.append("12");
            }else if (feats==FEATURES_13){
                result.append("13");
            }else if (feats==FEATURES_132){
                result.append("132");
            }else if (feats==FEATURES_14){
                result.append("14");
            }
        }
        return result.toString();
    }

    public static int getGen(String gen, int what){
        if (what==0){
            if (gen.startsWith("nobiomes/")){
                return GEN_BIOMELESS;
            }
            if (gen.startsWith("oldbiomes/")){
                return GEN_OLDBIOMES;
            }
            if (gen.startsWith("newbiomes/")){
                return GEN_NEWBIOMES;
            }
            return 0;
        }
        if (what==1){
            if (gen.startsWith("nobiomes/")){
                if (gen.contains("alpha11201")){
                    return FEATURES_ALPHA11201;
                }
                if (gen.contains("indev")){
                    return FEATURES_INDEV;
                }
                if (gen.contains("classic")){
                    return FEATURES_CLASSIC;
                }
                if (gen.contains("infdev")){
                    if (gen.contains("0608")){
                        return FEATURES_INFDEV0608;
                    }
                    if (gen.contains("0420")){
                        return FEATURES_INFDEV0420;
                    }
                    if (gen.contains("0227")){
                        return FEATURES_INFDEV0227;
                    }
                }
            }
            if (gen.startsWith("oldbiomes/")){
                if (gen.contains("halloween")){
                    return FEATURES_ALPHA120;
                }
                if (gen.contains("sky")){
                    return FEATURES_SKY;
                }
                if (gen.contains("beta12")){
                    return FEATURES_BETA12;
                }
                if (gen.contains("beta14")){
                    return FEATURES_BETA14;
                }
                if (gen.contains("beta15")){
                    return FEATURES_BETA15;
                }
                if (gen.contains("beta173")){
                    if (gen.endsWith("/jungle")){
                        return FEATURES_JUNGLE;
                    }else{
                        return FEATURES_BETA173;
                    }
                }
                return 0;
            }
            if (gen.startsWith("newbiomes/")){
                if (gen.contains("beta181")){
                    return FEATURES_BETA181;
                }
                if (gen.contains("10")){
                    return FEATURES_10;
                }
                if (gen.contains("11")){
                    return FEATURES_11;
                }
                if (gen.contains("12")){
                    return FEATURES_12;
                }
                if (gen.contains("13")){
                    return FEATURES_13;
                }
                if (gen.contains("132")){
                    return FEATURES_132;
                }
                if (gen.contains("14")){
                    return FEATURES_14;
                }
                return 0;
            }
            return 0;
        }
        if (what==2){
            return gen.endsWith("/snow") ? 1 : 0;
        }
        return 0;
    }

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, int override){
        if (id == gearRenderID){
            return BlockGear.renderBlockGear(r, i, b, x, y, z, override);
        }
        return false;
    }

    public static boolean isFinite(){
        World world = mod_OldDays.getMinecraft().theWorld;
        return Generator==GEN_BIOMELESS && (MapFeatures==FEATURES_INDEV || MapFeatures==FEATURES_CLASSIC) && (world==null || world.provider.dimensionId==0);
    }

    public boolean onTick(){
        Minecraft minecraft = mod_OldDays.getMinecraft();
        if (isFinite() && !minecraft.theWorld.isRemote){
            tickPushing(minecraft);
        }
        return true;
    }

    public boolean onGUITick(GuiScreen gui){
        Minecraft minecraft = mod_OldDays.getMinecraft();
        if (!rendererReplaced){
//             minecraft.entityRenderer = new EntityRenderer2(minecraft);
            minecraft.renderGlobal = new RenderGlobal2(minecraft, minecraft.renderEngine);
            rendererReplaced = true;
        }
        return true;
    }

    private boolean tickPushing(Minecraft minecraft){
        Entity entity;
        for (int k = 0; k < minecraft.theWorld.loadedEntityList.size(); k++)
        {
            entity = (Entity)minecraft.theWorld.loadedEntityList.get(k);
            pushBack(entity);
        }
        return true;
    }

    private void pushBack(Entity entity){
        if (MapFeatures==FEATURES_INDEV){
            if (entity.posX>IndevWidthX+8){
                entity.motionX-=(entity.posX-IndevWidthX)/950;
            }
            if (entity.posX<-8){
                entity.motionX-=(entity.posX)/950;
            }
            if (entity.posZ>IndevWidthZ+8){
                entity.motionZ-=(entity.posZ-IndevWidthZ)/950;
            }
            if (entity.posZ<-8){
                entity.motionZ-=(entity.posZ)/950;
            }
        }
        if (MapFeatures==FEATURES_CLASSIC){
            if (entity.posX>IndevWidthX){
                entity.motionX-=0.5;
            }
            if (entity.posX<0){
                entity.motionX+=0.5;
            }
            if (entity.posZ>IndevWidthZ){
                entity.motionZ-=0.5;
            }
            if (entity.posZ<0){
                entity.motionZ+=0.5;
            }
        }
    }

    private static void replaceBlocks(){
        try{
            /*Block.blocksList[Block.grass.blockID] = null;
            BlockGrass2 customgrass = (BlockGrass2)(new BlockGrass2(Block.grass.blockID));
            customgrass.setHardness(0.6F);
            customgrass.setStepSound(Block.soundGrassFootstep);
            customgrass.setBlockName("grass");
            Block.blocksList[Block.grass.blockID] = customgrass;
            mod_OldDays.setField(Block.class, null, 21, customgrass);//Block: grass*/
            Block.grass.toptex = mod_OldDays.getFreeTextureIndex();
            addTextureHook("/terrain.png", Block.grass.toptex, "/olddays/grasstop.png", 0, 1, 1);
            Block.grass.sidetex = mod_OldDays.getFreeTextureIndex();
            addTextureHook("/terrain.png", Block.grass.sidetex, "/olddays/grassside.png", 0, 1, 1);
            Block.blocksList[Block.leaves.blockID] = null;
            BlockLeaves2 customleaves = (BlockLeaves2)(new BlockLeaves2(Block.leaves.blockID, 52));
            customleaves.setHardness(0.2F);
            customleaves.setLightOpacity(1);
            customleaves.setStepSound(Block.soundGrassFootstep);
            customleaves.setBlockName("leaves");
            customleaves.setRequiresSelfNotify();
            Block.blocksList[Block.leaves.blockID] = customleaves;
            mod_OldDays.setField(Block.class, null, 37, customleaves);//Block: leaves
            customleaves.fasttex = mod_OldDays.getFreeTextureIndex();
            addTextureHook("/terrain.png", customleaves.fasttex, "/olddays/leavesfast.png", 0, 1, 1);
            customleaves.fancytex = mod_OldDays.getFreeTextureIndex();
            addTextureHook("/terrain.png", customleaves.fancytex, "/olddays/leavesfancy.png", 0, 1, 1);
            Block.blocksList[Block.tallGrass.blockID] = null;
            BlockTallGrass2 customtallgrass = (BlockTallGrass2)(new BlockTallGrass2(Block.tallGrass.blockID, 39));
            customtallgrass.setHardness(0.0F);
            customtallgrass.setStepSound(Block.soundGrassFootstep);
            customtallgrass.setBlockName("tallgrass");
            Block.blocksList[Block.tallGrass.blockID] = customtallgrass;
            mod_OldDays.setField(Block.class, null, 50, customtallgrass);//Block: tallGrass
            Block.blocksList[Block.vine.blockID] = null;
            BlockVine2 customvine = (BlockVine2)(new BlockVine2(Block.vine.blockID));
            customvine.setHardness(0.2F);
            customvine.setStepSound(Block.soundGrassFootstep);
            customvine.setBlockName("vine");
            customvine.setRequiresSelfNotify();
            Block.blocksList[Block.vine.blockID] = customvine;
            mod_OldDays.setField(Block.class, null, 125, customvine);//Block: vine

            Item.itemsList[256 + 34] = null;
            ItemHoe2 hoeWood = new ItemHoe2(34, EnumToolMaterial.WOOD);
            hoeWood.setIconCoord(0, 8);
            hoeWood.setItemName("hoeWood");
            Item.itemsList[256 + 35] = null;
            ItemHoe2 hoeStone = new ItemHoe2(35, EnumToolMaterial.STONE);
            hoeStone.setIconCoord(1, 8);
            hoeStone.setItemName("hoeStone");
            Item.itemsList[256 + 36] = null;
            ItemHoe2 hoeSteel = new ItemHoe2(36, EnumToolMaterial.IRON);
            hoeSteel.setIconCoord(2, 8);
            hoeSteel.setItemName("hoeIron");
            Item.itemsList[256 + 37] = null;
            ItemHoe2 hoeDiamond = new ItemHoe2(37, EnumToolMaterial.EMERALD);
            hoeDiamond.setIconCoord(3, 8);
            hoeDiamond.setItemName("hoeDiamond");
            Item.itemsList[256 + 38] = null;
            ItemHoe2 hoeGold = new ItemHoe2(38, EnumToolMaterial.GOLD);
            hoeGold.setIconCoord(4, 8);
            hoeGold.setItemName("hoeGold");
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    public static int GetFoliageColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex){
        if (Generator==GEN_BIOMELESS){
            if (tex && hasTextures("olddays/leavesfast.png", "olddays/leavesfancy.png")){
                return 0xffffff;
            }
            return ColorizerFoliage.getFoliageColor(1.0F, 1.0F);
        }else if (Generator==GEN_OLDBIOMES){
            WorldChunkManager man = mod_OldDays.getMinecraft().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerFoliage.getFoliageColor(d, d1);
        }else{
            if (MapFeatures>=FEATURES_11 && smooth){
                int i1 = 0;
                int j1 = 0;
                int k1 = 0;
                for (int l1 = -1; l1 <= 1; l1++)
                {
                    for (int i2 = -1; i2 <= 1; i2++)
                    {
                        int j2 = 0;
                        if (MapFeatures >= FEATURES_12){
                            j2 = iblockaccess.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor2();
                        }else{
                            j2 = iblockaccess.getBiomeGenForCoords(x + i2, z + l1).getFoliageColorAtCoords(x + i2, y, z + l1);
                        }
                        i1 += (j2 & 0xff0000) >> 16;
                        j1 += (j2 & 0xff00) >> 8;
                        k1 += j2 & 0xff;
                    }
                }
                return (i1 / 9 & 0xff) << 16 | (j1 / 9 & 0xff) << 8 | k1 / 9 & 0xff;
            }else{
                if (MapFeatures >= FEATURES_12){
                    return iblockaccess.getBiomeGenForCoords(x, z).getBiomeFoliageColor2();
                }
                return iblockaccess.getBiomeGenForCoords(x, z).getFoliageColorAtCoords(x, y, z);
            }
        }
    }

    public static int GetGrassColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z, boolean smooth, boolean tex){
        if(Generator==GEN_BIOMELESS){
            if (tex && hasTextures("olddays/grasstop.png", "olddays/grassside.png")){
                return 0xffffff;
            }
            return ColorizerGrass.getGrassColor(1.0F, 1.0F);
        } else if(Generator==GEN_OLDBIOMES){
            WorldChunkManager man = mod_OldDays.getMinecraft().theWorld.getWorldChunkManager();
            man.oldFunc_4069_a(x, z, 1, 1);
            double d = man.temperature[0];
            double d1 = man.humidity[0];
            return ColorizerGrass.getGrassColor(d, d1);
        }else{
            if (MapFeatures>=FEATURES_11 && smooth){
                int l = 0;
                int i1 = 0;
                int j1 = 0;
                for (int k1 = -1; k1 <= 1; k1++)
                {
                    for (int l1 = -1; l1 <= 1; l1++)
                    {
                        int i2 = 0;
                        if (MapFeatures >= FEATURES_12){
                            i2 = iblockaccess.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor2();
                        }else{
                            i2 = iblockaccess.getBiomeGenForCoords(x + l1, z + k1).getGrassColorAtCoords(x + l1, y, z + k1);
                        }
                        l += (i2 & 0xff0000) >> 16;
                        i1 += (i2 & 0xff00) >> 8;
                        j1 += i2 & 0xff;
                    }
                }
                return (l / 9 & 0xff) << 16 | (i1 / 9 & 0xff) << 8 | j1 / 9 & 0xff;
            }else{
                if (MapFeatures >= FEATURES_12){
                    return iblockaccess.getBiomeGenForCoords(x, z).getBiomeGrassColor2();
                }
                return iblockaccess.getBiomeGenForCoords(x, z).getGrassColorAtCoords(x, y, z);
            }
        }
    }

    public static int GetWaterColorAtCoords(IBlockAccess iblockaccess, int x, int y, int z){
        if (Generator==GEN_NEWBIOMES && MapFeatures>FEATURES_BETA181){
            if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_10){
                return iblockaccess.getBiomeGenForCoords(x, z).waterColorMultiplier;
            }else{
                int i = 0;
                int j = 0;
                int k = 0;
                for (int l = -1; l <= 1; l++){
                    for (int i1 = -1; i1 <= 1; i1++){
                        int j1 = iblockaccess.getBiomeGenForCoords(x + i1, z + l).waterColorMultiplier;
                        i += (j1 & 0xff0000) >> 16;
                        j += (j1 & 0xff00) >> 8;
                        k += j1 & 0xff;
                    }
                }
                return (i / 9 & 0xff) << 16 | (j / 9 & 0xff) << 8 | k / 9 & 0xff;
            }
        }
        return 0xffffff;
    }

    public static boolean mineshaftFloor(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>=FEATURES_12;
    }

    public static boolean mineshaftFences(){
        return true;
    }

    public static boolean mineshaftSomeValue(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>FEATURES_BETA181;
    }

    public static boolean desertVillages(){
        return (Generator==GEN_NEWBIOMES && MapFeatures>=FEATURES_13) || Generator==GEN_OLDBIOMES;
    }

    public static boolean villageChests(){
        return (Generator==GEN_NEWBIOMES && MapFeatures>=FEATURES_11) || Generator==GEN_OLDBIOMES;
    }

    public static boolean villagers(){
        return Generator!=GEN_NEWBIOMES || MapFeatures>FEATURES_BETA181;
    }

    public static boolean lowerVillages(){
        return Generator==GEN_NEWBIOMES && MapFeatures<=FEATURES_BETA181;
    }

    public static boolean oldStrongholds(){
        return Generator==GEN_NEWBIOMES && MapFeatures<=FEATURES_BETA181;
    }

    public static boolean noNewCrops(){
        return Generator==GEN_NEWBIOMES && MapFeatures<FEATURES_14;
    }

    public static void SetGenerator(){
        SetGenerator(Minecraft.getMinecraft().theWorld, Generator, MapFeatures, MapTheme, IndevMapType, SnowCovered, GenerateNewOres);
    }

    public static void SetGenerator(World world, int gen, int features, int theme, int type, boolean snow, boolean ores){
        Generator=gen;
        MapFeatures=features;
        if (gen==GEN_NEWBIOMES){
            BiomeGenBase.ocean.maxHeight = features==FEATURES_BETA181 ? 0.5F : 0.4F;
            BiomeGenBase.taiga.temperature = features>=FEATURES_11 ? 0.05F : 0.3F;
            BiomeGenBase.extremeHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.extremeHills.maxHeight = features<FEATURES_13 ? (features>=FEATURES_11 ? 1.3F : 1.8F) : 1.5F;
            BiomeGenBase.swampland.waterColorMultiplier = features>=FEATURES_11 ? 0xe0ffae : 0xe0ff70;
            BiomeGenBase.iceMountains.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.iceMountains.maxHeight = features<FEATURES_13 ? 1.2F : 1.3F;
            BiomeGenBase.desertHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.desertHills.maxHeight = features<FEATURES_13 ? 0.7F : 0.8F;
            BiomeGenBase.forestHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.forestHills.maxHeight = features<FEATURES_13 ? 0.6F : 0.7F;
            BiomeGenBase.taigaHills.minHeight = features<FEATURES_13 ? 0.2F : 0.3F;
            BiomeGenBase.taigaHills.maxHeight = features<FEATURES_13 ? 0.7F : 0.8F;
            BiomeGenBase.jungleHills.minHeight = features<FEATURES_13 ? 0.2F : 0.5F;
        }
        MapTheme = gen==GEN_BIOMELESS ? theme : 0;
        if ((Generator == GEN_OLDBIOMES && (MapFeatures == FEATURES_JUNGLE || MapFeatures == FEATURES_SKY)) || Generator == GEN_NEWBIOMES){
            world.provider.registerWorld(world);
        }
        SnowCovered = gen==GEN_BIOMELESS && features==FEATURES_ALPHA11201 && snow;
        GreenGrassSides = gen==GEN_OLDBIOMES && features<=FEATURES_BETA14 && !NoGreenGrassSides;
        RestrictSlimes = isFinite() && IndevHeight<96;
        IndevMapType = gen==GEN_BIOMELESS && features==FEATURES_INDEV ? type : 0;
        if (Generator==GEN_NEWBIOMES){
            VoidFog = 0;
        }else if (Generator==GEN_OLDBIOMES && MapFeatures==FEATURES_SKY){
            VoidFog = 3;
        }else if (Generator==GEN_BIOMELESS && MapFeatures==FEATURES_ALPHA11201 && MapTheme>THEME_NORMAL){
            VoidFog = 3;
        }else if (Generator==GEN_OLDBIOMES || (Generator==GEN_BIOMELESS && MapFeatures==FEATURES_ALPHA11201)){
            VoidFog = 2;
        }else if (Generator==GEN_BIOMELESS && MapFeatures>FEATURES_ALPHA11201){
            VoidFog = 4;
        }
        mod_OldDays.getModuleById(8).set(EntityRenderer.class, "oldFog", isFinite());
        mod_OldDays.getModuleById(8).set(EntityRenderer.class, "snow", SnowCovered);
        mod_OldDays.getModuleById(8).set(EntityRenderer.class, "bounds", isFinite());
        GenerateNewOres=ores;
        try{
            EntityAnimal.despawn = OldSpawning && Generator<GEN_NEWBIOMES;
            EntityWolf.despawn = OldSpawning && Generator<GEN_NEWBIOMES;
        }catch(Exception ex){}
        mod_OldDays.refreshConditionProperties();
    }

    public static void setTextureFX(){
        setTextureFX2();
        OldDaysModule.reload();
    }

    public static void setTextureFX2(){
        org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, mod_OldDays.getMinecraft().renderEngine.getTexture("/terrain.png"));
        textureWidth = org.lwjgl.opengl.GL11.glGetTexLevelParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, org.lwjgl.opengl.GL11.GL_TEXTURE_WIDTH) / 16;
        int id = SurrGroundType;
        if (SurrGroundHeight<=SurrWaterHeight || SurrWaterType==Block.lavaStill.blockID && SurrGroundType==Block.grass.blockID){
            id = Block.dirt.blockID;
        }
        if (MapFeatures==FEATURES_CLASSIC){
            id = Block.bedrock.blockID;
        }
        if (Block.blocksList[id] == null){
            return;
        }
        int tid = Block.blocksList[id].getBlockTextureFromSideAndMetadata(1, 0);
        terrfx.changeIndex(tid, false);
        bedrockfx.changeIndex(Block.bedrock.blockIndexInTexture, false);
        waterfx.changeIndex(Block.waterStill.blockIndexInTexture, false);
        lavafx.changeIndex(Block.lavaStill.blockIndexInTexture, false);
        emptyImage = mod_OldDays.getMinecraft().renderEngine.allocateAndSetupTexture(new java.awt.image.BufferedImage(textureWidth, textureWidth, 2));
    }

    public static void setIndevBounds(int groundtype, int groundheight, int watertype, int waterheight){
        SurrGroundType = groundtype;
        SurrGroundHeight = groundheight;
        SurrWaterType = watertype;
        SurrWaterHeight = waterheight;
    }

    public static void setIndevBounds(int type, int theme){
        SurrGroundType = Block.grass.blockID;
        SurrWaterType = theme==THEME_HELL ? Block.lavaStill.blockID : Block.waterStill.blockID;
        if (type==5){
            SurrWaterHeight = IndevHeight-32;
            SurrGroundHeight = SurrWaterHeight-2;
        }else if (type==TYPE_FLOATING){
            SurrGroundHeight = -128;
            SurrWaterHeight = SurrGroundHeight+1;
        }else if (type==TYPE_ISLAND){
            SurrWaterHeight = IndevHeight-32;
            SurrGroundHeight = SurrWaterHeight-9;
        }else{
            SurrGroundHeight = IndevHeight-31;
            SurrWaterHeight = SurrGroundHeight-16;
        }
    }

    public static float setCloudHeight(int gen, int feats, int theme, int type){
        if (gen==GEN_NEWBIOMES){
            return CloudHeight = 128F;
        }
        if (gen==GEN_OLDBIOMES && feats==FEATURES_SKY){
            return CloudHeight = 8F;
        }
        if (gen==GEN_BIOMELESS){
            if (feats==FEATURES_INFDEV0227 || feats==FEATURES_INFDEV0420 || feats==FEATURES_INFDEV0608){
                return CloudHeight = theme==THEME_PARADISE ? 182F : 120F;
            }
            if (feats==FEATURES_INDEV || feats==FEATURES_CLASSIC){
                if (theme==THEME_PARADISE){
                    return CloudHeight = IndevHeight+64;
                }
                if (type==TYPE_FLOATING && theme!=THEME_HELL){
                    return CloudHeight = -16F;
                }
                return CloudHeight = IndevHeight+2;
            }
            if(theme==THEME_PARADISE){
                return CloudHeight = 170F;
            }
            return CloudHeight = 108F;
        }
        return CloudHeight = 108F;
    }

    public static int setSkyBrightness(int theme){
        return SkyBrightness = getSkyBrightness(theme);
    }

    public static int getSkyBrightness(int theme){
        if (theme==THEME_HELL){
            return 7;
        }
        if (theme==THEME_WOODS){
            return 12;
        }
        if (theme==THEME_PARADISE){
            return 16;
        }
        return 15;
    }

    public static int setSkyColor(int gen, int feats, int theme, int num){
        if (num==0){
            return SkyColor = getSkyColor(gen, feats, theme, num);
        }
        if (num==1){
            return FogColor = getSkyColor(gen, feats, theme, num);
        }
        return CloudColor = getSkyColor(gen, feats, theme, num);
    }

    public static int getSkyColor(int gen, int feats, int theme, int num){
        if (num==0){
            if (theme==THEME_HELL){
                return 0x100400;
            }
            if (theme==THEME_WOODS){
                return 0x757d87;
            }
            if (theme==THEME_PARADISE){
                return 0xc6deff;
            }
            if (gen==GEN_BIOMELESS){
                if (feats==FEATURES_CLASSIC || feats==FEATURES_INDEV || feats==FEATURES_INFDEV0420 || feats==FEATURES_INFDEV0608){
                    return 0x99ccff;
                }
                if (feats==FEATURES_INFDEV0227){
                    return 0x0000ff;
                }
                return 0x88bbff;
            }
            if (gen==GEN_OLDBIOMES && feats==FEATURES_SKY){
                return 0xb9b8f4;
            }
            return 0;
        }
        if (num==1){
            if (theme==THEME_HELL){
                return 0x100400;
            }
            if (theme==THEME_WOODS){
                return 0x4d5a5b;
            }
            if (theme==THEME_PARADISE){
                return 0xc6deff;
            }
            if (gen==GEN_BIOMELESS){
                if (feats==FEATURES_CLASSIC || feats==FEATURES_INDEV || feats==FEATURES_INFDEV0227){
                    return 0xffffff;
                }
                if (feats==FEATURES_INFDEV0420 || feats==FEATURES_INFDEV0608){
                    return 0xb0d0ff;
                }
                return 0;
            }
            if (gen==GEN_OLDBIOMES && feats==FEATURES_SKY){
                return 0x9493bb;
            }
            return 0;
        }
        if (theme==THEME_HELL){
            return 0x210800;
        }
        if (theme==THEME_WOODS){
            return 0x4d5a5b;
        }
        if (theme==THEME_PARADISE){
            return 0xeeeeff;
        }
        return 0xffffff;
    }

    public static void generateIndevLevel(long seed){
        IndevGenerator gen2 = new IndevGenerator(mod_OldDays.getMinecraft().loadingScreen, seed);
        if (ODNBXlite.IndevMapType==ODNBXlite.TYPE_ISLAND){
            gen2.island=true;
        }
        if (ODNBXlite.IndevMapType==ODNBXlite.TYPE_FLOATING){
            gen2.floating=true;
        }
        if (ODNBXlite.IndevMapType==ODNBXlite.TYPE_FLAT){
            gen2.flat=true;
        }
        gen2.theme=ODNBXlite.MapTheme;
        if (mod_OldDays.getMinecraft().enableSP){
            mod_OldDays.getMinecraft().loadingScreen.resetProgressAndMessage(StatCollector.translateToLocal("menu.generatingLevel"));
        }
        ODNBXlite.IndevWorld = gen2.generateLevel("Created with NBXlite!", ODNBXlite.IndevWidthX, ODNBXlite.IndevWidthZ, ODNBXlite.IndevHeight);
        ODNBXlite.IndevSpawnX = gen2.spawnX;
        ODNBXlite.IndevSpawnY = gen2.spawnY;
        ODNBXlite.IndevSpawnZ = gen2.spawnZ;
    }

    public static void generateClassicLevel(long seed){
        ClassicGenerator gen2 = new ClassicGenerator(mod_OldDays.getMinecraft().loadingScreen, seed);
        ODNBXlite.IndevHeight = 64;
        if (mod_OldDays.getMinecraft().enableSP){
            mod_OldDays.getMinecraft().loadingScreen.resetProgressAndMessage(StatCollector.translateToLocal("menu.generatingLevel"));
        }
        ODNBXlite.IndevWorld = gen2.generateLevel("Created with NBXlite!", ODNBXlite.IndevWidthX, ODNBXlite.IndevWidthZ, ODNBXlite.IndevHeight);
        ODNBXlite.IndevSpawnX = gen2.spawnX;
        ODNBXlite.IndevSpawnY = gen2.spawnY;
        ODNBXlite.IndevSpawnZ = gen2.spawnZ;
    }

    public static int Generator = 2;
    public static boolean SnowCovered = false;
    public static int VoidFog=0;//0 - default; 1 - no void fog, horizon moves; 2 - no void fog, horizon doesn't move; 3 - no void fog, no bottom color; 4 - no void fog, no horizon
    public static boolean GreenGrassSides=false;
    public static boolean NoGreenGrassSides=false;
    public static boolean RestrictSlimes=false;//Makes slimes not spawn higher than 16 blocks altitude
    public static int MapTheme = 0;
    public static int MapFeatures = 3;
    public static int DayNight = 2;//0 - none, 1 - old, 2 - new
    public static int IndevMapType=0;
    public static int IndevHeight = 64;
    public static int IndevWidthX = 256;
    public static int IndevWidthZ = 256;
    public static int IndevSpawnX;
    public static int IndevSpawnY;
    public static int IndevSpawnZ;
    public static byte[] IndevWorld;
    public static int SurrGroundHeight;
    public static int SurrWaterHeight;
    public static int SurrWaterType;
    public static int SurrGroundType;
    public static boolean Import = false;
    public static int DefaultGenerator = 6;
    public static int DefaultFeaturesBeta = 5;
    public static int DefaultFeaturesRelease = 6;
    public static int DefaultTheme = 0;
    public static int DefaultIndevType = 1;
    public static int DefaultFiniteWidth = 2;
    public static int DefaultFiniteLength = 2;
    public static int DefaultFiniteDepth = 32;
    public static boolean DefaultNewOres = false;
    public static McLevelImporter mclevelimporter = null;
    public static int gearId = 200;
    public static TextureTerrainPngFX terrfx;
    public static TextureTerrainPngFX bedrockfx;
    public static TextureTerrainPngFX waterfx;
    public static TextureTerrainPngFX lavafx;
    public static int emptyImage;
    public static int textureWidth;

    public static final int GEN_BIOMELESS = 0;
    public static final int GEN_OLDBIOMES = 1;
    public static final int GEN_NEWBIOMES = 2;

    public static final int FEATURES_ALPHA11201 = 0;
    public static final int FEATURES_INFDEV0420 = 1;
    public static final int FEATURES_INFDEV0227 = 2;
    public static final int FEATURES_INDEV = 3;
    public static final int FEATURES_CLASSIC = 4;
    public static final int FEATURES_INFDEV0608 = 5;

    public static final int FEATURES_ALPHA120 = 0;
    public static final int FEATURES_BETA10 = 1;
    public static final int FEATURES_BETA12 = 2;
    public static final int FEATURES_BETA14 = 3;
    public static final int FEATURES_BETA15 = 4;
    public static final int FEATURES_BETA173 = 5;
    public static final int FEATURES_SKY = 6;
    public static final int FEATURES_JUNGLE = 7;

    public static final int FEATURES_BETA181 = 0;
    public static final int FEATURES_10 = 1;
    public static final int FEATURES_11 = 2;
    public static final int FEATURES_12 = 3;
    public static final int FEATURES_13 = 4;
    public static final int FEATURES_132 = 5;
    public static final int FEATURES_14 = 6;

    public static final int THEME_NORMAL = 0;
    public static final int THEME_HELL = 1;
    public static final int THEME_WOODS = 2;
    public static final int THEME_PARADISE = 3;

    public static final int TYPE_INLAND = 0;
    public static final int TYPE_ISLAND = 1;
    public static final int TYPE_FLOATING = 2;
    public static final int TYPE_FLAT = 3;

    public static int gearRenderID;
    public static boolean rendererReplaced = false;

    public static boolean oldLightEngine = false;

    public static ISaveFormat saveLoader = new SaveConverterMcRegion(new File(mod_OldDays.getMinecraft().getMinecraftDir(), "saves"));
}