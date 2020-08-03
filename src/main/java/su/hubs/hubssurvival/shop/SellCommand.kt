package su.hubs.hubssurvival.shop

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubssurvival.HubsSurvival
import su.hubs.hubssurvival.SurvivalCommand

class SellCommand : SurvivalCommand("sell", null, true, 0) {

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        SellBucket(HubsSurvival.strings.getString("shop.sell-bucket-title")).open(sender as Player)
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
}