package com.jaquadro.minecraft.storagedrawers.network;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CountUpdateMessage
{
    private int x;
    private int y;
    private int z;
    private int slot;
    private int count;

    private boolean failed;

    public CountUpdateMessage (BlockPos pos, int slot, int count) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.slot = slot;
        this.count = count;
        this.failed = false;
    }

    private CountUpdateMessage (boolean failed) {
        this.failed = failed;
    }

    public static CountUpdateMessage decode (ByteBuf buf) {
        try {
            int x = buf.readInt();
            int y = buf.readShort();
            int z = buf.readInt();
            int slot = buf.readByte();
            int count = buf.readInt();
            return new CountUpdateMessage(new BlockPos(x, y, z), slot, count);
        }
        catch (IndexOutOfBoundsException e) {
            StorageDrawers.log.error("CountUpdateMessage: Unexpected end of packet.\nMessage: " + ByteBufUtil.hexDump(buf, 0, buf.writerIndex()), e);
            return new CountUpdateMessage(true);
        }
    }

    public static void encode (CountUpdateMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.x);
        buf.writeShort(msg.y);
        buf.writeInt(msg.z);
        buf.writeByte(msg.slot);
        buf.writeInt(msg.count);
    }

    public static void handle(CountUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.runWhenOn(EnvType.CLIENT, () -> () -> handleClient(msg, ctx.get()));
    }

    @Environment(EnvType.CLIENT)
    private static void handleClient(CountUpdateMessage msg, NetworkEvent.Context ctx) {
        if (!msg.failed) {
            Level world = Minecraft.getInstance().level;
            if (world != null) {
                BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof TileEntityDrawers) {
                    ((TileEntityDrawers) tileEntity).clientUpdateCount(msg.slot, msg.count);
                }
            }
        }
        ctx.setPacketHandled(true);
    }
}
