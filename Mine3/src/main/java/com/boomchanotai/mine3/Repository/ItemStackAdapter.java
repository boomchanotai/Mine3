package com.boomchanotai.mine3.Repository;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.boomchanotai.mine3.Logger.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemStackAdapter {
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> itemMap = serialize(itemStack);
        try {
            return objectMapper.writeValueAsString(itemMap);
        } catch (JsonProcessingException e) {
            Logger.warning(e.getMessage());
        }

        return null;
    }
}
