package com.boomchanotai.mine3.Repository;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.boomchanotai.mine3.Logger.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemStackAdapter {
    static ObjectMapper objectMapper = new ObjectMapper();
    static TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
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

        if (itemNode.has("meta")) {
            Map<String, Object> metaMap = objectMapper.convertValue(itemNode.get("meta"), typeRef);
            System.out.println(metaMap);
        }

        return itemStack;
    }
}
