package info.jbcs.minecraft.waypoints.network;


import info.jbcs.minecraft.waypoints.Waypoint;
import info.jbcs.minecraft.waypoints.WaypointPlayerInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgDelete extends Message {
    private Waypoint w;

    public MsgDelete() {
    }

    public MsgDelete(Waypoint w) {
        this.w = w;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        w = Waypoint.getWaypoint(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(w.id);
    }

    public static class Handler implements IMessageHandler<MsgDelete, IMessage> {

        @Override
        public IMessage onMessage(MsgDelete message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (message.w == null) return null;

            //WaypointPlayerInfo info = WaypointPlayerInfo.get(player.getDisplayName());
            WaypointPlayerInfo info = WaypointPlayerInfo.get((player.getDisplayName().toString()));
            if (info == null) return null;
            info.removeWaypoint(message.w.id);

            return null;
        }
    }
}