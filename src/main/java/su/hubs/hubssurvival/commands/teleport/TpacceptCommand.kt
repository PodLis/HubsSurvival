package su.hubs.hubssurvival.commands.teleport

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import su.hubs.hubscore.PluginUtils
import su.hubs.hubscore.util.PlayerUtils
import su.hubs.hubssurvival.HubsSurvival
import su.hubs.hubssurvival.Player2Player
import su.hubs.hubssurvival.SurvivalCommand
import java.util.ArrayList

class TpacceptCommand : SurvivalCommand("tpaccept", null, true, 0) {

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val self = sender as Player
        var other: Player? = null

        if (args.isEmpty()) {
            for ((start, end) in HubsSurvival.tpRequests.keys)
                if (self == end) {
                    if (other != null) {
                        sendPlaceholderMessage(self, "not_one")
                        return false
                    }
                    other = start
                }
        } else {
            other = Bukkit.getServer().getPlayer(args[0])
        }

        if (other == null || !HubsSurvival.tpRequests.containsKey(Player2Player(other, self))) {
            sendPlaceholderMessage(self, "not_found")
            return false
        }

        PluginUtils.cancelTask(HubsSurvival.tpRequests[Player2Player(other, self)])
        sendPlaceholderMessage(self, "self", "player", other.displayName)
        sendPlaceholderMessage(other, "other", "player", self.displayName)
        PlayerUtils.teleport(other, self.location)

        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        val completionList: MutableCollection<String> = ArrayList()
        val partOfCommand: String
        val cmds = mutableListOf<String>()
        if (args.size == 1) {
            for (player in Bukkit.getServer().onlinePlayers)
                cmds.add(player.name)
            partOfCommand = args[0]
            StringUtil.copyPartialMatches(partOfCommand, cmds, completionList)
            return completionList.sorted()
        }
        return null
    }
}