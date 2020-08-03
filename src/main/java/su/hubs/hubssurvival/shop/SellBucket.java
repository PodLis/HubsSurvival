package su.hubs.hubssurvival.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import su.hubs.hubscore.module.chesterton.internal.item.ChestertonItem;
import su.hubs.hubscore.module.chesterton.internal.item.PlayerHeadItem;
import su.hubs.hubscore.module.chesterton.internal.special.PartialHolderInNeed;
import su.hubs.hubscore.module.chesterton.internal.special.PartialInventoryHolder;
import su.hubs.hubscore.util.StringUtils;
import su.hubs.hubssurvival.HubsSurvival;

import static su.hubs.hubscore.module.values.api.API.addDollars;

public class SellBucket implements PartialHolderInNeed {

    private String title;
    private PlayerHeadItem item;
    private Inventory inventory;

    public SellBucket(String title) {
        this.title = title;
        this.inventory = null;
        item = new PlayerHeadItem();
        item.setName(HubsSurvival.strings.getString("shop.sell-button.name"));
        item.setLore(HubsSurvival.strings.getStringList("shop.sell-button.lore"));
        item.setBase64(HubsSurvival.strings.getString("shop.sell-button.base"));
        item.setClickHandler((player, item) -> {
            if (inventory != null) {
                for (int i = 0; i < 53; i++) {
                    ItemStack itemStack = inventory.getItem(i);
                    if (itemStack != null) {
                        if (ShopUtils.isSold(itemStack.getType())) {
                            addDollars(player, ShopUtils.getSellPrice(itemStack.getType()) * itemStack.getAmount());
                        } else {
                            player.getInventory().addItem(itemStack);
                        }
                        inventory.clear(i);
                    }
                }
            }
            return false;
        });
    }

    public void open(Player player) {
        inventory = Bukkit.createInventory(new PartialInventoryHolder(this), 54, StringUtils.replaceColor(title));
        inventory.setItem(53, item.createItemStack(player));
        player.openInventory(inventory);
    }

    @Override
    public ChestertonItem getItem() {
        return item;
    }
}
