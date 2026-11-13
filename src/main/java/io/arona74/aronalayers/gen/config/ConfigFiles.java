package io.arona74.aronalayers.gen.config;

import io.arona74.aronalayers.gen.AronaLayersGen;
import io.arona74.aronalayers.gen.platform.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Where the config files are: {@code config/aronalayers/}.
 *
 * <p>The generator used to be its own mod, with its files in {@code config/aronalayersgen/}. The
 * first time this mod starts without a folder of its own, that folder is copied over, so settings
 * and edited mapping files carry on. The old folder is left as it is.
 */
public final class ConfigFiles {
    private ConfigFiles() {}

    /** Block id prefix of the former generator mod, still found in mapping files it wrote. */
    public static final String OLD_NAMESPACE = "aronalayersgen:";

    public static final Path DIR = Platform.configDir().resolve("aronalayers");

    static {
        Path old = Platform.configDir().resolve("aronalayersgen");
        if (!Files.exists(DIR) && Files.isDirectory(old)) importFrom(old);
    }

    private static void importFrom(Path old) {
        try (Stream<Path> paths = Files.walk(old)) {
            List<Path> sources = paths.toList();
            for (Path source : sources) {
                Path target = DIR.resolve(old.relativize(source).toString());
                if (Files.isDirectory(source)) {
                    Files.createDirectories(target);
                } else {
                    Files.copy(source, target);
                }
            }
            AronaLayersGen.LOGGER.info("Imported the generator's settings from {} to {}", old, DIR);
        } catch (IOException e) {
            AronaLayersGen.LOGGER.error("Could not import the generator's settings from {}: {}", old, e.toString());
        }
    }
}
