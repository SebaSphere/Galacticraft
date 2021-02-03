package micdoodle8.mods.galacticraft.core.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.resources.ResourceLocation;

public class AdvancementProviderGC extends AdvancementProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;
    private final List<Consumer<Consumer<Advancement>>> advancements = ImmutableList.of(new GCAdvancements());

    public AdvancementProviderGC(DataGenerator generator)
    {
        super(generator);
        this.generator = generator;
    }

    @Override
    public void run(HashCache cache) throws IOException
    {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = advancement ->
        {
            if (!set.add(advancement.getId()))
            {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            }
            else
            {
                Path path1 = createPath(path, advancement);

                try
                {
                    DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path1);
                }
                catch (IOException e)
                {
                    LOGGER.error("Couldn't save advancement {}", path1, e);
                }
            }
        };

        for (Consumer<Consumer<Advancement>> consumer1 : this.advancements)
        {
            consumer1.accept(consumer);
        }
    }

    private static Path createPath(Path path, Advancement advancement)
    {
        return path.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
    }
}