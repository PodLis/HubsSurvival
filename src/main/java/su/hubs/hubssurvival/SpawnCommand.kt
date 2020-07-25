package su.hubs.hubssurvival

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubscore.HubsCommand
import su.hubs.hubscore.util.PlayerUtils

class SpawnCommand : HubsCommand("spawn", null, true, 0) {

    override fun onHubsCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>): Boolean {
        PlayerUtils.teleport(p0 as Player, HubsSurvival.SPAWN_PLACE)
        return true
    }

    override fun onHubsComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<String>) = null

}