package waypoints.config;

import waypoints.Waypoints;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class Config_Gui extends GuiConfig {
	
	
  public Config_Gui(GuiScreen parent) {
    super(parent,
        //new ConfigElement(TestMod.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
        //"TestMod", false, false, GuiConfig.getAbridgedConfigPath(TestMod.configFile.toString()));
    
    	new ConfigElement(Config.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
    	Waypoints.MODID, false, false, GuiConfig.getAbridgedConfigPath(Config.configFile.toString()));
  }
}
