package drawing;

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
import java.nio.file.Path;

/**
 * Main window: graphics editor with shape list, property editing, and JSON save/load (variant 4).
 */
public class MainFrame extends JFrame {
    private final ShapeList shapeList = new ShapeList();
    private final ApplicationBootstrap bootstrap = new ApplicationBootstrap();
    private final DrawingPanel drawingPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> shapeListView = new JList<>(listModel);
    private final JsonShapeListSerializer serializer;

    public MainFrame() {
        setTitle("Graphics Editor — Lab 3 (JSON, variant 4)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);

        serializer = new JsonShapeListSerializer(bootstrap.getCodecRegistry());
        drawingPanel = new DrawingPanel(shapeList, bootstrap.getRendererRegistry());
        drawingPanel.addShapeListListener(this::refreshShapeListView);

        add(createToolBar(), BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.WEST);
        setJMenuBar(createMenuBar());

        refreshShapeListView();
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar("Tools");
        for (ApplicationBootstrap.ToolEntry entry : bootstrap.getTools()) {
            JButton button = new JButton(entry.getLabel());
            button.addActionListener(e -> drawingPanel.setTool(entry.createTool()));
            toolBar.add(button);
        }
        return toolBar;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(new java.awt.Dimension(260, 0));

        panel.add(new JLabel("Shapes"), BorderLayout.NORTH);
        panel.add(new JScrollPane(shapeListView), BorderLayout.CENTER);

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
        menuBar.add(fileMenu);
        return menuBar;
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
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
