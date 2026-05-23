package plugin.functional;

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
 * Loads functional plugins from JAR files via {@link ServiceLoader}.
 */
public class FunctionPluginLoader {
    private final List<URLClassLoader> classLoaders = new ArrayList<>();
    private final Set<String> loadedJarPaths = new HashSet<>();

    public List<String> loadFromDirectory(Path directory, FunctionPluginRegistry registry) throws Exception {
        List<String> loaded = new ArrayList<>();
        if (directory == null || !Files.isDirectory(directory)) {
            return loaded;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.jar")) {
            for (Path jar : stream) {
                loaded.add(loadJar(jar, registry));
            }
        }
        return loaded;
    }

    public String loadJar(Path jarPath, FunctionPluginRegistry registry) throws Exception {
        if (!Files.isRegularFile(jarPath)) {
            throw new IllegalArgumentException("Not a file: " + jarPath);
        }
        String absolute = jarPath.toAbsolutePath().normalize().toString();
        if (loadedJarPaths.contains(absolute)) {
            return "Already loaded: " + jarPath.getFileName();
        }

        URLClassLoader loader = new URLClassLoader(
                new URL[]{jarPath.toUri().toURL()},
                FunctionPluginLoader.class.getClassLoader());
        classLoaders.add(loader);

        ServiceLoader<FunctionPlugin> services = ServiceLoader.load(FunctionPlugin.class, loader);
        boolean found = false;
        String name = jarPath.getFileName().toString();
        for (FunctionPlugin plugin : services) {
            registry.register(plugin);
            name = plugin.getName() + " (" + plugin.getVersion() + ")";
            found = true;
        }
        if (!found) {
            loader.close();
            classLoaders.remove(loader);
            throw new IllegalStateException(
                    "No plugin.functional.FunctionPlugin service in " + jarPath.getFileName());
        }
        loadedJarPaths.add(absolute);
        return name;
    }
}
