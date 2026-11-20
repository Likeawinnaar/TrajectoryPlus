package com.trajectoryplus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Settings are only saved on explicit changes, not every render or tick. */
public final class ConfigManager {
    private static final Logger LOG = LoggerFactory.getLogger("Trajectory+");
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    private final Path path;
    private TrajectoryConfig current;

    public ConfigManager() {
        this(FabricLoader.getInstance().getConfigDir().resolve("trajectoryplus.json"));
    }

    public ConfigManager(Path path) {
        this.path = path;
        this.current = new TrajectoryConfig();
        load();
    }

    public TrajectoryConfig get() { return current; }

    public void load() {
        if (!Files.exists(path)) {
            save();
            return;
        }
        try {
            TrajectoryConfig parsed = GSON.fromJson(Files.readString(path, StandardCharsets.UTF_8), TrajectoryConfig.class);
            if (parsed == null) throw new JsonParseException("Empty configuration");
            parsed.validate();
            current = parsed;
        } catch (IOException | RuntimeException e) {
            LOG.warn("Cannot load trajectoryplus.json; defaults are active, leaving the file untouched: {}", e.toString());
            current = new TrajectoryConfig();
        }
    }

    public void update(TrajectoryConfig next) {
        next.validate();
        current = next.copy();
        save();
    }

    public void save() {
        try {
            Files.createDirectories(path.getParent());
            Path tmp = path.resolveSibling(path.getFileName() + ".tmp");
            Files.writeString(tmp, GSON.toJson(current), StandardCharsets.UTF_8);
            try { Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (java.nio.file.AtomicMoveNotSupportedException e) {
                Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOG.error("Cannot save trajectoryplus.json: {}", e.toString());
        }
    }
}
