package micdoodle8.mods.galacticraft.core.dimension;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer1;

public enum GCVoronoiZoomLayer implements AreaTransformer1
{
    INSTANCE;

    private static final int ZOOM_BITS = 2;

    @Override
    public int applyPixel(BigContext<?> context, Area area, int x, int y)
    {
        int absx = x - 2;
        int absy = y - 2;
        int shiftedAbsX = absx >> 2;
        int shiftedAbsY = absy >> 2;
        int backX = shiftedAbsX << 2;
        int backY = shiftedAbsY << 2;
        context.initRandom(backX, backY);
        double offsetX = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D;
        double offsetY = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D;
        context.initRandom(backX + 4, backY);
        double offsetYY = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double offsetXY = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D;
        context.initRandom(backX, backY + 4);
        double offsetYX = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D;
        double offsetXX = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        context.initRandom(backX + 4, backY + 4);
        double offsetYXY = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double offsetXXY = ((double) context.nextRandom(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        int xx = absx & 3;
        int yy = absy & 3;
        // TODO performance improve -> MathHelper
        double a0 = Math.abs(yy - offsetY) + Math.abs(xx - offsetX);
        //double a0 = ((double)yy - offsetY) * ((double)yy - offsetY) + ((double)xx - offsetX) * ((double)xx - offsetX);
        double a1 = Math.abs(yy - offsetXY) + Math.abs(xx - offsetYY);
        //double a1 = ((double)yy - offsetXY) * ((double)yy - offsetXY) + ((double)xx - offsetYY) * ((double)xx - offsetYY);
        double a2 = Math.abs(yy - offsetXX) + Math.abs(xx - offsetYX);
        //double a2 = ((double)yy - offsetXX) * ((double)yy - offsetXX) + ((double)xx - offsetYX) * ((double)xx - offsetYX);
        double a3 = Math.abs(yy - offsetXXY) + Math.abs(xx - offsetYXY);
        //double a3 = ((double)yy - offsetXXY) * ((double)yy - offsetXXY) + ((double)xx - offsetYXY) * ((double)xx - offsetYXY);
        if (a0 < a1 && a0 < a2 && a0 < a3)
        {
            return area.get(this.getParentX(backX), this.getParentY(backY));
        }
        else if (a1 < a0 && a1 < a2 && a1 < a3)
        {
            return area.get(this.getParentX(backX + 4), this.getParentY(backY)) & 255;
        }
        else
        {
            return a2 < a0 && a2 < a1 && a2 < a3 ? area.get(this.getParentX(backX), this.getParentY(backY + 4)) : area.get(this.getParentX(backX + 4), this.getParentY(backY + 4)) & 255;
        }
    }

    @Override
    public int getParentX(int x)
    {
        return x >> ZOOM_BITS;
    }

    @Override
    public int getParentY(int z)
    {
        return z >> ZOOM_BITS;
    }
}