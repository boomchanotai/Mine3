package com.boomchanotai.mine3.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.postgresql.util.PGobject;

import com.boomchanotai.mine3.logger.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemStackAdapter {
    private ObjectMapper objectMapper = new ObjectMapper();
    private TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
    };

    public Map<String, Object> serialize(ItemStack itemStack) {
        Map<String, Object> itemMap = itemStack.serialize();
        if (itemStack.hasItemMeta()) {
            itemMap.remove("meta");
            Object meta = itemStack.getItemMeta().serialize();
            itemMap.put("meta", meta);
        }

        return itemMap;
    }

    public String serializeToJsonString(ItemStack itemStack) {

        Map<String, Object> itemMap = serialize(itemStack);
        try {
            return objectMapper.writeValueAsString(itemMap);
        } catch (JsonProcessingException e) {
            Logger.warning(e.getMessage());
        }

        return null;
    }

    public ItemStack deserializeFromJson(JsonNode itemNode) {
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

    public ItemStack[] ParseItemStackListFromPGObject(PGobject itemStackObject) {
        if (itemStackObject == null) {
            return null;
        }

        String itemStackJson = itemStackObject.getValue();
        JsonNode itemStackNode = null;
        try {
            itemStackNode = objectMapper.readTree(itemStackJson);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "Failed to parse itemStack from PGObject");
        }

        if (itemStackNode == null) {
            Logger.warning("ItemStackNode is null");
            return null;
        }

        if (!itemStackNode.isArray()) {
            Logger.warning("itemStackNode is not an array");
            return null;
        }

        ArrayList<ItemStack> itemStackArray = new ArrayList<>();

        for (JsonNode itemNode : itemStackNode) {
            if (itemNode.isNull()) {
                itemStackArray.add(null);
                continue;
            }
            ItemStack itemStack = deserializeFromJson(itemNode);
            itemStackArray.add(itemStack);
        }

        ItemStack[] itemStackList = itemStackArray.toArray(new ItemStack[itemStackArray.size()]);

        return itemStackList;
    }

    public PGobject ParsePGObjectFromItemStackList(ItemStack[] itemStackList) {
        if (itemStackList == null) {
            return null;
        }

        ArrayList<String> itemStackArray = new ArrayList<>();
        for (ItemStack item : itemStackList) {
            if (item == null) {
                itemStackArray.add(null);
                continue;
            }

            String itemString = serializeToJsonString(item);
            itemStackArray.add(itemString);
        }

        PGobject itemStackObject = new PGobject();
        itemStackObject.setType("json");
        try {
            itemStackObject.setValue(itemStackArray.toString());
        } catch (SQLException e) {
            Logger.warning(e.getMessage(), "Failed to set value to PGObject");
        }

        return itemStackObject;
    }
}
