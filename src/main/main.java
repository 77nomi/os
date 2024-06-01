package main;
import java.util.*;

public class main {
    public static void main(String[] args) {
        int[] addressSequence = generateAddressSequence();
        int[] pageSequence = transformToPageSequence(addressSequence);
        int[] memorySizes = new int[37];
        for (int i = 0; i < 37; i++) {
            memorySizes[i] = i + 4;
        }

        calculateOptHitRate(pageSequence, 40);

        System.out.println("页框数\tOPT命中率\tFIFO命中率\tLRU命中率");
        for (int memorySize : memorySizes) {
            double optHitRate = calculateOptHitRate(pageSequence, memorySize);
            double fifoHitRate = calculateFifoHitRate(pageSequence, memorySize);
            double lruHitRate = calculateLruHitRate(pageSequence, memorySize);

            System.out.printf("[%d]\t%.4f\t\t%.4f\t\t%.4f\n", memorySize, optHitRate, fifoHitRate, lruHitRate);
        }
    }

    // 生成随机地址序列
    private static int[] generateAddressSequence() {
        int[] addressSequence = new int[400];
        int index = 0;

        for (int i = 0; i < 100; i++) { // 因为每次迭代添加两个地址，所以循环次数减半
            // 在前半部地址空间随机选择一个数 m
            int m = generateRandomAddress(0, 199);
            addressSequence[index++] = m; // 非顺序执行
            addressSequence[index++] = m + 1; // 顺序执行

            // 在后半部地址空间随机选择一个数 m'
            int mPrime = generateRandomAddress(200, 399);
            addressSequence[index++] = mPrime; // 非顺序执行
            addressSequence[index++] = mPrime + 1; // 顺序执行
        }

        return addressSequence;
    }

    private static int generateRandomAddress(int start, int end) {
        Random RANDOM = new Random();
        return RANDOM.nextInt(end - start + 1) + start;
    }

    // 将给定的地址序列数组转换为页序列数组
    private static int[] transformToPageSequence(int[] addressSequence) {
        int[] pageSequence = new int[addressSequence.length];
        for (int i = 0; i < addressSequence.length; i++) {
            pageSequence[i] = addressSequence[i] / 10;
        }
        return pageSequence;
    }


    // 最佳置换算法(OPT)
    private static double calculateOptHitRate(int[] pageSequence, int memorySize) {
        List<Integer> memory = new ArrayList<>(); // 页框
        int pageFaults = 0; //缺页数

        for (int page : pageSequence) {
            if (!memory.contains(page)) {  // 缺页
                if (memory.size() < memorySize) {  // 页框未满直接添加
                    memory.add(page);
                } else {
                    int farthestIndex = -1;
                    int farthestPage = -1;
                    for (int i = 0; i < memory.size(); i++) {
                        int nextPageIndex = findNextPageIndex(pageSequence, page, i);
                        if (nextPageIndex > farthestIndex) {
                            farthestIndex = nextPageIndex;
                            farthestPage = i;
                        }
                    }
                    memory.set(farthestPage, page);
                }
                pageFaults++;
            }
        }
        return 1 - (double)(pageFaults - 40) / 400.0;
    }

    // 先进先出(FIFO)
    private static double calculateFifoHitRate(int[] pageSequence, int memorySize) {
        Queue<Integer> memory = new LinkedList<>(); // 页框，用队列存储
        int pageFaults = 0; //缺页数

        for (int page : pageSequence) {
            if (!memory.contains(page)) {  // 缺页
                if (memory.size() < memorySize) {  // 页框未满直接添加
                    memory.add(page);
                } else {
                    memory.poll();
                    memory.add(page);
                }
                pageFaults++;
            }
        }

        return 1 - (double)(pageFaults - 40) / 400.0;
    }

    // LRU算法
    private static double calculateLruHitRate(int[] pageSequence, int memorySize) {
        List<Integer> memory = new ArrayList<>(); // 页框
        int pageFaults = 0; //缺页数

        for (int page : pageSequence) {
            if (!memory.contains(page)) {  // 缺页
                if (memory.size() < memorySize) {  // 页框未满直接添加
                    memory.add(page);
                } else {
                    int leastRecentlyUsed = -1;
                    int leastRecentlyUsedIndex = Integer.MAX_VALUE;
                    for (int i = 0; i < memory.size(); i++) {
                        int index = findPreviousPageIndex(pageSequence, page, i);
                        if (index < leastRecentlyUsedIndex) {
                            leastRecentlyUsedIndex = index;
                            leastRecentlyUsed = i;
                        }
                    }
                    memory.set(leastRecentlyUsed, page);
                }
                pageFaults++;
            }
        }

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