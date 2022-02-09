package glowredman.defaultserverlist.mixins;

import java.util.List;

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
	private List<ServerData> servers;
	
	/**
	 * Gets the ServerData instance stored for the given index in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@Overwrite
	public ServerData getServerData(int index) {
		if(index < servers.size()) {
			return (ServerData) servers.get(index);
		}
		return Config.SERVERS.get(index - servers.size());
	}
	
	/**
	 * Counts the number of ServerData instances in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@Overwrite
	public int countServers() {
		return servers.size() + Config.SERVERS.size();
	}
	
	@Inject(method = "swapServers(II)V", at = @At("HEAD"), cancellable = true)
	public void swapServers(int pos1, int pos2, CallbackInfo ci) {
		if(pos1 >= servers.size() || pos2 >= servers.size()) {
			ci.cancel();
		}
	}
	
	/**
	 * Sets the ServerData instance stored for the given index in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@Overwrite
	public void set(int index, ServerData server) {
		if(index < servers.size()) {
			servers.set(index, server);
		}
	}

}
