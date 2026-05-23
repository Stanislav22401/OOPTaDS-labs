package drawing;

import drawing.persistence.DrawingPersistenceFacade;
import plugin.ClassmatePluginLoader;
import plugin.PluginLoader;
import plugin.functional.FunctionPlugin;
import plugin.functional.FunctionPluginLoader;
import plugin.functional.FunctionPluginRegistry;
import serialization.JsonShapeListSerializer;
import serialization.ProcessingShapeListSerializer;
import shapes.Shape;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main window: labs 3–6 — plugins, XML pipeline, Adapter for classmate modules, Facade/Observer/Strategy.
 */
public class MainFrame extends JFrame {
    private static final String PLUGINS_DIR_PROPERTY = "plugins.dir";
    private static final String FUNCTION_PLUGINS_DIR_PROPERTY = "function.plugins.dir";
    private static final String CLASSMATE_PLUGINS_DIR_PROPERTY = "classmate.plugins.dir";

    private final ShapeList shapeList = new ShapeList();
    private final ApplicationBootstrap bootstrap = new ApplicationBootstrap();
    private final PluginLoader shapePluginLoader = new PluginLoader();
    private final FunctionPluginLoader functionPluginLoader = new FunctionPluginLoader();
    private final ClassmatePluginLoader classmatePluginLoader = new ClassmatePluginLoader();
    private final FunctionPluginRegistry functionPluginRegistry = new FunctionPluginRegistry();
    private final DrawingPersistenceFacade persistenceFacade;
    private final DrawingPanel drawingPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> shapeListView = new JList<>(listModel);
    private final DefaultListModel<String> shapePluginListModel = new DefaultListModel<>();
    private final DefaultListModel<String> functionPluginListModel = new DefaultListModel<>();
    private final DefaultListModel<String> classmatePluginListModel = new DefaultListModel<>();
    private final Map<FunctionPlugin, JCheckBoxMenuItem> functionPluginMenuItems = new HashMap<>();
    private JToolBar toolBar;
    private JMenu settingsMenu;

