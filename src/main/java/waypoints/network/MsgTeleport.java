package waypoints.network;


import waypoints.Waypoint;
import waypoints.Waypoints;
import waypoints.block.BlockWaypoint;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgTeleport extends Message {
    private static Waypoint src, dest;

    public MsgTeleport() {
    }

    public MsgTeleport(Waypoint src, Waypoint dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        src = Waypoint.getWaypoint(buf.readInt());
        dest = Waypoint.getWaypoint(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(src.id);
        buf.writeInt(dest.id);
    }

    public static class Handler implements IMessageHandler<MsgTeleport, IMessage> {

        @Override
        public IMessage onMessage(MsgTeleport message, MessageContext ctx) {
            if (src == null || dest == null) return null;

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (!BlockWaypoint.isEntityOnWaypoint(player.worldObj, src.x, src.y, src.z, player)) return null;
            player.mountEntity((Entity) null);

            MsgRedDust msg1 = new MsgRedDust(player.dimension, player.posX, player.posY, player.posZ);

            if (player.dimension != dest.dimension) player.travelToDimension(dest.dimension);
            int size = BlockWaypoint.checkSize(player.worldObj, dest.x, dest.y, dest.z);
            player.setLocationAndAngles(dest.x + size / 2.0, dest.y + 0.5, dest.z + size / 2.0, player.rotationYaw, 0);
            player.setPositionAndUpdate(dest.x + size / 2.0, dest.y + 0.5, dest.z + size / 2.0);

            MsgRedDust msg2 = new MsgRedDust(dest.dimension, dest.x + size / 2.0, dest.y + 0.5, dest.z + size / 2.0);
            Waypoints.instance.messagePipeline.sendToAllAround(msg1, new NetworkRegistry.TargetPoint(msg1.getDimension(), msg1.getX(), msg1.getY(), msg1.getZ(), 25));
            Waypoints.instance.messagePipeline.sendToAllAround(msg2, new NetworkRegistry.TargetPoint(msg2.getDimension(), msg2.getX(), msg2.getY(), msg2.getZ(), 25));

            return null;
        }
    }
}