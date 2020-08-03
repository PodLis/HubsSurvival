package su.hubs.hubssurvival.commands.teleport

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import su.hubs.hubscore.PluginUtils
import su.hubs.hubssurvival.HubsSurvival
import su.hubs.hubssurvival.Player2Player
import su.hubs.hubssurvival.SurvivalCommand
import java.util.ArrayList

class TpaCommand : SurvivalCommand("tpa", null, true, 1) {

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val self = sender as Player
        val other = Bukkit.getServer().getPlayer(args[0])

        if (other == null) {
            sendPlaceholderMessage(self, "only_online")
            return false
        }
        if (other == self) {
            sendPlaceholderMessage(self, "only_other")
            return false
        }
        for ((player, _) in HubsSurvival.tpRequests.keys)
            if (self == player) {
                sendPlaceholderMessage(self, "only_one")
                return false
            }

        val p2p = Player2Player(self, other)
        sendPlaceholderMessage(self, "request_self", "player", other.displayName, "time", "${HubsSurvival.TPA_REQ_TIME}")
        sendPlaceholderMessage(other, "request_other", "player", self.displayName, "time", "${HubsSurvival.TPA_REQ_TIME}")
        val task = PluginUtils.runTaskLater({
            HubsSurvival.tpRequests.remove(p2p)
            sendPlaceholderMessage(self, "timed_out_self", "player", other.displayName)
            sendPlaceholderMessage(other, "timed_out_other", "player", self.displayName)
        }, HubsSurvival.TPA_REQ_TIME * 20L)
        HubsSurvival.tpRequests[p2p] = task
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