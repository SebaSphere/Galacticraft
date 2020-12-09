package team.galacticraft.galacticraft.common.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import team.galacticraft.galacticraft.core.entities.EntityTier1Rocket;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelRocketTier1 extends EntityModel<EntityTier1Rocket>
{
    ModelPart insideRoof;
    ModelPart rocketBase1;
    ModelPart rocketBase2;
    ModelPart tip;
    ModelPart wing4d;
    ModelPart wing4c;
    ModelPart wing4e;
    ModelPart wing4b;
    ModelPart wing4a;
    ModelPart wing1a;
    ModelPart wing1b;
    ModelPart wing1c;
    ModelPart wing1e;
    ModelPart wing1d;
    ModelPart wing2e;
    ModelPart wing2d;
    ModelPart wing2c;
    ModelPart wing2b;
    ModelPart wing2a;
    ModelPart wing3e;
    ModelPart wing3d;
    ModelPart wing3c;
    ModelPart wing3b;
    ModelPart wing3a;
    ModelPart top1;
    ModelPart top2;
    ModelPart top3;
    ModelPart top4;
    ModelPart top5;
    ModelPart top6;
    ModelPart top7;
    ModelPart insideBottom;
    ModelPart insideLeft;
    ModelPart insidetop;
    ModelPart rocketBase3;
    ModelPart insideRight;
    ModelPart insideSideLeft;
    ModelPart insideSideRight;
    ModelPart insideSideBack;
    ModelPart insideFloor;

    public ModelRocketTier1()
    {
        this.texWidth = 256;
        this.texHeight = 256;

        this.insideRoof = new ModelPart(this, 0, 59);
        this.insideRoof.addBox(-9F, -45F, -9F, 18, 1, 18);
        this.insideRoof.setPos(0F, 23F, 0F);
        this.insideRoof.setTexSize(256, 256);
        this.insideRoof.mirror = true;
        this.setRotation(this.insideRoof, 0F, 0F, 0F);
        this.rocketBase1 = new ModelPart(this, 0, 0);
        this.rocketBase1.addBox(-7F, -1F, -7F, 14, 1, 14);
        this.rocketBase1.setPos(0F, 24F, 0F);
        this.rocketBase1.setTexSize(256, 256);
        this.rocketBase1.mirror = true;
        this.setRotation(this.rocketBase1, 0F, 0F, 0F);
        this.rocketBase2 = new ModelPart(this, 0, 15);
        this.rocketBase2.addBox(-6F, -2F, -6F, 12, 1, 12);
        this.rocketBase2.setPos(0F, 24F, 0F);
        this.rocketBase2.setTexSize(256, 256);
        this.rocketBase2.mirror = true;
        this.setRotation(this.rocketBase2, 0F, 0F, 0F);
        this.tip = new ModelPart(this, 248, 144);
        this.tip.addBox(-1F, -76F, -1F, 2, 18, 2);
        this.tip.setPos(0F, 24F, 0F);
        this.tip.setTexSize(256, 256);
        this.tip.mirror = true;
        this.setRotation(this.tip, 0F, 0F, 0F);
        this.wing4d = new ModelPart(this, 66, 0);
        this.wing4d.addBox(11F, -14F, -1F, 2, 8, 2);
        this.wing4d.setPos(0F, 24F, 0F);
        this.wing4d.setTexSize(256, 256);
        this.wing4d.mirror = true;
        this.setRotation(this.wing4d, 0F, 0F, 0F);
        this.wing4c = new ModelPart(this, 66, 0);
        this.wing4c.addBox(13F, -12F, -1F, 2, 8, 2);
        this.wing4c.setPos(0F, 24F, 0F);
        this.wing4c.setTexSize(256, 256);
        this.wing4c.mirror = true;
        this.setRotation(this.wing4c, 0F, 0F, 0F);
        this.wing4e = new ModelPart(this, 66, 0);
        this.wing4e.addBox(9.1F, -15F, -1F, 2, 8, 2);
        this.wing4e.setPos(0F, 24F, 0F);
        this.wing4e.setTexSize(256, 256);
        this.wing4e.mirror = true;
        this.setRotation(this.wing4e, 0F, 0F, 0F);
        this.wing4b = new ModelPart(this, 66, 0);
        this.wing4b.addBox(15F, -9F, -1F, 2, 8, 2);
        this.wing4b.setPos(0F, 24F, 0F);
        this.wing4b.setTexSize(256, 256);
        this.wing4b.mirror = true;
        this.setRotation(this.wing4b, 0F, 0F, 0F);
        this.wing4a = new ModelPart(this, 74, 0);
        this.wing4a.addBox(17F, -14F, -1F, 1, 15, 2);
        this.wing4a.setPos(0F, 24F, 0F);
        this.wing4a.setTexSize(256, 256);
        this.wing4a.mirror = true;
        this.setRotation(this.wing4a, 0F, 0F, 0F);
        this.wing1a = new ModelPart(this, 60, 0);
        this.wing1a.addBox(-1F, -14F, -18F, 2, 15, 1);
        this.wing1a.setPos(0F, 24F, 0F);
        this.wing1a.setTexSize(256, 256);
        this.wing1a.mirror = true;
        this.setRotation(this.wing1a, 0F, 0F, 0F);
        this.wing1b = new ModelPart(this, 66, 0);
        this.wing1b.addBox(-1F, -9F, -17F, 2, 8, 2);
        this.wing1b.setPos(0F, 24F, 0F);
        this.wing1b.setTexSize(256, 256);
        this.wing1b.mirror = true;
        this.setRotation(this.wing1b, 0F, 0F, 0F);
        this.wing1c = new ModelPart(this, 66, 0);
        this.wing1c.addBox(-1F, -12F, -15F, 2, 8, 2);
        this.wing1c.setPos(0F, 24F, 0F);
        this.wing1c.setTexSize(256, 256);
        this.wing1c.mirror = true;
        this.setRotation(this.wing1c, 0F, 0F, 0F);
        this.wing1e = new ModelPart(this, 66, 0);
        this.wing1e.addBox(-1F, -15F, -11.1F, 2, 8, 2);
        this.wing1e.setPos(0F, 24F, 0F);
        this.wing1e.setTexSize(256, 256);
        this.wing1e.mirror = true;
        this.setRotation(this.wing1e, 0F, 0F, 0F);
        this.wing1d = new ModelPart(this, 66, 0);
        this.wing1d.addBox(-1F, -14F, -13F, 2, 8, 2);
        this.wing1d.setPos(0F, 24F, 0F);
        this.wing1d.setTexSize(256, 256);
        this.wing1d.mirror = true;
        this.setRotation(this.wing1d, 0F, 0F, 0F);
        this.wing2e = new ModelPart(this, 66, 0);
        this.wing2e.addBox(-11.1F, -15F, -1F, 2, 8, 2);
        this.wing2e.setPos(0F, 24F, 0F);
        this.wing2e.setTexSize(256, 256);
        this.wing2e.mirror = true;
        this.setRotation(this.wing2e, 0F, 0F, 0F);
        this.wing2d = new ModelPart(this, 66, 0);
        this.wing2d.addBox(-13F, -14F, -1F, 2, 8, 2);
        this.wing2d.setPos(0F, 24F, 0F);
        this.wing2d.setTexSize(256, 256);
        this.wing2d.mirror = true;
        this.setRotation(this.wing2d, 0F, 0F, 0F);
        this.wing2c = new ModelPart(this, 66, 0);
        this.wing2c.addBox(-15F, -12F, -1F, 2, 8, 2);
        this.wing2c.setPos(0F, 24F, 0F);
        this.wing2c.setTexSize(256, 256);
        this.wing2c.mirror = true;
        this.setRotation(this.wing2c, 0F, 0F, 0F);
        this.wing2b = new ModelPart(this, 66, 0);
        this.wing2b.addBox(-17F, -9F, -1F, 2, 8, 2);
        this.wing2b.setPos(0F, 24F, 0F);
        this.wing2b.setTexSize(256, 256);
        this.wing2b.mirror = true;
        this.setRotation(this.wing2b, 0F, 0F, 0F);
        this.wing2a = new ModelPart(this, 74, 0);
        this.wing2a.addBox(-18F, -14F, -1F, 1, 15, 2);
        this.wing2a.setPos(0F, 24F, 0F);
        this.wing2a.setTexSize(256, 256);
        this.wing2a.mirror = true;
        this.setRotation(this.wing2a, 0F, 0F, 0F);
        this.wing3e = new ModelPart(this, 66, 0);
        this.wing3e.addBox(-1F, -15F, 9.1F, 2, 8, 2);
        this.wing3e.setPos(0F, 24F, 0F);
        this.wing3e.setTexSize(256, 256);
        this.wing3e.mirror = true;
        this.setRotation(this.wing3e, 0F, 0F, 0F);
        this.wing3d = new ModelPart(this, 66, 0);
        this.wing3d.addBox(-1F, -14F, 11F, 2, 8, 2);
        this.wing3d.setPos(0F, 24F, 0F);
        this.wing3d.setTexSize(256, 256);
        this.wing3d.mirror = true;
        this.setRotation(this.wing3d, 0F, 0F, 0F);
        this.wing3c = new ModelPart(this, 66, 0);
        this.wing3c.addBox(-1F, -12F, 13F, 2, 8, 2);
        this.wing3c.setPos(0F, 24F, 0F);
        this.wing3c.setTexSize(256, 256);
        this.wing3c.mirror = true;
        this.setRotation(this.wing3c, 0F, 0F, 0F);
        this.wing3b = new ModelPart(this, 66, 0);
        this.wing3b.addBox(-1F, -9F, 15F, 2, 8, 2);
        this.wing3b.setPos(0F, 24F, 0F);
        this.wing3b.setTexSize(256, 256);
        this.wing3b.mirror = true;
        this.setRotation(this.wing3b, 0F, 0F, 0F);
        this.wing3a = new ModelPart(this, 60, 0);
        this.wing3a.addBox(-1F, -14F, 17F, 2, 15, 1);
        this.wing3a.setPos(0F, 24F, 0F);
        this.wing3a.setTexSize(256, 256);
        this.wing3a.mirror = true;
        this.setRotation(this.wing3a, 0F, 0F, 0F);
        this.top1 = new ModelPart(this, 192, 60);
        this.top1.addBox(-8F, -48F, -8F, 16, 2, 16);
        this.top1.setPos(0F, 24F, 0F);
        this.top1.setTexSize(256, 256);
        this.top1.mirror = true;
        this.setRotation(this.top1, 0F, 0F, 0F);
        this.top2 = new ModelPart(this, 200, 78);
        this.top2.addBox(-7F, -50F, -7F, 14, 2, 14);
        this.top2.setPos(0F, 24F, 0F);
        this.top2.setTexSize(256, 256);
        this.top2.mirror = true;
        this.setRotation(this.top2, 0F, 0F, 0F);
        this.top3 = new ModelPart(this, 208, 94);
        this.top3.addBox(-6F, -52F, -6F, 12, 2, 12);
        this.top3.setPos(0F, 24F, 0F);
        this.top3.setTexSize(256, 256);
        this.top3.mirror = true;
        this.setRotation(this.top3, 0F, 0F, 0F);
        this.top4 = new ModelPart(this, 216, 108);
        this.top4.addBox(-5F, -54F, -5F, 10, 2, 10);
        this.top4.setPos(0F, 24F, 0F);
        this.top4.setTexSize(256, 256);
        this.top4.mirror = true;
        this.setRotation(this.top4, 0F, 0F, 0F);
        this.top5 = new ModelPart(this, 224, 120);
        this.top5.addBox(-4F, -56F, -4F, 8, 2, 8);
        this.top5.setPos(0F, 24F, 0F);
        this.top5.setTexSize(256, 256);
        this.top5.mirror = true;
        this.setRotation(this.top5, 0F, 0F, 0F);
        this.top6 = new ModelPart(this, 232, 130);
        this.top6.addBox(-3F, -58F, -3F, 6, 2, 6);
        this.top6.setPos(0F, 24F, 0F);
        this.top6.setTexSize(256, 256);
        this.top6.mirror = true;
        this.setRotation(this.top6, 0F, 0F, 0F);
        this.top7 = new ModelPart(this, 240, 138);
        this.top7.addBox(-2F, -60F, -2F, 4, 2, 4);
        this.top7.setPos(0F, 24F, 0F);
        this.top7.setTexSize(256, 256);
        this.top7.mirror = true;
        this.setRotation(this.top7, 0F, 0F, 0F);
        this.insideBottom = new ModelPart(this, 85, 18);
        this.insideBottom.addBox(-3.9F, -22F, -8.9F, 8, 17, 1);
        this.insideBottom.setPos(0F, 24F, 0F);
        this.insideBottom.setTexSize(256, 256);
        this.insideBottom.mirror = true;
        this.setRotation(this.insideBottom, 0F, 0F, 0F);
        this.insideLeft = new ModelPart(this, 103, 0);
        this.insideLeft.addBox(3.9F, -46F, -8.9F, 5, 41, 1);
        this.insideLeft.setPos(0F, 24F, 0F);
        this.insideLeft.setTexSize(256, 256);
        this.insideLeft.mirror = true;
        this.setRotation(this.insideLeft, 0F, 0F, 0F);
        this.insidetop = new ModelPart(this, 85, 0);
        this.insidetop.addBox(-3.9F, -46F, -8.9F, 8, 17, 1);
        this.insidetop.setPos(0F, 24F, 0F);
        this.insidetop.setTexSize(256, 256);
        this.insidetop.mirror = true;
        this.setRotation(this.insidetop, 0F, 0F, 0F);
        this.rocketBase3 = new ModelPart(this, 0, 28);
        this.rocketBase3.addBox(-5F, -4F, -5F, 10, 2, 10);
        this.rocketBase3.setPos(0F, 24F, 0F);
        this.rocketBase3.setTexSize(256, 256);
        this.rocketBase3.mirror = true;
        this.setRotation(this.rocketBase3, 0F, 0F, 0F);
        this.insideRight = new ModelPart(this, 103, 42);
        this.insideRight.addBox(-8.9F, -46F, -8.9F, 5, 41, 1);
        this.insideRight.setPos(0F, 24F, 0F);
        this.insideRight.setTexSize(256, 256);
        this.insideRight.mirror = true;
        this.setRotation(this.insideRight, 0F, 0F, 0F);
        this.insideSideLeft = new ModelPart(this, 119, 57);
        this.insideSideLeft.addBox(8.1F, -46F, -7.9F, 1, 41, 17);
        this.insideSideLeft.setPos(0F, 24F, 0F);
        this.insideSideLeft.setTexSize(256, 256);
        this.insideSideLeft.mirror = true;
        this.setRotation(this.insideSideLeft, 0F, 0F, 0F);
        this.insideSideRight = new ModelPart(this, 120, 0);
        this.insideSideRight.addBox(-8.9F, -46F, -7.9F, 1, 41, 16);
        this.insideSideRight.setPos(0F, 24F, 0F);
        this.insideSideRight.setTexSize(256, 256);
        this.insideSideRight.mirror = true;
        this.setRotation(this.insideSideRight, 0F, 0F, 0F);
        this.insideSideBack = new ModelPart(this, 120, 114);
        this.insideSideBack.addBox(-8.9F, -46F, 8.1F, 17, 41, 1);
        this.insideSideBack.setPos(0F, 24F, 0F);
        this.insideSideBack.setTexSize(256, 256);
        this.insideSideBack.mirror = true;
        this.setRotation(this.insideSideBack, 0F, 0F, 0F);
        this.insideFloor = new ModelPart(this, 0, 40);
        this.insideFloor.addBox(-9F, -4F, -9F, 18, 1, 18);
        this.insideFloor.setPos(0F, 23F, 0F);
        this.insideFloor.setTexSize(256, 256);
        this.insideFloor.mirror = true;
        this.setRotation(this.insideFloor, 0F, 0F, 0F);
    }

    @Override
    public void setRotationAngles(EntityTier1Rocket entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        matrixStackIn.pushPose();

        this.insideRoof.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rocketBase1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rocketBase2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.tip.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing4d.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing4c.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing4e.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing4b.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing4a.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing1a.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing1b.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing1c.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing1e.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing1d.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing2e.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing2d.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing2c.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing2b.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing2a.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing3e.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing3d.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing3c.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing3b.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.wing3a.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.top1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.top2.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.top3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.top4.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.top5.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.top6.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.top7.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insideBottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insideLeft.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insidetop.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.rocketBase3.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insideRight.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insideSideLeft.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insideSideRight.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insideSideBack.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        this.insideFloor.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        matrixStackIn.popPose();
    }

    private void setRotation(ModelPart model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
