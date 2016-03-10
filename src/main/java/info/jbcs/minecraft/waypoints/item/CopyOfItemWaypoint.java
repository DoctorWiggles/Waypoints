package info.jbcs.minecraft.waypoints.item;

import info.jbcs.minecraft.waypoints.Waypoints;
import info.jbcs.minecraft.waypoints.util.BSHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class CopyOfItemWaypoint extends ItemBlock {
    Block blockWaypoint;

    public CopyOfItemWaypoint(Block block) {
        super(block);
        blockWaypoint = block;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hx, float hy, float hz) {
    	int x= pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ();    	
    	//Block block1 = world.getBlock(pos);
    	Block block1 = BSHelper.getBlockfromState(world, pos);

        if (block1 != Blocks.vine && block1 != Blocks.tallgrass && block1 != Blocks.deadbush) { //&& (block1 == null || block1.isReplaceable(world, x, y, z))) {
            switch (side) {
                case DOWN:
                    y--;
                    break;
                case UP:
                    y++;
                    break;
                case NORTH:
                    z--;
                    break;
                case SOUTH:
                    z++;
                    break;
                case WEST:
                    x--;
                    break;
                case EAST:
                    x++;
                    break;
            }
        }
        if (stack.stackSize == 0) return false;
        if (!player.canPlayerEdit(pos, side, stack)) return false;
        if (!world.canBlockBePlaced(blockWaypoint, pos, false, side, player, stack)) return false;

        int north = countWaypointBlocks(world, x, y, z, 0, 0, 1, Waypoints.maxSize-1);
        int south = countWaypointBlocks(world, x, y, z, 0, 0, -1, Waypoints.maxSize-1);
        int east = countWaypointBlocks(world, x, y, z, 1, 0, 0, Waypoints.maxSize-1);
        int west = countWaypointBlocks(world, x, y, z, -1, 0, 0, Waypoints.maxSize-1);

        if(north==-1 || south==-1 || east==-1 || west==-1) return false;
        if(north+south+1>Waypoints.maxSize || east+west+1>Waypoints.maxSize) return false;
        if(isActivated(world,x+1,y,z) || isActivated(world,x-1,y,z) ||
                isActivated(world,x,y,z+1) || isActivated(world,x,y,z-1)) return false;

        Block block = blockWaypoint;
        int oldMeta = this.getMetadata(stack.getItemDamage());
        IBlockState meta = blockWaypoint.onBlockPlaced(world, pos, side, hx, hy, hz, oldMeta, player);

        int ox = x, oz = z;
        while (BSHelper.getBlockfromState(world,new BlockPos(ox - 1, y, oz)) == blockWaypoint) ox--;
        while (BSHelper.getBlockfromState(world,new BlockPos(ox, y, oz - 1)) == blockWaypoint) oz--;

        if (placeBlockAt(stack, player, world, pos, side, hx, hy, hz, meta)) {
            world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getFrequency() * 0.8F);
            --stack.stackSize;
        }

        return true;
    }
    int countWaypointBlocks(World world, int x, int y, int z, int px, int py, int pz, int maxSize){
        for(int c=0; c<maxSize+1; c++){
            if(BSHelper.getBlockfromState(world, new BlockPos(x+(c+1)*px, y+(c+1)*py, z+(c+1)*pz))!=blockWaypoint) return c;
        }
        return -1;
    }
    boolean isActivated(World world, int x, int y, int z){
    	BlockPos pos = new BlockPos(x,y,z);
        return BSHelper.getMetafromState(world,pos) != 0 && BSHelper.getBlockfromState(world,pos)==blockWaypoint;
    }

}
