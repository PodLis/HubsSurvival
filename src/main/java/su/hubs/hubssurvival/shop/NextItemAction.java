package su.hubs.hubssurvival.shop;

import org.bukkit.entity.Player;
import su.hubs.hubscore.module.chesterton.internal.action.ItemAction;
import su.hubs.hubscore.module.chesterton.internal.item.ChestertonItem;

public class NextItemAction extends ItemAction {

    ShopCategoryMenu menu;

    public NextItemAction(ShopCategoryMenu menu) {
        this.menu = menu;
    }

    @Override
    public void execute(Player player, ChestertonItem item) {
        ShopCategoryMenu nextMenu = menu.getNextMenu();
        if (nextMenu != null) {
            nextMenu.setParentMenu(menu.getParentMenu());
            nextMenu.setPrevMenu(menu);
            nextMenu.setNextMenu(ShopUtils.parseCategoryMenu(menu.getCategoryName(), menu.getOriginalTitle(), menu.getPage() + 2, menu.getMaxPage()));
            nextMenu.open(player);
        }
    }

}
