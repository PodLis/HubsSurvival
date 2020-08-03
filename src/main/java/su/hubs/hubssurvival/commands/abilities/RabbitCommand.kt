package su.hubs.hubssurvival.commands.abilities

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import su.hubs.hubssurvival.SurvivalCommand
import su.hubs.hubssurvival.SurvivalPermission

class RabbitCommand : SurvivalCommand("rabbit", SurvivalPermission.RABBIT, true, 0) {

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        (sender as Player).inventory.addItem(ItemStack(Material.RABBIT_FOOT))
        sendPlaceholderMessage(sender, "ok")
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>) = null

}