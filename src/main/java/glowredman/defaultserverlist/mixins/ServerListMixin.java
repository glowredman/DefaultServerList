package glowredman.defaultserverlist.mixins;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import glowredman.defaultserverlist.Config;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

@Mixin(ServerList.class)
public class ServerListMixin {
	
	@Shadow
	@Final
	private List<ServerData> serverList;

    /**
     * Removes all servers from servers.dat that are already in the default list
     * @author glowredman
     */
    @Inject(at = @At("TAIL"), method = "load()V")
    private void removeDuplicateServers(CallbackInfo ci) {
    	serverList.removeIf(o -> {
            String s1 = ((ServerData) o)
                    .ip
                    .replace("http://", "")
                    .replace("https://", "")
                    .replace(":25565", "");
            for (ServerData s2 : Config.SERVERS) {
                if (s1.equals(s2.ip
                        .replace("http://", "")
                        .replace("https://", "")
                        .replace(":25565", ""))) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Save default servers
     * @author glowredman
     */
    @Inject(at = @At("TAIL"), method = "save()V")
    private void saveDefaultServerList(CallbackInfo ci) {
        if (Config.config.allowDeletions) {
            try {
                Map<String, String> newServers = new LinkedHashMap<>();
                Config.SERVERS.forEach(serverData -> newServers.put(serverData.name, serverData.ip));
                Config.config.servers = newServers;
                Config.saveConfig(Config.config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * Gets the ServerData instance stored for the given index in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@Overwrite
	public ServerData get(int pIndex) {
		if(pIndex < serverList.size()) {
			return (ServerData) serverList.get(pIndex);
		}
		return Config.SERVERS.get(pIndex - serverList.size());
	}

    /**
     * Removes the ServerData instance from the default server list if deletions are allowed
     * @reason DefaultServerList
     * @author glowredman
     */
    @Inject(at = @At("HEAD"), method = "remove(Lnet/minecraft/client/multiplayer/ServerData;)V")
    public void removeDefaultServer(ServerData server) {
        if (Config.config.allowDeletions) {
            Config.SERVERS.remove(server);
        }
    }
	
	/**
	 * Counts the number of ServerData instances in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@Overwrite
	public int size() {
		return serverList.size() + Config.SERVERS.size();
	}
	
	@Inject(method = "swap(II)V", at = @At("HEAD"), cancellable = true)
	public void swap(int pPos1, int pPos2, CallbackInfo ci) {
		if(pPos1 >= serverList.size() || pPos2 >= serverList.size()) {
			ci.cancel();
		}
	}
	
	/**
	 * Sets the ServerData instance stored for the given index in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@Overwrite
	public void replace(int pIndex, ServerData pServer) {
		if(pIndex < serverList.size()) {
			serverList.set(pIndex, pServer);
		}
	}

}
