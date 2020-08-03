package su.hubs.hubssurvival.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import su.hubs.hubscore.PluginUtils;
import su.hubs.hubscore.module.chesterton.internal.action.ItemAction;
import su.hubs.hubscore.module.chesterton.internal.item.ChestertonItem;
import su.hubs.hubscore.util.MessageUtils;

import static su.hubs.hubscore.module.values.api.API.takeDollars;

public class BuyShopItemAction extends ItemAction {

    private ShopItem item;
    private int price;

    public BuyShopItemAction(ShopItem item, int price) {
        this.item = item;
        this.price = price;
    }

    @Override
    public void execute(Player player, ChestertonItem chestertonItem) {
        if (item.getMaterial().equals(Material.ENCHANTED_BOOK)) {
            int level = ((int) (Math.log10(item.getData()) / Math.log10(2))) + 1;
            if (takeDollars(player, ShopUtils.getEnchantedBookPrice(item.getStoredEnchantments(), level)) == 0) {
                MessageUtils.sendPrefixMessage(player, "Недостаточно долларов!");
                return;
            }
            player.getInventory().addItem(item.createBuyEnchantedStack(player, level));
        } else {
            if (takeDollars(player, price * item.getData()) == 0) {
                MessageUtils.sendPrefixMessage(player, "Недостаточно долларов!");
                return;
            }
            player.getInventory().addItem(item.createItemStack(player, item.getData()));
        }
    }

}
