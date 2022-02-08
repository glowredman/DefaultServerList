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
	private List<ServerData> serverList;
	
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
