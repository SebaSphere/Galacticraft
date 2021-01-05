package micdoodle8.mods.galacticraft.core.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.entities.Tier1RocketEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class Tier1RocketModel extends EntityModel<Tier1RocketEntity>
{
    ModelRenderer insideRoof;
    ModelRenderer rocketBase1;
    ModelRenderer rocketBase2;
    ModelRenderer tip;
    ModelRenderer wing4d;
    ModelRenderer wing4c;
    ModelRenderer wing4e;
    ModelRenderer wing4b;
    ModelRenderer wing4a;
    ModelRenderer wing1a;
    ModelRenderer wing1b;
    ModelRenderer wing1c;
    ModelRenderer wing1e;
    ModelRenderer wing1d;
    ModelRenderer wing2e;
    ModelRenderer wing2d;
    ModelRenderer wing2c;
    ModelRenderer wing2b;
    ModelRenderer wing2a;
    ModelRenderer wing3e;
    ModelRenderer wing3d;
    ModelRenderer wing3c;
    ModelRenderer wing3b;
    ModelRenderer wing3a;
    ModelRenderer top1;
    ModelRenderer top2;
    ModelRenderer top3;
    ModelRenderer top4;
    ModelRenderer top5;
    ModelRenderer top6;
    ModelRenderer top7;
    ModelRenderer insideBottom;
    ModelRenderer insideLeft;
    ModelRenderer insidetop;
    ModelRenderer rocketBase3;
    ModelRenderer insideRight;
    ModelRenderer insideSideLeft;
    ModelRenderer insideSideRight;
    ModelRenderer insideSideBack;
    ModelRenderer insideFloor;

    public Tier1RocketModel()
    {
        this.textureWidth = 256;
        this.textureHeight = 256;

        this.insideRoof = new ModelRenderer(this, 0, 59);
        this.insideRoof.addBox(-9F, -45F, -9F, 18, 1, 18);
        this.insideRoof.setRotationPoint(0F, 23F, 0F);
        this.insideRoof.setTextureSize(256, 256);
        this.insideRoof.mirror = true;
        this.setRotation(this.insideRoof, 0F, 0F, 0F);
        this.rocketBase1 = new ModelRenderer(this, 0, 0);
        this.rocketBase1.addBox(-7F, -1F, -7F, 14, 1, 14);
        this.rocketBase1.setRotationPoint(0F, 24F, 0F);
        this.rocketBase1.setTextureSize(256, 256);
        this.rocketBase1.mirror = true;
        this.setRotation(this.rocketBase1, 0F, 0F, 0F);
        this.rocketBase2 = new ModelRenderer(this, 0, 15);
        this.rocketBase2.addBox(-6F, -2F, -6F, 12, 1, 12);
        this.rocketBase2.setRotationPoint(0F, 24F, 0F);
        this.rocketBase2.setTextureSize(256, 256);
        this.rocketBase2.mirror = true;
        this.setRotation(this.rocketBase2, 0F, 0F, 0F);
        this.tip = new ModelRenderer(this, 248, 144);
        this.tip.addBox(-1F, -76F, -1F, 2, 18, 2);
        this.tip.setRotationPoint(0F, 24F, 0F);
        this.tip.setTextureSize(256, 256);
        this.tip.mirror = true;
        this.setRotation(this.tip, 0F, 0F, 0F);
        this.wing4d = new ModelRenderer(this, 66, 0);
        this.wing4d.addBox(11F, -14F, -1F, 2, 8, 2);
        this.wing4d.setRotationPoint(0F, 24F, 0F);
        this.wing4d.setTextureSize(256, 256);
        this.wing4d.mirror = true;
        this.setRotation(this.wing4d, 0F, 0F, 0F);
        this.wing4c = new ModelRenderer(this, 66, 0);
        this.wing4c.addBox(13F, -12F, -1F, 2, 8, 2);
        this.wing4c.setRotationPoint(0F, 24F, 0F);
        this.wing4c.setTextureSize(256, 256);
        this.wing4c.mirror = true;
        this.setRotation(this.wing4c, 0F, 0F, 0F);
        this.wing4e = new ModelRenderer(this, 66, 0);
        this.wing4e.addBox(9.1F, -15F, -1F, 2, 8, 2);
        this.wing4e.setRotationPoint(0F, 24F, 0F);
        this.wing4e.setTextureSize(256, 256);
        this.wing4e.mirror = true;
        this.setRotation(this.wing4e, 0F, 0F, 0F);
        this.wing4b = new ModelRenderer(this, 66, 0);
        this.wing4b.addBox(15F, -9F, -1F, 2, 8, 2);
        this.wing4b.setRotationPoint(0F, 24F, 0F);
        this.wing4b.setTextureSize(256, 256);
        this.wing4b.mirror = true;
        this.setRotation(this.wing4b, 0F, 0F, 0F);
        this.wing4a = new ModelRenderer(this, 74, 0);
        this.wing4a.addBox(17F, -14F, -1F, 1, 15, 2);
        this.wing4a.setRotationPoint(0F, 24F, 0F);
        this.wing4a.setTextureSize(256, 256);
        this.wing4a.mirror = true;
        this.setRotation(this.wing4a, 0F, 0F, 0F);
        this.wing1a = new ModelRenderer(this, 60, 0);
        this.wing1a.addBox(-1F, -14F, -18F, 2, 15, 1);
        this.wing1a.setRotationPoint(0F, 24F, 0F);
        this.wing1a.setTextureSize(256, 256);
        this.wing1a.mirror = true;
        this.setRotation(this.wing1a, 0F, 0F, 0F);
        this.wing1b = new ModelRenderer(this, 66, 0);
        this.wing1b.addBox(-1F, -9F, -17F, 2, 8, 2);
        this.wing1b.setRotationPoint(0F, 24F, 0F);
        this.wing1b.setTextureSize(256, 256);
        this.wing1b.mirror = true;
        this.setRotation(this.wing1b, 0F, 0F, 0F);
        this.wing1c = new ModelRenderer(this, 66, 0);
        this.wing1c.addBox(-1F, -12F, -15F, 2, 8, 2);
        this.wing1c.setRotationPoint(0F, 24F, 0F);
        this.wing1c.setTextureSize(256, 256);
        this.wing1c.mirror = true;
        this.setRotation(this.wing1c, 0F, 0F, 0F);
        this.wing1e = new ModelRenderer(this, 66, 0);
        this.wing1e.addBox(-1F, -15F, -11.1F, 2, 8, 2);
        this.wing1e.setRotationPoint(0F, 24F, 0F);
        this.wing1e.setTextureSize(256, 256);
        this.wing1e.mirror = true;
        this.setRotation(this.wing1e, 0F, 0F, 0F);
        this.wing1d = new ModelRenderer(this, 66, 0);
        this.wing1d.addBox(-1F, -14F, -13F, 2, 8, 2);
        this.wing1d.setRotationPoint(0F, 24F, 0F);
        this.wing1d.setTextureSize(256, 256);
        this.wing1d.mirror = true;
        this.setRotation(this.wing1d, 0F, 0F, 0F);
        this.wing2e = new ModelRenderer(this, 66, 0);
        this.wing2e.addBox(-11.1F, -15F, -1F, 2, 8, 2);
        this.wing2e.setRotationPoint(0F, 24F, 0F);
        this.wing2e.setTextureSize(256, 256);
        this.wing2e.mirror = true;
        this.setRotation(this.wing2e, 0F, 0F, 0F);
        this.wing2d = new ModelRenderer(this, 66, 0);
        this.wing2d.addBox(-13F, -14F, -1F, 2, 8, 2);
        this.wing2d.setRotationPoint(0F, 24F, 0F);
        this.wing2d.setTextureSize(256, 256);
        this.wing2d.mirror = true;
        this.setRotation(this.wing2d, 0F, 0F, 0F);
        this.wing2c = new ModelRenderer(this, 66, 0);
        this.wing2c.addBox(-15F, -12F, -1F, 2, 8, 2);
        this.wing2c.setRotationPoint(0F, 24F, 0F);
        this.wing2c.setTextureSize(256, 256);
        this.wing2c.mirror = true;
        this.setRotation(this.wing2c, 0F, 0F, 0F);
        this.wing2b = new ModelRenderer(this, 66, 0);
        this.wing2b.addBox(-17F, -9F, -1F, 2, 8, 2);
        this.wing2b.setRotationPoint(0F, 24F, 0F);
        this.wing2b.setTextureSize(256, 256);
        this.wing2b.mirror = true;
        this.setRotation(this.wing2b, 0F, 0F, 0F);
        this.wing2a = new ModelRenderer(this, 74, 0);
        this.wing2a.addBox(-18F, -14F, -1F, 1, 15, 2);
        this.wing2a.setRotationPoint(0F, 24F, 0F);
        this.wing2a.setTextureSize(256, 256);
        this.wing2a.mirror = true;
        this.setRotation(this.wing2a, 0F, 0F, 0F);
        this.wing3e = new ModelRenderer(this, 66, 0);
        this.wing3e.addBox(-1F, -15F, 9.1F, 2, 8, 2);
        this.wing3e.setRotationPoint(0F, 24F, 0F);
        this.wing3e.setTextureSize(256, 256);
        this.wing3e.mirror = true;
        this.setRotation(this.wing3e, 0F, 0F, 0F);
        this.wing3d = new ModelRenderer(this, 66, 0);
        this.wing3d.addBox(-1F, -14F, 11F, 2, 8, 2);
        this.wing3d.setRotationPoint(0F, 24F, 0F);
        this.wing3d.setTextureSize(256, 256);
        this.wing3d.mirror = true;
        this.setRotation(this.wing3d, 0F, 0F, 0F);
        this.wing3c = new ModelRenderer(this, 66, 0);
        this.wing3c.addBox(-1F, -12F, 13F, 2, 8, 2);
        this.wing3c.setRotationPoint(0F, 24F, 0F);
        this.wing3c.setTextureSize(256, 256);
        this.wing3c.mirror = true;
        this.setRotation(this.wing3c, 0F, 0F, 0F);
        this.wing3b = new ModelRenderer(this, 66, 0);
        this.wing3b.addBox(-1F, -9F, 15F, 2, 8, 2);
        this.wing3b.setRotationPoint(0F, 24F, 0F);
        this.wing3b.setTextureSize(256, 256);
        this.wing3b.mirror = true;
        this.setRotation(this.wing3b, 0F, 0F, 0F);
        this.wing3a = new ModelRenderer(this, 60, 0);
        this.wing3a.addBox(-1F, -14F, 17F, 2, 15, 1);
        this.wing3a.setRotationPoint(0F, 24F, 0F);
        this.wing3a.setTextureSize(256, 256);
        this.wing3a.mirror = true;
        this.setRotation(this.wing3a, 0F, 0F, 0F);
        this.top1 = new ModelRenderer(this, 192, 60);
        this.top1.addBox(-8F, -48F, -8F, 16, 2, 16);
        this.top1.setRotationPoint(0F, 24F, 0F);
        this.top1.setTextureSize(256, 256);
        this.top1.mirror = true;
        this.setRotation(this.top1, 0F, 0F, 0F);
        this.top2 = new ModelRenderer(this, 200, 78);
        this.top2.addBox(-7F, -50F, -7F, 14, 2, 14);
        this.top2.setRotationPoint(0F, 24F, 0F);
        this.top2.setTextureSize(256, 256);
        this.top2.mirror = true;
        this.setRotation(this.top2, 0F, 0F, 0F);
        this.top3 = new ModelRenderer(this, 208, 94);
        this.top3.addBox(-6F, -52F, -6F, 12, 2, 12);
        this.top3.setRotationPoint(0F, 24F, 0F);
        this.top3.setTextureSize(256, 256);
        this.top3.mirror = true;
        this.setRotation(this.top3, 0F, 0F, 0F);
        this.top4 = new ModelRenderer(this, 216, 108);
        this.top4.addBox(-5F, -54F, -5F, 10, 2, 10);
        this.top4.setRotationPoint(0F, 24F, 0F);
        this.top4.setTextureSize(256, 256);
        this.top4.mirror = true;
        this.setRotation(this.top4, 0F, 0F, 0F);
        this.top5 = new ModelRenderer(this, 224, 120);
        this.top5.addBox(-4F, -56F, -4F, 8, 2, 8);
        this.top5.setRotationPoint(0F, 24F, 0F);
        this.top5.setTextureSize(256, 256);
        this.top5.mirror = true;
        this.setRotation(this.top5, 0F, 0F, 0F);
        this.top6 = new ModelRenderer(this, 232, 130);
        this.top6.addBox(-3F, -58F, -3F, 6, 2, 6);
        this.top6.setRotationPoint(0F, 24F, 0F);
        this.top6.setTextureSize(256, 256);
        this.top6.mirror = true;
        this.setRotation(this.top6, 0F, 0F, 0F);
        this.top7 = new ModelRenderer(this, 240, 138);
        this.top7.addBox(-2F, -60F, -2F, 4, 2, 4);
        this.top7.setRotationPoint(0F, 24F, 0F);
        this.top7.setTextureSize(256, 256);
        this.top7.mirror = true;
        this.setRotation(this.top7, 0F, 0F, 0F);
        this.insideBottom = new ModelRenderer(this, 85, 18);
        this.insideBottom.addBox(-3.9F, -22F, -8.9F, 8, 17, 1);
        this.insideBottom.setRotationPoint(0F, 24F, 0F);
        this.insideBottom.setTextureSize(256, 256);
        this.insideBottom.mirror = true;
        this.setRotation(this.insideBottom, 0F, 0F, 0F);
        this.insideLeft = new ModelRenderer(this, 103, 0);
        this.insideLeft.addBox(3.9F, -46F, -8.9F, 5, 41, 1);
        this.insideLeft.setRotationPoint(0F, 24F, 0F);
        this.insideLeft.setTextureSize(256, 256);
        this.insideLeft.mirror = true;
        this.setRotation(this.insideLeft, 0F, 0F, 0F);
        this.insidetop = new ModelRenderer(this, 85, 0);
        this.insidetop.addBox(-3.9F, -46F, -8.9F, 8, 17, 1);
        this.insidetop.setRotationPoint(0F, 24F, 0F);
        this.insidetop.setTextureSize(256, 256);
        this.insidetop.mirror = true;
        this.setRotation(this.insidetop, 0F, 0F, 0F);
        this.rocketBase3 = new ModelRenderer(this, 0, 28);
        this.rocketBase3.addBox(-5F, -4F, -5F, 10, 2, 10);
        this.rocketBase3.setRotationPoint(0F, 24F, 0F);
        this.rocketBase3.setTextureSize(256, 256);
        this.rocketBase3.mirror = true;
        this.setRotation(this.rocketBase3, 0F, 0F, 0F);
        this.insideRight = new ModelRenderer(this, 103, 42);
        this.insideRight.addBox(-8.9F, -46F, -8.9F, 5, 41, 1);
        this.insideRight.setRotationPoint(0F, 24F, 0F);
        this.insideRight.setTextureSize(256, 256);
        this.insideRight.mirror = true;
        this.setRotation(this.insideRight, 0F, 0F, 0F);
        this.insideSideLeft = new ModelRenderer(this, 119, 57);
        this.insideSideLeft.addBox(8.1F, -46F, -7.9F, 1, 41, 17);
        this.insideSideLeft.setRotationPoint(0F, 24F, 0F);
        this.insideSideLeft.setTextureSize(256, 256);
        this.insideSideLeft.mirror = true;
        this.setRotation(this.insideSideLeft, 0F, 0F, 0F);
        this.insideSideRight = new ModelRenderer(this, 120, 0);
        this.insideSideRight.addBox(-8.9F, -46F, -7.9F, 1, 41, 16);
        this.insideSideRight.setRotationPoint(0F, 24F, 0F);
        this.insideSideRight.setTextureSize(256, 256);
        this.insideSideRight.mirror = true;
        this.setRotation(this.insideSideRight, 0F, 0F, 0F);
        this.insideSideBack = new ModelRenderer(this, 120, 114);
        this.insideSideBack.addBox(-8.9F, -46F, 8.1F, 17, 41, 1);
        this.insideSideBack.setRotationPoint(0F, 24F, 0F);
        this.insideSideBack.setTextureSize(256, 256);
        this.insideSideBack.mirror = true;
        this.setRotation(this.insideSideBack, 0F, 0F, 0F);
        this.insideFloor = new ModelRenderer(this, 0, 40);
        this.insideFloor.addBox(-9F, -4F, -9F, 18, 1, 18);
        this.insideFloor.setRotationPoint(0F, 23F, 0F);
        this.insideFloor.setTextureSize(256, 256);
        this.insideFloor.mirror = true;
        this.setRotation(this.insideFloor, 0F, 0F, 0F);
    }

    @Override
    public void setRotationAngles(Tier1RocketEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        matrixStack.push();

        this.insideRoof.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rocketBase1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rocketBase2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.tip.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing4d.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing4c.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing4e.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing4b.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing4a.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing1a.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing1b.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing1c.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing1e.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing1d.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing2e.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing2d.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing2c.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing2b.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing2a.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing3e.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing3d.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing3c.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing3b.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wing3a.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.top1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.top2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.top3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.top4.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.top5.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.top6.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.top7.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insideBottom.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insideLeft.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insidetop.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rocketBase3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insideRight.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insideSideLeft.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insideSideRight.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insideSideBack.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.insideFloor.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        matrixStack.pop();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}