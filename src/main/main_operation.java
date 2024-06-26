package main;
import java.util.*;

public class main_operation {

    // 生成随机地址序列
    public static int[] newPageSequence() {
        int[] pageSequence = new int[400];
        int index = 0;

        for (int i = 0; i < 100; i++) { // 因为每次迭代添加两个地址，所以循环次数减半
            // 在前半部地址空间随机选择一个数 m
            int m = randomFunc(0, 199);
            pageSequence[index++] = m / 10;
            pageSequence[index++] = (m + 1) / 10;

            // 在后半部地址空间随机选择一个数 m'
            int mPrime = randomFunc(200, 399);
            pageSequence[index++] = mPrime / 10;
            pageSequence[index++] = (mPrime + 1) / 10;
        }

        return pageSequence;
    }

    private static int randomFunc(int start, int end) {
        Random RANDOM = new Random();
        return RANDOM.nextInt(end - start + 1) + start;
    }


    // 最佳置换算法(OPT)
    public static double getOPT(int[] pageSequence, int pageFramesSize) {
        List<Integer> pageFrames = new ArrayList<>(); // 页框
        int pageFaults = 0; //缺页数

        for (int i = 0; i < 400; i++) {
            int page = pageSequence[i];
            if (!pageFrames.contains(page)) {  // 缺页
                pageFaults++;
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
        }

        if(pageFaults<=40)
            return 1.0;
        else
            return 1 - (double)(pageFaults - 40) / 400.0;
    }

    // 先进先出(FIFO)
    public static double getFIFO(int[] pageSequence, int pageFramesSize) {
        Queue<Integer> pageFrames = new LinkedList<>(); // 页框，用队列存储
        int pageFaults = 0; //缺页数

        for (int page : pageSequence) {
            if (!pageFrames.contains(page)) {  // 缺页
                if (pageFrames.size() < pageFramesSize) {  // 页框未满直接添加
                    pageFrames.add(page);
                } else {
                    pageFrames.poll();
                    pageFrames.add(page);
                }
                pageFaults++;
            }
        }

        if(pageFaults<=40)
            return 1.0;
        else
            return 1 - (double)(pageFaults - 40) / 400.0;
    }

    // LRU算法
    public static double getLRU(int[] pageSequence, int pageFramesSize) {
        List<Integer> pageFrames = new ArrayList<>(); // 页框
        int pageFaults = 0; //缺页数

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
                pageFaults++;
            }
        }

        if(pageFaults<=40)
            return 1.0;
        else
            return 1 - (double)(pageFaults - 40) / 400.0;
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