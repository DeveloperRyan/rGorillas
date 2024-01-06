package org.ryno

import org.powbot.api.Area
import org.powbot.api.Tile

object Constants {
    // ITEM IDs
    const val SHARK_ID = 385
    const val VIAL_ID = 229
    const val ZENYTE_ID = 19529

    const val SUPER_ATTACK_1_ID = 149
    const val SUPER_ATTACK_2_ID = 147
    const val SUPER_ATTACK_3_ID = 145
    const val SUPER_ATTACK_4_ID = 2436
    const val SUPER_STRENGTH_1_ID = 161
    const val SUPER_STRENGTH_2_ID = 159
    const val SUPER_STRENGTH_3_ID = 157
    const val SUPER_STRENGTH_4_ID = 2440
    const val SUPER_COMBAT_1_ID = 12701
    const val SUPER_COMBAT_2_ID = 12699
    const val SUPER_COMBAT_3_ID = 12697
    const val SUPER_COMBAT_4_ID = 12695
    const val DIVINE_SUPER_COMBAT_1_ID = 23694
    const val DIVINE_SUPER_COMBAT_2_ID = 23691
    const val DIVINE_SUPER_COMBAT_3_ID = 23688
    const val DIVINE_SUPER_COMBAT_4_ID = 23685
    const val RANGING_POTION_1_ID = 173
    const val RANGING_POTION_2_ID = 171
    const val RANGING_POTION_3_ID = 169
    const val RANGING_POTION_4_ID = 2444
    const val DIVINE_RANGING_POTION_1_ID = 23742
    const val DIVINE_RANGING_POTION_2_ID = 23739
    const val DIVINE_RANGING_POTION_3_ID = 23736
    const val DIVINE_RANGING_POTION_4_ID = 23733
    const val PRAYER_POTION_1_ID = 143
    const val PRAYER_POTION_2_ID = 141
    const val PRAYER_POTION_3_ID = 139
    const val PRAYER_POTION_4_ID = 2434
    const val SUPER_RESTORE_1_ID = 3030
    const val SUPER_RESTORE_2_ID = 3028
    const val SUPER_RESTORE_3_ID = 3026
    const val SUPER_RESTORE_4_ID = 3024

    val ATTACK_POTIONS = intArrayOf(
        SUPER_ATTACK_1_ID,
        SUPER_ATTACK_2_ID,
        SUPER_ATTACK_3_ID,
        SUPER_ATTACK_4_ID,
    )
    val STRENGTH_POTIONS = intArrayOf(
        SUPER_STRENGTH_1_ID,
        SUPER_STRENGTH_2_ID,
        SUPER_STRENGTH_3_ID,
        SUPER_STRENGTH_4_ID,
    )
    val COMBAT_POTIONS = intArrayOf(
        SUPER_COMBAT_1_ID,
        SUPER_COMBAT_2_ID,
        SUPER_COMBAT_3_ID,
        SUPER_COMBAT_4_ID,
    )
    val DIVINE_COMBAT_POTIONS = intArrayOf(
        DIVINE_SUPER_COMBAT_1_ID,
        DIVINE_SUPER_COMBAT_2_ID,
        DIVINE_SUPER_COMBAT_3_ID,
        DIVINE_SUPER_COMBAT_4_ID,
    )
    val RANGING_POTIONS = intArrayOf(
        RANGING_POTION_1_ID,
        RANGING_POTION_2_ID,
        RANGING_POTION_3_ID,
        RANGING_POTION_4_ID,
    )
    val DIVINE_RANGING_POTIONS = intArrayOf(
        DIVINE_RANGING_POTION_1_ID,
        DIVINE_RANGING_POTION_2_ID,
        DIVINE_RANGING_POTION_3_ID,
        DIVINE_RANGING_POTION_4_ID,
    )
    val PRAYER_POTIONS = intArrayOf(
        PRAYER_POTION_1_ID,
        PRAYER_POTION_2_ID,
        PRAYER_POTION_3_ID,
        PRAYER_POTION_4_ID,
    )
    val SUPER_RESTORE_POTIONS = intArrayOf(
        SUPER_RESTORE_1_ID,
        SUPER_RESTORE_2_ID,
        SUPER_RESTORE_3_ID,
        SUPER_RESTORE_4_ID,
    )

