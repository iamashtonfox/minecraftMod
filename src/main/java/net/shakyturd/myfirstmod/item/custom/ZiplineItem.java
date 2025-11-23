package net.shakyturd.myfirstmod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.shakyturd.myfirstmod.block.ModBlocks;

import java.util.List;
import java.util.function.Predicate;


public class ZiplineItem extends Item {

    public ZiplineItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
//        Level level = pContext.getLevel();
//        BlockPos blockpos = pContext.getClickedPos();
//        BlockState blockstate = level.getBlockState(blockpos);
//        if (blockstate.is(ModBlocks.ANCHOR.get())){ //for vanilla blocks use blockstate.is(BlockTags.BLOCK)
//            Player player = pContext.getPlayer();
//            if (!level.isClientSide && player != null) {
//                bindPlayerMobs(player, level, blockpos);
//            }
//
//        return InteractionResult.m_19078_(level.isClientSide);
//        } else{
//            return InteractionResult.PASS;
//        }
        return null; //remove this line if you ever come back to this idea
    }
    public static InteractionResult bindPlayerMobs(Player pPlayer, Level pLevel, BlockPos pPos) {
        LeashFenceKnotEntity ziplineanchorknotentity = null;
        List<Leashable> list = LeashLimit(pLevel, pPos, pleash -> pleash.getLeashHolder() == pPlayer);

        for (Leashable leashable : list) {
            if (ziplineanchorknotentity == null) {
                ziplineanchorknotentity = LeashFenceKnotEntity.getOrCreateKnot(pLevel, pPos);
                ziplineanchorknotentity.playPlacementSound();
            }

            leashable.setLeashedTo(ziplineanchorknotentity, true);
        }

        if(!list.isEmpty()){
            pLevel.gameEvent(GameEvent.BLOCK_ATTACH,pPos, GameEvent.Context.of(pPlayer));
            return InteractionResult.SUCCESS;
        } else{
            return InteractionResult.PASS;
        }
    }

    public static List<Leashable> LeashLimit(Level pLevel, BlockPos bPos, Predicate<Leashable> canLeash){
        double zipLength = 30.0;
        int blockX = bPos.getX();
        int blockY = bPos.getY();
        int blockZ = bPos.getZ();
        AABB aabb = new AABB((double) blockX - 30.0, (double) blockY - 30.0, (double) blockZ - 30.0,
                (double) blockX + 30.0, (double) blockY + 30.0, (double) blockZ + 30.0);
        return pLevel.getEntitiesOfClass(Entity.class, aabb, pEntity ->{
            if (pEntity instanceof Leashable leashable && canLeash.test(leashable)){
                return true;
            }

            return false;
        }).stream().map(Leashable.class::cast).toList();
    }
}
