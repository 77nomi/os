package main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;

public class display extends JFrame {
    private DefaultCategoryDataset dataset;
    private JTable sequenceTable;
    private JTable hitRateTable;
    private boolean isClick = false;

    public display() {
        setTitle("Page Replacement Algorithms");
        setSize(980, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dataset = new DefaultCategoryDataset();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);

        // 创建按钮
        JButton generateButton = new JButton("生成新序列");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newSequence();
            }
        });
        generateButton.setBounds(20,20,150,30);
        mainPanel.add(generateButton, BorderLayout.NORTH);


        // 随机序列表
        // 创建一个大标题 JLabel
        JLabel sequenceTitle = new JLabel("随机序列表");
        sequenceTitle.setFont(new Font("宋体", Font.BOLD, 24));
        sequenceTitle.setHorizontalAlignment(SwingConstants.CENTER);

        String[] sequenceColumns = {"序列号", "页码"};
        Object[][] sequenceData = new Object[400][2];
        sequenceTable = new JTable(sequenceData, sequenceColumns);
        sequenceTable.getTableHeader().setResizingAllowed(false);
        sequenceTable.setDefaultRenderer(Object.class, new renderer());

        // 创建一个 JPanel，将标题和表格放置在其中
        JPanel sequencePanel = new JPanel(new BorderLayout());
        // 设置随机序列表格大小
        sequencePanel.setBounds(20,70,200,300);
        sequencePanel.setBackground(Color.WHITE);
        sequencePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        sequencePanel.add(sequenceTitle, BorderLayout.NORTH);
        sequencePanel.add(new JScrollPane(sequenceTable), BorderLayout.CENTER);

        mainPanel.add(sequencePanel);


        // 创建折线图
        JFreeChart lineChart = ChartFactory.createLineChart("命中率折线图", "页框数", "命中率", dataset, PlotOrientation.VERTICAL, true, false, false);
        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        // 设置横坐标范围
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0.05); // 设置左侧空白区域比例
        domainAxis.setUpperMargin(0.05); // 设置右侧空白区域比例
        domainAxis.setCategoryMargin(0.15); // 设置类别之间的间距
        // 设置纵坐标范围
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.55, 1.02);
        // 添加
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        chartPanel.setBackground(Color.white);
        chartPanel.setBounds(240,70,700, 300);
        mainPanel.add(chartPanel);



        // 命中率表
        // 创建一个大标题 JLabel
        JLabel hitTitle = new JLabel("命中率表，点击表格可查看具体演示过程");
        hitTitle.setFont(new Font("宋体", Font.BOLD, 24));
        hitTitle.setHorizontalAlignment(SwingConstants.CENTER);

        String[] hitRateColumns = {"页框数", "Opt算法", "FIFO算法", "LRU算法"};
        Object[][] hitRateData = new Object[37][4]; // 40行4列
        hitRateTable = new JTable(hitRateData, hitRateColumns);
        hitRateTable.setDefaultRenderer(Object.class, new renderer());
        hitRateTable.addMouseListener(new MouseAdapter() {
            // 点击事件
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = hitRateTable.rowAtPoint(evt.getPoint());
                int col = hitRateTable.columnAtPoint(evt.getPoint());

                // 处理点击事件
                if(isClick){
                    if (col >= 1) { // 检查列索引是否在后三列内
                        System.out.println("页框数："+(row+4)+"，算法："+col);
                    }
                }
            }
        });

        // 创建一个 JPanel，将标题和表格放置在其中
        JPanel hitPanel = new JPanel(new BorderLayout());
        // 设置命中率表格大小
        hitPanel.setBounds(20,390,920, 250);
        hitPanel.setBackground(Color.WHITE);
        hitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        hitPanel.add(hitTitle, BorderLayout.NORTH);
        hitPanel.add(new JScrollPane(hitRateTable), BorderLayout.CENTER);

        mainPanel.add(hitPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    // 生成新的随机地址序列并重新计算命中率
    private void newSequence() {
        isClick = true;

        // 生成随机地址序列
        int[] pageSequence = main_operation.newPageSequence();

        // 计算不同算法在不同页框数下的命中率
        for (int memorySize = 4; memorySize <= 40; memorySize++) {
            double optHitRate = main_operation.getOPT(pageSequence,memorySize);
            double fifoHitRate = main_operation.getFIFO(pageSequence,memorySize);
            double lruHitRate = main_operation.getLRU(pageSequence,memorySize);

            // 更新表格数据
            hitRateTable.setValueAt(memorySize, memorySize - 4, 0);
            hitRateTable.setValueAt(optHitRate, memorySize - 4, 1);
            hitRateTable.setValueAt(fifoHitRate, memorySize - 4, 2);
            hitRateTable.setValueAt(lruHitRate, memorySize - 4, 3);

            dataset.addValue(optHitRate, "OPT", String.valueOf(memorySize));
            dataset.addValue(fifoHitRate, "FIFO", String.valueOf(memorySize));
            dataset.addValue(lruHitRate, "LRU", String.valueOf(memorySize));
        }

        // 更新随机地址序列表格数据
        for (int i = 0; i < 400; i++) {
            sequenceTable.setValueAt(i + 1, i, 0);
            sequenceTable.setValueAt(pageSequence[i], i, 1);
        }
    }

    public static class renderer extends DefaultTableCellRenderer {
        private DecimalFormat format = new DecimalFormat("#0.0000"); // 设置数据显示格式

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // 设置行颜色交替
            if (row % 2 == 0) {
                setBackground(Color.WHITE);
            } else {
                setBackground(new Color(245, 245, 245));
            }

            // 如果值是Double类型，则进行格式化
            if (value instanceof Double) {
                setText(format.format((Double) value)); // 格式化Double类型的数据
            }
            return this; // 返回当前实例（this）
        }
    }

    @Override
    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                display display = new display();
                display.setVisible(true);
            }
        });
    }
}