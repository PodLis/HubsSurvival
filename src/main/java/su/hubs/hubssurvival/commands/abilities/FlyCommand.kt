package su.hubs.hubssurvival.commands.abilities

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import su.hubs.hubssurvival.SurvivalCommand
import su.hubs.hubssurvival.SurvivalPermission
import java.util.ArrayList

class FlyCommand : SurvivalCommand("fly", SurvivalPermission.FLY, true, 0) {

    private fun flyOn(player: Player) {
        player.allowFlight = true
        player.isFlying = true
        sendPlaceholderMessage(player, "turned_on")
    }

    private fun flyOff(player: Player) {
        player.allowFlight = false
        player.isFlying = false
        sendPlaceholderMessage(player, "turned_off")
    }

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (args.isEmpty()) {
            if (player.isFlying)
                flyOff(sender)
            else
                flyOn(sender)
        } else {
            when (args[0]) {
                "on" -> flyOn(sender)
                "off" -> flyOff(sender)
                else -> sendDefaultUsage(sender)
            }
        }
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {

        val completionList: MutableCollection<String> = ArrayList()
        val partOfCommand: String
        val cmds = listOf("on", "off")
        if (args.size == 1) {
            partOfCommand = args[0]
            StringUtil.copyPartialMatches(partOfCommand, cmds, completionList)
            return completionList.sorted()
        }
        return null
    }
}