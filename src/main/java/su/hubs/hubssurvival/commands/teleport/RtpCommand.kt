package su.hubs.hubssurvival.commands.teleport

import com.sk89q.worldguard.bukkit.WGBukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import su.hubs.hubscore.util.PlayerUtils
import su.hubs.hubssurvival.HubsSurvival
import su.hubs.hubssurvival.SurvivalCommand
import java.util.*

class RtpCommand : SurvivalCommand("rtp", null, true, 0) {

    private val rn = Random()

    override fun onHubsCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (player.world != HubsSurvival.survivalWorld) {
            sendPlaceholderMessage(player, "wrong-world")
            return true
        }
        val location = randomLoc(player)
        if (location == null) {
            sendPlaceholderMessage(player, "limit", "attempts", "${HubsSurvival.rtpSettings.maxAttempts}")
            return true
        }
        PlayerUtils.teleport(player, location)
        sendPlaceholderMessage(player, "success")
        return true
    }

    override fun onHubsComplete(sender: CommandSender, command: Command, label: String, args: Array<String>) = null

    private fun randomLoc(player: Player): Location? {
        val world = HubsSurvival.survivalWorld
        val yaw = player.location.yaw
        val pitch = player.location.pitch
        val minVal: Int = HubsSurvival.rtpSettings.minR

        if (HubsSurvival.rtpSettings.useWorldBorder) {
            val border = world.worldBorder
            val borderRad: Int = border.size.toInt() / 2
            val centerX: Int = border.center.blockX
            val centerZ: Int = border.center.blockZ
            for (i in 0..HubsSurvival.rtpSettings.maxAttempts) {
                val loc: Location? = normal(borderRad, minVal, centerX, centerZ, rn.nextInt(4), world, yaw, pitch)
                if (loc != null && checkDepends(loc)) return loc
            }
        } else {
            val borderRad: Int = HubsSurvival.rtpSettings.maxR
            val centerX: Int = HubsSurvival.rtpSettings.centerX
            val centerZ: Int = HubsSurvival.rtpSettings.centerZ
            for (i in 0..HubsSurvival.rtpSettings.maxAttempts) {
                val loc: Location? = normal(borderRad, minVal, centerX, centerZ, rn.nextInt(4), world, yaw, pitch)
                if (loc != null && checkDepends(loc)) return loc
            }
        }
        return null
    }

    private fun normal(borderRad: Int, minVal: Int, CenterX: Int, CenterZ: Int, quadrant: Int, world: World,
                       yaw: Float, pitch: Float): Location? {
        val z: Int = rn.nextInt(borderRad - minVal) + CenterZ + minVal
        val z2: Int = -(rn.nextInt(borderRad - minVal) - CenterZ - minVal)
        val x: Int = rn.nextInt(borderRad - minVal) + CenterX + minVal
        val x2: Int = -rn.nextInt(borderRad - minVal) + CenterX - minVal
        return when (quadrant) {
            0 -> getLocAtNormal(x, z, world, yaw, pitch)
            1 -> getLocAtNormal(x2, z2, world, yaw, pitch)
            2 -> getLocAtNormal(x2, z, world, yaw, pitch)
            else -> getLocAtNormal(x, z2, world, yaw, pitch)
        }
    }

    private fun getLocAtNormal(x: Int, z: Int, world: World, yaw: Float, pitch: Float): Location? {
        var b = world.getHighestBlockAt(x, z)
        if (b.type.toString().endsWith("AIR"))
            b = world.getBlockAt(x, b.y - 1, z) else if (!b.type.isSolid) {
            if (!isBadBlock(b.type.name)) {
                b = world.getBlockAt(x, b.y - 1, z)
            }
        }
        return if (b.y > 0 && !isBadBlock(b.type.name)) {
            Location(world, x + 0.5, (b.y + 1).toDouble(), z + 0.5, yaw, pitch)
        } else null
    }

    private fun checkDepends(loc: Location): Boolean {
        return try {
            val plugin = WGBukkit.getPlugin()
            val container = plugin.regionContainer
            val regions = container[loc.world]
            return regions!!.getApplicableRegions(loc).size() == 0
        } catch (e: NoClassDefFoundError) {
            true
        }
    }

    private fun isBadBlock(block: String): Boolean {
        for (currentBlock in HubsSurvival.rtpSettings.badBlocks)
            if (currentBlock.toUpperCase() == block) return true
        return false
    }
}