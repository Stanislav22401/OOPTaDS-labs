package plugin.star;

import editing.StarPropertyEditor;
import plugin.PluginContext;
import plugin.ShapePlugin;
import rendering.StarRenderer;
import serialization.StarCodec;
import shapes.Star;
import tools.StarTool;

/**
 * Sample shape plugin: adds a five-pointed star to the editor.
 */
public class StarPlugin implements ShapePlugin {
    @Override
    public String getName() {
        return "Star Shape Plugin";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void register(PluginContext context) {
        context.registerRenderer(Star.class, new StarRenderer());
        context.registerCodec(new StarCodec());
        context.registerPropertyEditor(new StarPropertyEditor());
        context.registerTool("Star", StarTool::new);
    }
}
