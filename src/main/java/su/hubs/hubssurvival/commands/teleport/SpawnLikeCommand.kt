package su.hubs.hubssurvival.commands.teleport

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubscore.util.PlayerUtils
import su.hubs.hubssurvival.HubsSurvival
import su.hubs.hubssurvival.SurvivalCommand

class SpawnLikeCommand : SurvivalCommand("spawn", null, true, 0) {
    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        PlayerUtils.teleport(sender as Player, HubsSurvival.SPAWN_PLACE)
        sendPlaceholderMessage(sender, "success")
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>) = null

}