package org.bioshock.audio;

//public class AudioController {
//    public MediaPlayer bgMusicController;
//    //private Preferences prefs;
//    private Thread bgMusicControllerThread;
//
//    public AudioController(boolean play) throws URISyntaxException {
//        playMusicController(play);
//    }
//
//    public void playMusicController(boolean b) throws URISyntaxException {
//        bgMusicControllerThread = new Thread(new Task<>() {
//            @Override
//            protected Object call() throws URISyntaxException {
//                bgMusicController = new MediaPlayer(new Media(getClass().getResource("backgroundMusicController.mp3").toURI().toString()));
//                if (b) {
//                    bgMusicController.play();
//                } else {
//                    bgMusicController.stop();
//                }
//                return null;
//            }
//        });
//        bgMusicControllerThread.start();
//        //bgMusicController.setAutoPlay(b);
//    }
//
//    public boolean musicPlaying() {
//        this.bgMusicController.getStatus();
//        return this.bgMusicController.getStatus().equals(MediaPlayer.Status.READY);
//    }
//
//    public void stopMusicController() {
//        bgMusicController.stop();
//    }
//}
import javafx.application.Platform;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.ConcurrentHashMap;

public class AudioController {

    /** The singleton instance. */
    private static AudioController instance;

    /** Whether the V2DAudio library should output debugging information to the terminal. */
    public static boolean debuggingEnabled = false;

    /** A mapping of effect names to URIs. */
    private final ConcurrentHashMap<String, Path> effectUris = new ConcurrentHashMap<>();
    /** A mapping of music names to URIs. */
    private final ConcurrentHashMap<String, Path> musicUris = new ConcurrentHashMap<>();

    /** A cache of recently loaded music. */
    private Cache<String, MusicController> musicCache = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(5))
            .removalListener((key, music, cause) -> {
                if (music instanceof MusicController) {
                    ((MusicController) music).dispose();
                }
            })
            .build();
    /** A cache of recently loaded effects. */
    private Cache<String, EffectController> effectCache = Caffeine.newBuilder().expireAfterAccess(Duration.ofMinutes(5)).build();

    private AudioController() {
        /*
         * Prevents the JavaFX runtime from shutting down when all of the JFXPanels are closed.
         *
         * If we allow it to shut down, then the GameAudio class won't be able to make use of the Media
         * and MediaPlayer JavaFX classes. The program will just throw NPEs when you try to create new
         * MediaPlayers.
         */
        instance = new AudioController();
        Platform.setImplicitExit(false);
        Platform.startup(() -> {});
    }

    /**
     * Initializes this AudioController, by loading all of the music and effect data from a JSON file.
     *
     * If this AudioController has already been initialized, this resets all internal data and then
     * re-initializes this AudioController.
     *
     * @param jsonPath
     *          The path of the JSON data.
     *
     * @throws IOException
     *          If an I/O error occurs.
     *
     */
    public void initialize(final @NonNull Path jsonPath) throws IOException {
        effectUris.clear();
        musicUris.clear();

        musicCache.invalidateAll();
        effectCache.invalidateAll();

        if (!Files.exists(jsonPath)) {
            throw new FileNotFoundException("There is no file at '" + jsonPath + "'.");
        }

        if (Files.isDirectory(jsonPath)) {
            throw new IllegalArgumentException("The path '" + jsonPath + "' points to a directory, not a file.");
        }

        final var parser = new JSONArray(Files.readString(jsonPath, StandardCharsets.UTF_8));
        parser.forEach(object -> {
            final var jsonObject = (JSONObject) object;

            if (!jsonObject.has("type")) {
                throw new MissingFormatArgumentException("There is no type");
            }

            if (!jsonObject.has("name")) {
                throw new MissingFormatArgumentException("There is no name.");
            }

            if (!jsonObject.has("path")) {
                throw new MissingFormatArgumentException("There is no path.");
            }

            if (!jsonObject.has("filesystem")) {
                throw new MissingFormatArgumentException("There is no filesystem.");
            }

            final var type = ((String) jsonObject.get("type")).toLowerCase();
            final var name = ((String) jsonObject.get("name")).toLowerCase();
            final var path = ((String) jsonObject.get("path")).toLowerCase();
            final var filesystem = ((String) jsonObject.get("filesystem")).toLowerCase();

            if (name.isEmpty()) {
                throw new IllegalArgumentException("Names cannot be blank.");
            }

            final Path nioPath;
            switch (filesystem) {
                case "jar": {
                    try {
                        nioPath = Paths.get(AudioController.class.getResource(path).toURI());
                    } catch (final URISyntaxException e) {
                        throw new IllegalArgumentException("There is not file, within the JAR, at '" + path + "'.");
                    }
                    break;
                }
                case "local": {
                    nioPath = Paths.get(path);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("'" + filesystem + "' is not a supported filesystem.");
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
                    throw new IllegalArgumentException("'" + type + "' is not a supported type.");
                }
            }
        });
    }

    /**
     * Retrieves the singleton instance.
     *
     * @return
     *          The singleton instance.
     */
    public static synchronized AudioController getInstance() {
        //instance = new AudioController();
        return instance;
    }

    /**
     * Loads an effect.
     *
     * @param name
     *          The effect's name.
     *
     * @return
     *          A new instance of the effect, if it does not exist in the cache, or a cached instance of the
     *          effect.
     */
    public EffectController loadEffect(final @NonNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Names cannot be blank.");
        }

        var effect = effectCache.getIfPresent(name);
        if (effect != null) {
            if (debuggingEnabled) {
                System.out.println("Loaded Effect: " + name);
            }

            return effect;
        }

        effect = new EffectController(effectUris.get(name.toLowerCase()));
        effectCache.put(name, effect);

        if (debuggingEnabled) {
            System.out.println("Loaded Effect: " + name);
        }

        return effect;
    }

    /**
     * Loads music.
     *
     * @param name
     *          The music's name.
     *
     * @return
     *          A new instance of the music, if it does not exist in the cache, or a cached instance of the
     *          music.
     */
    public MusicController loadMusicController(final @NonNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Names cannot be blank.");
        }

        var music = musicCache.getIfPresent(name);
        if (music != null) {
            if (debuggingEnabled) {
                System.out.println("Loaded MusicController: " + name);
            }

            return music;
        }

        music = new MusicController(musicUris.get(name.toLowerCase()));
        musicCache.put(name, music);

        if (debuggingEnabled) {
            System.out.println("Loaded MusicController: " + name);
        }

        return music;
    }
}