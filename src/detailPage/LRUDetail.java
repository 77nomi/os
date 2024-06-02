package detailPage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LRUDetail extends JFrame {

    private JTable mainTable;
    private final int[] pageSequence;
    private final int pageFramesSize;

    // 构造函数
    public LRUDetail(int[] pageSequence, int pageFramesSize) {
        this.pageSequence = pageSequence;
        this.pageFramesSize = pageFramesSize;

        initComponents();
        getDatas();
    }

    // 初始化组件
    private void initComponents() {
        setTitle("LRU算法模拟 22软工4班 陈芷炫");
        setSize(900, 700);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);


        //当前序列号、缺页情况、pageFrame
        String[] titleColumns = {"访问页", "缺页情况"};
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
        ArrayList<Integer> pageFrames = new ArrayList<>();

        for (int i = 0; i < 400; i++) {
            int page = pageSequence[i];
            if (!pageFrames.contains(page)) {  // 缺页
                if (pageFrames.size() < pageFramesSize) {  // 页框未满直接添加
                    pageFrames.add(page);
                } else {
                    int leastRecentlyUsed = -1;
                    int leastRecentlyUsedIndex = Integer.MAX_VALUE;
                    for (int j = 0; j < pageFrames.size(); j++) {
                        int index = findPreviousPageIndex(pageSequence, pageFrames.get(j), i);
                        if (index < leastRecentlyUsedIndex) {
                            leastRecentlyUsedIndex = index;
                            leastRecentlyUsed = j;
                        }
                    }
                    pageFrames.set(leastRecentlyUsed, page);
                }
                mainTable.setValueAt("F",i,1);
            }
            mainTable.setValueAt(page,i,0);
            for(int j =0;j<pageFramesSize;j++){
                if(j>= pageFrames.size())
                    mainTable.setValueAt("NULL",i,j+2);
                else if(pageFrames.get(j)==null)
                    mainTable.setValueAt("NULL",i,j+2);
                else
                    mainTable.setValueAt(pageFrames.get(j),i,j+2);
            }
        }

    }


    //LRU算法——寻找当前内存页面最近的一次使用时间
    private static int findPreviousPageIndex(int[] pageSequence, int page, int startIndex) {
        for (int i = startIndex - 1; i >= 0; i--) {
            if (pageSequence[i] == page) {
                return i;
            }
        }
        return Integer.MIN_VALUE;
    }

}