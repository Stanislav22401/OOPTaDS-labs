package drawing;

import plugin.PluginLoader;
import serialization.JsonShapeListSerializer;
import shapes.Shape;

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
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Main window: lab 3 editor extended with dynamic plugin loading (lab 4).
 */
public class MainFrame extends JFrame {
    private static final String PLUGINS_DIR_PROPERTY = "plugins.dir";

    private final ShapeList shapeList = new ShapeList();
    private final ApplicationBootstrap bootstrap = new ApplicationBootstrap();
    private final PluginLoader pluginLoader = new PluginLoader();
    private final DrawingPanel drawingPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> shapeListView = new JList<>(listModel);
    private final DefaultListModel<String> pluginListModel = new DefaultListModel<>();
    private final JsonShapeListSerializer serializer;
    private JToolBar toolBar;

    public MainFrame(String[] args) {
        setTitle("Graphics Editor — Lab 4 (Plugins)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        serializer = new JsonShapeListSerializer(bootstrap.getCodecRegistry());
        drawingPanel = new DrawingPanel(shapeList, bootstrap.getRendererRegistry());
        drawingPanel.addShapeListListener(this::refreshShapeListView);

        add(createToolBar(), BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.WEST);
        setJMenuBar(createMenuBar());

        loadPluginsOnStartup(args);
        refreshShapeListView();
    }

    private JToolBar createToolBar() {
        toolBar = new JToolBar("Tools");
        rebuildToolBar();
        return toolBar;
    }

    /** Rebuilds toolbar buttons from registered tools (built-in + plugins). */
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
        panel.setPreferredSize(new java.awt.Dimension(280, 0));

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Shapes"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(shapeListView), BorderLayout.CENTER);

        JPanel pluginPanel = new JPanel(new BorderLayout());
        pluginPanel.add(new JLabel("Loaded plugins"), BorderLayout.NORTH);
        pluginPanel.add(new JScrollPane(new JList<>(pluginListModel)), BorderLayout.CENTER);

        JPanel stacked = new JPanel();
        stacked.setLayout(new java.awt.GridLayout(2, 1));
        stacked.add(listPanel);
        stacked.add(pluginPanel);
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
        JMenuItem saveItem = new JMenuItem("Save to JSON...");
        saveItem.addActionListener(e -> saveToFile());
        JMenuItem loadItem = new JMenuItem("Load from JSON...");
        loadItem.addActionListener(e -> loadFromFile());
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        JMenu pluginsMenu = new JMenu("Plugins");
        JMenuItem loadJarItem = new JMenuItem("Load plugin JAR...");
        loadJarItem.addActionListener(e -> loadPluginFromFileChooser());
        JMenuItem reloadDirItem = new JMenuItem("Reload plugins folder");
        reloadDirItem.addActionListener(e -> reloadPluginsDirectory());
        pluginsMenu.add(loadJarItem);
        pluginsMenu.add(reloadDirItem);

        menuBar.add(fileMenu);
        menuBar.add(pluginsMenu);
        return menuBar;
    }

    private Path resolvePluginsDirectory() {
        String override = System.getProperty(PLUGINS_DIR_PROPERTY);
        if (override != null && !override.isBlank()) {
            return Paths.get(override);
        }
        return Paths.get("plugins").toAbsolutePath();
    }

    private void loadPluginsOnStartup(String[] args) {
        Path pluginsDir = resolvePluginsDirectory();
        try {
            Files.createDirectories(pluginsDir);
            List<String> loaded = pluginLoader.loadFromDirectory(pluginsDir, bootstrap);
            loaded.forEach(pluginListModel::addElement);
        } catch (Exception ex) {
            showError("Plugin folder load failed", ex);
        }

        if (args != null) {
            for (String arg : args) {
                if (arg != null && !arg.isBlank()) {
                    loadPluginJar(Paths.get(arg));
                }
            }
        }

        rebuildToolBar();
    }

    private void reloadPluginsDirectory() {
        pluginListModel.clear();
        Path pluginsDir = resolvePluginsDirectory();
        try {
            Files.createDirectories(pluginsDir);
            List<String> loaded = pluginLoader.loadFromDirectory(pluginsDir, bootstrap);
            loaded.forEach(pluginListModel::addElement);
            rebuildToolBar();
            JOptionPane.showMessageDialog(this,
                    "Loaded " + loaded.size() + " plugin(s) from\n" + pluginsDir,
                    "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Reload failed", ex);
        }
    }

    private void loadPluginFromFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select plugin JAR");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        loadPluginJar(chooser.getSelectedFile().toPath());
    }

    private void loadPluginJar(Path jarPath) {
        try {
            String name = pluginLoader.loadJar(jarPath, bootstrap);
            pluginListModel.addElement(name + " ← " + jarPath.getFileName());
            rebuildToolBar();
            JOptionPane.showMessageDialog(this,
                    "Plugin loaded: " + name,
                    "Plugins",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Plugin load failed", ex);
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
        chooser.setSelectedFile(new java.io.File("shapes.json"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = chooser.getSelectedFile().toPath();
        try {
            serializer.save(shapeList, path);
            JOptionPane.showMessageDialog(this, "Saved to " + path, "Save", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            showError("Save failed", ex);
        }
    }

    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = chooser.getSelectedFile().toPath();
        try {
            serializer.load(shapeList, path);
            refreshShapeListView();
            JOptionPane.showMessageDialog(this, "Loaded from " + path, "Load", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | RuntimeException ex) {
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
