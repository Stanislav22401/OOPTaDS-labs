package plugin.adapter;

import classmate.api.XmlPipelinePlugin;
import plugin.functional.FunctionPlugin;

/**
 * Adapter: maps classmate {@link XmlPipelinePlugin} to host {@link FunctionPlugin}.
 */
public class ClassmateFunctionPluginAdapter implements FunctionPlugin {
    private final XmlPipelinePlugin classmatePlugin;

    public ClassmateFunctionPluginAdapter(XmlPipelinePlugin classmatePlugin) {
        this.classmatePlugin = classmatePlugin;
    }

    public XmlPipelinePlugin getClassmatePlugin() {
        return classmatePlugin;
    }

    @Override
    public String getName() {
        return classmatePlugin.getPluginTitle() + " [Adapter]";
    }

    @Override
    public String getVersion() {
        return classmatePlugin.getPluginVersion();
    }

    @Override
    public String getMenuLabel() {
        return classmatePlugin.getSettingsLabel() + " (classmate via Adapter)";
    }

    @Override
    public boolean isEnabledByDefault() {
        return classmatePlugin.isActiveByDefault();
    }

    @Override
    public String processBeforeSave(String xml) throws Exception {
        return classmatePlugin.onExport(xml);
    }

    @Override
    public String processAfterLoad(String xml) throws Exception {
        return classmatePlugin.onImport(xml);
    }
}
