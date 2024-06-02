package detailPage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FIFODetail extends JFrame {

    private JPanel mainPanel;
    private JTable mainTable;
    private ArrayList<Integer> pageFrames;
    private int[] pageSequence; // 假设这个数组在类的其他部分被初始化
    private int pageFramesSize; // 假设这个变量在类的其他部分被初始化

    // 构造函数
    public FIFODetail(int[] pageSequence, int pageFramesSize) {
        this.pageSequence = pageSequence;
        this.pageFramesSize = pageFramesSize;

        initComponents();
        getDatas();
    }

    // 初始化组件
    private void initComponents() {
        setTitle("FIFO算法模拟 22软工4班 陈芷炫");
        setSize(900, 700);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);


        //当前序列号、缺页情况、pageFrame
        String[] titleColumns = {"地址序列号", "缺页情况"};
        for (int i = 0; i < pageFramesSize; i++) {
            titleColumns = addElement(titleColumns, String.valueOf((i+1)));
        }
        Object[][] mainData = new Object[400][(pageFramesSize+2)];
        mainTable = new JTable(mainData, titleColumns);

        // 设置列宽不可调整
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // 设置每一列的宽度
        int columnWidth = 100; // 例如，设定每一列的宽度为100像素
        for (int i = 0; i < mainTable.getColumnCount(); i++) {
            mainTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
            mainTable.getColumnModel().getColumn(i).setResizable(false); // 设置列宽不可调整
        }

        // 添加自定义格式
        for (int i = 1; i < mainTable.getColumnCount(); i++) {
            mainTable.getColumnModel().getColumn(i).setCellRenderer(new CustomRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(mainTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static String[] addElement(String[] array, String element) {
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = "页框"+element;
        return newArray;
    }



    // 模拟
    private void getDatas() {
        Queue<Integer> pageFrames = new LinkedList<>(); // 页框，用队列存储
        int[] memory = new int[40];
        int index=0;


        for (int i = 0; i < pageSequence.length; i++) {
            int page = pageSequence[i];
            boolean pageDefault = false;
            if (!pageFrames.contains(page)) {  // 缺页
                pageDefault = true;
                if (pageFrames.size() < pageFramesSize) {  // 页框未满直接添加
                    pageFrames.add(page);
                    memory[index++]=page;
                } else {
                    pageFrames.poll();
                    pageFrames.add(page);
                    if(index==pageFramesSize)
                        index = 0;
                    memory[index++]=page;
                }
            }

            mainTable.setValueAt(page,i,0);
            if(pageDefault){
                mainTable.setValueAt("F",i,1);
            }
            int columnIndex = 2; // 列索引从2开始
            for(int j=0;j<pageFrames.size();j++){
                mainTable.setValueAt(memory[j], i, j+2);
                columnIndex++;
            }
            // 如果队列没有填满，填充剩余位置为 "NULL"
            while (columnIndex < pageFramesSize + 2) {
                mainTable.setValueAt("NULL", i, columnIndex);
                columnIndex++;
            }
        }
    }


}