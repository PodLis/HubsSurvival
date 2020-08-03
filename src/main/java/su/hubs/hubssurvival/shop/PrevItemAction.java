package su.hubs.hubssurvival.shop;

import org.bukkit.entity.Player;
import su.hubs.hubscore.module.chesterton.internal.action.ItemAction;
import su.hubs.hubscore.module.chesterton.internal.item.ChestertonItem;

public class PrevItemAction extends ItemAction {

    ShopCategoryMenu menu;

    public PrevItemAction(ShopCategoryMenu menu) {
        this.menu = menu;
    }

    @Override
    public void execute(Player player, ChestertonItem item) {
        ShopCategoryMenu prevMenu = menu.getPrevMenu();
        if (prevMenu != null) {
            prevMenu.setParentMenu(menu.getParentMenu());
            prevMenu.setNextMenu(menu);
            prevMenu.setPrevMenu(ShopUtils.parseCategoryMenu(menu.getCategoryName(), menu.getOriginalTitle(), menu.getPage() - 2, menu.getMaxPage()));
            prevMenu.open(player);
        }
    }

}
