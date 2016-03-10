package info.jbcs.minecraft.waypoints.generation;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WayPoint_Generation implements IWorldGenerator {
	
	int chance = 1; 
	
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		
		switch (world.provider.getDimensionId()) {
		case -1:
			generateNether(world, random, chunkX * 16, chunkZ * 16);
		case 0:
			generateSurface(world, random, chunkX * 16, chunkZ * 16);
		}
	}

	private void generateSurface(World world, Random random, int blockX,
			int blockZ) {
		/**
		int Xcoord = blockX + random.nextInt(16);
		int Ycoord = random.nextInt(60);
		int Zcoord = blockZ + random.nextInt(16);

		(new WorldGenMinable(Tutorial.oreblock.blockID, 10)).generate(world,
				random, Xcoord, Ycoord, Zcoord);
		**/
		int genrate = random.nextInt(4);
		
		if (genrate == 0){
			
		
		
		int Xcoord1 = blockX + random.nextInt(16);
		//int Ycoord1 = random.nextInt(80);
		int Ycoord1 = random.nextInt(214);
		int Zcoord1 = blockZ + random.nextInt(16);
		BlockPos pos = new BlockPos(Xcoord1,Ycoord1,Zcoord1 );

		
		(new Generate_WP()).generate(world, random, pos);
		
		}

	}

	private void generateNether(World world, Random random, int blockX,
			int blockZ) {
		/**
		int Xcoord = blockX + random.nextInt(16);
		int Ycoord = random.nextInt(60);
		int Zcoord = blockZ + random.nextInt(16);

		(new WorldGenMinableNether(Tutorial.oreblock.blockID, 1, 10)).generate(
				world, random, Xcoord, Ycoord, Zcoord);
				**/
	}

}