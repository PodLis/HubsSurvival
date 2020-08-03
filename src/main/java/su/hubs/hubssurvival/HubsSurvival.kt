package su.hubs.hubssurvival

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import su.hubs.hubscore.HubsPermission
import su.hubs.hubscore.HubsPlugin
import su.hubs.hubscore.PluginUtils
import su.hubs.hubscore.module.chesterton.internal.item.ChestertonItem
import su.hubs.hubscore.module.chesterton.internal.menu.ChestMenu
import su.hubs.hubscore.util.ConfigUtils
import su.hubs.hubscore.util.MessageUtils
import su.hubs.hubssurvival.commands.abilities.FlyCommand
import su.hubs.hubssurvival.commands.abilities.RabbitCommand
import su.hubs.hubssurvival.commands.abilities.SpeedCommand
import su.hubs.hubssurvival.commands.abilities.VisionCommand
import su.hubs.hubssurvival.commands.home.DelhomeCommand
import su.hubs.hubssurvival.commands.home.HomeCommand
import su.hubs.hubssurvival.commands.home.HomeManager
import su.hubs.hubssurvival.commands.home.SethomeCommand
import su.hubs.hubssurvival.commands.teleport.*
import su.hubs.hubssurvival.shop.SellCommand
import su.hubs.hubssurvival.shop.ShopCommand
import su.hubs.hubssurvival.shop.ShopUtils
import java.io.File

class HubsSurvival : HubsPlugin() {

    var survivalFolder: File? = null
    private var configuration: FileConfiguration? = null

    override fun afterCoreStart(): Boolean {
        instance = this
        loadFiles()
        PluginUtils.setCommandExecutorAndTabCompleter(
                RtpCommand(),
                RabbitCommand(),
                VisionCommand(),
                SethomeCommand(),
                DelhomeCommand(),
                HomeCommand(),
                SpawnLikeCommand(),
                FlyCommand(),
                SpeedCommand(),
                TpaCommand(),
                TpcancelCommand(),
                TpacceptCommand(),
                TpdenyCommand(),
                ShopCommand(),
                SellCommand()
        )
        PluginUtils.logConsole("HubsSurvival successfully loaded!")
        return true
    }

    override fun beforeCoreStop() {
        PluginUtils.logConsole("HubsSurvival successfully disabled!")
    }

    override fun onPluginEnable() {}

    override fun onPluginDisable() {}

    override fun onPlayerJoin(player: Player?) {}

    override fun onPlayerQuit(player: Player?) {}

    override fun onReload() {
        loadFiles()
    }

    override fun onStringsReload() {
        strings = PluginUtils.getConfigInServerFolder("strings", this)
    }

    override fun onSchedule() {
        HomeManager.makeCycle()
    }

    override fun getStringData(key: String?): String? {
        return when (key) {
            "tablo" -> configuration?.getString("tablo")
            "can_give_items" -> "true"
            "folder" -> "server_survival"
            else -> ""
        }
    }

    override fun getServerPermissions(): Array<HubsPermission>? {
        val perms = mutableListOf<HubsPermission>()
        for (perm in SurvivalPermission.values())
            perms.add(perm)
        return perms.toTypedArray()
    }

    override fun getServerActions(): Map<String, (Player, ChestertonItem, ChestMenu, String) -> Unit>? {
        return mapOf(
                "category" to { player: Player, item: ChestertonItem, menu: ChestMenu, data: String ->
                    val title = item.createItemStack(player).itemMeta?.displayName
                    val childMenu = ShopUtils.parseCategoryMenu(data, title)
                    if (childMenu == null) {
                        MessageUtils.sendPrefixMessage(player, "Данного меню не существует!")
                    } else {
                        childMenu.parentMenu = menu
                        childMenu.nextMenu = ShopUtils.parseCategoryMenu(data, title, 2, childMenu.maxPage)
                        childMenu.open(player)
                    }
        }
        )
    }

    private fun loadFiles() {
        survivalFolder = File(PluginUtils.getMainFolder(), "server_survival")
        val configuration = PluginUtils.getConfigInFolder(survivalFolder, "config")!!
        survivalWorld = Bukkit.getWorld(ConfigUtils.getStringInSection(configuration, "world"))!!
        SPAWN_PLACE = ConfigUtils.parseLocation(configuration, "spawn", survivalWorld)
        TPA_REQ_TIME = configuration.getInt("tpa.req_time")
        rtpSettings = RtpSettings(
                configuration.getStringList("rtp.blacklist_rtp") as List<String>,
                configuration.getInt("rtp.max_radius"),
                configuration.getInt("rtp.min_radius"),
                configuration.getInt("rtp.center_x"),
                configuration.getInt("rtp.center_z"),
                configuration.getBoolean("rtp.use_world_border"),
                configuration.getInt("rtp.max_attempts"),
                configuration.getInt("rtp.cost")
        )
        this.configuration = configuration
        ShopUtils.loadConfiguration()
    }

    fun getShopSection() = configuration?.getConfigurationSection("shop")!!

    companion object {
        lateinit var instance: HubsSurvival
        lateinit var strings: FileConfiguration
        lateinit var SPAWN_PLACE: Location
        var TPA_REQ_TIME = 30
        lateinit var survivalWorld: World
        lateinit var rtpSettings: RtpSettings
        val tpRequests = hashMapOf<Player2Player, BukkitTask>()
    }

}

data class RtpSettings(
        val badBlocks: List<String>,
        val maxR: Int, val minR: Int, val centerX: Int, val centerZ: Int,
        val useWorldBorder: Boolean,
        val maxAttempts: Int, val cost: Int
)

data class Player2Player(val start: Player, val end: Player) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Player2Player
        if (start != other.start) return false
        if (end != other.end) return false
        return true
    }
    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }
}
