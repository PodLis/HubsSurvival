package su.hubs.hubssurvival.commands.abilities

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import su.hubs.hubssurvival.SurvivalCommand
import su.hubs.hubssurvival.SurvivalPermission
import java.util.ArrayList

class SpeedCommand : SurvivalCommand("speed", SurvivalPermission.SPEED, true, 1) {

    private fun setFlySpeed(player: Player, number: Int) {
        player.flySpeed = 0.1f * (number - 1)
        sendPlaceholderMessage(player, "fly", "speed", "$number")
    }

    private fun setWalkSpeed(player: Player, number: Int) {
        player.walkSpeed = 0.1f * (number - 1)
        sendPlaceholderMessage(player, "walk", "speed", "$number")
    }

    private fun setWalkAndFlySpeed(player: Player, number: Int) {
        player.walkSpeed = 0.1f * (number - 1)
        player.flySpeed = 0.1f * (number - 1)
        sendPlaceholderMessage(player, "walk_and_fly", "speed", "$number")
    }

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (args.size == 1) {
            when(args[0]) {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" -> {
                    setWalkAndFlySpeed(player, args[0].toInt())
                }
                else -> {
                    sendDefaultUsage(player)
                    return false
                }
            }
        } else {
            when (args[0]) {
                "fly" -> {
                    when(args[1]) {
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" -> {
                            setFlySpeed(player, args[1].toInt())
                        }
                        else -> {
                            sendDefaultUsage(player)
                            return false
                        }
                    }
                }
                "walk" -> {
                    when(args[1]) {
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" -> {
                            setWalkSpeed(player, args[1].toInt())
                        }
                        else -> {
                            sendDefaultUsage(player)
                            return false
                        }
                    }
                }
                else -> {
                    sendDefaultUsage(player)
                    return false
                }
            }
        }
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {

        val completionList: MutableCollection<String> = ArrayList()
        val partOfCommand: String
        val cmds = when(args.size) {
            1 -> listOf("fly", "walk", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
            2 -> listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
            else -> null
        }
        if (cmds != null) {
            partOfCommand = args[0]
            StringUtil.copyPartialMatches(partOfCommand, cmds, completionList)
            return completionList.sorted()
        }
        return null
    }
}