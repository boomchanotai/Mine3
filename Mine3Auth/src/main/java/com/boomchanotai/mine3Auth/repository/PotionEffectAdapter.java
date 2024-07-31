package com.boomchanotai.mine3Auth.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.potion.PotionEffect;
import org.postgresql.util.PGobject;

import com.boomchanotai.mine3Lib.logger.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PotionEffectAdapter {
    private ObjectMapper objectMapper = new ObjectMapper();
    private TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
    };

    public Map<String, Object> serialize(PotionEffect potionEffect) {
        return potionEffect.serialize();
    }

    public String serializeToJsonString(PotionEffect potionEffect) {
        Map<String, Object> potionMap = serialize(potionEffect);
        try {
            return objectMapper.writeValueAsString(potionMap);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to serialize potion effect to json");
        }

        return null;
    }

    public PotionEffect deserializeFromJson(JsonNode potionNode) {
        Map<String, Object> potionMap = objectMapper.convertValue(potionNode, typeRef);
        PotionEffect potionEffect = (PotionEffect) ConfigurationSerialization.deserializeObject(potionMap,
                ConfigurationSerialization.getClassByAlias("PotionEffect"));

        return potionEffect;
    }

    public Collection<PotionEffect> ParsePotionEffectsFromPGObject(PGobject potionObject) {
        if (potionObject == null) {
            return null;
        }

        String potionEffects = potionObject.getValue();
        JsonNode potionNode = null;
        try {
            potionNode = objectMapper.readTree(potionEffects);
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to parse potion effects from PGObject");
        }

        if (potionNode == null) {
            Logger.warning("failed to parse potion effects from PGObject, null node");
            return null;
        }

        if (!potionNode.isArray()) {
            Logger.warning("failed to parse potion effects from PGObject, not an array");
            return null;
        }

        Collection<PotionEffect> potionEffectList = new ArrayList<>();
        for (JsonNode node : potionNode) {
            PotionEffect potionEffect = deserializeFromJson(node);
            potionEffectList.add(potionEffect);
        }

        return potionEffectList;
    }

    public PGobject ParsePGObjectFromPotionEffects(Collection<PotionEffect> potionEffects) {
        if (potionEffects == null) {
            return null;
        }

        ArrayList<String> potionEffectArray = new ArrayList<>();
        for (PotionEffect potionEffect : potionEffects) {
            if (potionEffect == null) {
                continue;
            }

            String potionEffectJson = serializeToJsonString(potionEffect);
            potionEffectArray.add(potionEffectJson);
        }

        PGobject potionEffectsObject = new PGobject();
        potionEffectsObject.setType("json");
        try {
            potionEffectsObject.setValue(potionEffectArray.toString());
        } catch (Exception e) {
            Logger.warning(e.getMessage(), "failed to parse potion effects to PGObject");
        }

        return potionEffectsObject;
    }
}
