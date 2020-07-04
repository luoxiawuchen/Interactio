package dev.maxneedssnacks.interactio.proxy;

import dev.maxneedssnacks.interactio.command.CommandItemInfo;
import dev.maxneedssnacks.interactio.command.CommandRegistryDump;
import dev.maxneedssnacks.interactio.recipe.util.InWorldRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SharedConstants;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import javax.annotation.Nullable;

public abstract class ModProxy implements IProxy {

    private MinecraftServer server = null;

    public ModProxy() {

        // init methods
        InWorldRecipeType.registerTypes();

        // Forge Event Bus events
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);

    }

    private void serverStarting(FMLServerStartingEvent event) {
        this.server = event.getServer();
        CommandItemInfo.register(event.getCommandDispatcher());
        CommandRegistryDump.register(event.getCommandDispatcher());
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return server;
    }

    @Override
    public String getVersion() {
        return SharedConstants.getVersion().getName();
    }

    @Override
    public RecipeManager getRecipeManager() {
        return getWorld() == null ? null : getWorld().getRecipeManager();
    }

    public static class Client extends ModProxy {
        public Client() {
        }

        @Nullable
        @Override
        public ClientWorld getWorld() {
            return Minecraft.getInstance().world;
        }
    }

    public static class Server extends ModProxy {
        public Server() {
        }

        @Nullable
        @Override
        public ServerWorld getWorld() {
            return getServer() == null ? null : getServer().getWorld(World.field_234918_g_);
        }
    }
}
