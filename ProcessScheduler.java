import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ProcessScheduler extends JFrame {

    //RENK PALETİ 
    private static final Color BG_MAIN = Color.decode("#FFF0F5"); 
    private static final Color BG_TAB = Color.decode("#DB7093");  
    private static final Color BTN_BG = Color.decode("#FF69B4");  
    private static final Color BTN_FG = Color.WHITE;
    private static final Color TXT_BG = Color.WHITE;              
    private static final Color ACCENT = Color.decode("#C71585"); // arayüzde nasıl duruyor
    private static final Color BORDER_COL = Color.decode("#FFB6C1"); //başka renk bakılabilir

    private JTextField filePathField;
    private JTextField fileQuantumField;
    private JTextField manualQuantumField;
    private JTable processTable;
    private DefaultTableModel tableModel;
    private JTextArea resultsArea;
    
    // Analiz Sekmesi
    private JPanel analysisPanel;
    private JTextArea assistantArea;
    private ChartPanel chartPanel;
    private JTabbedPane tabbedPane;

    // Grafik Ver.
    private Map<String, Double> avgWaitingTimes = new LinkedHashMap<>();

    public ProcessScheduler() {
        setTitle("CPU Zamanlama Simülatörü");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        //Başlık 
        JLabel headerLabel = new JLabel("Process Scheduling Simulator", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(ACCENT);
        headerLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(headerLabel, BorderLayout.NORTH);

        //Sekmeler
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(BG_MAIN);
        tabbedPane.setForeground(ACCENT);
        
        tabbedPane.addTab(" Dosyadan Yükle ", createFilePanel());
        tabbedPane.addTab(" Manuel Giriş ", createManualPanel());
        JPanel analysisContainer = createAnalysisPanel();
        tabbedPane.addTab(" Analiz ve Grafik ", analysisContainer);

        JPanel inputContainer = new JPanel(new BorderLayout());
        inputContainer.setBackground(BG_MAIN);
        inputContainer.add(tabbedPane, BorderLayout.CENTER);
        inputContainer.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Log Alanı
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(BG_MAIN);
        resultsPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
    
        JLabel resultsLabel = new JLabel("Detaylı Simülasyon");
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultsLabel.setForeground(ACCENT);
        resultsLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        resultsPanel.add(resultsLabel, BorderLayout.NORTH);

        resultsArea = new JTextArea();
        resultsArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultsArea.setBackground(TXT_BG);
        resultsArea.setForeground(Color.decode("#333333"));
        resultsArea.setEditable(false);
        resultsArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COL, 2));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputContainer, resultsPanel);
        splitPane.setDividerLocation(450); 
        splitPane.setResizeWeight(0.5);   
        splitPane.setBackground(BG_MAIN);
        splitPane.setBorder(null);         
        splitPane.setDividerSize(5);       
        
        add(splitPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Analiz ve Grafik 
    private JPanel createAnalysisPanel() {
        analysisPanel = new JPanel(new BorderLayout());
        analysisPanel.setBackground(BG_MAIN);
        analysisPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Akıllı Asistan
        JLabel lblAssist = new JLabel("Akıllı Asistan Sıralaması ve Analizi");
        lblAssist.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAssist.setForeground(ACCENT);
        
        assistantArea = new JTextArea(8, 50); 
        assistantArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        assistantArea.setForeground(Color.decode("#333333"));
        assistantArea.setBackground(Color.decode("#FFF5F8"));
        assistantArea.setLineWrap(true);
        assistantArea.setWrapStyleWord(true);
        assistantArea.setEditable(false);
        assistantArea.setText("Henüz bir simülasyon çalıştırılmadı. Lütfen veri girip 'HESAPLA' butonuna basın.");
        assistantArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COL, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(BG_MAIN);
        textPanel.add(lblAssist, BorderLayout.NORTH);
        textPanel.add(new JScrollPane(assistantArea), BorderLayout.CENTER);
        textPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); 

        // Grafik
        chartPanel = new ChartPanel();
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(BORDER_COL, 2));

        analysisPanel.add(textPanel, BorderLayout.NORTH);
        analysisPanel.add(chartPanel, BorderLayout.CENTER);

        return analysisPanel;
    }

    private JPanel createFilePanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_MAIN);
        
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG_MAIN);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COL, 2),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dosya
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel fileLabel = new JLabel("Kaynak Dosya (.txt):");
        fileLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fileLabel.setForeground(ACCENT);
        content.add(fileLabel, gbc);

        gbc.gridx = 1;
        filePathField = createStyledTextField();
        filePathField.setPreferredSize(new Dimension(300, 35));
        content.add(filePathField, gbc);

        gbc.gridx = 2;
        JButton browseBtn = createStyledButton("Gözat...", BG_TAB);
        browseBtn.addActionListener(e -> selectFile());
        content.add(browseBtn, gbc);

        // Quantum
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel quantumLabel = new JLabel("Zaman Kuantumu (RR):");
        quantumLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        quantumLabel.setForeground(ACCENT);
        content.add(quantumLabel, gbc);

        gbc.gridx = 1;
        fileQuantumField = createStyledTextField();
        fileQuantumField.setText("3"); 
        fileQuantumField.setHorizontalAlignment(JTextField.CENTER);
        content.add(fileQuantumField, gbc);

        // Buton
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(25, 10, 10, 10); 
        JButton runBtn = createStyledButton("SİMÜLASYONU BAŞLAT", BTN_BG);
        runBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        runBtn.setPreferredSize(new Dimension(200, 45));
        runBtn.addActionListener(e -> runFileSimulationWithEffect()); 
        content.add(runBtn, gbc);

        wrapper.add(content);
        return wrapper;
    }

    private JPanel createManualPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Kontrol
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(BG_MAIN);

        JLabel qLabel = new JLabel("Zaman Kuantumu:");
        qLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        manualQuantumField = createStyledTextField();
        manualQuantumField.setText("3");
        manualQuantumField.setPreferredSize(new Dimension(50, 30));
        manualQuantumField.setHorizontalAlignment(JTextField.CENTER);
        
        JButton addRowBtn = createStyledButton("+ Satır Ekle", BG_TAB);
        JButton randomBtn = createStyledButton("Rastgele Doldur", new Color(200, 150, 230)); 
        JButton clearBtn = createStyledButton("Temizle", new Color(150, 0, 150));
        JButton calcBtn = createStyledButton("HESAPLA", BTN_BG);

        controlPanel.add(qLabel);
        controlPanel.add(manualQuantumField);
        controlPanel.add(addRowBtn);
        controlPanel.add(randomBtn);
        controlPanel.add(clearBtn);
        controlPanel.add(calcBtn);

        panel.add(controlPanel, BorderLayout.NORTH);

        // Tablo
        String[] columnNames = {"Process ID", "Arrival Time", "Burst Time", "Priority"};
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel);
        
        processTable.setRowHeight(30);
        processTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        processTable.setSelectionBackground(Color.decode("#FFC0CB")); 
        processTable.setGridColor(BORDER_COL);
        
        JTableHeader header = processTable.getTableHeader();
        header.setBackground(BG_TAB);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int x=0; x<4; x++){
             processTable.getColumnModel().getColumn(x).setCellRenderer(centerRenderer);
        }
        
        for (int i = 1; i <= 4; i++) {
            tableModel.addRow(new Object[]{"P" + i, "", "", ""});
        }

        JScrollPane tableScroll = new JScrollPane(processTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER_COL, 2));
        tableScroll.getViewport().setBackground(Color.WHITE);
        panel.add(tableScroll, BorderLayout.CENTER);

        // Butonlar
        addRowBtn.addActionListener(e -> {
            int count = tableModel.getRowCount() + 1;
            tableModel.addRow(new Object[]{"P" + count, "", "", ""});
        });

        // Random Veri
        randomBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            Random r = new Random();
            int num = 5; 
            for(int i=1; i<=num; i++){
                tableModel.addRow(new Object[]{
                    "P"+i, 
                    String.valueOf(r.nextInt(8)), 
                    String.valueOf(r.nextInt(15)+1), 
                    String.valueOf(r.nextInt(5)+1) 
                });
            }
        });

        clearBtn.addActionListener(e -> tableModel.setRowCount(0));
        calcBtn.addActionListener(e -> runManualSimulationWithEffect()); 

        return panel;
    }

    
    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return tf;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(BTN_FG);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bg.darker(), 1, true),
            new EmptyBorder(8, 20, 8, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    
    // YÜKLEME EKRANI 
    
    private void showLoadingEffect(Runnable onComplete) {
        // Yükleme 
        JDialog dialog = new JDialog(this, "Simülasyon Çalışıyor...", true);
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true); 
        dialog.setLayout(new BorderLayout());
        
        // Tasarım
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ACCENT, 2));
        
        JLabel lblStatus = new JLabel("Veriler Hazırlanıyor...", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setForeground(ACCENT);
        lblStatus.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setForeground(BTN_BG);
        progressBar.setBackground(BG_MAIN);
        progressBar.setStringPainted(true);
        progressBar.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        panel.add(lblStatus, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        dialog.add(panel);

        // Animasyon Zam.
        Timer timer = new Timer(25, null);
        timer.addActionListener(e -> {
            int val = progressBar.getValue();
            if (val >= 100) {
                timer.stop();
                dialog.dispose();
                onComplete.run(); 
            } else {
                progressBar.setValue(val + 1);
                // Yazılar
                if(val < 30) lblStatus.setText("Veriler İşleniyor...");
                else if(val < 60) lblStatus.setText("Algoritmalar Yarıştırılıyor...");
                else if(val < 90) lblStatus.setText("En İyi Sonuç Analiz Ediliyor...");
                else lblStatus.setText("Tamamlandı!");
            }
        });
        
        
        SwingUtilities.invokeLater(() -> {
            timer.start();
            dialog.setVisible(true);
        });
    }

    private void runFileSimulationWithEffect() {
        showLoadingEffect(() -> runFileSimulation());
    }

    private void runManualSimulationWithEffect() {
        showLoadingEffect(() -> runManualSimulation());
    }


    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void runFileSimulation() {
        String path = filePathField.getText();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen bir dosya seçin.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Process> processes = readProcessesFromFile(path);
            int quantum = Integer.parseInt(fileQuantumField.getText().trim());
            runSimulationLogic(processes, quantum);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runManualSimulation() {
        List<Process> processes = new ArrayList<>();
        try {
            int rowCount = tableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                String pid = (String) tableModel.getValueAt(i, 0);
                String atStr = (String) tableModel.getValueAt(i, 1);
                String btStr = (String) tableModel.getValueAt(i, 2);
                String prStr = (String) tableModel.getValueAt(i, 3);

                if (btStr == null || btStr.trim().isEmpty()) continue;
                if (atStr == null || atStr.trim().isEmpty()) atStr = "0";
                if (prStr == null || prStr.trim().isEmpty()) prStr = "1";

                int at = Integer.parseInt(atStr.trim());
                int bt = Integer.parseInt(btStr.trim());
                int pr = Integer.parseInt(prStr.trim());

                processes.add(new Process(pid, at, bt, pr));
            }

            if (processes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "En az bir süreç girmelisiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
            int quantum = Integer.parseInt(manualQuantumField.getText().trim());
            runSimulationLogic(processes, quantum);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lütfen sayısal değerleri kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Process> readProcessesFromFile(String path) throws Exception {
        List<Process> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String pid = parts[0].trim();
                    int at = Integer.parseInt(parts[1].trim());
                    int bt = Integer.parseInt(parts[2].trim());
                    int pr = Integer.parseInt(parts[3].trim());
                    list.add(new Process(pid, at, bt, pr));
                }
            }
        }
        list.sort(Comparator.comparingInt(p -> p.arrivalTime));
        return list;
    }

    // DETAYLI ANALİZ

    private void runSimulationLogic(List<Process> originalData, int quantum) {
        StringBuilder sb = new StringBuilder();
        avgWaitingTimes.clear(); 

        sb.append(runAlgoAndGetStr("FCFS", solveFCFS(deepCopy(originalData))));
        sb.append("\n\n");
        sb.append(runAlgoAndGetStr("SJF (Non-Preemptive)", solveSJF(deepCopy(originalData))));
        sb.append("\n\n");
        sb.append(runAlgoAndGetStr("Round Robin (Q=" + quantum + ")", solveRR(deepCopy(originalData), quantum)));
        sb.append("\n\n");
        sb.append(runAlgoAndGetStr("Priority (Non-Preemptive)", solvePriority(deepCopy(originalData))));

        resultsArea.setText(sb.toString());
        resultsArea.setCaretPosition(0);
        
        //AKILLI ASİSTAN 
        StringBuilder assistantText = new StringBuilder();
        assistantText.append("=== PERFORMANS KARŞILAŞTIRMASI (Ortalama Bekleme Süresi) ===\n\n");

        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(avgWaitingTimes.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());

        for (int i = 0; i < sortedList.size(); i++) {
            Map.Entry<String, Double> entry = sortedList.get(i);
            assistantText.append(String.format("%d. %-20s : %.4f birim\n", (i + 1), entry.getKey(), entry.getValue()));
        }

        assistantText.append("\n=== SONUÇ YORUMU ===\n");

        double bestVal = sortedList.get(0).getValue();
        List<String> winners = new ArrayList<>();

        for (Map.Entry<String, Double> entry : sortedList) {
            if (Math.abs(entry.getValue() - bestVal) < 0.0001) {
                winners.add(entry.getKey());
            }
        }

        if (winners.size() > 1) {
            assistantText.append("Eşitlik Durumu: ");
            for (int i = 0; i < winners.size(); i++) {
                assistantText.append(winners.get(i));
                if (i < winners.size() - 1) assistantText.append(" ve ");
            }
            assistantText.append(" algoritmaları aynı en iyi performansı gösterdi.\n");
        } else {
            assistantText.append("En İyi Algoritma: ").append(winners.get(0)).append("\n");
        }
        
        

        if (winners.contains("Round")) {
            assistantText.append("- Not: Round Robin, özellikle tepki süresi (response time) önemliyse tercih edilir.\n");
        }
        if (winners.contains("FCFS") && winners.size() == 1) {
            assistantText.append("- Not: FCFS'nin en iyi çıkması ilginçtir, süreçler muhtemelen ideal bir sırayla gelmiş.\n");
        }

        assistantArea.setText(assistantText.toString());
        chartPanel.repaint(); 
        tabbedPane.setSelectedIndex(2); 
    }

    private String runAlgoAndGetStr(String name, SimulationResult res) {
        double totalWt = 0;
        for (Process p : res.processes) {
            p.turnaroundTime = p.finishTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            totalWt += p.waitingTime;
        }
        double avgWt = res.processes.isEmpty() ? 0 : totalWt / res.processes.size();
        
        String shortName = name.split(" ")[0]; 
        avgWaitingTimes.put(shortName, avgWt);
        
        return getResultsString(name, res);
    }

    // Grafik 
    class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(avgWaitingTimes.isEmpty()) {
                g.drawString("Veri yok. Lütfen simülasyonu çalıştırın.", 50, 50);
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            int pad = 50;
            int barWidth = 80;
            int maxH = h - (2 * pad);
            
            double maxVal = 0;
            for(double v : avgWaitingTimes.values()) if(v > maxVal) maxVal = v;
            if(maxVal == 0) maxVal = 1; 
            
            g2.setColor(Color.GRAY);
            g2.drawLine(pad, h-pad, w-pad, h-pad); 
            g2.drawLine(pad, pad, pad, h-pad);     
            
            int i = 0;
            Color[] colors = {
                Color.decode("#FF69B4"), 
                Color.decode("#DB7093"), 
                Color.decode("#C71585"), 
                Color.decode("#FFB6C1")  
            };
            
            int startX = pad + 40;
            int gap = 120; 
            
            for(Map.Entry<String, Double> entry : avgWaitingTimes.entrySet()) {
                String algo = entry.getKey();
                double val = entry.getValue();
                
                int barHeight = (int) ((val / maxVal) * maxH);
                int x = startX + (i * gap);
                int y = (h - pad) - barHeight;
                
                g2.setColor(colors[i % colors.length]);
                g2.fillRoundRect(x, y, barWidth, barHeight, 10, 10);
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(String.format("%.2f", val), x + 15, y - 5);
                g2.drawString(algo, x + 10, h - pad + 20);
                
                i++;
            }
            
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString("Ortalama Bekleme Süresi (Düşük = İyi)", w/2 - 150, 30);
        }
    }

    // Standart Algo

    private List<Process> deepCopy(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) copy.add(new Process(p));
        return copy;
    }
    
    private SimulationResult solveFCFS(List<Process> processes) {
        int t = 0;
        List<GanttBlock> log = new ArrayList<>();
        for (Process p : processes) {
            if (t < p.arrivalTime) t = p.arrivalTime;
            int start = t; t += p.burstTime;
            p.finishTime = t;
            log.add(new GanttBlock(start, t, p.pid));
        }
        return new SimulationResult(processes, log);
    }
    
    private SimulationResult solveSJF(List<Process> processes) {
        int t = 0;
        List<Process> done = new ArrayList<>();
        List<Process> ready = new ArrayList<>();
        List<GanttBlock> log = new ArrayList<>();
        List<Process> rem = new ArrayList<>(processes);
        rem.sort(Comparator.comparingInt(p -> p.arrivalTime));

        while (done.size() < processes.size()) {
            while (!rem.isEmpty() && rem.get(0).arrivalTime <= t) ready.add(rem.remove(0));
            if (ready.isEmpty() && !rem.isEmpty()) { t = rem.get(0).arrivalTime; continue; }
            ready.sort(Comparator.comparingInt(p -> p.burstTime));
            if (!ready.isEmpty()) {
                Process p = ready.remove(0);
                int start = t; t += p.burstTime;
                p.finishTime = t;
                done.add(p);
                log.add(new GanttBlock(start, t, p.pid));
            }
        }
        return new SimulationResult(done, log);
    }
    
    private SimulationResult solvePriority(List<Process> processes) {
        int t = 0;
        List<Process> done = new ArrayList<>();
        List<Process> ready = new ArrayList<>();
        List<GanttBlock> log = new ArrayList<>();
        List<Process> rem = new ArrayList<>(processes);
        rem.sort(Comparator.comparingInt(p -> p.arrivalTime));

        while (done.size() < processes.size()) {
            while (!rem.isEmpty() && rem.get(0).arrivalTime <= t) ready.add(rem.remove(0));
            if (ready.isEmpty() && !rem.isEmpty()) { t = rem.get(0).arrivalTime; continue; }
            ready.sort(Comparator.comparingInt(p -> p.priority));
            if (!ready.isEmpty()) {
                Process p = ready.remove(0);
                int start = t; t += p.burstTime;
                p.finishTime = t;
                done.add(p);
                log.add(new GanttBlock(start, t, p.pid));
            }
        }
        return new SimulationResult(done, log);
    }
    
    private SimulationResult solveRR(List<Process> processes, int quantum) {
        int t = 0;
        List<Process> done = new ArrayList<>();
        List<GanttBlock> log = new ArrayList<>();
        LinkedList<Process> q = new LinkedList<>();
        List<Process> wait = new ArrayList<>(processes);
        wait.sort(Comparator.comparingInt(p -> p.arrivalTime));

        if (!wait.isEmpty()) {
            if (wait.get(0).arrivalTime > t) t = wait.get(0).arrivalTime;
            while (!wait.isEmpty() && wait.get(0).arrivalTime <= t) q.add(wait.remove(0));
        }

        while (!q.isEmpty() || !wait.isEmpty()) {
            if (q.isEmpty()) {
                t = wait.get(0).arrivalTime;
                while (!wait.isEmpty() && wait.get(0).arrivalTime <= t) q.add(wait.remove(0));
            }
            Process p = q.poll();
            int start = t;
            int exec = Math.min(quantum, p.remainingTime);
            p.remainingTime -= exec;
            t += exec;
            log.add(new GanttBlock(start, t, p.pid));
            while (!wait.isEmpty() && wait.get(0).arrivalTime <= t) q.add(wait.remove(0));
            if (p.remainingTime > 0) q.add(p);
            else { p.finishTime = t; done.add(p); }
        }
        return new SimulationResult(done, log);
    }

    private String getResultsString(String algoName, SimulationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("============================================================\n");
        sb.append("Algoritma: ").append(algoName).append("\n");
        sb.append("============================================================\n");

        sb.append("Gantt Şeması:\n");
        if (!result.log.isEmpty()) {
            sb.append("[").append(result.log.get(0).start).append("]");
            int last = result.log.get(0).start;
            for (GanttBlock b : result.log) {
                if (b.start > last) sb.append("---BOŞ---[").append(b.start).append("]");
                sb.append("---").append(b.pid).append("---[").append(b.end).append("]");
                last = b.end;
            }
        }
        sb.append("\n------------------------------------------------------------\n");
        sb.append(String.format("%-10s | %-8s | %-12s | %-8s\n", "Process", "Bitis", "Turnaround", "Bekleme"));
        sb.append("----------------------------------------------\n");

        double totalTat = 0;
        double totalWt = 0;
        List<Process> sorted = new ArrayList<>(result.processes);
        sorted.sort(Comparator.comparing(p -> p.pid));

        for (Process p : sorted) {
            p.turnaroundTime = p.finishTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            totalTat += p.turnaroundTime;
            totalWt += p.waitingTime;
            sb.append(String.format("%-10s | %-8d | %-12d | %-8d\n", p.pid, p.finishTime, p.turnaroundTime, p.waitingTime));
        }

        double avgTat = sorted.isEmpty() ? 0 : totalTat / sorted.size();
        double avgWt = sorted.isEmpty() ? 0 : totalWt / sorted.size();
        int busy = sorted.stream().mapToInt(p -> p.burstTime).sum();
        int finish = sorted.stream().mapToInt(p -> p.finishTime).max().orElse(0);
        double util = finish > 0 ? ((double)busy / finish) * 100 : 0;

        sb.append("----------------------------------------------\n");
        sb.append(String.format("Ortalama Turnaround Suresi: %.2f\n", avgTat));
        sb.append(String.format("Ortalama Bekleme Suresi:    %.2f\n", avgWt));
        sb.append(String.format("CPU Kullanimi:              %.2f%%\n", util));
        return sb.toString();
    }

    static class Process {
        String pid; int arrivalTime, burstTime, priority, remainingTime, finishTime, waitingTime, turnaroundTime;
        public Process(String pid, int at, int bt, int pr) { this.pid=pid; this.arrivalTime=at; this.burstTime=bt; this.priority=pr; this.remainingTime=bt; }
        public Process(Process o) { this(o.pid, o.arrivalTime, o.burstTime, o.priority); }
    }
    static class GanttBlock {
        int start, end; String pid;
        public GanttBlock(int s, int e, String p) { this.start=s; this.end=e; this.pid=p; }
    }
    static class SimulationResult {
        List<Process> processes; List<GanttBlock> log;
        public SimulationResult(List<Process> p, List<GanttBlock> l) { this.processes=p; this.log=l; }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProcessScheduler());
    }
}