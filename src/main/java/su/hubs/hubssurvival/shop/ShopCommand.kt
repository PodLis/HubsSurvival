package su.hubs.hubssurvival.shop

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubscore.module.chesterton.internal.parser.MenuParser
import su.hubs.hubssurvival.HubsSurvival
import su.hubs.hubssurvival.SurvivalCommand
import java.io.File

class ShopCommand : SurvivalCommand("shop", null, true, 0) {

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        MenuParser.parseChestMenuInFolder(
                "shop", File(HubsSurvival.instance.survivalFolder, "shop")
        ).open(sender as Player)
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
}