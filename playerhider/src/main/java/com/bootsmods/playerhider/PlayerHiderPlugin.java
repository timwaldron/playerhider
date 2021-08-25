package com.bootsmods.playerhider;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;
import java.util.*;

@Extension
@PluginDescriptor(
	name = "Player Hider",
	description = "Hide other players based on their combat levels",
	enabledByDefault = false,
	tags = { "player", "hider", "entity" }
)
@Slf4j
public class PlayerHiderPlugin extends Plugin
{
	@Inject
	private PlayerHiderConfig config;

	@Inject
	private Client client;

	@Provides
	PlayerHiderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PlayerHiderConfig.class);
	}

	@Override
	protected void startUp()
	{

	}

	@Override
	protected void shutDown()
	{
		// runs on plugin shutdown
		log.info("Plugin stopped");
		client.setHideSpecificPlayers(List.of());
	}

	@Subscribe
	private void onGameTick(GameTick gameTick)
	{
		List<Player> surroundingPlayers = client.getPlayers();
		List<String> hiddenPlayers = new ArrayList<String>();

		Player local = client.getLocalPlayer();

		int bracket = config.cblvlbracket();
		int maxCb = local.getCombatLevel() + bracket;
		int minCb = local.getCombatLevel() - bracket;

		for (Player p : surroundingPlayers)
		{
			if (hiddenPlayers.contains(p.getName()) || p == local)
			{
				continue;
			}

			int pCb = p.getCombatLevel();

			// Greater than max bracket
			if (pCb > maxCb)
			{
				hiddenPlayers.add(p.getName());
				continue;
			}

			// Less than max bracket
			if (pCb < minCb)
			{
				hiddenPlayers.add(p.getName());
				continue;
			}
		}

		client.setHideSpecificPlayers(hiddenPlayers);
	}
}