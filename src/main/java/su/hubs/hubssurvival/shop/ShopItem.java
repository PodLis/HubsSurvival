package su.hubs.hubssurvival.shop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.hubs.hubscore.module.chesterton.internal.ActionClickHandler;
import su.hubs.hubscore.module.chesterton.internal.action.OpenMenuItemAction;
import su.hubs.hubscore.module.chesterton.internal.item.ExtendedItem;

import java.util.LinkedList;
import java.util.Map;

public class ShopItem extends ExtendedItem {

    private int price;
    private int data;

    public ShopItem(Material material, int price) {
        super(material);
        this.price = price;
        setNeedToRefresh(true);
    }

    public void prepareToCategoryMenu(ShopCategoryMenu shopCategoryMenu) {
        setLore(new LinkedList<>());
        addLore("&e" + price + "$");
        addLore("&aКликните, чтобы перейти к покупке");
        setLoreIsUpdated(false);

        setClickHandler(new ActionClickHandler(new OpenMenuItemAction(shopCategoryMenu, new ShopQuantityMenu(this))));
    }

    public void prepareToQuantityMenu() {
        setLore(new LinkedList<>());
        addLore("&e" + price + "$");
        setLoreIsUpdated(false);

        setClickHandler(new ActionClickHandler(new BuyShopItemAction(this, price)));
    }

    public void prepareToBuyAction() {
        setLore(new LinkedList<>());
        setLoreIsUpdated(false);
    }

    public ItemStack createShopStack(Player player, int amount) {
        setLore(new LinkedList<>());
        addLore("&e" + (price * amount) + "$");
        setLoreIsUpdated(false);

        return createItemStack(player, amount);
    }

    private void setEnchantmentLevel(int level) {
        Map<Enchantment, Integer> storedEnchantments = getStoredEnchantments();
        for (Map.Entry<Enchantment, Integer> entry : storedEnchantments.entrySet()) {
            storedEnchantments.replace(entry.getKey(), level);
        }
        setStoredEnchantments(storedEnchantments);
    }

    public ItemStack createShopEnchantedStack(Player player, int level) {
        setEnchantmentLevel(level);
        setLore(new LinkedList<>());
        addLore("&e" + ShopUtils.getEnchantedBookPrice(storedEnchantments) + "$");
        setLoreIsUpdated(false);
        return createItemStack(player);
    }

    public ItemStack createBuyEnchantedStack(Player player, int level) {
        setEnchantmentLevel(level);
        setLore(new LinkedList<>());
        setLoreIsUpdated(false);
        return createItemStack(player);
    }

    public ShopItem changeData(int data) {
        this.data = data;
        return this;
    }

    public int getData() {
        return data;
    }

}
