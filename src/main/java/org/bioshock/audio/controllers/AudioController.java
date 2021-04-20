package org.bioshock.audio.controllers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import javafx.application.Platform;
import lombok.NonNull;
import org.bioshock.main.App;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AudioController {

    /**
     *
     */
    private static final String AUDIOJSONPATH =
        "src/main/resources/org/bioshock/audio/audio-data.json";

    /** A mapping of effect names to URIs. */
    private static final ConcurrentHashMap<String, Path> effectUris =
        new ConcurrentHashMap<>();
    /** A mapping of music names to URIs. */
    private static final ConcurrentHashMap<String, Path> musicUris =
        new ConcurrentHashMap<>();

    /** A cache of recently loaded music. */
    private static Cache<String, MusicController> musicCache =
        Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(5))
            .removalListener((key, music, cause) -> {
                if (music instanceof MusicController) {
                    ((MusicController) music).dispose();
                }
            })
            .build();

    /** A cache of recently loaded effects. */
    private static Cache<String, EffectController> effectCache =
        Caffeine.newBuilder().expireAfterAccess(Duration.ofMinutes(5)).build();

    private AudioController() {
        /*
         * Prevents the JavaFX runtime from shutting down when all of the
         * JFXPanels are closed.
         *
         * If we allow it to shut down, then the GameAudio class won't be able
         * to make use of the Media and MediaPlayer JavaFX classes. The program
         * will just throw NPEs when you try to create new MediaPlayers.
         */
        Platform.setImplicitExit(false);
    }

    /**
     * Initializes this AudioController, by loading all of the music and effect
     * data from a JSON file.
     *
     * If this AudioController has already been initialized, this resets all
     * internal data and then re-initializes this AudioController.
     *
     */
    public static void initialise() {
        Path jsonPath = Paths.get(AUDIOJSONPATH);
        effectUris.clear();
        musicUris.clear();

        musicCache.invalidateAll();
        effectCache.invalidateAll();

        JSONArray parser;
        if (!Files.exists(jsonPath)) {
            App.logger.error("There is no file at '{}'.", jsonPath);
        }

        if (Files.isDirectory(jsonPath)) {
            App.logger.error(
                "The path '{}' points to a directory, not a file.",
                jsonPath
            );
        }

        try {
            parser = new JSONArray(Files.readString(
                jsonPath,
                StandardCharsets.UTF_8
            ));
        } catch (Exception e) {
            App.logger.error(e);
            return;
        }

        parser.forEach(object -> {
            final JSONObject jsonObject = (JSONObject) object;

            List<String> keys = Arrays.asList(
                "type", "name", "path", "filesystem"
            );
            if (!jsonObject.keySet().containsAll(keys)) {
                App.logger.error("json has is missing one of '{}'", keys);
                return;
            }

            String typeObject = (String) jsonObject.get("type");
            String nameObject = (String) jsonObject.get("name");
            String pathObject = (String) jsonObject.get("path");
            String filesystemObject = (String) jsonObject.get("filesystem");

            final String type = typeObject.toLowerCase();
            final String name = nameObject.toLowerCase();
            final String filesystem = filesystemObject.toLowerCase();

            if (name.isEmpty()) {
                App.logger.error("'Name' key cannot be blank.");
            }

            final Path nioPath;
            switch (filesystem) {
                case "jar":
                    try {
                        nioPath = Paths.get(
                            AudioController.class.getResource(pathObject).toURI()
                        );
                    } catch (final URISyntaxException e) {
                        App.logger.error(
                            "There is not file, within the JAR, at '{}'.",
                                pathObject
                        );
                        return;
                    }
                    break;
                case "local":
                    nioPath = Paths.get(pathObject);
                    break;

                default:
                    App.logger.error(
                        "'{}' is not a supported filesystem.",
                        filesystem
                    );
                    return;
            }

            switch (type) {
                case "effect":
                    effectUris.put(name, nioPath);
                    break;

                case "music":
                    musicUris.put(name, nioPath);
                    break;

                default:
                    App.logger.error("'{}}' is not a supported type.", type);
            }
        });
    }

    /**
     * Loads an effect.
     *
     * @param name The effect's name.
     *
     * @return A new instance of the effect, if it does not exist in the
     * cache, or a cached instance of the effect.
     */
    public static EffectController loadEffectController(
        final @NonNull String name
    ) {
        if (name.isEmpty()) {
            try {
                throw new IllegalArgumentException("Names cannot be blank.");
            } catch (IllegalArgumentException e) {
                App.logger.error(e);
            }
        }

        @Nullable EffectController effect = effectCache.getIfPresent(name);
        if (effect != null) {
//            App.logger.debug("Loaded Effect: {}", name);

            return effect;
        }

        effect = new EffectController(effectUris.get(name.toLowerCase()));
        effectCache.put(name, effect);

//        App.logger.debug("Loaded Effect: {}", name);

        return effect;
    }

    /**
     * Loads music.
     *
     * @param name The music's name.
     *
     * @return A new instance of the music, if it does not exist in the cache,
     * or a cached instance of the music.
     */
    public static MusicController loadMusicController(
        final @NonNull String name
    ) {
        if (name.isEmpty()) {
            App.logger.error(
                "Error loading music controller: Names cannot be blank."
            );
        }

        @Nullable MusicController music = musicCache.getIfPresent(name);
        if (music != null) {
            App.logger.debug("Loaded MusicController: {}", name);

            return music;
        }

        music = new MusicController(musicUris.get(name.toLowerCase()));
        musicCache.put(name, music);

        App.logger.debug("Loaded MusicController: {}", name);

        return music;
    }
}
