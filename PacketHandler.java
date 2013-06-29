package vovapolu.modularchests;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import scala.collection.generic.BitOperations.Int;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler {

	static private long packetTime = 0;

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {		

		DataInputStream istream = new DataInputStream(new ByteArrayInputStream(packet.data));

		if (packet.channel.equals("TE")) {
			try {
				int x, y, z;
				NBTTagCompound tag;
				x = istream.readInt();
				y = istream.readInt();
				z = istream.readInt();
				tag = Packet.readNBTTagCompound(istream);

				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					EntityPlayerMP playerMP = (EntityPlayerMP) player;
					TileEntity te = playerMP.worldObj.getBlockTileEntity(x, y,z);
					te.readFromNBT(tag);
					playerMP.worldObj.setBlockTileEntity(x, y, z, te);
				} else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
					EntityPlayer playerSP = (EntityPlayer) player;
					TileEntity te = playerSP.worldObj.getBlockTileEntity(x, y,z);
					te.readFromNBT(tag);
					playerSP.worldObj.setBlockTileEntity(x, y, z, te);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		if (packet.channel.equals("SB")) {
			try {
				int x, y, z;
				int shiftVal;
				x = istream.readInt();
				y = istream.readInt();
				z = istream.readInt();
				shiftVal = istream.readInt();

				TileEntity te = null;

				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					EntityPlayerMP playerMP = (EntityPlayerMP) player;
					te = playerMP.worldObj.getBlockTileEntity(x, y, z);

				} else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
					EntityPlayer playerSP = (EntityPlayer) player;
					te = playerSP.worldObj.getBlockTileEntity(x, y, z);
				}

				if (te instanceof ModularChestTileEntity) {
					ModularChestTileEntity mte = (ModularChestTileEntity) te;
					//mte.shiftItems(shiftVal);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("packet handled");
		}

		if (packet.channel.equals("UTE")) {
			try {
				int x, y, z;
				boolean update;
				x = istream.readInt();
				y = istream.readInt();
				z = istream.readInt();
				update = istream.readBoolean();

				TileEntity te = null;

				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					EntityPlayerMP playerMP = (EntityPlayerMP) player;
					te = playerMP.worldObj.getBlockTileEntity(x, y, z);

				} else if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
					EntityPlayer playerSP = (EntityPlayer) player;
					te = playerSP.worldObj.getBlockTileEntity(x, y, z);
				}

				if (te instanceof ModularChestTileEntity) {
					ModularChestTileEntity mte = (ModularChestTileEntity) te;

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Packet250CustomPayload createTEPacket(TileEntity tileEntity) {
		Packet250CustomPayload packet = new Packet250CustomPayload();

		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream ostream = new DataOutputStream(array);

		try {			
			int x = tileEntity.xCoord, y = tileEntity.yCoord, z = tileEntity.zCoord;
			NBTTagCompound tag = new NBTTagCompound();
			tileEntity.writeToNBT(tag);
			byte[] abyte = CompressedStreamTools.compress(tag);
			
			ostream.writeInt(x);
			ostream.writeInt(y);
			ostream.writeInt(z);
			ostream.writeShort((short) abyte.length);
			ostream.write(abyte);
			ostream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		packet.channel = "TE";
		packet.data = array.toByteArray();
		packet.length = array.size();
		
		return packet;
	}

	public static Packet250CustomPayload createSBPacket(
			ModularChestTileEntity tileEntity) {
		Packet250CustomPayload packet = new Packet250CustomPayload();

		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream ostream = new DataOutputStream(array);

		try {
			int x = tileEntity.xCoord, y = tileEntity.yCoord, z = tileEntity.zCoord;
			//XXX int shiftVal = tileEntity.getShift();

			ostream.writeInt(x);
			ostream.writeInt(y);
			ostream.writeInt(z);
			//XXX ostream.writeInt(shiftVal);
			ostream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		packet.data = array.toByteArray();
		packet.length = array.size();
		packet.channel = "SB";

		System.out.println("Packet created.");
		packetTime = System.currentTimeMillis();
		return packet;
	}

	public static Packet250CustomPayload createUTEPacket(
			ModularChestTileEntity tileEntity, boolean value) {
		Packet250CustomPayload packet = new Packet250CustomPayload();

		ByteArrayOutputStream array = new ByteArrayOutputStream();
		DataOutputStream ostream = new DataOutputStream(array);

		try {
			int x = tileEntity.xCoord, y = tileEntity.yCoord, z = tileEntity.zCoord;
			//XXX int shiftVal = tileEntity.getShift();
			
			ostream.writeInt(x);
			ostream.writeInt(y);
			ostream.writeInt(z);
			ostream.writeBoolean(value);
			ostream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		packet.data = array.toByteArray();
		packet.length = array.size();
		packet.channel = "UTE";
		return packet;
	}

}
