package glowredman.defaultserverlist;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	public static final List<ServerData> SERVERS = new ArrayList<>();
	
	public static void preInit(File configDir) {
		Configuration config = new Configuration(new File(configDir, "defaultserverlist.cfg"));
		config.load();
		
		boolean useURL = config.get("general", "useURL", false).getBoolean();
		String url = config.get("general", "url", "").getString();
		String[] servers = config.get("general", "servers", new String[0], "Pattern: NAME;URL").getStringList();
		
		if(config.hasChanged()) {
			config.save();
		}
		
		if(useURL) {
			InputStream inputStream = null;
			try {
				inputStream = new URL(url).openStream();
				servers = IOUtils.toString(inputStream, StandardCharsets.UTF_8).split("\n");
			} catch (Exception e) {
				FMLLog.severe("Could not parse default server list from URL!");
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}
		for(String s : servers) {
			String[] info = s.split(";", 2);
			if(info.length < 2) {
				continue;
			}
			SERVERS.add(new ServerData(info[0], info[1]));
		}
	}

}
