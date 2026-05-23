package plugin;

import classmate.api.XmlPipelinePlugin;
import plugin.adapter.ClassmateFunctionPluginAdapter;
import plugin.functional.FunctionPluginRegistry;

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
 * Loads classmate JARs that expose {@link XmlPipelinePlugin} and registers {@link ClassmateFunctionPluginAdapter} instances.
 */
public class ClassmatePluginLoader {
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
                ClassmatePluginLoader.class.getClassLoader());
        classLoaders.add(loader);

        ServiceLoader<XmlPipelinePlugin> services = ServiceLoader.load(XmlPipelinePlugin.class, loader);
        boolean found = false;
        String name = jarPath.getFileName().toString();
        for (XmlPipelinePlugin classmate : services) {
            registry.register(new ClassmateFunctionPluginAdapter(classmate));
            name = classmate.getPluginTitle() + " (classmate, adapted)";
            found = true;
        }
        if (!found) {
            loader.close();
            classLoaders.remove(loader);
            throw new IllegalStateException(
                    "No classmate.api.XmlPipelinePlugin service in " + jarPath.getFileName());
        }
        loadedJarPaths.add(absolute);
        return name;
    }
}
