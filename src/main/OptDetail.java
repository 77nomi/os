package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OptDetail extends JFrame {

    private JPanel mainPanel;
    private JTable mainTable;
    private ArrayList<Integer> pageFrames;
    private int[] pageSequence; // 假设这个数组在类的其他部分被初始化
    private int pageFramesSize; // 假设这个变量在类的其他部分被初始化

    // 构造函数
    public OptDetail(int[] pageSequence, int pageFramesSize) {
        this.pageSequence = pageSequence;
        this.pageFramesSize = pageFramesSize;

        initComponents();
        getDatas();
    }

    // 初始化组件
    private void initComponents() {
        setTitle("OPT算法模拟");
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
        pageFrames = new ArrayList<>();

        for (int i = 0; i < 400; i++) {
            boolean pageDefault = false;
            int page = pageSequence[i];
            if (!pageFrames.contains(page)) {  // 缺页
                pageDefault = true;
                if (pageFrames.size() < pageFramesSize) {  // 页框未满直接添加
                    pageFrames.add(page);
                } else {
                    int farthestIndex = -1;
                    int farthestPage = -1;
                    for (int j = 0; j < pageFrames.size(); j++) {
                        int nextPageIndex = findNextPageIndex(pageSequence, pageFrames.get(j), i);
                        if (nextPageIndex > farthestIndex) {
                            farthestIndex = nextPageIndex;
                            farthestPage = j;
                        }
                    }
                    pageFrames.set(farthestPage, page);
                }
            }
            mainTable.setValueAt(page,i,0);
            if(pageDefault){
                mainTable.setValueAt("F",i,1);
            }
            for(int j =0;j<pageFramesSize;j++){
                if(j>=pageFrames.size())
                    mainTable.setValueAt("NULL",i,j+2);
                else if(pageFrames.get(j)==null)
                    mainTable.setValueAt("NULL",i,j+2);
                else
                    mainTable.setValueAt(pageFrames.get(j),i,j+2);
            }
        }

    }

    // 最佳置换算法——寻找当前内存页面在未来的访问序列中的下一个访问位置
    private static int findNextPageIndex(int[] pageSequence, int page, int startIndex) {
        for (int i = startIndex; i < pageSequence.length; i++) {
            if (pageSequence[i] == page) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

}