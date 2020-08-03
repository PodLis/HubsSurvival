package su.hubs.hubssurvival.commands.home

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubssurvival.SurvivalCommand
import su.hubs.hubssurvival.SurvivalPermission

class SethomeCommand : SurvivalCommand("sethome", null, true, 0) {
    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (!SurvivalPermission.MANY_HOMES.senderHasPerm(player) && HomeManager.homeExist(player)) {
            sendPlaceholderMessage(player, "max-homes")
            return false
        }
        if (args.isEmpty()) {
            if (HomeManager.homeExist(player)) {
                sendPlaceholderMessage(player, "default-already-exist")
                return false
            }
            HomeManager.setHome(player)
            sendPlaceholderMessage(player, "success")
        } else {
            val homeName = args[0]
            if (!isRightString(homeName)) {
                sendPlaceholderMessage(player, "bad-name")
                return false
            }
            if (HomeManager.homeExist(player, homeName)) {
                sendPlaceholderMessage(player, "already-exist", "name", homeName)
                return false
            }
            HomeManager.setHome(player, homeName)
            sendPlaceholderMessage(player, "success")
        }
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>) = null

    private fun isRightString(s: String): Boolean {
        if (s.length >= 36)
            return false
        for (x in s)
            if (!x.isLetterOrDigit())
                return false
        return true
    }

}