    public MainFrame(String[] args) {
        setTitle("Graphics Editor — Lab 6 (Design Patterns)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);

        JsonShapeListSerializer jsonSerializer = new JsonShapeListSerializer(bootstrap.getCodecRegistry());
        ProcessingShapeListSerializer processingSerializer =
                new ProcessingShapeListSerializer(jsonSerializer, functionPluginRegistry);
        persistenceFacade = new DrawingPersistenceFacade(jsonSerializer, processingSerializer);
        drawingPanel = new DrawingPanel(shapeList, bootstrap.getRendererRegistry());
        shapeList.addObserver(source -> refreshShapeListView());

        add(createToolBar(), BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.WEST);
        setJMenuBar(createMenuBar());

        loadPluginsOnStartup(args);
        refreshShapeListView();
        rebuildSettingsMenu();
    }

    private JToolBar createToolBar() {
        toolBar = new JToolBar("Tools");
        rebuildToolBar();
        return toolBar;
    }

    private void rebuildToolBar() {
        toolBar.removeAll();
        for (ApplicationBootstrap.ToolEntry entry : bootstrap.getTools()) {
            JButton button = new JButton(entry.getLabel());
            button.addActionListener(e -> drawingPanel.setTool(entry.createTool()));
            toolBar.add(button);
        }
        toolBar.revalidate();
        toolBar.repaint();
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(new java.awt.Dimension(300, 0));

        JPanel shapesPanel = new JPanel(new BorderLayout());
        shapesPanel.add(new JLabel("Shapes"), BorderLayout.NORTH);
        shapesPanel.add(new JScrollPane(shapeListView), BorderLayout.CENTER);

        JPanel shapePluginsPanel = new JPanel(new BorderLayout());
        shapePluginsPanel.add(new JLabel("Shape plugins"), BorderLayout.NORTH);
        shapePluginsPanel.add(new JScrollPane(new JList<>(shapePluginListModel)), BorderLayout.CENTER);

        JPanel functionPluginsPanel = new JPanel(new BorderLayout());
        functionPluginsPanel.add(new JLabel("Functional plugins"), BorderLayout.NORTH);
        functionPluginsPanel.add(new JScrollPane(new JList<>(functionPluginListModel)), BorderLayout.CENTER);

        JPanel classmatePluginsPanel = new JPanel(new BorderLayout());
        classmatePluginsPanel.add(new JLabel("Classmate (via Adapter)"), BorderLayout.NORTH);
        classmatePluginsPanel.add(new JScrollPane(new JList<>(classmatePluginListModel)), BorderLayout.CENTER);

        JPanel stacked = new JPanel(new java.awt.GridLayout(4, 1));
        stacked.add(shapesPanel);
        stacked.add(shapePluginsPanel);
        stacked.add(functionPluginsPanel);
        stacked.add(classmatePluginsPanel);
        panel.add(stacked, BorderLayout.CENTER);

        JButton editButton = new JButton("Edit properties");
        editButton.addActionListener(e -> editSelectedShape());
        JButton deleteButton = new JButton("Delete selected");
        deleteButton.addActionListener(e -> deleteSelectedShape());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(editButton);
        buttons.add(deleteButton);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save...");
        saveItem.addActionListener(e -> saveToFile());
        JMenuItem loadItem = new JMenuItem("Load...");
        loadItem.addActionListener(e -> loadFromFile());
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        JMenu pluginsMenu = new JMenu("Plugins");
        JMenuItem loadShapeJar = new JMenuItem("Load shape plugin JAR...");
        loadShapeJar.addActionListener(e -> loadShapePluginFromChooser());
        JMenuItem reloadShapeDir = new JMenuItem("Reload shape plugins folder");
        reloadShapeDir.addActionListener(e -> reloadShapePluginsDirectory());
        JMenuItem loadFunctionJar = new JMenuItem("Load functional plugin JAR...");
        loadFunctionJar.addActionListener(e -> loadFunctionPluginFromChooser());
        JMenuItem reloadFunctionDir = new JMenuItem("Reload functional plugins folder");
        reloadFunctionDir.addActionListener(e -> reloadFunctionPluginsDirectory());
        JMenuItem loadClassmateJar = new JMenuItem("Load classmate plugin JAR...");
        loadClassmateJar.addActionListener(e -> loadClassmatePluginFromChooser());
        JMenuItem reloadClassmateDir = new JMenuItem("Reload classmate plugins folder");
        reloadClassmateDir.addActionListener(e -> reloadClassmatePluginsDirectory());
        pluginsMenu.add(loadShapeJar);
        pluginsMenu.add(reloadShapeDir);
        pluginsMenu.addSeparator();
        pluginsMenu.add(loadFunctionJar);
        pluginsMenu.add(reloadFunctionDir);
        pluginsMenu.addSeparator();
        pluginsMenu.add(loadClassmateJar);
        pluginsMenu.add(reloadClassmateDir);

        settingsMenu = new JMenu("Settings");
        settingsMenu.setEnabled(false);

        menuBar.add(fileMenu);
        menuBar.add(pluginsMenu);
        menuBar.add(settingsMenu);
        return menuBar;
    }

    private void rebuildSettingsMenu() {
        settingsMenu.removeAll();
        functionPluginMenuItems.clear();

        List<FunctionPluginRegistry.RegisteredPlugin> entries = functionPluginRegistry.getPlugins();
        if (entries.isEmpty()) {
            settingsMenu.setEnabled(false);
            return;
        }

        settingsMenu.setEnabled(true);
        for (FunctionPluginRegistry.RegisteredPlugin entry : entries) {
            FunctionPlugin plugin = entry.getPlugin();
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(plugin.getMenuLabel(), entry.isEnabled());
            item.addActionListener(e -> functionPluginRegistry.setEnabled(plugin, item.isSelected()));
            settingsMenu.add(item);
            functionPluginMenuItems.put(plugin, item);
        }
    }

    private Path resolveShapePluginsDirectory() {
        String override = System.getProperty(PLUGINS_DIR_PROPERTY);
        if (override != null && !override.isBlank()) {
            return Paths.get(override);
        }
        return Paths.get("plugins").toAbsolutePath();
    }

    private Path resolveFunctionPluginsDirectory() {
        String override = System.getProperty(FUNCTION_PLUGINS_DIR_PROPERTY);
        if (override != null && !override.isBlank()) {
            return Paths.get(override);
        }
        return Paths.get("function-plugins").toAbsolutePath();
    }

    private Path resolveClassmatePluginsDirectory() {
        String override = System.getProperty(CLASSMATE_PLUGINS_DIR_PROPERTY);
        if (override != null && !override.isBlank()) {
            return Paths.get(override);
        }
        return Paths.get("classmate-plugins").toAbsolutePath();
    }

    private void loadPluginsOnStartup(String[] args) {
        try {
            Path shapeDir = resolveShapePluginsDirectory();
            Files.createDirectories(shapeDir);
            shapePluginLoader.loadFromDirectory(shapeDir, bootstrap)
                    .forEach(shapePluginListModel::addElement);
        } catch (Exception ex) {
            showError("Shape plugin folder load failed", ex);
        }

        try {
            Path functionDir = resolveFunctionPluginsDirectory();
            Files.createDirectories(functionDir);
            functionPluginLoader.loadFromDirectory(functionDir, functionPluginRegistry)
                    .forEach(functionPluginListModel::addElement);
        } catch (Exception ex) {
            showError("Functional plugin folder load failed", ex);
        }

        try {
            Path classmateDir = resolveClassmatePluginsDirectory();
            Files.createDirectories(classmateDir);
            classmatePluginLoader.loadFromDirectory(classmateDir, functionPluginRegistry)
                    .forEach(classmatePluginListModel::addElement);
        } catch (Exception ex) {
            showError("Classmate plugin folder load failed", ex);
        }

        if (args != null) {
            for (String arg : args) {
                if (arg != null && !arg.isBlank()) {
                    loadShapePluginJar(Paths.get(arg));
                }
            }
        }

        rebuildToolBar();
        rebuildSettingsMenu();
    }

    private void reloadShapePluginsDirectory() {
        shapePluginListModel.clear();
        try {
            Path dir = resolveShapePluginsDirectory();
            Files.createDirectories(dir);
            shapePluginLoader.loadFromDirectory(dir, bootstrap).forEach(shapePluginListModel::addElement);
            rebuildToolBar();
            JOptionPane.showMessageDialog(this, "Shape plugins reloaded from\n" + dir, "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Reload failed", ex);
        }
    }

    private void reloadFunctionPluginsDirectory() {
        functionPluginListModel.clear();
        try {
            Path dir = resolveFunctionPluginsDirectory();
            Files.createDirectories(dir);
            functionPluginLoader.loadFromDirectory(dir, functionPluginRegistry)
                    .forEach(functionPluginListModel::addElement);
            rebuildSettingsMenu();
            JOptionPane.showMessageDialog(this, "Functional plugins reloaded from\n" + dir, "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Reload failed", ex);
        }
    }

    private void loadShapePluginFromChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select shape plugin JAR");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadShapePluginJar(chooser.getSelectedFile().toPath());
        }
    }

    private void loadFunctionPluginFromChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select functional plugin JAR");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadFunctionPluginJar(chooser.getSelectedFile().toPath());
        }
    }

    private void loadShapePluginJar(Path jarPath) {
        try {
            String name = shapePluginLoader.loadJar(jarPath, bootstrap);
            shapePluginListModel.addElement(name + " ← " + jarPath.getFileName());
            rebuildToolBar();
            JOptionPane.showMessageDialog(this, "Shape plugin loaded: " + name, "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Shape plugin load failed", ex);
        }
    }

    private void loadFunctionPluginJar(Path jarPath) {
        try {
            String name = functionPluginLoader.loadJar(jarPath, functionPluginRegistry);
            functionPluginListModel.addElement(name + " ← " + jarPath.getFileName());
            rebuildSettingsMenu();
            JOptionPane.showMessageDialog(this, "Functional plugin loaded: " + name, "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Functional plugin load failed", ex);
        }
    }

    private void reloadClassmatePluginsDirectory() {
        classmatePluginListModel.clear();
        try {
            Path dir = resolveClassmatePluginsDirectory();
            Files.createDirectories(dir);
            classmatePluginLoader.loadFromDirectory(dir, functionPluginRegistry)
                    .forEach(classmatePluginListModel::addElement);
            rebuildSettingsMenu();
            JOptionPane.showMessageDialog(this, "Classmate plugins reloaded from\n" + dir, "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Reload failed", ex);
        }
    }

    private void loadClassmatePluginFromChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select classmate plugin JAR");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadClassmatePluginJar(chooser.getSelectedFile().toPath());
        }
    }

    private void loadClassmatePluginJar(Path jarPath) {
        try {
            String name = classmatePluginLoader.loadJar(jarPath, functionPluginRegistry);
            classmatePluginListModel.addElement(name + " ← " + jarPath.getFileName());
            rebuildSettingsMenu();
            JOptionPane.showMessageDialog(this, "Classmate plugin adapted: " + name, "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Classmate plugin load failed", ex);
        }
    }

    private void refreshShapeListView() {
        listModel.clear();
        for (int i = 0; i < shapeList.size(); i++) {
            listModel.addElement((i + 1) + ". " + shapeList.get(i).getDisplayName());
        }
        drawingPanel.repaint();
    }

    private int getSelectedIndex() {
        return shapeListView.getSelectedIndex();
    }

    private void editSelectedShape() {
        int index = getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Select a shape to edit.", "Edit", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Shape shape = shapeList.get(index);
        ShapeEditDialog dialog = new ShapeEditDialog(this, shape, bootstrap.getPropertyEditorRegistry());
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            refreshShapeListView();
        }
    }

    private void deleteSelectedShape() {
        int index = getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Select a shape to delete.", "Delete", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        shapeList.removeAt(index);
        refreshShapeListView();
    }

    private void saveToFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("shapes.xml"));
        chooser.setFileFilter(new FileNameExtensionFilter("XML shapes (*.xml)", "xml"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON shapes (*.json)", "json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = chooser.getSelectedFile().toPath();
        try {
            persistenceFacade.save(shapeList, path);
            JOptionPane.showMessageDialog(this, "Saved to " + path, "Save", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Save failed", ex);
        }
    }

    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Shape files (*.xml, *.json)", "xml", "json"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = chooser.getSelectedFile().toPath();
        try {
            persistenceFacade.load(shapeList, path);
            refreshShapeListView();
            JOptionPane.showMessageDialog(this, "Loaded from " + path, "Load", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Load failed", ex);
        }
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame(args).setVisible(true));
    }
}
