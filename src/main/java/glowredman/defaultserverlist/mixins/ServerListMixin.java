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
	
	@SuppressWarnings("rawtypes")
	@Shadow
	@Final
	private List servers;
	
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
	 * Removes the ServerData instance stored for the given index in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@Overwrite
	public void removeServerData(int index) {
		if(index < servers.size()) {
			servers.remove(index);
		}
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
	public void swapServers(int index1, int index2, CallbackInfo ci) {
		if(index1 >= servers.size() || index2 >= servers.size()) {
			ci.cancel();
		}
	}
	
	/**
	 * Sets the ServerData instance stored for the given index in the list.
	 * @reason DefaultServerList
	 * @author glowredman
	 */
	@SuppressWarnings("unchecked")
	@Overwrite
	public void func_147413_a(int index, ServerData data) {
		if(index < servers.size()) {
			servers.set(index, data);
		}
	}

}
