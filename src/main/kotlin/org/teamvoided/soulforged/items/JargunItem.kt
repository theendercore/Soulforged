package org.teamvoided.soulforged.items

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.teamvoided.voidlib.core.nbt.getBoolean
import org.teamvoided.voidlib.core.nbt.putBoolean
import kotlin.math.abs

class JargunItem(settings: Settings?) : Item(settings) {
    constructor() : this(FabricItemSettings())

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity): Boolean =
        !miner.isCreative

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip.add(Text.literal(if (stack.getCharge()) "Charged" else "Not Charged").formatted(Formatting.GRAY))
    }

    override fun getDefaultStack(): ItemStack {
        val stack = super.getDefaultStack()
        stack.setCharge(false)
        return stack
    }

    override fun getUseAction(stack: ItemStack?): UseAction = UseAction.BRUSH


    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        val time = ((3 * 21) - abs(remainingUseTicks))
        if (time <= 0 && !stack.getCharge()) {
            stack.setCharge(true)
        }
    }

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        val time = ((3 * 21) - abs(remainingUseTicks))
        if (time == 0 && !stack.getCharge()) {
            world.playSound(
                null, user.x, user.y, user.z, SoundEvents.BLOCK_DECORATED_POT_PLACE,
                SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f
            )
        }

    }

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val hStack = player.getStackInHand(hand)
        if (world.isClient) return TypedActionResult.fail(player.getStackInHand(hand))
        if (hStack.getCharge()) {
            fire(player, world, hStack)
            player.incrementStat(Stats.USED.getOrCreateStat(this))
            return TypedActionResult.consume(hStack)
        }
        player.setCurrentHand(hand)
        return TypedActionResult.pass(hStack)
    }


    companion object {
        const val CHARGE = "charge"
        fun ItemStack.getCharge(): Boolean = this.getBoolean(CHARGE)
        fun ItemStack.setCharge(state: Boolean): ItemStack {
            this.putBoolean(CHARGE, state)
            return this
        }

        fun fire(player: PlayerEntity, world: World, stack: ItemStack) {
            for (i in 0..5) {
                val persistentProjectileEntity =
                    (Items.ARROW as ArrowItem).createArrow(world, Items.ARROW.defaultStack, player)
                persistentProjectileEntity
                    .setProperties(player, player.pitch, player.yaw, 1.0f, 3.0f, 1.0f)

                world.spawnEntity(persistentProjectileEntity)
            }
            stack.setCharge(false)
        }

        fun PlayerEntity.getAmmo(stack: ItemStack): ItemStack = ItemStack.EMPTY
    }
}