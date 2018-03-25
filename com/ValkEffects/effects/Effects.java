package com.ValkEffects.effects;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ValkEffects.ValkEffects;

public class Effects implements Listener {
	ValkEffects plugin = null;

	public Effects() {
		plugin = JavaPlugin.getPlugin(ValkEffects.class);
	}

	@EventHandler
	private void blockDamageEffect(BlockDamageEvent e) {
		e.getBlock().getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getBlock().getLocation(), 1);
	}

	@EventHandler
	private void breakBlockEffect(BlockBreakEvent e) {
		Block b = e.getBlock();
		Location loc = b.getLocation();
		World w = b.getWorld();

		switch (b.getType()) {
		case COAL_ORE:
		case DIAMOND_ORE:
		case EMERALD_ORE:
		case GLOWING_REDSTONE_ORE:
		case GOLD_ORE:
		case IRON_ORE:
		case LAPIS_ORE:
		case QUARTZ_ORE:
		case REDSTONE_ORE:
			w.spawnParticle(Particle.LAVA, loc, 5, 0.1, 0.1, 0.1);
		default:
			w.playEffect(loc, Effect.SMOKE, 1);
			break;
		}
	}

	@EventHandler
	private void playerDamageEffects(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Monster) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (p.getGameMode().equals(GameMode.SURVIVAL)) {
					switch (e.getCause()) {
					case BLOCK_EXPLOSION:
					case ENTITY_EXPLOSION:
						addPotionEffect(p, PotionEffectType.BLINDNESS, 4);
						addPotionEffect(p, PotionEffectType.SLOW, 4);
						addPotionEffect(p, PotionEffectType.CONFUSION, 4);
						break;
					case MAGIC:
					case MELTING:
					case POISON:
					case CONTACT:
						addPotionEffect(p, PotionEffectType.HUNGER, 4);
						break;
					case DRAGON_BREATH:
					case DROWNING:
					case ENTITY_ATTACK:
					case ENTITY_SWEEP_ATTACK:
					case PROJECTILE:
					case FALL:
					case FALLING_BLOCK:
					case FLY_INTO_WALL:
						addPotionEffect(p, PotionEffectType.SLOW, 4);
						addPotionEffect(p, PotionEffectType.HUNGER, 4);
						addPotionEffect(p, PotionEffectType.BLINDNESS, 4);
					case FIRE:
					case FIRE_TICK:
					case HOT_FLOOR:
					case LAVA:
					case LIGHTNING:
						addPotionEffect(p, PotionEffectType.BLINDNESS, 4);
						addPotionEffect(p, PotionEffectType.SLOW, 4);
						addPotionEffect(p, PotionEffectType.HUNGER, 4);
						addPotionEffect(p, PotionEffectType.WEAKNESS, 4);
						break;
					case SUFFOCATION:
					case SUICIDE:
					case THORNS:
					case VOID:
					case WITHER:
						addPotionEffect(p, PotionEffectType.BLINDNESS, 4);
						break;
					case STARVATION:
					case CRAMMING:
					case CUSTOM:
						break;
					default:
						break;
					}
				}
			}
		}
	}

	@EventHandler
	private void entityHitEffect(EntityDamageByEntityEvent e) {
		LivingEntity entity = (LivingEntity) e.getEntity();
		Location loc = entity.getLocation();
		World w = entity.getWorld();
		if (e.getDamager() instanceof Player) {
			if (entity instanceof Monster) {
				w.spawnParticle(Particle.LAVA, loc, 3);
				w.playSound(loc, Sound.AMBIENT_CAVE, 1, 1);
			}
		}
	}

	@EventHandler
	private void entityDeathEffect(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		final Location loc = entity.getLocation();
		final World w = entity.getWorld();

		if (entity instanceof Monster) {
			for (LivingEntity ent : w.getLivingEntities()) {
				if (ent.getLocation().distance(entity.getLocation()) < 10) {
					if (ent instanceof Player) {
						Player p = (Player) ent;
						if (p.getGameMode().equals(GameMode.SURVIVAL)) {
							addPotionEffect(p, PotionEffectType.BLINDNESS, 6);

							new BukkitRunnable() {
								int counter = 0;

								public void run() {
									counter++;
									if (counter >= 20 * 3) {
										cancel();
									}
									w.playEffect(loc, Effect.PORTAL_TRAVEL, 1);
									w.spawnParticle(Particle.PORTAL, loc, 5);
								}

							}.runTaskTimer(plugin, 0, 1);
						}
						
					}
				}
			}
		}
	}

	@EventHandler
	private void swingEffect(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		World w = e.getPlayer().getWorld();
		Material type = e.getMaterial();
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			switch (type) {
			case DIAMOND_SWORD:
			case GOLD_SWORD:
			case IRON_SWORD:
			case STONE_SWORD:
			case WOOD_SWORD:
				w.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1, 1);
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	private void onRespawn(PlayerRespawnEvent e) {
		final Player p = e.getPlayer();
		new BukkitRunnable() {
			public void run() {
				addPotionEffect(p, PotionEffectType.BLINDNESS, 6);
			}
		}.runTaskLater(plugin, 20 * 1);
	}

	/*
	 * Duration is in seconds.
	 */
	private void addPotionEffect(Player p, PotionEffectType type, int duration) {
		if (!p.hasPotionEffect(type)) {
			p.addPotionEffect(new PotionEffect(type, 20 * duration, 4));
		}
	}
}
