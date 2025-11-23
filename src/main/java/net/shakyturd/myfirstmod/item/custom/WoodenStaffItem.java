package net.shakyturd.myfirstmod.item.custom;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.shakyturd.myfirstmod.entity.custom.MagicMissileEntity;

public class WoodenStaffItem extends Item{
    public WoodenStaffItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack staff = pPlayer.getItemInHand(pHand);
        pLevel.playSound(
                null,
                pPlayer.getX(),
                pPlayer.getY(),
                pPlayer.getZ(),
                SoundEvents.FIREWORK_ROCKET_LAUNCH,
                SoundSource.PLAYERS,
                0.5F,
                0.4F/ (pLevel.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        pPlayer.getCooldowns().addCooldown(this, 15);
        if(!pLevel.isClientSide) {
            MagicMissileEntity magicMissile = new MagicMissileEntity(pLevel, pPlayer);
            magicMissile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(magicMissile);
            System.out.println("hi");
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            staff.hurtAndBreak(1, pPlayer, EquipmentSlot.MAINHAND);
        }

        return super.use(pLevel, pPlayer, pHand);
    }
}
