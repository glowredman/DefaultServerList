package glowredman.defaultserverlist;

import java.io.File;
import java.io.IOException;
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
	
    public static boolean allowDeletions;
    public static String url;
	public static final List<ServerData> SERVERS = new ArrayList<>();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static Path configPath;
	
	public static void preInit(File configDir) {
		Reader fileReader = null;
		try {
			configPath = configDir.toPath().resolve("defaultserverlist.json");
			ConfigObj config = new ConfigObj();
			
			if(!Files.exists(configPath)) {
				saveConfig(config);
			} else {
				fileReader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8);
				config = GSON.fromJson(fileReader, ConfigObj.class);
			}
            
            allowDeletions = config.allowDeletions;
            url = config.url;
			
			Map<String, String> servers;
			if(config.useURL) {
				servers = GSON.fromJson(IOUtils.toString(new URL(config.url), StandardCharsets.UTF_8), new TypeToken<Map<String, String>>() {private static final long serialVersionUID = -1786059589535074931L;}.getType());
				ConfigObj newConfig = new ConfigObj(servers);
				newConfig.useURL = !allowDeletions;
                saveConfig(newConfig);
			} else {
				servers = config.servers;
			}
			
			for(Entry<String, String> e : servers.entrySet()) {
				SERVERS.add(new ServerData(e.getKey(), e.getValue()));
			}
			
		} catch (Exception e) {
			FMLLog.severe("Could not parse default server list!");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fileReader);
		}
	}
	
	public static void saveConfig(ConfigObj config) throws IOException {
	    File f = configPath.toFile();
	    if(f.exists()) {
	        f.delete();
	    }
	    Files.write(configPath, Arrays.asList(GSON.toJson(config)), StandardCharsets.UTF_8);
	}
	
	public static final class ConfigObj {
		
		public boolean useURL = false;
		public boolean allowDeletions = true;
		public String url = "";
		public Map<String, String> servers = new HashMap<>();
		
		public ConfigObj() {}
		
		public ConfigObj(Map<String, String> servers) {
		    this.servers = servers;
        }
		
	}

}
