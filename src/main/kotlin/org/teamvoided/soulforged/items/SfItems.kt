package org.teamvoided.soulforged.items

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.soulforged.Soulforged.id
import org.teamvoided.soulforged.items.JargunItem.Companion.setCharge

object SfItems {

    val JarGun = red("jargun", JargunItem())
    fun init() {
    val list = listOf<ItemStack>(
        JarGun.defaultStack, JarGun.defaultStack.setCharge(true)
    )
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(
            ItemGroupEvents.ModifyEntries { list.forEach { item-> it.prepend(item) } }
        )
    }

    private fun red(id: String, item: Item): Item {
        return Registry.register(Registries.ITEM, id(id), item)
    }
}