    val LOOT_WHITELIST = intArrayOf(
        ZENYTE_ID, // Zenyte Shard
        19592, // Ballista Limbs
        19601, // Ballista Spring
        19586, // Light Frame
        19589, // Heavy Frame
        19610, // Monkey Tail
        1079, // Rune Platelegs
        1093, // Rune Plateskirt
        1113, // Rune Chainbody
        4587, // Dragon Scimitar
        9144, // Runite Bolts
        5295, // Ranarr Seed
        5300, // Snapdragon Seed
        5304, // Torstol Seed
        5314, // Maple Seed
        5315, // Yew Seed
        5316, // Magic Seed
        5289, // Palm Tree Seed
        22877, // Dragonfruit Tree Seed
        22869, // Celastrus Seed
        22871, // Redwood Tree Seed
        5317, // Spirit Seed
        PRAYER_POTION_3_ID, // Prayer potion(3)
        SHARK_ID, // Shark
        995, // Coins
        19580, // Rune javelin heads
        19582, // Dragon javelin heads
        560, // Death rune
        2362, // Adamantite bar (noted)
        2364, // Runite bar (noted)
        1602, // Diamond (noted)
        214, // Grimy Kwuarm (noted)
        216, // Grimy Cadantine (noted)
        218, // Grimy Dwarf Weed (noted)
        2486, // Grimy Lantadyme (noted)
        1373, // Rune battleaxe
        1319, // Rune 2h sword
        //TODO: add rare drop table items
    )

    val ALCH_WHITELIST = intArrayOf(
        1079, // Rune Platelegs
        1093, // Rune Plateskirt
        1113, // Rune Chainbody
        4587, // Dragon Scimitar
        1373, // Rune Battleaxe
        1319, // Rune 2h sword
    )

    val BANKING_WHITELIST = LOOT_WHITELIST.filter {
        it != SHARK_ID && it != PRAYER_POTION_3_ID
    }.toIntArray()

    val CHARGED_ITEMS = intArrayOf(
        19675, // Arclight
        12926, // Toxic Blowpipe
        25865, // Bow of Faerdhinen
        12006, // Abyssal Tentacle
    )

    val UNCHARGED_ITEMS = intArrayOf(
        6746, // Darklight
        12924, // Uncharged Toxic Blowpipe
        25862, // Bow of Faerdhinen (inactive)
        12004, // Kraken Tentacle
    )

    // LOCATIONS
    val GORILLAS_AREA = Area(Tile(2090, 5666), Tile(2119, 5638))
    val GRAND_TREE_AREA_LOWER = Area(Tile(2460, 3501), Tile(2471, 3489))
    val GRAND_TREE_AREA_UPPER = Area(Tile(2432, 3515, 1), Tile(2497, 3475, 1))

    val GORILLAS_DESTINATION_TILE = Tile(2103, 5657)
    val GORILLAS_SAFESPOT_TILE = Tile(2140, 5652)

    // ANIMATIONS
    const val MAGIC_ATTACK_ANIMATION_ID = 7225
    const val MELEE_ATTACK_ANIMATION_ID = 7226
    const val RANGED_ATTACK_ANIMATION_ID = 7227
    const val DEATH_ANIMATION_ID = 7229

    // PROJECTILES
    const val BOULDER_PROJECTILE_ID = 856

    // OTHER
    val PRAYER_POTION_NAME = "Prayer potion"
    val SUPER_RESTORE_POTION_NAME = "Super restore"
    val SUPER_COMBAT_POTION_NAME = "Super combat potion"
    val DIVINE_SUPER_COMBAT_POTION_NAME = "Divine super combat potion"
    val RANGING_POTION_NAME = "Ranging potion"
    val DIVINE_RANGING_POTION_NAME = "Divine ranging potion"
    val SUPER_ATTACK_POTION_NAME = "Super attack"
    val SUPER_STRENGTH_POTION_NAME = "Super strength"
    val ROYAL_SEED_POD_NAME = "Royal seed pod"
    val DEMONIC_GORILLA_NAME = "Demonic gorilla"

    val GORILLA_ROAR_TEXT = "Rhaaaaaaa!"

    enum class CombatPotionType {
        NONE, SUPER_COMBAT, DIVINE_SUPER_COMBAT, SUPER_ATTACK, SUPER_STRENGTH, SUPER_SET
    }

    enum class RangePotionType {
        NONE, RANGING, DIVINE_RANGING
    }

    enum class PrayerPotionType {
        PRAYER, SUPER_RESTORE
    }

    enum class AttackStyles {
        NONE, MELEE, RANGED, MAGIC
    }
}