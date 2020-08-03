package su.hubs.hubssurvival

import su.hubs.hubscore.HubsCommand

abstract class SurvivalCommand(name: String, permission: SurvivalPermission?, mustBePlayer: Boolean, minArgs: Int, vararg aliases: String) : HubsCommand(HubsSurvival.instance, name, permission, mustBePlayer, minArgs, *aliases)