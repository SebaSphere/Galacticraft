package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.entities.EvolvedWitchEntity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.WitchModel;

public class EvolvedWitchModel extends WitchModel<EvolvedWitchEntity>
{
    ModelRenderer tank1;
    ModelRenderer tank2;
    ModelRenderer oxygenMask;
    ModelRenderer tube1;
    ModelRenderer tube2;
    ModelRenderer tube3;
    ModelRenderer tube4;
    ModelRenderer tube5;
    ModelRenderer tube6;
    ModelRenderer tube7;
    ModelRenderer tube8;
    ModelRenderer tube9;
    ModelRenderer tube10;
    ModelRenderer tube11;
    ModelRenderer tube12;
    ModelRenderer tube13;
    ModelRenderer tube14;
    ModelRenderer tube15;
    ModelRenderer tube16;
    ModelRenderer tube17;
    ModelRenderer tube18;

    public EvolvedWitchModel()
    {
        super(0.0F);
        this.textureWidth = 64;
        this.textureHeight = 128;

        this.tank1 = new ModelRenderer(this, 52, 66);
        this.tank1.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank1.setRotationPoint(0F, 4F, 3F);

        this.tank2 = new ModelRenderer(this, 52, 66);
        this.tank2.addBox(0F, 0F, 0F, 3, 7, 3);
        this.tank2.setRotationPoint(-3F, 4F, 3F);

        this.oxygenMask = new ModelRenderer(this, 24, 90);
        this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10);
        this.oxygenMask.setRotationPoint(0F, 1F, 0F);

        this.tube1 = new ModelRenderer(this, 60, 76);
        this.tube1.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube1.setRotationPoint(1F, 4.5F, 6F);

        this.tube2 = new ModelRenderer(this, 60, 76);
        this.tube2.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube2.setRotationPoint(1F, 3.5F, 7F);

        this.tube3 = new ModelRenderer(this, 60, 76);
        this.tube3.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube3.setRotationPoint(1F, 2.5F, 7.5F);

        this.tube4 = new ModelRenderer(this, 60, 76);
        this.tube4.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube4.setRotationPoint(1F, 1.5F, 7.5F);

        this.tube5 = new ModelRenderer(this, 60, 76);
        this.tube5.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube5.setRotationPoint(1F, 0.5F, 7.5F);

        this.tube6 = new ModelRenderer(this, 60, 76);
        this.tube6.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube6.setRotationPoint(1F, -0.5F, 7F);

        this.tube7 = new ModelRenderer(this, 60, 76);
        this.tube7.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube7.setRotationPoint(1F, -1.5F, 6F);

        this.tube8 = new ModelRenderer(this, 60, 76);
        this.tube8.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube8.setRotationPoint(1F, -2.5F, 5F);

        this.tube9 = new ModelRenderer(this, 60, 76);
        this.tube9.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube9.setRotationPoint(1F, -3F, 4F);

        this.tube10 = new ModelRenderer(this, 60, 76);
        this.tube10.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube10.setRotationPoint(-2F, 4.5F, 6F);

        this.tube11 = new ModelRenderer(this, 60, 76);
        this.tube11.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube11.setRotationPoint(-2F, 3.5F, 7F);

        this.tube12 = new ModelRenderer(this, 60, 76);
        this.tube12.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube12.setRotationPoint(-2F, 2.5F, 7.5F);

        this.tube13 = new ModelRenderer(this, 60, 76);
        this.tube13.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube13.setRotationPoint(-2F, 1.5F, 7.5F);

        this.tube14 = new ModelRenderer(this, 60, 76);
        this.tube14.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube14.setRotationPoint(-2F, 0.5F, 7.5F);

        this.tube15 = new ModelRenderer(this, 60, 76);
        this.tube15.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube15.setRotationPoint(-2F, -0.5F, 7F);

        this.tube16 = new ModelRenderer(this, 60, 76);
        this.tube16.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube16.setRotationPoint(-2F, -1.5F, 6F);

        this.tube17 = new ModelRenderer(this, 60, 76);
        this.tube17.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube17.setRotationPoint(-2F, -2.5F, 5F);

        this.tube18 = new ModelRenderer(this, 60, 76);
        this.tube18.addBox(0F, 0F, 0F, 1, 1, 1);
        this.tube18.setRotationPoint(-2F, -3F, 4F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tank1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tank2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.oxygenMask.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube8.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube9.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube10.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube11.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube12.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube13.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube14.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube15.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube16.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube17.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.tube18.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    @Override
    public void setRotationAngles(EvolvedWitchEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.oxygenMask.rotateAngleY = this.villagerHead.rotateAngleY;
        this.oxygenMask.rotateAngleX = this.villagerHead.rotateAngleX;
    }
}