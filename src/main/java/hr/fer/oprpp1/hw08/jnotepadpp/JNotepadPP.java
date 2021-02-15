package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.actions.*;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

/**
 * Model of notepad
 */
public class JNotepadPP extends JFrame {

    /**
     * Tabbed pane with documents
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Open new document
     */
    private OpenNewDocumentAction openNewDocumentAction;

    /**
     * Open existing document
     */
    private OpenExistingDocumentAction openExistingDocumentAction;

    /**
     * Save document
     */
    private SaveDocumentAction saveDocumentAction;

    /**
     * Save document to certain path
     */
    private SaveDocumentAsAction saveDocumentAsAction;

    /**
     * Close document
     */
    private CloseDocumentAction closeDocumentAction;

    /**
     * Exit application
     */
    private ExitAction exitAction;

    /**
     * Copy selected test
     */
    private CopyAction copyAction;

    /**
     * Cut selected text
     */
    private CutAction cutAction;

    /**
     * Paste copied or cut text
     */
    private PasteAction pasteAction;

    /**
     * Statistic informations of document
     */
    private StatisticsAction statisticsAction;

    /**
     * Statusbar
     */
    private StatusBar statusBar;

    /**
     * Translate app to english
     */
    private LanguageAction english;

    /**
     * Translate app to croatian
     */
    private LanguageAction croatian;

    /**
     * Translate app to german
     */
    private LanguageAction german;

    /**
     * Turns selected text to uppercase
     */
    private ToolsAction toUpperCaseAction;

    /**
     * Turns selected text to lowercase
     */
    private ToolsAction toLoverCaseAction;

    /**
     * Switches selected text's case
     */
    private ToolsAction invertCaseAction;

    /**
     * Sorts selected lines ascending
     */
    private SortAction ascendingSortAction;

    /**
     * Sorts selected lines descending
     */
    private SortAction descendingSortAction;

    /**
     * Removes all duplicated lines
     */
    private UniqueLinesAction uniqueLinesAction;

    /**
     * Buffered storage for actions like cut or paste
     */
    private final BufferedStorage bufferedStorage = BufferedStorage.getInstance();

    /**
     * Localization provider
     */
    private final FormLocalizationProvider flp;

    /**
     * Constructing notepad app
     */
    public JNotepadPP() {

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        model = new DefaultMultipleDocumentModel(this);

        flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        this.addWindowListener(new ExitApplication(this, model));

        initGUI();
        pack();

        flp.addLocalizationListener(() -> {
        });

        bufferedStorage.addBufferedStorageListener(() -> {
            if (bufferedStorage.getStorage() != null)
                pasteAction.setEnabled(true);
        });

        model.createNewDocument();
    }

