package su.hubs.hubssurvival.commands.teleport

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubssurvival.HubsSurvival
import su.hubs.hubssurvival.SurvivalCommand

class TpcancelCommand : SurvivalCommand("tpcancel", null, true, 0) {

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val self = sender as Player
        for (p2p in HubsSurvival.tpRequests.keys)
            if (self == p2p.start) {
                HubsSurvival.tpRequests.remove(p2p)
                sendPlaceholderMessage(self, "success")
                return true
            }
        sendPlaceholderMessage(self, "not_found")
        return false
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>) = null
}