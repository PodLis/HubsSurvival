package su.hubs.hubssurvival.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import su.hubs.hubscore.PluginUtils;
import su.hubs.hubscore.module.chesterton.HubsChesterton;
import su.hubs.hubscore.module.chesterton.internal.ActionClickHandler;
import su.hubs.hubscore.module.chesterton.internal.item.ChestertonItem;
import su.hubs.hubscore.module.chesterton.internal.item.PlayerHeadItem;
import su.hubs.hubscore.module.chesterton.internal.menu.ChestertonMenu;

import java.util.HashMap;
import java.util.Map;

public class ShopCategoryMenu extends ChestertonMenu {

    private PlayerHeadItem prevButton, nextButton, returnButton;
    private ShopCategoryMenu prevMenu, nextMenu;
    private Map<Integer, ShopItem> items;
    private int page, maxPage;
    private String originalTitle, categoryName;

    public ShopCategoryMenu(String categoryName, String title, int page, int maxPage) {
        super(title + " &7[" + page + "/" + maxPage + "]", 54);
        this.items = new HashMap<>();
        this.page = page;
        this.maxPage = maxPage;
        this.originalTitle = title;
        this.categoryName = categoryName;
        prevMenu = null;
        nextMenu = null;

        prevButton = new PlayerHeadItem();
        prevButton.setName(PluginUtils.getStringsConfig().getString("menus.buttons.prev.name"));
        prevButton.setClickHandler(new ActionClickHandler(new PrevItemAction(this)));

        nextButton = new PlayerHeadItem();
        nextButton.setName(PluginUtils.getStringsConfig().getString("menus.buttons.next.name"));
        nextButton.setClickHandler(new ActionClickHandler(new NextItemAction(this)));

        returnButton = HubsChesterton.getReturnButton(this);

    }

    @Override
    protected void setItemsToInventory(Inventory inventory, Player player) {
        for (Map.Entry<Integer, ShopItem> entry : items.entrySet()) {
            entry.getValue().prepareToCategoryMenu(this);
            inventory.setItem(entry.getKey(), entry.getValue().createItemStack(player));
        }

        if (prevMenu == null) {
            prevButton.setBase64(PluginUtils.getStringsConfig().getString("menus.buttons.prev.negative-base"));
        } else {
            prevButton.setBase64(PluginUtils.getStringsConfig().getString("menus.buttons.prev.positive-base"));
        }
        if (nextMenu == null) {
            nextButton.setBase64(PluginUtils.getStringsConfig().getString("menus.buttons.next.negative-base"));
        } else {
            nextButton.setBase64(PluginUtils.getStringsConfig().getString("menus.buttons.next.positive-base"));
        }

        inventory.setItem(25, prevButton.createItemStack(player));
        inventory.setItem(26, nextButton.createItemStack(player));
        inventory.setItem(34, returnButton.createItemStack(player));
        inventory.setItem(35, returnButton.createItemStack(player));
    }

    @Override
    public ChestertonItem getItem(int slot) {
        switch (slot) {
            case 25:
                return prevButton;
            case 26:
                return nextButton;
            case 34:
            case 35:
                return returnButton;
            default:
                return items.get(slot);
        }
    }

    public void setItem(int slot, ShopItem item) {
        if (slot >= 0 && slot < size()) {
            items.put(slot, item);
        }
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getPage() {
        return page;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public ShopCategoryMenu getPrevMenu() {
        return prevMenu;
    }

    public void setPrevMenu(ShopCategoryMenu prevMenu) {
        this.prevMenu = prevMenu;
    }

    public ShopCategoryMenu getNextMenu() {
        return nextMenu;
    }

    public void setNextMenu(ShopCategoryMenu nextMenu) {
        this.nextMenu = nextMenu;
    }
}
