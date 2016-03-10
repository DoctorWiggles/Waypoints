package info.jbcs.minecraft.waypoints.block;


import info.jbcs.minecraft.waypoints.Text;
import info.jbcs.minecraft.waypoints.Waypoint;
import info.jbcs.minecraft.waypoints.WaypointPlayerInfo;
import info.jbcs.minecraft.waypoints.WaypointTeleporter;
import info.jbcs.minecraft.waypoints.Waypoints;
import info.jbcs.minecraft.waypoints.network.MsgEditWaypoint;
import info.jbcs.minecraft.waypoints.network.MsgNameWaypoint;
import info.jbcs.minecraft.waypoints.network.MsgRedDust;
import info.jbcs.minecraft.waypoints.network.Packets;
import info.jbcs.minecraft.waypoints.util.BSHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockWaypoint extends Block {
    
    public BlockWaypoint(String unlocal) {
        super(Material.rock);
        //setBlockName("waypoint");
        this.setUnlocalizedName(unlocal);    
        this.setCreativeTab(CreativeTabs.tabTransport);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        setLightOpacity(255);
        this.setLightOpacity(0);

        this.setResistance(10F).setStepSound(Blocks.stone.stepSound).setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.BASE));

    }


    public boolean isValidWaypoint(World world, int ox, int oy, int oz) {
        if (checkSize(world, ox, oy, oz) == 1){
        	//Text.out("Size_Bad");
        	return false;}
        if (BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz)) != this) {
        	//Text.out("Bad_Block");
        	return false;} //checkSize do not check this one
        //BSHelper.getMetafromState(world, new BlockPos(
        if (BSHelper.getMetafromState(world, new BlockPos(ox, oy, oz)) == 0) {
        	//Text.out("Bad_Meta @"+new BlockPos(ox, oy, oz)+" #"+
        //BSHelper.getMetafromState(world, new BlockPos(ox, oy, oz)));
        	return false;} //check if activated
        //here we can check metadata it _should_ be always true so let omit this until some bug happen

        return true;
    }

    @Override
    //public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMeta)
    public void breakBlock(World world, BlockPos pos, IBlockState state){    	
        super.breakBlock(world, pos, state);
        int x= pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ(); 
    	
        int size = checkSize(world, x, y, z);
        while (BSHelper.getBlockfromState(world, new BlockPos(x - 1, y, z)) == this) x--;
        while (BSHelper.getBlockfromState(world, new BlockPos(x, y, z - 1)) == this) z--;

        final Waypoint wp = Waypoint.getWaypoint(x, y, z, world.provider.getDimensionId());


        for (int px = 0; px < size; px++) {
            for (int pz = 0; pz < size; pz++) {
                BSHelper.setStatefromMeta(world, new BlockPos(x + px, y, z + pz), 0, 3);
            }
        }

        if (wp == null) return;
        Waypoint.removeWaypoint(wp);
    }

    static public boolean isEntityOnWaypoint(World world, int x, int y, int z, Entity entity) {
        int size = checkSize(world, x, y, z);
        return entity.posX >= x && entity.posX <= x + size && entity.posZ >= z && entity.posZ <= z + size;
    }


    @Override
    //public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int par6, float par7, float par8, float par9) {
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
    	int x= pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ();   
    	
    	Text.out(world.getBlockState(pos) + "" + pos, EnumChatFormatting.BLUE);
    	
    	if (world.isRemote) return true;

    	String Meta = "Meta: " + BSHelper.getMetafromState(world,pos);
		Text.out(player, Meta, EnumChatFormatting.GREEN);
	
    	
        while (BSHelper.getBlockfromState(world, new BlockPos(x - 1, y, z)) == this) x--;
        while (BSHelper.getBlockfromState(world, new BlockPos(x, y, z - 1)) == this) z--;
        //boolean isOP = MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
        boolean isOP = MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile());
       // if (Waypoints.allowActivation || isOP)
        	activateStructure(world, x, y, z);
		Text.out(player, "HERE", EnumChatFormatting.GRAY);

		
        if (!isValidWaypoint(world, x, y, z)) {
        	Text.out(player, "INVALID", EnumChatFormatting.RED);
        	return true;}
		 
        final Waypoint src = Waypoint.getWaypoint(x, y, z, player.dimension);
        if (src == null) return true;

        if (!isEntityOnWaypoint(world, x, y, z, player)) return true;
        if (src.name.isEmpty()) {
            MsgNameWaypoint msg = new MsgNameWaypoint(src, "Waypoint #" + src.id);
            Waypoints.instance.messagePipeline.sendTo(msg, (EntityPlayerMP) player);

        } else if (player.isSneaking() && (Waypoints.allowActivation || isOP)) {
            //Add waypoints
            ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
            final WaypointPlayerInfo info = WaypointPlayerInfo.get(player.getDisplayName().toString());
            if (info == null) return false;
            info.addWaypoint(src.id);

            for (Waypoint w : Waypoint.existingWaypoints)
                if (info.discoveredWaypoints.containsKey(w.id))
                    waypoints.add(w);

            MsgEditWaypoint msg = new MsgEditWaypoint(src.id, src.name, src.linked_id, waypoints);
            Waypoints.instance.messagePipeline.sendTo(msg, (EntityPlayerMP) player);
        } else {
            try {
                Packets.sendWaypointsToPlayer((EntityPlayerMP) player, src.id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    static public int checkSize(World world, int ox, int oy, int oz) {
    	
    	//Text.out( "checkSize", EnumChatFormatting.DARK_RED);
    	
        int x = ox;
        int z = oz;
        while (BSHelper.getBlockfromState(world, new BlockPos(ox - 1, oy, oz)) == Waypoints.blockWaypoint) ox--;
        while (BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz - 1)) == Waypoints.blockWaypoint) oz--;
        outerloop:
        for (int px = 0; px < 3; px++) {
            for (int pz = 0; pz < 3; pz++) {
                if ((BSHelper.getBlockfromState(world, new BlockPos(ox + px, oy, oz + pz)) == Waypoints.blockWaypoint || (ox + px == x && oz + pz == z)) && px == 2 && pz == 2) {
                    return 3;
                }
                if (BSHelper.getBlockfromState(world, new BlockPos(ox + px, oy, oz + pz)) != Waypoints.blockWaypoint && !((ox + px) == x && (oz + pz) == z)) {
                    break outerloop;
                }
            }
        }
        outerloop:
        for (int px = 0; px < 2; px++) {
            for (int pz = 0; pz < 2; pz++) {
                if ((BSHelper.getBlockfromState(world, new BlockPos(ox + px, oy, oz + pz)) == Waypoints.blockWaypoint || (ox + px == x && oz + pz == z)) && px == 1 && pz == 1) {
                    return 2;
                }
                if (BSHelper.getBlockfromState(world, new BlockPos(ox + px, oy, oz + pz)) != Waypoints.blockWaypoint && !((ox + px) == x && (oz + pz) == z)) {
                    break outerloop;
                }
            }
        }
        return 1;
    }

    public void activateStructure(World world, int ox, int oy, int oz) {
    	int a= 0;
    	//if( a==0)return;
        if (checkSize(world, ox, oy, oz) == 3 
        		//&& BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz)) == this
        		) {
            
        	BSHelper.setStatefromMeta(world, new BlockPos(ox + 0, oy, oz + 0), 1, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 1, oy, oz + 0), 2, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 2, oy, oz + 0), 3, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 2, oy, oz + 1), 4, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 2, oy, oz + 2), 5, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 1, oy, oz + 2), 6, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 0, oy, oz + 2), 7, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 0, oy, oz + 1), 8, 3);
            BSHelper.setStatefromMeta(world, new BlockPos(ox + 1, oy, oz + 1), 9, 3);
        }
      
        ox--;
        oz--;
        for (int xp = 0; xp < 4; xp++) {
            for (int zp = 0; zp < 4; zp++) {
                if ((xp == 0 | xp == 3 | zp == 0 | zp == 3) && xp != zp && !(xp == 0 && zp == 3) && !(xp == 3 && zp == 0)) {
                    if (BSHelper.getBlockfromState(world, new BlockPos(ox + xp, oy, oz + zp)) == this) return;
                } else if (!(xp == 0 | xp == 3 | zp == 0 | zp == 3)) {
                    if (BSHelper.getBlockfromState(world, new BlockPos(ox + xp, oy, oz + zp)) != this) return;
                }
            }
        }
        
        //BSHelper.setStatefromMeta(world, new BlockPos(
        //BSHelper.setStatefromMeta(world, new BlockPos(ox + 1, oy, oz + 1, 1, 3);
        BSHelper.setStatefromMeta(world, new BlockPos(ox + 1, oy, oz + 1), 1, 3);
        BSHelper.setStatefromMeta(world, new BlockPos(ox + 2, oy, oz + 1), 3, 3);
        BSHelper.setStatefromMeta(world, new BlockPos(ox + 2, oy, oz + 2), 5, 3);
        BSHelper.setStatefromMeta(world, new BlockPos(ox + 1, oy, oz + 2), 7, 3);
       
    }

   

    public boolean isPowered(World world, int ox, int oy, int oz) {
    	
    	int a= 0;
    	if( a==0)return true;
    	
        while (BSHelper.getBlockfromState(world, new BlockPos(ox - 1, oy, oz)) == Waypoints.blockWaypoint) ox--;
        while (BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz - 1)) == Waypoints.blockWaypoint) oz--;

        int size = checkSize(world, ox, oy, oz);
        for (int xp = 0; xp < size; xp++) {
            for (int zp = 0; zp < size; zp++) {
                //if (world.isBlockIndirectlyGettingPowered(ox + xp, oy, oz + zp)) return true;
                if (world.isBlockIndirectlyGettingPowered(new BlockPos(ox + xp, oy, oz + zp)) > 0) return true;
               // if (world.isBlockPowered(new BlockPos(ox + xp, oy, oz + zp))) return true;
            }
        }
        return false;
    }

   // public void onEntityCollidedWithBlock(World world, int ox, int oy, int oz, Entity entity) {
    @Override
     public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity) {    	
    	int ox= pos.getX();
    	int oy = pos.getY();
    	int oz = pos.getZ();   
    	//
    	int a= 0;
    	//if( a==0)return ;
    	
    	//if (!world.isRemote) {Text.out("collision: "+ entity);}
    	
        while (BSHelper.getBlockfromState(world, new BlockPos(ox - 1, oy, oz)) == Waypoints.blockWaypoint) ox--;
        while (BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz - 1)) == Waypoints.blockWaypoint) oz--;
        Random rand = new Random();
        if (!world.isRemote) {
            Waypoint src = Waypoint.getWaypoint(ox, oy, oz, entity.dimension);
            if (BlockWaypoint.isEntityOnWaypoint(world, ox, oy, oz, entity)) {
            	//
            	//Text.out("On Point",EnumChatFormatting.GREEN);
                Waypoint w = Waypoint.getWaypoint(src.linked_id - 1);
                if (w == null){ 
                	//Text.out("null", EnumChatFormatting.RED);
                	return;}
                ByteBuf buffer1 = Unpooled.buffer();
                buffer1.writeInt(3);
                buffer1.writeDouble(entity.posX);
                buffer1.writeDouble(entity.posY);
                buffer1.writeDouble(entity.posZ);
                //FMLProxyPacket packet1 = new FMLProxyPacket(buffer1.copy(), "Waypoints");
                //FMLProxyPacket packet1 = new FMLProxyPacket((PacketBuffer) buffer1.copy(), "Waypoints");
              
                
                int size = BlockWaypoint.checkSize(entity.worldObj, w.x, w.y, w.z);
                boolean teleported = false;
                if (entity instanceof EntityPlayer || entity instanceof EntityLiving) {
                    if (entity.timeUntilPortal > 0 && entity.timeUntilPortal <= entity.getPortalCooldown()) {
                        entity.timeUntilPortal = entity.getPortalCooldown();
                    } else if (entity.timeUntilPortal > entity.getPortalCooldown() && entity.timeUntilPortal < 2 * entity.getPortalCooldown()) {
                        MinecraftServer minecraftServer = MinecraftServer.getServer();
                        entity.timeUntilPortal = entity.getPortalCooldown();
                        teleported = new WaypointTeleporter(minecraftServer.worldServerForDimension(world.provider.getDimensionId())).teleport(entity, world, w);
                    } else if (src.powered && entity instanceof EntityPlayer && entity.timeUntilPortal == 0) {
                        entity.timeUntilPortal = 2 * entity.getPortalCooldown() + 20;
                    } else if (src.powered && entity.timeUntilPortal == 0) {
                        entity.timeUntilPortal = 2 * entity.getPortalCooldown();
                    }
                }
                if (teleported) {
                    MsgRedDust msg1 = new MsgRedDust(src.dimension, entity.posX, entity.posY, entity.posZ);
                    MsgRedDust msg2 = new MsgRedDust(w.dimension, w.x + size / 2.0, w.y + 0.5, w.z + size / 2.0);
                    Waypoints.instance.messagePipeline.sendToAllAround(msg1, new NetworkRegistry.TargetPoint(src.dimension, entity.posX, entity.posY, entity.posZ, 25));
                    Waypoints.instance.messagePipeline.sendToAllAround(msg2, new NetworkRegistry.TargetPoint(w.dimension, w.x + size / 2.0, w.y, w.z + size / 2.0, 25));
                }
            }
        }
    }

    //public void onNeighborBlockChange(World world, int ox, int oy, int oz, Block block) {
     @Override
    public void onNeighborBlockChange(World world, BlockPos pos,IBlockState state, Block block) {
    	int ox= pos.getX();
    	int oy = pos.getY();
    	int oz = pos.getZ();  
    	
    	
        while (BSHelper.getBlockfromState(world, new BlockPos(ox - 1, oy, oz)) == Waypoints.blockWaypoint) ox--;
        while (BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz - 1)) == Waypoints.blockWaypoint) oz--;
        try{
        if (isPowered(world, ox, oy, oz)) {
            Waypoint waypoint = Waypoint.getWaypoint(ox, oy, oz, world.provider.getDimensionId());
            waypoint.powered = true;
            waypoint.changed = true;
        } else {
            Waypoint waypoint = Waypoint.getWaypoint(ox, oy, oz, world.provider.getDimensionId());
            waypoint.powered = false;
            waypoint.changed = true;
        }
        }
        catch(Exception e){
        	System.out.println(e);
        	System.out.println("Failed to Generate Waypoint @ "+pos);
        }
    }
    
    
    
    
    //====================================================================================//
    /** ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ **/
    //====================================================================================//

    
    
    
    //RENDERING APERATUS FOR BLOCK
    
    
    
    @Override
    public boolean isOpaqueCube() { return false;}
    @Override
    public boolean isFullCube()  { return false; }

    @Override
    public boolean isNormalCube() {
        return false;
    }
    
    
    @Override
    //public void randomDisplayTick(World world, int ox, int oy, int oz, Random rand)
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand){
    	
    	int ox= pos.getX();
    	int oy = pos.getY();
    	int oz = pos.getZ(); 
        BlockWaypoint blockWaypoint = (BlockWaypoint) BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz));
        if (!blockWaypoint.isValidWaypoint(world, ox, oy, oz)){return;}
        float fx = (float) ox + 0.5F;
        float fy = (float) oy + 1.0F + rand.nextFloat() * 6.0F / 16.0F;
        float fz = (float) oz + 0.5F;
        while (BSHelper.getBlockfromState(world, new BlockPos(ox - 1, oy, oz)) == Waypoints.blockWaypoint) ox--;
        while (BSHelper.getBlockfromState(world, new BlockPos(ox, oy, oz - 1)) == Waypoints.blockWaypoint) oz--;
        Waypoint src = Waypoint.getWaypoint(ox, oy, oz, world.provider.getDimensionId());
        if (src.powered) {
            Waypoint w = Waypoint.getWaypoint(src.linked_id - 1);
            if (w == null) {return;}
            world.spawnParticle(EnumParticleTypes.PORTAL, (double) fx + rand.nextFloat() % 1 - 0.5, (double) fy, (double) fz + rand.nextFloat() % 1 - 0.5, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.PORTAL, (double) fx, (double) fy, (double) fz, 0.0D, 0.0D, 0.0D);
        }
    }
	
    
    
    
    @Override
	 public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	/*
    	for (int i = 0; i < 10; ++i)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
    	*/
	     list.add(new ItemStack(itemIn, 1, 0)); //Meta 0
	 }
    
    @Override
	 protected BlockState createBlockState() {
	     return new BlockState(this, new IProperty[] { TYPE });
	 }
    
	   //public int getMetaFromState(IBlockState state) { return 0; }
	   
	   public int getMetaFromState(IBlockState state) {
		   int meta = 0;
		   
		   return (int)((EnumType) state.getValue(TYPE)).getID();
	   }
	   
	   
	   //public IBlockState getStateFromMeta(int meta) { return this.getDefaultState(); }
	   
	   public IBlockState getStateFromMeta(int meta) { 
		   
		   return this.getDefaultState().withProperty(TYPE, 
				   EnumType.byID(meta));
	   }
	   
    
    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumType.class);
    
    public enum EnumType implements IStringSerializable {
	    BASE(0, "base"),
	    S1(1,"1"),
	    S2(2,"2"),
	    S3(3,"3"),
	    S4(4,"4"),
	    S5(5,"5"),
	    S6(6,"6"),
	    S7(7,"7"),
	    S8(8,"8"),
	    s9(9,"9");	    

	    private int ID;
	    private String name;
	    
	    private EnumType(int ID, String name) {
	        this.ID = ID;
	        this.name = name;
	    }
	    
	    @Override
	    public String getName() {
	        return name;
	    }

	    public int getID() {
	        return ID;
	    }
	    
	    public static EnumType byID(int id){
	    	if(id < 0 || id > values().length){
	    		id = 0;
	    	}
	    	return values()[id];
	    	
	    }
	    @Override
	    public String toString() {
	        return getName();
	    }	    
	}    
}
