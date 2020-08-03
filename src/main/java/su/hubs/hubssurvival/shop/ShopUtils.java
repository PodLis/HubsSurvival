package su.hubs.hubssurvival.shop;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import su.hubs.hubscore.PluginUtils;
import su.hubs.hubscore.module.chesterton.internal.parser.SubParser;
import su.hubs.hubssurvival.HubsSurvival;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShopUtils {

    private static final int GREAT_PRICE = 1000000;
    private static Map<Material, Integer> pricesMap, sellMap;
    private static Map<Enchantment, Integer> enchantmentsPriceMap;
    private static Map<PotionType, Integer> potionsPriceMap;
    private static File shopFolder;

    public static void loadConfiguration() {
        shopFolder = new File(HubsSurvival.instance.getSurvivalFolder(), "shop");
        FileConfiguration priceConfiguration = PluginUtils.getConfigInServerFolder("prices", HubsSurvival.instance);
        ConfigurationSection shopSection = HubsSurvival.instance.getShopSection();

        pricesMap = new HashMap<>();
        for (String key : priceConfiguration.getKeys(false)) {
            int price = priceConfiguration.getInt(key);
            pricesMap.put(Material.getMaterial(key.toUpperCase()), price > 0 ? price : GREAT_PRICE);
        }

        enchantmentsPriceMap = new HashMap<>();
        for (String key : shopSection.getConfigurationSection("enchantments").getKeys(false)) {
            enchantmentsPriceMap.put(Enchantment.getByName(key), shopSection.getInt("enchantments." + key));
        }

        potionsPriceMap = new HashMap<>();
        for (String key : shopSection.getConfigurationSection("potions").getKeys(false)) {
            potionsPriceMap.put(PotionType.valueOf(key), shopSection.getInt("potions." + key));
        }

        sellMap = new HashMap<>();
        for (String key : shopSection.getConfigurationSection("sell-cost").getKeys(false)) {
            sellMap.put(Material.getMaterial(key.toUpperCase()), shopSection.getInt("sell-cost." + key));
        }

    }

    static ShopItem parseShopItem(ConfigurationSection section) {

        ShopItem shopItem;
        String type = section.getString("type");
        Material material;
        if (type == null) {
            material = Material.BEDROCK;
        } else {
            material = Material.getMaterial(type);
        }

        switch (material) {
            case POTION:
            case SPLASH_POTION:
            case LINGERING_POTION:
            {
                if (section.getConfigurationSection("potion") != null) {
                    PotionData potionData = SubParser.INSTANCE.parsePotionData(section.getConfigurationSection("potion"));
                    shopItem = new ShopItem(material, getPotionPrice(potionData, material));
                    shopItem.setPotionData(potionData);
                    return shopItem;
                }
            }
            case TIPPED_ARROW:
            {
                if (section.getConfigurationSection("potion") != null) {
                    PotionData potionData = SubParser.INSTANCE.parsePotionData(section.getConfigurationSection("potion"));
                    shopItem = new ShopItem(material, getArrowPrice(potionData));
                    shopItem.setPotionData(potionData);
                    return shopItem;
                }
            }
            case ENCHANTED_BOOK:
            {
                if (section.getConfigurationSection("storage") != null) {
                    Map<Enchantment, Integer> enchantments = SubParser.INSTANCE.parseEnchantments(section.getConfigurationSection("storage"));
                    shopItem = new ShopItem(material, getEnchantedBookPrice(enchantments));
                    shopItem.setStoredEnchantments(enchantments);
                    return shopItem;
                }
            }
            default:
            {
                return new ShopItem(material, getItemPrice(material));
            }
        }
    }

    public static ShopCategoryMenu parseCategoryMenu(String categoryName, String title) {
        int n = 1;
        String tFileName = "shop-" + categoryName + "-" + n;
        while (fileExists(tFileName)) {
            n += 1;
            tFileName = "shop-" + categoryName + "-" + n;
        }

        return parseCategoryMenu(categoryName, title, 1, n-1);
    }

    public static ShopCategoryMenu parseCategoryMenu(String categoryName, String title, int page, int maxPage) {
        if (page > maxPage || page < 1) {
            return null;
        }
        String fileName = "shop-" + categoryName + "-" + page;

        Configuration configuration = PluginUtils.getConfigInFolder(shopFolder, fileName);
        ShopCategoryMenu shopCategoryMenu = new ShopCategoryMenu(categoryName, title, page, maxPage);

        for (String slot : configuration.getKeys(false)) {
            int iSlot = Integer.parseInt(slot);
            if (iSlot >= 0 && iSlot < 54) {

                ShopItem shopItem = ShopUtils.parseShopItem(configuration.getConfigurationSection(slot));

                shopItem.prepareToCategoryMenu(shopCategoryMenu);

                shopCategoryMenu.setItem(iSlot, shopItem);

            }
        }

        return shopCategoryMenu;
    }


    public static int getItemPrice(Material material) {
        if (pricesMap.containsKey(material)) {
            return pricesMap.get(material);
        }
        return GREAT_PRICE;
    }

    public static int getEnchantmentPrice(Enchantment enchantment) {
        if (enchantmentsPriceMap.containsKey(enchantment)) {
            return enchantmentsPriceMap.get(enchantment);
        }
        return GREAT_PRICE;
    }

    public static int getEffectPrice(PotionType potionType) {
        if (potionsPriceMap.containsKey(potionType)) {
            return potionsPriceMap.get(potionType);
        }
        return GREAT_PRICE;
    }

    public static int getBrewPrice(Material material) {
        return getItemPrice(material) + HubsSurvival.instance.getShopSection().getInt("brew-addition");
    }


    public static int getPotionPrice(PotionData potionData, Material material) {
        int startPrice = getEffectPrice(potionData.getType()) + getItemPrice(Material.GLASS_BOTTLE);

        if (potionData.isExtended()) {
            startPrice += getBrewPrice(Material.REDSTONE);
        }
        if (potionData.isUpgraded()) {
            startPrice += getBrewPrice(Material.GLOWSTONE_DUST);
        }

        switch (material) {
            case POTION:
                return startPrice;
            case LINGERING_POTION:
                return startPrice + getBrewPrice(Material.DRAGON_BREATH);
            case SPLASH_POTION:
                return startPrice + getBrewPrice(Material.GUNPOWDER);
            default:
                return GREAT_PRICE;
        }
    }

    public static int getArrowPrice(PotionData potionData) {
        return getPotionPrice(potionData, Material.LINGERING_POTION) / 8 + getItemPrice(Material.ARROW);
    }

    public static int getEnchantedBookPrice(Map<Enchantment, Integer> enchantments) {
        int startPrice = getItemPrice(Material.BOOK), tPrice;
        for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
            tPrice = getEnchantmentPrice(enchantment.getKey());
            for (int i = 2; i <= enchantment.getValue(); i++) {
                tPrice += tPrice + (Math.pow(2, i-1) + i - 2)*HubsSurvival.instance.getShopSection().getInt("exp-cost");
            }
            startPrice += tPrice;
        }
        return startPrice;
    }

    public static int getEnchantedBookPrice(Map<Enchantment, Integer> enchantments, int level) {
        int startPrice = getItemPrice(Material.BOOK), tPrice;
        for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()) {
            tPrice = getEnchantmentPrice(enchantment.getKey());
            for (int i = 2; i <= level; i++) {
                tPrice += tPrice + (Math.pow(2, i - 1) + i - 2) * HubsSurvival.instance.getShopSection().getInt("exp-cost");
            }
            startPrice += tPrice;
        }
        return startPrice;
    }

    public static int getSellPrice(Material material) {
        if (sellMap.containsKey(material)) {
            return sellMap.get(material);
        }
        return 0;
    }

    public static boolean isSold(Material material) {
        return sellMap.containsKey(material);
    }

    public static Set<Material> getSellSet() {
        return sellMap.keySet();
    }

    public static boolean fileExists(String name) {
        return (new File(shopFolder, name + ".yml")).exists();
    }

}
