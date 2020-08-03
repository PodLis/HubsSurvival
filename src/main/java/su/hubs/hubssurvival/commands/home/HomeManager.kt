package su.hubs.hubssurvival.commands.home

import org.bukkit.Location
import org.bukkit.entity.Player
import su.hubs.hubscore.module.values.PlayerData
import su.hubs.hubssurvival.HubsSurvival

object HomeManager {

    private val homeData: PlayerData = PlayerData("hubs_homes", "name", "x", "y", "z", "yaw", "pitch")

    private var cycle: Byte = 0

    init {
        homeData.prepareToWork(arrayOf("default"), intArrayOf(), doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0))
    }

    fun setHome(player: Player, name: String = "default") {
        homeData.insertAnotherRecord(player.uniqueId.toString(), arrayOf(name), intArrayOf(), doubleArrayOf(
                player.location.x,
                player.location.y,
                player.location.z,
                player.location.yaw.toDouble(),
                player.location.pitch.toDouble()
        ))
    }

    fun delHome(player: Player, name: String = "default") {
        homeData.deleteRecord(player.uniqueId.toString(), "name", name)
    }

    fun getHome(player: Player, name: String = "default"): Location? {
        val uuid = player.uniqueId.toString()
        if (!homeData.selectExist(uuid, "name", name))
            return null
        val x = homeData.selectDoubleValue(uuid, "name", name, "x")
        val y = homeData.selectDoubleValue(uuid, "name", name, "y")
        val z = homeData.selectDoubleValue(uuid, "name", name, "z")
        val yaw = homeData.selectDoubleValue(uuid, "name", name, "yaw")
        val pitch = homeData.selectDoubleValue(uuid, "name", name, "pitch")
        return Location(HubsSurvival.survivalWorld, x, y, z, yaw.toFloat(), pitch.toFloat())
    }

    fun homeExist(player: Player, name: String = "default"): Boolean {
        return homeData.selectExist(player.uniqueId.toString(), "name", name)
    }

    fun getHomesNames(player: Player): List<String> {
        return homeData.selectAllStringsValues(player.uniqueId.toString(), "name")
    }

    fun makeCycle() {
        homeData.saveValue("cycle", "x", cycle.toDouble())
        cycle++
    }

}