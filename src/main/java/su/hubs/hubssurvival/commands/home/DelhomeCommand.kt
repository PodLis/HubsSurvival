package su.hubs.hubssurvival.commands.home

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubssurvival.SurvivalCommand

class DelhomeCommand : SurvivalCommand("delhome", null, true, 0) {
    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (args.isEmpty()) {
            if (!HomeManager.homeExist(player)) {
                sendPlaceholderMessage(player, "default-not-found")
                return false
            }
            HomeManager.delHome(player)
            sendPlaceholderMessage(player, "success")
        } else {
            val homeName = args[0]
            if (!HomeManager.homeExist(player, homeName)) {
                sendPlaceholderMessage(player, "not-found")
                return false
            }
            HomeManager.delHome(player, homeName)
            sendPlaceholderMessage(player, "success")
        }
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>) = null

}