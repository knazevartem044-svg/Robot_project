package gui;

import gui.state.StateManager;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Главное окно приложения
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * Менеджер сохранения и восстановления состояния окон.
     */
    private StateManager stateManager;

    /**
     * Создаёт и настраивает главное окно приложения
     */
    public MainApplicationFrame()
    {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        RobotModel robotModel = new RobotModel();


        GameWindow gameWindow = new GameWindow(robotModel);
        addWindow(gameWindow);


        RobotInfoWindow robotInfoWindow = new RobotInfoWindow(robotModel);
        addWindow(robotInfoWindow);
        gameWindow.startController();

        String homeDir = System.getProperty("user.home");
        File stateFile = new File(new File(homeDir, "knyazev"), "state.cfg");

        stateManager = new StateManager(stateFile);
        stateManager.register("log", logWindow);
        stateManager.register("model", gameWindow);
        stateManager.register("info", robotInfoWindow);
        stateManager.loadAll();

        setJMenuBar(generateMenuBar());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                requestExit();
            }
        });
    }

    /**
     * Создаёт внутреннее окно протокола
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Добавляет внутреннее окно на рабочий стол приложения
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Создаёт строку меню приложения
     */
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        return menuBar;

    }

    /**
     * Формирует меню Файл
     */
    private JMenu createFileMenu()
    {
        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("Файл");

        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_X);
        exitItem.addActionListener((event) ->
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        fileMenu.add(exitItem);
        return fileMenu;
    }

    /**
     * Формирует меню выбора внешнего вида
     */
    private JMenu createLookAndFeelMenu()
    {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossPlatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_U);
        crossPlatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossPlatformLookAndFeel);

        return lookAndFeelMenu;
    }

    /**
     * Формирует меню Тесты
     */
    private JMenu createTestMenu()
    {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_M);
        addLogMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
        testMenu.add(addLogMessageItem);

        JMenuItem customLogItem = new JMenuItem("Свое сообщение");
        customLogItem.addActionListener((event) -> Logger.debug("НОВОЕ СООБЩЕНИЕ"));
        testMenu.add(customLogItem);

        return testMenu;
    }

    /**
     * Запрашивает подтверждение выхода и сохраняет состояние окон.
     */
    private void requestExit()
    {
        Object[] options = {"Да", "Нет"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "Вы действительно хотите выйти из приложения?",
                "Выход",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION)
        {
            stateManager.saveAll();
            System.exit(0);
        }
    }

    /**
     * Устанавливает внешний вид приложения.
     */
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
        }
    }
}