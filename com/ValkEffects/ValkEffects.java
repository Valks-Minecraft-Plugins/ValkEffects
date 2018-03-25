package com.ValkEffects;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.ValkEffects.effects.Effects;

public class ValkEffects extends JavaPlugin {
	@Override
	public void onEnable() {
		registerEvents();
	}
	
	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Effects(), this);
	}
}
