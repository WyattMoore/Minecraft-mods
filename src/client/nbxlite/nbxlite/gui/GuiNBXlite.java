package net.minecraft.src.nbxlite.gui;

import java.util.List;
import java.util.Collections;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class GuiNBXlite extends GuiScreen{
    private String selectedWorld;
    private int number;
    private boolean newworld;
    private int leftmargin = 90;
    private GuiScreen parent;
    private GuiButton[] genButtons;

    private Page page;

    public GuiNBXlite(GuiScreen guiscreen){
        parent = guiscreen;
        newworld = true;
        applySettings(true);
    }

    public GuiNBXlite(GuiScreen guiscreen, String world, int i){
        this(guiscreen);
        selectedWorld = world;
        number = i;
        newworld = false;
    }

    @Override
    public void updateScreen()
    {
    }

    @Override
    public void initGui()
    {
        buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, mod_OldDays.lang.get("continue")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StringTranslate.getInstance().translateKey("gui.cancel")));
        genButtons = new GuiButton[GeneratorList.genlength + 1];
        for (int i = 0; i < genButtons.length; i++){
            genButtons[i] = new GuiButton(2 + i, width / 2 - 170, height / 6 + (i * 21), 100, 20, "");
            genButtons[i].displayString = mod_OldDays.lang.get("nbxlite.defaultgenerator" + (i + 1));
            buttonList.add(genButtons[i]);
        }
        genButtons[GeneratorList.gencurrent].enabled = false;
    }

    public void setPage(){
        switch (GeneratorList.gencurrent){
            case 0: page = new PageFinite(this, false); break;
            case 1: page = new PageFinite(this, true); break;
            case 2: page = new PageAlpha(this, 0); break;
            case 3: page = new PageAlpha(this, 1); break;
            case 4: page = new PageAlpha(this, 2); break;
            case 5: page = new PageBeta(this); break;
            default: page = new PageRelease(this); break;
        }
    }

    public void refreshPage(){
        setPage();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int scaledWidth = scaledresolution.getScaledWidth();
        int scaledHeight = scaledresolution.getScaledHeight();
        page.setWorldAndResolution(mc, scaledWidth, scaledHeight);
        page.initButtons();
        page.updateButtonText();
        page.updateButtonVisibility();
        page.scrolled();
        page.calculateMinScrolling();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height){
        super.setWorldAndResolution(mc, width, height);
        refreshPage();
    }

    public void selectWorld()
    {
        ISaveFormat isaveformat = ODNBXlite.saveLoader;
        List saveList = null;
        try{
            saveList = isaveformat.getSaveList();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        Collections.sort(saveList);
        mc.displayGuiScreen(null);
        applySettings(false);
        String s = selectedWorld;
        if (s == null)
        {
            s = (new StringBuilder()).append("World").append(selectedWorld).toString();
        }
        if (mc.enableSP){
            mc.setController(((SaveFormatComparator)saveList.get(number)).getEnumGameType());
            mc.startWorldSSP(s, selectedWorld, null);
            mc.displayGuiScreen(null);
        }else{
            mc.launchIntegratedServer(s, selectedWorld, null);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 1){
            applySettings(true);
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id == 0){
            if (!newworld){
                selectWorld();
                return;
            }
            applySettings(false);
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id >= 2 && guibutton.id < 2 + genButtons.length){
            genButtons[GeneratorList.gencurrent].enabled = true;
            GeneratorList.gencurrent = guibutton.id - 2;
            guibutton.enabled = false;
            refreshPage();
        }
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3){
        if (page != null){
            page.mouseMovedOrUp2(par1, par2, par3);
        }
        super.mouseMovedOrUp(par1, par2, par3);
    }

    @Override
    protected void func_85041_a(int i, int j, int k, long l)
    {
        if (page != null){
            page.func_85041_a_2(i, j, k, l);
        }
        super.func_85041_a(i, j, k, l);
    }

    @Override
    public void handleMouseInput()
    {
        if (page != null){
            page.handleMouseInput();
        }
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        int pageTop = page.getTop();
        int pageBottom = page.getBottom();
        int pageLeft = page.getLeft();
        int pageRight = page.getRight();
        if (page.canBeScrolled()){
            drawDirtRect(pageLeft, pageRight, pageTop, pageBottom, true, page.getScrolling());
        }
        page.drawScreen(i, j, f);
        if (page.canBeScrolled()){
            drawDirtRect(pageLeft, pageRight, 0, pageTop, false, 0);
            drawDirtRect(pageLeft, pageRight, pageBottom, height, false, 0);
            drawGradientRect(pageLeft, pageTop, pageRight, pageTop + 5, 0xff000000, 0x00000000);
            drawGradientRect(pageLeft, pageBottom - 5, pageRight, pageBottom, 0x00000000, 0xff000000);
            drawRect(pageLeft - 1, pageTop, pageLeft, pageBottom, 0xff000000);
//             drawRect(pageRight, pageTop, pageRight + 1, pageBottom, 0xff000000);
            page.drawScrollbar();
        }
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("nbxlite.defaultgenerator" + (GeneratorList.gencurrent + 1) + ".desc"), width / 2 + leftmargin, height / 6 - 30, 0xa0a0a0);
        super.drawScreen(i, j, f);
    }

    private void drawDirtRect(int x1, int x2, int y1, int y2, boolean scrolling, int i)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        mc.renderEngine.bindTexture("/gui/background.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32F;
        tessellator.startDrawingQuads();
        float xOffset = (x1 % 32) / f;
        float yOffset = (y1 % 32) / f;
        tessellator.setColorOpaque_I(scrolling ? 0x202020 : 0x404040);
        tessellator.addVertexWithUV(x1, y2, 0.0D, xOffset, (float)(y2 - y1 - i) / f + yOffset);
        tessellator.addVertexWithUV(x2, y2, 0.0D, (float)(x2 - x1) / f + xOffset, (float)(y2 - y1 - i) / f + yOffset);
        tessellator.addVertexWithUV(x2, y1, 0.0D, (float)(x2 - x1) / f + xOffset, yOffset - i / f);
        tessellator.addVertexWithUV(x1, y1, 0.0D, xOffset, yOffset - i / f);
        tessellator.draw();
    }

    public static String getButtonName(){
        StringBuilder str = new StringBuilder();
        str.append(mod_OldDays.lang.get("settings"));
        str.append(": ");
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            str.append(mod_OldDays.lang.get("nbxlite.defaultgenerator" + (GeneratorList.gencurrent + 1)));
            if (GeneratorList.genplus[GeneratorList.gencurrent]==0){
                str.append(", ");
            }
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2){
            str.append(" (");
            str.append(ODNBXlite.IndevWidthX);
            str.append("x");
            str.append(ODNBXlite.IndevWidthZ);
            if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
                str.append("x");
                str.append(ODNBXlite.IndevHeight-32);
            }
            str.append("), ");
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
            str.append(mod_OldDays.lang.get(GeneratorList.typename[GeneratorList.typecurrent]));
            str.append(", ");
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            str.append(mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]));
            if (GeneratorList.gencurrent == 4 && ODNBXlite.SnowCovered){
                str.append(" (");
                str.append(StringTranslate.getInstance().translateKey("tile.snow.name"));
                str.append(")");
            }
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
            str.append(mod_OldDays.lang.get(GeneratorList.feat1name[GeneratorList.feat1current]));
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
            str.append(mod_OldDays.lang.get("nbxlite.releasefeatures"+(GeneratorList.feat2current+1)));
        }
        return str.toString();
    }

    public void applySettings(boolean def){
        if (def){
            GeneratorList.gencurrent = ODNBXlite.DefaultGenerator;
            setPage();
            setDefaultSettings();
        }else{
            page.selectSettings();
        }
        ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
        ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
    }

    public void setDefaultSettings(){
        GeneratorList.themecurrent = ODNBXlite.DefaultTheme;
        GeneratorList.feat1current = ODNBXlite.DefaultFeaturesBeta;
        GeneratorList.feat2current = ODNBXlite.DefaultFeaturesRelease;
        GeneratorList.typecurrent = ODNBXlite.DefaultIndevType;
        GeneratorList.shapecurrent = 0;
        GeneratorList.sizecurrent = 1;
        GeneratorList.xcurrent = ODNBXlite.DefaultFiniteWidth;
        GeneratorList.zcurrent = ODNBXlite.DefaultFiniteLength;
        ODNBXlite.Generator = GeneratorList.genfeatures[ODNBXlite.DefaultGenerator];
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
            ODNBXlite.MapFeatures = GeneratorList.genfeats[ODNBXlite.DefaultGenerator];
        }else if (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES){
            ODNBXlite.MapFeatures = ODNBXlite.DefaultFeaturesBeta;
        }else if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES){
            ODNBXlite.MapFeatures = ODNBXlite.DefaultFeaturesRelease;
        }
        ODNBXlite.MapTheme = ODNBXlite.DefaultTheme;
        ODNBXlite.IndevMapType = ODNBXlite.DefaultIndevType;
        ODNBXlite.IndevWidthX = 1 << GeneratorList.xcurrent + 6;
        ODNBXlite.IndevWidthZ = 1 << GeneratorList.zcurrent + 6;
        ODNBXlite.IndevHeight = ODNBXlite.DefaultFiniteDepth+32;
        ODNBXlite.GenerateNewOres = ODNBXlite.DefaultNewOres;
        if(ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && (ODNBXlite.MapTheme==ODNBXlite.THEME_NORMAL || ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS) && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_ALPHA11201){
            ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
        }
    }

    public void loadSettingsFromWorldInfo(WorldInfo par1WorldInfo){
        ODNBXlite.IndevWidthX = par1WorldInfo.indevX;
        ODNBXlite.IndevWidthZ = par1WorldInfo.indevZ;
        ODNBXlite.IndevHeight = par1WorldInfo.indevY;
        ODNBXlite.SurrWaterType = par1WorldInfo.surrwatertype;
        ODNBXlite.SurrWaterHeight = par1WorldInfo.surrwaterheight;
        ODNBXlite.SurrGroundType = par1WorldInfo.surrgroundtype;
        ODNBXlite.SurrGroundHeight = par1WorldInfo.surrgroundheight;
        ODNBXlite.CloudHeight = par1WorldInfo.cloudheight;
        ODNBXlite.SkyBrightness = par1WorldInfo.skybrightness;
        ODNBXlite.SkyColor = par1WorldInfo.skycolor;
        ODNBXlite.FogColor = par1WorldInfo.fogcolor;
        ODNBXlite.CloudColor = par1WorldInfo.cloudcolor;
        ODNBXlite.Generator = par1WorldInfo.mapGen;
        ODNBXlite.MapFeatures = par1WorldInfo.mapGenExtra;
        ODNBXlite.MapTheme = par1WorldInfo.mapTheme;
        ODNBXlite.IndevMapType = par1WorldInfo.mapType;
        ODNBXlite.SnowCovered = par1WorldInfo.snowCovered;
        ODNBXlite.GenerateNewOres = par1WorldInfo.newOres;
        if (ODNBXlite.Generator == ODNBXlite.GEN_OLDBIOMES){
            GeneratorList.gencurrent = 5;
            GeneratorList.feat1current = ODNBXlite.MapFeatures;
        }else if (ODNBXlite.Generator == ODNBXlite.GEN_NEWBIOMES){
            GeneratorList.gencurrent = 6;
            GeneratorList.feat2current = ODNBXlite.MapFeatures;
        }else{
            switch (ODNBXlite.MapFeatures){
                case ODNBXlite.FEATURES_ALPHA11201: GeneratorList.gencurrent = 4; break;
                case ODNBXlite.FEATURES_INFDEV0420: GeneratorList.gencurrent = 3; break;
                case ODNBXlite.FEATURES_INFDEV0608: GeneratorList.gencurrent = 3; break;
                case ODNBXlite.FEATURES_INFDEV0227: GeneratorList.gencurrent = 2; break;
                case ODNBXlite.FEATURES_INDEV: GeneratorList.gencurrent = 1; break;
                case ODNBXlite.FEATURES_CLASSIC: GeneratorList.gencurrent = 0; break;
            }
            GeneratorList.typecurrent = ODNBXlite.IndevMapType;
            GeneratorList.themecurrent = ODNBXlite.MapTheme;
        }
    }
}