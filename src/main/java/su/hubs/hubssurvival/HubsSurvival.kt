package su.hubs.hubssurvival

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import su.hubs.hubscore.HubsPlugin
import su.hubs.hubscore.PluginUtils
import su.hubs.hubscore.util.ConfigUtils
import java.io.File

class HubsSurvival : HubsPlugin() {

    //s

    private var lobbyFolder: File? = null
    private var configuration: FileConfiguration? = null

    override fun afterCoreStart(): Boolean {
        loadFiles()
        PluginUtils.setCommandExecutorAndTabCompleter(SpawnCommand())
        PluginUtils.logConsole("HubsSurvival successfully loaded!")
        return true
    }

    override fun beforeCoreStop() {
        PluginUtils.logConsole("HubsLobby successfully disabled!")
    }

    override fun onPluginEnable() {}

    override fun onPluginDisable() {}

    override fun onPlayerJoin(player: Player?) {}

    override fun onPlayerQuit(player: Player?) {}

    override fun onReload() {
        loadFiles()
    }

    override fun onStringsReload() {}

    override fun onSchedule() {}

    override fun getStringData(s: String?): String? {
        return when (s) {
            "tablo" -> configuration?.getString("tablo")
            "can_give_items" -> "true"
            else -> ""
        }
    }

    private fun loadFiles() {
        lobbyFolder = File(PluginUtils.getMainFolder(), "server_survival")
        configuration = PluginUtils.getConfigInFolder(lobbyFolder, "config")
        val survivalWorld = Bukkit.getWorld(ConfigUtils.getStringInSection(configuration, "world"))
        SPAWN_PLACE = ConfigUtils.parseLocation(configuration, "spawn", survivalWorld)
    }

    companion object {
        lateinit var SPAWN_PLACE: Location
    }

}