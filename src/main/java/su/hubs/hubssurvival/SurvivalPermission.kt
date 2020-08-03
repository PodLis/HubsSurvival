package su.hubs.hubssurvival

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubscore.HubsPermission

enum class SurvivalPermission(private val perm: String) : HubsPermission {

    RABBIT("hubs.rabbit"),
    VISION("hubs.vision"),
    MANY_HOMES("hubs.homes"),
    FLY("hubs.fly"),
    SPEED("hubs.speed");

    override fun senderHasPerm(sender: CommandSender): Boolean {
        return (sender as? Player)?.hasPermission(perm) ?: true
    }

    override fun getPerm(): String {
        return perm
    }

}