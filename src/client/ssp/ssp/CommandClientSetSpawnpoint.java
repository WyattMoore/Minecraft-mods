package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientSetSpawnpoint extends CommandSetSpawnpoint
{
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        EntityPlayer entityplayer = getCommandSenderAsPlayer2(par1ICommandSender);

        if (par2ArrayOfStr.length == 4)
        {
            if (entityplayer.worldObj != null)
            {
                int i = 1;
                int j = 0x1c9c380;
                int k = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                int l = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], 0, 256);
                int i1 = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                entityplayer.setSpawnChunk(new ChunkCoordinates(k, l, i1), true);
                notifyAdmins(par1ICommandSender, "commands.spawnpoint.success", new Object[]
                        {
                            entityplayer.getEntityName(), Integer.valueOf(k), Integer.valueOf(l), Integer.valueOf(i1)
                        });
            }
        }
        else if (par2ArrayOfStr.length <= 1)
        {
            ChunkCoordinates chunkcoordinates = entityplayer.getPlayerCoordinates();
            entityplayer.setSpawnChunk(chunkcoordinates, true);
            notifyAdmins(par1ICommandSender, "commands.spawnpoint.success", new Object[]
                    {
                        entityplayer.getEntityName(), Integer.valueOf(chunkcoordinates.posX), Integer.valueOf(chunkcoordinates.posY), Integer.valueOf(chunkcoordinates.posZ)
                    });
        }
        else
        {
            throw new WrongUsageException("commands.spawnpoint.usage", new Object[0]);
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayer getCommandSenderAsPlayer2(ICommandSender par0ICommandSender)
    {
        if (par0ICommandSender instanceof EntityPlayer)
        {
            return (EntityPlayer)par0ICommandSender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }
}
