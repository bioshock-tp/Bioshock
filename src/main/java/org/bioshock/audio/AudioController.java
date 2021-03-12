package org.bioshock.audio;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.ConcurrentHashMap;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.bioshock.main.App;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
import lombok.NonNull;

public class AudioController {

    /**
     *
     */
    private static final String audioJsonPath =
        "src/main/resources/org/bioshock/audio/audio-data.json";

    /** A mapping of effect names to URIs. */
    private final static ConcurrentHashMap<String, Path> effectUris =
        new ConcurrentHashMap<>();
    /** A mapping of music names to URIs. */
    private final static ConcurrentHashMap<String, Path> musicUris =
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
     * @param jsonPath
     *          The path of the JSON data.
     */
    public static void initialise() {
        Path jsonPath = Paths.get(audioJsonPath);
        effectUris.clear();
        musicUris.clear();

        musicCache.invalidateAll();
        effectCache.invalidateAll();

        JSONArray parser;
        try {
            if (!Files.exists(jsonPath)) {
                throw new FileNotFoundException(
                    "There is no file at '" + jsonPath + "'."
                );
            }

            if (Files.isDirectory(jsonPath)) {
                throw new IllegalArgumentException(String.format(
                    "The path '%s' points to a directory, not a file.",
                    jsonPath.toString()
                ));
            }

            parser = new JSONArray(Files.readString(
                jsonPath,
                StandardCharsets.UTF_8
            ));

            parser.forEach(object -> {
                final JSONObject jsonObject = (JSONObject) object;

                if (!jsonObject.has("type")) {
                    throw new MissingFormatArgumentException(
                        "There is no type"
                    );
                }

                if (!jsonObject.has("name")) {
                    throw new MissingFormatArgumentException(
                        "There is no name."
                    );
                }

                if (!jsonObject.has("path")) {
                    throw new MissingFormatArgumentException(
                        "There is no path."
                    );
                }

                if (!jsonObject.has("filesystem")) {
                    throw new MissingFormatArgumentException(
                        "There is no filesystem."
                    );
                }

                String typeObject = (String) jsonObject.get("type");
                String nameObject = (String) jsonObject.get("name");
                String pathObject = (String) jsonObject.get("path");
                String filesystemObject = (String)jsonObject.get("filesystem");

                final String type = typeObject.toLowerCase();
                final String name = nameObject.toLowerCase();
                final String path = pathObject;
                final String filesystem = filesystemObject.toLowerCase();

                if (name.isEmpty()) {
                    throw new IllegalArgumentException(
                        "Names cannot be blank."
                    );
                }

                final Path nioPath;
                switch (filesystem) {
                    case "jar": {
                        try {
                            nioPath = Paths.get(
                                AudioController.class.getResource(path).toURI()
                            );
                        } catch (final URISyntaxException e) {
                            throw new IllegalArgumentException(String.format(
                                "There is not file, within the JAR, at '%s'.",
                                path.toString()
                            ));
                        }
                        break;
                    }
                    case "local": {
                        nioPath = Paths.get(path);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException(String.format(
                            "'%s' is not a supported filesystem.",
                            filesystem
                        ));
                    }
                }

                switch (type) {
                    case "effect": {
                        effectUris.put(name, nioPath);
                        break;
                    }
                    case "music": {
                        musicUris.put(name, nioPath);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException(String.format(
                            "'%s' is not a supported type.",
                            type
                        ));
                    }
                }
            });
        } catch (Exception e) {
            App.logger.error("Error initialising audio: {}", e.getMessage());
        }


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
                App.logger.error(e.getMessage());
            }
        }

        @Nullable EffectController effect = effectCache.getIfPresent(name);
        if (effect != null) {
            App.logger.debug("Loaded Effect: {}", name);

            return effect;
        }

        effect = new EffectController(effectUris.get(name.toLowerCase()));
        effectCache.put(name, effect);

        App.logger.debug("Loaded Effect: {}", name);

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
                try {
                    throw new IllegalArgumentException(
                        "Names cannot be blank."
                    );
                } catch (IllegalArgumentException e) {
                    App.logger.error(
                        "Error loading music controller: {}",
                        e.getMessage()
                    );
                }
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
