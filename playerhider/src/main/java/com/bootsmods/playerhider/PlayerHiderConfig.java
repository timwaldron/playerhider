package com.bootsmods.playerhider;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("PlayerHiderConfig")

public interface PlayerHiderConfig extends Config
{
	@ConfigItem(
		keyName = "cblvlbracket",
		name = "Combat Level Bracket",
		description = "Hide players based on combat level bracket",
		position = 0
	)
	default int cblvlbracket() { return 15; }
}