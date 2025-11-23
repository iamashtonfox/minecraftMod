package net.shakyturd.myfirstmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.shakyturd.myfirstmod.entity.ModEntities;
import org.jetbrains.annotations.NotNull;

public class MagicMissileEntity extends Projectile {

    private static final EntityDataAccessor<Integer> TYPE =
            SynchedEntityData.defineId(MagicMissileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(MagicMissileEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COUNT =
            SynchedEntityData.defineId(MagicMissileEntity.class, EntityDataSerializers.INT);

    public MagicMissileEntity(EntityType<? extends Projectile> entityType, Level plevel){
        super(entityType, plevel);
    }

    public MagicMissileEntity(Level pLevel, Player pPlayer){
        this(ModEntities.MAGIC_PROJECTILE.get(), pLevel);
        setOwner(pPlayer);
        BlockPos blockpos = pPlayer.blockPosition();
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 1.75D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        this.setPosRaw(d0,d1,d2);

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(TYPE, 0);
        pBuilder.define(COUNT, 0);
        pBuilder.define(HIT, false);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entityData.get(HIT)){
            this.entityData.set(COUNT, this.entityData.get(COUNT)+1);
            if(this.entityData.get(COUNT) >= 5){
                this.destroy();
            }
        }
        if (this.tickCount >= 150) {
            this.remove(RemovalReason.DISCARDED);
        }
        Vec3 vec3 = this.getDeltaMovement();
        Vec3 vec32 = vec3.add(vec3);
        HitResult hitResult = this.level().clip(new ClipContext(vec3, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS){
            vec32 = hitResult.getLocation();
        }
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        double d5 = vec3.x;
        double d6 = vec3.y;
        double d7 = vec3.z;

        for(int i = 1; i < 5; i++){
            this.level().addParticle(ParticleTypes.SONIC_BOOM,
                    d0-(d5*2),
                    d1-(d6*2),
                    d2-(d7*2),
                    -d5,
                    -d6 - 0.1D, -d7);
        }

        if(this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        }else{
            this.setPos(d0, d1, d2);
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity hitEntity = pResult.getEntity();
        Entity owner = this.getOwner();

        if(hitEntity == owner || this.level().isClientSide){
            return;
        }

        LivingEntity livingentity = owner instanceof LivingEntity ? (LivingEntity)owner : null;
        float damage = 8f;
        boolean hurt = hitEntity.hurt(this.damageSources().mobProjectile(this, livingentity), damage); // for later use for additional effects on hit
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        for(int x = 0; x < 18; x++) {
            for (int y = 0; 0 < 18; y++){
                this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX(), this.getY(), this.getZ(),
                        Math.cos(x*20) *0.15d, Math.cos(y*20) * 0.15d, Math.sin(x*20) *0.15d);
            }
        }
        if(this.level().isClientSide()){
            return;
        }
        if(pResult.getType() == HitResult.Type.ENTITY && pResult instanceof EntityHitResult entityHitResult) {
            Entity hit = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if(owner != hit) {
                this.entityData.set(HIT, true);
            }
        }else{
            this.entityData.set(HIT,true);
        }
    }
    private void destroy(){
        this.discard();
        this.level().gameEvent(GameEvent.ENTITY_DAMAGE, this.position(), GameEvent.Context.of(this));
    }
}
