package plugin.functional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds loaded functional plugins and applies enabled processors in registration order.
 */
public class FunctionPluginRegistry {
    private final List<RegisteredPlugin> plugins = new ArrayList<>();

    public void register(FunctionPlugin plugin) {
        plugins.add(new RegisteredPlugin(plugin, plugin.isEnabledByDefault()));
    }

    public List<RegisteredPlugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }

    public void setEnabled(FunctionPlugin plugin, boolean enabled) {
        for (RegisteredPlugin entry : plugins) {
            if (entry.plugin == plugin) {
                entry.enabled = enabled;
                return;
            }
        }
        throw new IllegalArgumentException("Plugin not registered: " + plugin.getName());
    }

    public boolean isEnabled(FunctionPlugin plugin) {
        for (RegisteredPlugin entry : plugins) {
            if (entry.plugin == plugin) {
                return entry.enabled;
            }
        }
        return false;
    }

    public boolean hasEnabledPlugins() {
        for (RegisteredPlugin entry : plugins) {
            if (entry.enabled) {
                return true;
            }
        }
        return false;
    }

    public String applyBeforeSave(String xml) throws Exception {
        String result = xml;
        for (RegisteredPlugin entry : plugins) {
            if (entry.enabled) {
                result = entry.plugin.processBeforeSave(result);
            }
        }
        return result;
    }

    public String applyAfterLoad(String xml) throws Exception {
        String result = xml;
        List<RegisteredPlugin> reversed = new ArrayList<>(plugins);
        Collections.reverse(reversed);
        for (RegisteredPlugin entry : reversed) {
            if (entry.enabled) {
                result = entry.plugin.processAfterLoad(result);
            }
        }
        return result;
    }

    public static final class RegisteredPlugin {
        private final FunctionPlugin plugin;
        private boolean enabled;

        private RegisteredPlugin(FunctionPlugin plugin, boolean enabled) {
            this.plugin = plugin;
            this.enabled = enabled;
        }

        public FunctionPlugin getPlugin() {
            return plugin;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }
}
