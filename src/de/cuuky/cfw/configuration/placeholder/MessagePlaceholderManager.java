package de.cuuky.cfw.configuration.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import de.cuuky.cfw.configuration.placeholder.placeholder.GeneralMessagePlaceholder;
import de.cuuky.cfw.configuration.placeholder.placeholder.PlayerMessagePlaceholder;
import de.cuuky.cfw.configuration.placeholder.placeholder.type.PlaceholderType;
import de.cuuky.cfw.manager.FrameworkManager;
import de.cuuky.cfw.manager.FrameworkManagerType;
import de.cuuky.cfw.player.CustomPlayer;

public class MessagePlaceholderManager extends FrameworkManager {

	private int tickTolerance;

	private Map<PlaceholderType, List<MessagePlaceholder>> placeholders;
	private Map<PlaceholderType, Map<String, List<MessagePlaceholder>>> cachedRequests;

	public MessagePlaceholderManager(JavaPlugin instance) {
		super(FrameworkManagerType.PLACEHOLDER, instance);

		this.tickTolerance = 900;

		this.placeholders = new HashMap<>();
		this.cachedRequests = new HashMap<>();
	}

	private Object[] replaceByList(String value, CustomPlayer vp, List<MessagePlaceholder> list) {
		if (list == null)
			return new Object[] { value, null };

		ArrayList<MessagePlaceholder> cached = new ArrayList<>();
		for (MessagePlaceholder pmp : list) {
			if (!pmp.containsPlaceholder(value))
				continue;

			if (pmp instanceof PlayerMessagePlaceholder)
				value = ((PlayerMessagePlaceholder) pmp).replacePlaceholder(value, vp);
			else
				value = ((GeneralMessagePlaceholder) pmp).replacePlaceholder(value);

			cached.add(pmp);
		}

		return new Object[] { value, cached };
	}

	@SuppressWarnings("unchecked")
	public String replacePlaceholders(String value, CustomPlayer vp, PlaceholderType type) {
		Map<String, List<MessagePlaceholder>> reqs = cachedRequests.get(type);
		if (reqs == null)
			reqs = new HashMap<>();

		if (reqs.get(value) != null)
			return (String) replaceByList(value, vp, reqs.get(value))[0];
		else {
			Object[] result = replaceByList(value, vp, getPlaceholders(type));
			if (result[1] != null)
				reqs.put(value, (List<MessagePlaceholder>) result[1]);
			cachedRequests.put(type, reqs);
			return (String) result[0];
		}
	}

	public String replacePlaceholders(String value, PlaceholderType type) {
		return replacePlaceholders(value, null, type);
	}

	public MessagePlaceholder registerPlaceholder(MessagePlaceholder placeholder) {
		if (placeholders.containsKey(placeholder.getType()))
			placeholders.get(placeholder.getType()).add(placeholder);
		else {
			ArrayList<MessagePlaceholder> holders = new ArrayList<>();
			holders.add(placeholder);

			this.placeholders.put(placeholder.getType(), holders);
		}

		return placeholder;
	}

	public MessagePlaceholder unregisterPlaceholder(MessagePlaceholder placeholder) {
		if (placeholders.containsKey(placeholder.getType()))
			placeholders.get(placeholder.getType()).remove(placeholder);

		return placeholder;
	}

	public void clear() {
		for (PlaceholderType type : placeholders.keySet())
			for (MessagePlaceholder pl : this.placeholders.get(type))
				pl.clearValue();

		this.cachedRequests.clear();
	}

	public int getTickTolerance() {
		return this.tickTolerance;
	}

	public void setTickTolerance(int tickTolerance) {
		this.tickTolerance = tickTolerance;
	}

	public Map<PlaceholderType, List<MessagePlaceholder>> getPlaceholders() {
		return this.placeholders;
	}

	public List<MessagePlaceholder> getAllPlaceholders() {
		ArrayList<MessagePlaceholder> msg = new ArrayList<>();
		for (PlaceholderType type : this.placeholders.keySet())
			msg.addAll(this.placeholders.get(type));

		return msg;
	}

	public List<MessagePlaceholder> getPlaceholders(PlaceholderType type) {
		return placeholders.get(type);
	}
}
