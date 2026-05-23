package plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ServiceLoader;

/**
 * Loads shape plugins from JAR files using {@link ServiceLoader} and a child {@link URLClassLoader}.
 */
public class PluginLoader {
    private final List<URLClassLoader> classLoaders = new ArrayList<>();
    private final Set<String> loadedJarPaths = new HashSet<>();

    /**
     * Loads every {@code *.jar} in the given directory.
     *
     * @return names of successfully loaded plugins
     */
    public List<String> loadFromDirectory(Path directory, PluginContext context) throws Exception {
        List<String> loaded = new ArrayList<>();
        if (directory == null || !Files.isDirectory(directory)) {
            return loaded;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.jar")) {
            for (Path jar : stream) {
                loaded.add(loadJar(jar, context));
            }
        }
        return loaded;
    }

    /**
     * Loads a single plugin JAR file.
     *
     * @return plugin display name
     */
    public String loadJar(Path jarPath, PluginContext context) throws Exception {
        if (!Files.isRegularFile(jarPath)) {
            throw new IllegalArgumentException("Not a file: " + jarPath);
        }
        String absolute = jarPath.toAbsolutePath().normalize().toString();
        if (loadedJarPaths.contains(absolute)) {
            return "Already loaded: " + jarPath.getFileName();
        }
        URL jarUrl = jarPath.toUri().toURL();
        URLClassLoader loader = new URLClassLoader(
                new URL[]{jarUrl},
                PluginLoader.class.getClassLoader());
        classLoaders.add(loader);

        ServiceLoader<ShapePlugin> services = ServiceLoader.load(ShapePlugin.class, loader);
        boolean found = false;
        String name = jarPath.getFileName().toString();
        for (ShapePlugin plugin : services) {
            plugin.register(context);
            name = plugin.getName() + " (" + plugin.getVersion() + ")";
            found = true;
        }
        if (!found) {
            loader.close();
            classLoaders.remove(loader);
            throw new IllegalStateException(
                    "No plugin.ShapePlugin service found in " + jarPath.getFileName());
        }
        loadedJarPaths.add(absolute);
        return name;
    }

    /**
     * Closes class loaders opened for loaded plugins.
     */
    public void close() throws Exception {
        for (URLClassLoader loader : classLoaders) {
            loader.close();
        }
        classLoaders.clear();
    }
}
