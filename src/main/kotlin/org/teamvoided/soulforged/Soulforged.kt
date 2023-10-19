package org.teamvoided.soulforged

import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import org.teamvoided.soulforged.items.SfItems

@Suppress("unused")
object Soulforged {
    const val MODID = "soulforged"

    @JvmField
    val LOGGER = LoggerFactory.getLogger(Soulforged::class.java)

    fun commonInit() {
        LOGGER.info("sucnut")
        SfItems.init()
    }

    fun clientInit() {
        LOGGER.info("Hello from Client")
    }

    fun id(path: String) = Identifier(MODID, path)
}