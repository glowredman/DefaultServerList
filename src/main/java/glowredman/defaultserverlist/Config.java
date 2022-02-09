package glowredman.defaultserverlist;

import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.multiplayer.ServerData;

public class Config {
	
	public static final List<ServerData> SERVERS = new ArrayList<>();
	
	public static void preInit(File configDir) {
		Reader fileReader = null;
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Path path = configDir.toPath().resolve("defaultserverlist.json");
			ConfigObj config = new ConfigObj();
			
			if(!Files.exists(path)) {
				Files.write(path, Arrays.asList(gson.toJson(config)), StandardCharsets.UTF_8);
			} else {
				fileReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
				config = gson.fromJson(fileReader, ConfigObj.class);
			}
			
			Map<String, String> servers;
			if(config.useURL) {
				servers = gson.fromJson(IOUtils.toString(new URL(config.url), StandardCharsets.UTF_8), new TypeToken<Map<String, String>>() {private static final long serialVersionUID = -1786059589535074931L;}.getType());
			} else {
				servers = config.servers;
			}
			
			for(Entry<String, String> e : servers.entrySet()) {
				SERVERS.add(new ServerData(e.getKey(), e.getValue()));
			}
			
		} catch (Exception e) {
			FMLLog.severe("Could not parse default server list from URL!");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fileReader);
		}
	}
	
	private static final class ConfigObj {
		
		public boolean useURL = false;
		public String url = "";
		public Map<String, String> servers = new HashMap<>();
		
	}

}
