package de.cuuky.cfw.configuration.language;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import de.cuuky.cfw.configuration.language.languages.LoadableMessage;

public class Language {

	private String name;
	private LanguageManager manager;
	private boolean loaded;

	private File file;
	private YamlConfiguration configuration;

	private Class<? extends LoadableMessage> clazz;
	private Map<String, String> messages;

	public Language(String name, LanguageManager manager) {
		this(name, manager, null);
	}

	public Language(String name, LanguageManager manager, Class<? extends LoadableMessage> clazz) {
		this.name = name;
		this.clazz = clazz;
		this.manager = manager;
		this.messages = new HashMap<>();
		this.file = new File(manager.getLanguagePath(), this.name + ".yml");

		if (manager.getDefaultLanguage() != null && !this.file.exists())
			load();
	}

	private void saveConfiguration() {
		try {
			this.configuration.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		if (manager.getDefaultLanguage() == null)
			throw new IllegalStateException("Cannot load language while no default language is given");

		this.configuration = YamlConfiguration.loadConfiguration(this.file);
		this.configuration.options().copyDefaults(true);

		boolean save = file.exists();
		HashMap<String, String> msg = new HashMap<>();
		if (this.clazz != null)
			msg = manager.getValues(clazz);

		for (String defaultPath : manager.getDefaultMessages().keySet()) {
			if (this.configuration.contains(defaultPath))
				continue;

			save = true;
			String defaultValue = manager.getDefaultMessages().get(defaultPath);
			String value = msg.containsKey(defaultPath) ? msg.get(defaultPath) : defaultValue;
			this.configuration.addDefault(defaultPath, value);
		}

		for (String path : this.configuration.getKeys(true)) {
			if (this.configuration.isConfigurationSection(path))
				continue;

			if (!manager.getDefaultMessages().keySet().contains(path)) {
				save = true;
				System.out.println("Removed lang path " + path);
				this.configuration.set(path, null);
				continue;
			}

			messages.put(path, this.configuration.getString(path));
		}

		if (save)
			saveConfiguration();

		this.loaded = true;
	}

	public String getMessage(String path) {
		if (!loaded)
			load();

		return messages.get(path);
	}

	public Class<? extends LoadableMessage> getClazz() {
		return this.clazz;
	}

	public String getName() {
		return this.name;
	}

	public File getFile() {
		return this.file;
	}

	public boolean isLoaded() {
		return this.loaded;
	}

	public Map<String, String> getMessages() {
		return this.messages;
	}
}