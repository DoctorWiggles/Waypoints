package waypoints;


import waypoints.block.BlockWaypoint;
import waypoints.config.Config;
import waypoints.generation.WayPoint_Generation;
import waypoints.item.Debug_States;
import waypoints.item.ItemWaypoint;
import waypoints.item.Item_spawn_waypoint;
import waypoints.network.MessagePipeline;
import waypoints.proxy.Proxy;
import waypoints.render.Render_Registry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Mod(modid = Waypoints.MODID, name = Waypoints.MODNAME, version = Waypoints.VERSION, guiFactory = Waypoints.guiFactory)
public class Waypoints {
	
	
	public static final String MODID = "Waypoints";
	public static final String MODNAME = "Waypoints";
	public static final String VERSION = "1.1";	
	public static final String guiFactory = "waypoints.config.Config_GuiFactory";
	
	
	
    //TODO
	//@Deprecated
	//static Configuration config;
	/**
    public static boolean compactView;
    public static String default_recipe = "3x2,minecraft:stone:1,minecraft:stone:1,minecraft:stone:1,minecraft:stone:1,minecraft:ender_pearl:1,minecraft:stone:1";
    public static String recipe;
    public static boolean craftable;
    public static boolean allowActivation;
    **/
    public MessagePipeline messagePipeline;

    public static int maxSize = 3;

    public static Item debug_states_tool;
    public static Item item_spawn_waypoint;
    public static BlockWaypoint blockWaypoint;

    @Mod.Instance("Waypoints")
    public static Waypoints instance;

    public static CreativeTabs tabWaypoints;
    private File loadedWorldDir;

    @SidedProxy(clientSide = "waypoints.proxy.ProxyClient", serverSide = "waypoints.proxy.Proxy")
    public static Proxy proxy;
    
    public Waypoints() {
        messagePipeline = new MessagePipeline();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	
    	Config.preinit(event);
    	
        //config = new Configuration(event.getSuggestedConfigurationFile());
        //config.load();
        
        GameRegistry.registerBlock(blockWaypoint =new BlockWaypoint("waypoint"),ItemWaypoint.class, "waypoint");
        //GameRegistry.registerBlock(blockWaypoint =new BlockWaypoint("waypoint"), "waypoint");
        
        GameRegistry.registerItem(debug_states_tool = new Debug_States( "debug_states_item"),"debug_states_item");
        GameRegistry.registerItem(item_spawn_waypoint = new Item_spawn_waypoint( "item_spawn_waypoint"),"item_spawn_waypoint");
        
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
       
        tabWaypoints = CreativeTabs.tabDecorations;

        if (Config.craftable)Config.addRecipe(new ItemStack(blockWaypoint, 1), Config.recipe);
        
        //blockWaypoint = new BlockWaypoint();
        //GameRegistry.registerBlock(blockWaypoint, ItemWaypoint.class, "waypoint");
        
        MinecraftForge.EVENT_BUS.register(this);
        proxy.registerPackets(messagePipeline);
        
        //
        GameRegistry.registerWorldGenerator(new WayPoint_Generation(), 0);
        
        //TODO configuration
        MinecraftForge.EVENT_BUS.register(instance);
    }
    
	
	@SubscribeEvent
	  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
	    if(eventArgs.modID.equals(Waypoints.MODID))
	      Config.syncConfig();
	  }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    public File getWorldDir(World world) {
        ISaveHandler handler = world.getSaveHandler();
        if (!(handler instanceof SaveHandler)) return null;
        return ((SaveHandler) handler).getWorldDirectory();
    }

    @EventHandler
    public void onServerStop(FMLServerStoppingEvent evt) {
        File file = loadedWorldDir;
        if (file == null) return;

        try {
            Waypoint.write(new File(file, "waypoints.dat"));
            WaypointPlayerInfo.writeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onLoadingWorld(FMLServerStartingEvent evt) {
        File file = getWorldDir(evt.getServer().getEntityWorld());
        if (file == null) return;
        loadedWorldDir = file;

        WaypointPlayerInfo.location = new File(file, "waypoints-discovery");
        File waypointsLocation = new File(file, "waypoints.dat");

        if (!waypointsLocation.exists()) return;

        try {
            Waypoint.read(waypointsLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	
}



