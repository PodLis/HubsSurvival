package su.hubs.hubssurvival.commands.home

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import su.hubs.hubscore.util.PlayerUtils
import su.hubs.hubssurvival.SurvivalCommand

class HomeCommand : SurvivalCommand("home", null, true, 0) {

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (args.isEmpty()) {
            val home = HomeManager.getHome(player)
            if (home == null) {
                sendPlaceholderMessage(player, "default-not-found")
                return false
            }
            PlayerUtils.teleport(player, home)
            sendPlaceholderMessage(player, "success")
        } else {
            val home = HomeManager.getHome(player, args[0])
            if (home == null) {
                sendPlaceholderMessage(player, "not-found")
                return false
            }
            PlayerUtils.teleport(player, home)
            sendPlaceholderMessage(player, "success")
        }
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {
        val completionList: MutableCollection<String> = ArrayList()
        val partOfCommand: String
        val cmds: List<String> = HomeManager.getHomesNames(sender as Player)
        if (args.size == 1) {
            partOfCommand = args[0]
            StringUtil.copyPartialMatches(partOfCommand, cmds, completionList)
            val result: MutableList<String> = ArrayList()
            completionList.forEach { result.add(it) }
            return result
        }
        return null
    }

}