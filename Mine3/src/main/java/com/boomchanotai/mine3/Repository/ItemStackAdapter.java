package com.boomchanotai.mine3.Repository;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.boomchanotai.mine3.Logger.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemStackAdapter {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
    };

    public static Map<String, Object> serialize(ItemStack itemStack) {
        Map<String, Object> itemMap = itemStack.serialize();
        if (itemStack.hasItemMeta()) {
            itemMap.remove("meta");
            Object meta = itemStack.getItemMeta().serialize();
            itemMap.put("meta", meta);
        }

        return itemMap;
    }

    public static String serializeToJsonString(ItemStack itemStack) {

        Map<String, Object> itemMap = serialize(itemStack);
        try {
            return objectMapper.writeValueAsString(itemMap);
        } catch (JsonProcessingException e) {
            Logger.warning(e.getMessage());
        }

        return null;
    }

    public static ItemStack deserializeFromJson(JsonNode itemNode) {
        Map<String, Object> itemMap = objectMapper.convertValue(itemNode, typeRef);
        ItemStack itemStack = ItemStack.deserialize(itemMap);

        if (itemMap.get("meta") != null) {
            Map<String, Object> metaMap = objectMapper.convertValue(itemNode.get("meta"), typeRef);

            ItemMeta itemMeta = (ItemMeta) ConfigurationSerialization.deserializeObject(metaMap,
                    ConfigurationSerialization.getClassByAlias("ItemMeta"));
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
}