    /**
     * Initializes GUI components
     */
    private void initGUI() {
        Container mainContainer = getContentPane();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setPreferredSize(new Dimension(800, 500));

        JPanel documentPane = new JPanel(new BorderLayout());
        documentPane.add(model, BorderLayout.CENTER);

        mainContainer.add(documentPane, BorderLayout.CENTER);

        initializeActions();

        documentPane.add(createToolBar(), BorderLayout.PAGE_START);
        createMenuBar();

        statusBar = new StatusBar();
        mainContainer.add(statusBar, BorderLayout.PAGE_END);

        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                closeDocumentAction.setEnabled(model.getNumberOfDocuments() != 0);
                statusBar.setLength(currentModel.getTextComponent().getText().length());
                statusBar.updateStatusBar();

                currentModel.getTextComponent().getCaret().addChangeListener(e -> {

                    System.out.println("jnpp state changed 137");

                    Caret c = (Caret) e.getSource();
                    int selectionLength = Math.abs(c.getDot() - c.getMark());

                    cutAction.setEnabled(selectionLength != 0);
                    copyAction.setEnabled(selectionLength != 0);

                    toUpperCaseAction.setEnabled(selectionLength != 0);
                    toLoverCaseAction.setEnabled(selectionLength != 0);
                    invertCaseAction.setEnabled(selectionLength != 0);

                    ascendingSortAction.setEnabled(selectionLength != 0);
                    descendingSortAction.setEnabled(selectionLength != 0);

                    uniqueLinesAction.setEnabled(selectionLength != 0);

                    updateStatusBarData();
                });

                currentModel.getTextComponent().getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        model.setIconAt(model.getSelectedIndex(), loadIcon("modified", 12, 12));
                        saveDocumentAction.setEnabled(currentModel.getFilePath() != null);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        model.setIconAt(model.getSelectedIndex(), loadIcon("modified", 12, 12));
                        saveDocumentAction.setEnabled(true);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                    }
                });

                currentModel.addSingleDocumentListener(new SingleDocumentListener() {
                    @Override
                    public void documentModifyStatusUpdated(SingleDocumentModel documentModel) {
                        model.setIconAt(model.getSelectedIndex(), loadIcon(documentModel.isModified() ? "modified" : "saveIcon", 12, 12));
                        saveDocumentAction.setEnabled(documentModel.isModified() && documentModel.getFilePath() != null);
                    }

                    @Override
                    public void documentFilePathUpdated(SingleDocumentModel document) {
                        model.setTitleAt(model.getSelectedIndex(), document.getFilePath().getFileName().toString());
                        model.setToolTipTextAt(model.getSelectedIndex(), document.getFilePath().toString());
                        JNotepadPP.this.setTitle(document.getFilePath().toString() + " - JNotepad++");
                    }
                });
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        });
    }

    /**
     * @return returns new <code>JToolBar</code> with initialized buttons
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        addButtons(toolBar);

        return toolBar;
    }

    /**
     * Adds buttons to toolbar
     *
     * @param toolBar parent toolbar
     */
    private void addButtons(JToolBar toolBar) {
        JButton button;

        button = new JButton(openNewDocumentAction);
        toolBar.add(button);

        button = new JButton(openExistingDocumentAction);
        toolBar.add(button);

        button = new JButton(saveDocumentAction);
        toolBar.add(button);

        button = new JButton(saveDocumentAsAction);
        toolBar.add(button);

        button = new JButton(copyAction);
        toolBar.add(button);

        button = new JButton(cutAction);
        toolBar.add(button);

        button = new JButton(pasteAction);
        toolBar.add(button);

        button = new JButton(statisticsAction);
        toolBar.add(button);

        button = new JButton(closeDocumentAction);
        toolBar.add(button);
    }

    /**
     * Creates menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new LMenuBar("file", flp);
        JMenu editMenu = new LMenuBar("edit", flp);
        JMenu statisticsMenu = new LMenuBar("statistics", flp);
        JMenu languages = new LMenuBar("languages", flp);
        JMenu tools = new LMenuBar("tools", flp);
        JMenu changeCase = new LMenuBar("change_case", flp);
        JMenu sort = new LMenuBar("sort", flp);

        JMenuItem menuItem;

        menuItem = new JMenuItem(openNewDocumentAction);
        fileMenu.add(menuItem);

        menuItem = new JMenuItem(openExistingDocumentAction);
        fileMenu.add(menuItem);


        menuItem = new JMenuItem(saveDocumentAction);
        fileMenu.add(menuItem);

        menuItem = new JMenuItem(saveDocumentAsAction);
        fileMenu.add(menuItem);

        fileMenu.addSeparator();

        menuItem = new JMenuItem(closeDocumentAction);
        fileMenu.add(menuItem);

        fileMenu.addSeparator();

        menuItem = new JMenuItem(exitAction);
        fileMenu.add(menuItem);

        menuBar.add(fileMenu);

        menuItem = new JMenuItem(copyAction);
        editMenu.add(menuItem);

        menuItem = new JMenuItem(cutAction);
        editMenu.add(menuItem);

        menuItem = new JMenuItem(pasteAction);
        editMenu.add(menuItem);

        menuBar.add(editMenu);

        menuItem = new JMenuItem(statisticsAction);
        statisticsMenu.add(menuItem);

        menuBar.add(statisticsMenu);

        menuItem = new JMenuItem(english);
        languages.add(menuItem);

        menuItem = new JMenuItem(croatian);
        languages.add(menuItem);

        menuItem = new JMenuItem(german);
        languages.add(menuItem);

        menuBar.add(languages);

        menuItem = new JMenuItem(toUpperCaseAction);
        changeCase.add(menuItem);

        menuItem = new JMenuItem(toLoverCaseAction);
        changeCase.add(menuItem);
        changeCase.add(menuItem);

        menuItem = new JMenuItem(invertCaseAction);
        changeCase.add(menuItem);

        tools.add(changeCase);

        menuItem = new JMenuItem(ascendingSortAction);
        sort.add(menuItem);

        menuItem = new JMenuItem(descendingSortAction);
        sort.add(menuItem);

        tools.add(sort);

        menuItem = new JMenuItem(uniqueLinesAction);
        tools.add(menuItem);

        menuBar.add(tools);

        this.setJMenuBar(menuBar);
    }

    /**
     * Initializes all used actions
     */
    private void initializeActions() {

        openNewDocumentAction = new OpenNewDocumentAction("new", flp, model);
        openExistingDocumentAction = new OpenExistingDocumentAction("open", flp, model);
        saveDocumentAction = new SaveDocumentAction("save", flp, this, model);
        saveDocumentAsAction = new SaveDocumentAsAction("save_as", flp, this, model);
        closeDocumentAction = new CloseDocumentAction("close", flp, this, model);
        exitAction = new ExitAction("exit", flp, this);

        copyAction = new CopyAction("copy", flp, bufferedStorage, model);
        cutAction = new CutAction("cut", flp, bufferedStorage, model);
        pasteAction = new PasteAction("paste", flp, bufferedStorage, model);

        statisticsAction = new StatisticsAction("statistics", flp, this, model);

        english = new LanguageAction("english", flp, "en");
        croatian = new LanguageAction("croatian", flp, "hr");
        german = new LanguageAction("german", flp, "de");

        toUpperCaseAction = new ToolsAction("to_upper_case", flp, BufferedStorage.getInstance(), model) {
            @Override
            public String changeCase(String text) {
                return text.toUpperCase();
            }
        };

        toLoverCaseAction = new ToolsAction("to_lower_case", flp, BufferedStorage.getInstance(), model) {
            @Override
            public String changeCase(String text) {
                return text.toLowerCase();
            }
        };

        invertCaseAction = new ToolsAction("invert_case", flp, BufferedStorage.getInstance(), model) {
            @Override
            public String changeCase(String text) {
                char[] chars = text.toCharArray();
                for(int i = 0; i < chars.length; i++) {
                    char c = chars[i];
                    if(Character.isLowerCase(c)) {
                        chars[i] = Character.toUpperCase(c);
                    } else if(Character.isUpperCase(c)) {
                        chars[i] = Character.toLowerCase(c);
                    }
                }
                return new String(chars);
            }
        };

        ascendingSortAction = new SortAction("asc_sort", flp, model, true);
        descendingSortAction = new SortAction("desc_sort", flp, model, false);
        uniqueLinesAction = new UniqueLinesAction("unique", flp, model);

        openNewDocumentAction.putValue(Action.NAME, "New");
        openNewDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        openNewDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        openNewDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open new document");
        openNewDocumentAction.putValue(Action.SMALL_ICON, loadIcon("newDocIcon", 16, 16));

        openExistingDocumentAction.putValue(Action.NAME, "Open");
        openExistingDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openExistingDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        openExistingDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open existing file from disk.");
        openExistingDocumentAction.putValue(Action.SMALL_ICON, loadIcon("openDocIcon", 16, 16));

        saveDocumentAction.putValue(Action.NAME, "Save");
        saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save modified document");
        saveDocumentAction.putValue(Action.SMALL_ICON, loadIcon("saveIcon", 16, 16));

        saveDocumentAsAction.putValue(Action.NAME, "Save As");
        saveDocumentAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
        saveDocumentAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        saveDocumentAsAction.putValue(Action.SHORT_DESCRIPTION, "Used to specify file name and location of saving");
        saveDocumentAsAction.putValue(Action.SMALL_ICON, loadIcon("saveAsIcon", 16, 16));

        closeDocumentAction.putValue(Action.NAME, "Close");
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
        closeDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to close current document");
        closeDocumentAction.putValue(Action.SMALL_ICON, loadIcon("exitIcon", 16, 16));

        exitAction.putValue(Action.NAME, "Exit");
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Used to close application");

        copyAction.putValue(Action.NAME, "Copy");
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        copyAction.putValue(Action.SHORT_DESCRIPTION, "Copy selected part of document");
        copyAction.putValue(Action.SMALL_ICON, loadIcon("copyIcon", 16, 16));

        cutAction.putValue(Action.NAME, "Cut");
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        cutAction.putValue(Action.SHORT_DESCRIPTION, "Cut selected part of document");
        cutAction.putValue(Action.SMALL_ICON, loadIcon("cutIcon", 16, 16));

        pasteAction.putValue(Action.NAME, "Paste");
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
        pasteAction.putValue(Action.SHORT_DESCRIPTION, "Paste copied or cut part of document");
        pasteAction.putValue(Action.SMALL_ICON, loadIcon("pasteIcon", 16, 16));

        statisticsAction.putValue(Action.NAME, "Statistics");
        statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt S"));
        statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        statisticsAction.putValue(Action.SHORT_DESCRIPTION, "Gives simple statistics of document");
        statisticsAction.putValue(Action.SMALL_ICON, loadIcon("informationIcon", 16, 16));


        saveDocumentAction.setEnabled(false);
        cutAction.setEnabled(false);
        copyAction.setEnabled(false);
        pasteAction.setEnabled(false);
        closeDocumentAction.setEnabled(false);
        toUpperCaseAction.setEnabled(false);
        toLoverCaseAction.setEnabled(false);
        invertCaseAction.setEnabled(false);
        ascendingSortAction.setEnabled(false);
        descendingSortAction.setEnabled(false);
        uniqueLinesAction.setEnabled(false);
    }

    /**
     * Loads image from resources
     *
     * @param imageName image
     * @param width width of image
     * @param height height of image
     * @return returns image if image with <code>imageName</code> exists, null otherwise
     */
    public static ImageIcon loadIcon(String imageName, int width, int height) {
        String imgLocation = "icons/"
                + imageName
                + ".png";
        URL imageURL = JNotepadPP.class.getResource(imgLocation);

        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } else {
            System.err.println("Resource not found: "
                    + imgLocation);
            return null;
        }
    }

    /**
     * Updates statusbar GUI
     */
    private void updateStatusBarData() {
       JTextArea area = model.getCurrentDocument().getTextComponent();

       int length = area.getText().length();
       statusBar.setLength(length);
       int sel = Math.abs(area.getCaret().getDot() - area.getCaret().getMark());
       statusBar.setSel(sel);
       int line;
       int col;
        try {

            line = area.getLineOfOffset(area.getCaretPosition());
            statusBar.setLn(line + 1);
            col = area.getCaretPosition() - area.getLineStartOffset(line);
            statusBar.setCol(col);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        statusBar.updateStatusBar();
    }

    public static void main(String[] args) {
        final String language = "en";
        SwingUtilities.invokeLater(() -> {
            LocalizationProvider.getInstance().setLanguage(language);
            new JNotepadPP().setVisible(true);
        });
    }
}
