

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by 姚鹏 on 2016/11/9.
 */
public class FightOfAnimal {
    private static String[][] tilelMap = new String[7][9];
    private static String[][] animalMap = new String[7][9];
    private static String[][][] animalMapStore = new String[100000][7][9];
    private static int step = 0;
    private static Scanner inputScanner = new Scanner(System.in);
    private static int i = 0;
    private static int j = 0;
    private static int iPlanNext = 0;
    private static int jPlanNext = 0;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("欢迎来到野兽之战，敌军已经到达战场！" + "\n");
        help();

        // 初始化地图
        Scanner scanner = new Scanner(new File("tile.txt"));
        Scanner scanner1 = new Scanner(new File("animal.txt"));
        for (i = 0; i < 7; i++) {
            String row = scanner.nextLine();
            String row1 = scanner1.nextLine();
            for (j = 0; j < 9; j++) {
                if (j < 3) {
                    if (row1.charAt(j) != '0') {
                        animalMap[i][j] = row1.charAt(j) + "l";
                    } else animalMap[i][j] = row1.charAt(j) + " ";
                } else if (j > 5) {
                    if (row1.charAt(j) != '0') {
                        animalMap[i][j] = row1.charAt(j) + "r";
                    } else animalMap[i][j] = row1.charAt(j) + " ";
                } else {
                    animalMap[i][j] = row1.charAt(j) + " ";
                }
                tilelMap[i][j] = row.charAt(j) + "";
            }
        }

        animalMapStore[1][1][1] = "";//防止一开始就输入“redo”报错

        copyArray(animalMapStore, animalMap, step);//储存第一张地图

        boolean player;//利用布尔变量进行玩家的改变

        //开始进行游戏
        start:
//用于restart
        while (true) {
            player = true;
            step = 0;
            copyArray(animalMap, animalMapStore, step);
            System.out.print("\n" + getFinalMap(animalMap, tilelMap));
            animalMapStore[1][1][1] = ""; //重新开始时地图初始化


            while (true) {
                int operationReturnValue = operation(player); //进行操作并获得返回值

                //重新开始
                if (operationReturnValue == 1)
                    continue start;

                //悔棋
                if (operationReturnValue == 2) {
                    if (step - 1 < 0) {
                        System.out.println("已回到起始位置，你还要我怎样。。。");
                        continue;
                    }
                    step = step - 1;
                    player = !player;
                    copyArray(animalMap, animalMapStore, step);
                    System.out.print("\n" + getFinalMap(animalMap, tilelMap));
                    continue;
                }

                //取消悔棋
                if (operationReturnValue == 3) {
                    if (animalMapStore[step + 1][1][1].equals("")) {
                        System.out.println("没有悔棋可以取消!");
                        continue;
                    }
                    step = step + 1;
                    player = !player;
                    copyArray(animalMap, animalMapStore, step);
                    System.out.print("\n" + getFinalMap(animalMap, tilelMap));
                    continue;
                }

                //退出游戏
                if (operationReturnValue == 4) {
                    System.out.println("这局本来高高兴兴，泥为什么要退出游戏~~o(>_<)o ~~");
                    break start;
                }

                player = !player;//改变玩家
                step = step + 1;
                copyArray(animalMapStore, animalMap, step);//进行操作动物的操作后储存地图
                animalMapStore[step + 1][1][1] = "";//防止移动过动物仍然可以进行redo操作

                //胜负判断
                if (!(winLose(animalMap) == 0)) {
                    switch (winLose(animalMap)) {
                        case 1:
                            System.out.println("左方玩家胜利:攻占敌方兽穴!");
                            break;
                        case 2:
                            System.out.println("右方玩家胜利:攻占敌方兽穴!");
                            break;
                        case 3:
                            System.out.println("左方玩家胜利；右方玩家的兽已被吃完！");
                            break;
                        case 4:
                            System.out.println("右方玩家胜利；左方玩家的兽已被吃完！");
                            break;
                        case 5:
                            System.out.println("左方玩家胜利：右方玩家的兽已经动不了了！");
                            break;
                        case 6:
                            System.out.println("右方玩家胜利：左方玩家的兽已经动不了了！");
                            break;
                    }
                    System.out.println("强烈建议输入“restart”再来一局！");
                    String input = inputScanner.nextLine();
                    if (input.equals("restart")) {
                        continue start;
                    } else {
                        System.out.println("输入的不是restart，自动结束游戏。。。");
                        break start;
                    }
                }
            }
        }
    }

    //实现动物地图对地形地图的覆盖并返回一张最终地图，便于输出
    public static String getFinalMap(String[][] animalMap, String[][] tilelMap) {
        String finalmap = "";
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                switch (animalMap[i][j]) {
                    case "0 "://该地形上无动物
                        switch (tilelMap[i][j]) {
                            case "0":
                                finalmap = finalmap + "    ";
                                break;
                            case "1":
                                finalmap = finalmap + " 水 ";
                                break;
                            case "2":
                                finalmap = finalmap + " 陷 ";
                                break;
                            case "3":
                                finalmap = finalmap + " 家 ";
                                break;
                            case "4":
                                finalmap = finalmap + " 陷 ";
                                break;
                            case "5":
                                finalmap = finalmap + " 家 ";
                                break;
                        }
                    default:
                        switch (animalMap[i][j]) {
                            case "1l":
                                finalmap = finalmap + "1鼠 ";
                                break;
                            case "2l":
                                finalmap = finalmap + "2猫 ";
                                break;
                            case "3l":
                                finalmap = finalmap + "3狼 ";
                                break;
                            case "4l":
                                finalmap = finalmap + "4狗 ";
                                break;
                            case "5l":
                                finalmap = finalmap + "5豹 ";
                                break;
                            case "6l":
                                finalmap = finalmap + "6虎 ";
                                break;
                            case "7l":
                                finalmap = finalmap + "7狮 ";
                                break;
                            case "8l":
                                finalmap = finalmap + "8象 ";
                                break;
                            case "1r":
                                finalmap = finalmap + " 鼠1";
                                break;
                            case "2r":
                                finalmap = finalmap + " 猫2";
                                break;
                            case "3r":
                                finalmap = finalmap + " 狼3";
                                break;
                            case "4r":
                                finalmap = finalmap + " 狗4";
                                break;
                            case "5r":
                                finalmap = finalmap + " 豹5";
                                break;
                            case "6r":
                                finalmap = finalmap + " 虎6";
                                break;
                            case "7r":
                                finalmap = finalmap + " 狮7";
                                break;
                            case "8r":
                                finalmap = finalmap + " 象8";
                                break;
                        }
                }
            }
            finalmap = finalmap + "\n";
        }
        return finalmap;
    }

    //输入指令，判断正误并完成操作
    public static int operation(boolean player) {
        String animal;
        module:
        while (true) {
            String camp = "";
            String campOpposite = "";
            if (player) {
                camp = camp + "l";
                campOpposite = campOpposite + "r";
                System.out.println("请左方玩家行动:");
            } else {
                camp = camp + "r";
                campOpposite = campOpposite + "l";
                System.out.println("请右方玩家行动:");
            }

            String input = inputScanner.nextLine();
            switch (input) {
                case "help":
                    help();
                    break;
                case "restart":
                    return 1;
                case "undo":
                    return 2;
                case "redo":
                    return 3;
                case "exit":
                    return 4;
                default:

                    // 各种错误输入
                    if (input.length() != 2) {
                        System.out.println("不能识别指令“" + input + "”，请重新输入。");
                    } else {
                        if (player) {
                            animal = input.substring(0, 1) + "l";
                        } else {
                            animal = input.substring(0, 1) + "r";
                        }
                        int animalnumber;
                        try {
                            animalnumber = Integer.parseInt(input.substring(0, 1));
                            if (animalnumber < 1 || animalnumber > 8) {
                                System.out.println("不能识别指令“" + input + "”，请重新输入。");
                                continue module;
                            }
                        } catch (Exception e) {
                            System.out.println("不能识别指令“" + input + "”，请重新输入。");
                            continue module;
                        }
                        String operation = input.substring(1, 2);
                        switch (operation) {
                            case "w":
                            case "a":
                            case "s":
                            case "d":
                                break;
                            default:
                                System.out.println("不能识别指令“" + input + "”，请重新输入。");
                                continue module;
                        }


                        //终于输入正确，可以开始操作了
                        //遍历数组进行匹配
                        for (i = 0; i < 7; i++) {
                            for (j = 0; j < 9; j++) {
                                if (animal.equals(animalMap[i][j])) {
                                    switch (move(i, j, operation, camp, campOpposite)) {
                                        case -1:
                                            System.out.println("你是想蹦到异世界去么，重新输指令！");
                                            continue module;
                                        case -2:
                                            System.out.println("这只动物过河会淹死，换个指令！");
                                            continue module;
                                        case -3:
                                            System.out.println("河里有对面老鼠，跳不过河的尴尬");
                                            continue module;
                                        case -4:
                                            System.out.println("你是想吃友军？怎么可能让你得逞！");
                                            continue module;
                                        case -5:
                                            System.out.println(matchAnimal(animalMap[i][j].substring(0, 1)) + "吃不了"
                                                    + matchAnimal(animalMap[iPlanNext][jPlanNext].substring(0, 1))
                                                    + "，no zuo nuo die 哦，童鞋");
                                            continue module;
                                        case -6:
                                            System.out.println("年轻人冷静点，这是自己家。。。");
                                            continue module;
                                        case -7:
                                            System.out.println("别想着去攻击岸上的动物，老实点");
                                            continue module;
                                        case 0:
                                            animalMap[iPlanNext][jPlanNext] = animalMap[i][j];
                                            animalMap[i][j] = "0 ";
                                            break module;
                                    }
                                }
                            }
                        }
                        System.out.println("对不起，该动物已经升天了，请重新输入");//没找到这个动物
                    }
            }
        }
        System.out.print("\n" + getFinalMap(animalMap, tilelMap));//完成一次操作
        return 0;//防止没有返回值
    }

    //尝试移动并获得返回值以确定该操作是否有效，接下来进行哪名玩家的操作
    public static int move(int i, int j, String operation, String camp, String campOpposite) {
        iPlanNext = i;
        jPlanNext = j;
        switch (operation) {
            case "a":
                if (j == 0)
                    return -1;
                else {
                    jPlanNext = j - 1;
                    break;
                }
            case "d":
                if (j == 8)
                    return -1;
                else
                    jPlanNext = j + 1;
                break;
            case "w":
                if (i == 0)
                    return -1;
                else
                    iPlanNext = i - 1;
                break;
            case "s":
                if (i == 6)
                    return -1;
                else
                    iPlanNext = i + 1;
                break;
        }

        //下一步是不是自己的家
        if (home(i, j) == -6)
            return -6;

        //动物过河
        switch (testRiver(i, j, camp, campOpposite, operation)) {
            case -2://不能过河的动物
                return -2;
            case -3://河里有对面老鼠
                return -3;
            case -4://过河后是己方动物
                return -4;
            case -5://河对面的动物比操作的动物6
                return -5;
            case 1:
                return 0;
            default:
                break;
        }

        //判断下一个是否为本阵营的动物
        if (testCamp(iPlanNext, jPlanNext, camp) == -4)
            return -4;

        //判断老鼠是不是能上岸
        if (testMouseLand(i, j) == -7)
            return -7;

        //判断下一个动物能不能被吃
        if (trap(camp) == 1)//判断下一步是不是存在陷阱
            return 0;

        //无陷阱才能正确比较大小
        if (testEat(animalMap[i][j], animalMap[iPlanNext][jPlanNext]) == -5)
            return -5;

        return 0;//一次有效的操作
    }

    //各种动物的过河
    public static int testRiver(int i, int j, String camp, String campOpposite, String operation) {
        if (tilelMap[iPlanNext][jPlanNext].equals("1")) {//下一步是河
            switch (animalMap[i][j].substring(0, 1)) {
                case "1":
                    return 1;//老鼠正常走
                case "6":
                case "7":
                    switch (operation) {
                        case "a":
                            jPlanNext = jPlanNext - 3;
                            break;
                        case "d":
                            jPlanNext = jPlanNext + 3;
                            break;
                        case "w":
                            iPlanNext = iPlanNext - 2;
                            break;
                        case "s":
                            iPlanNext = iPlanNext + 2;
                            break;
                    }//狮虎跳河
                    if (testMouseInRiver(operation, campOpposite) == -3)//看看有没有老鼠
                        return -3;
                    if (testCamp(iPlanNext, jPlanNext, camp) == -4)//看看对面是不是自己人
                        return -4;
                    if (testEat(animalMap[i][j], animalMap[iPlanNext][jPlanNext]) == -5)//看看干不干得掉对面
                        return -5;
                    return 1;
                default:
                    return -2;//不能过河的动物
            }
        }
        return 0;//下一步不是河
    }

    //检查是不是有对面老鼠在河里
    public static int testMouseInRiver(String operation, String campOpposite) {
        switch (operation) {
            case "w":
                for (int i = iPlanNext + 1; i < iPlanNext + 3; i++) {
                    if (animalMap[i][jPlanNext].equals("1" + campOpposite)) {
                        return -3;
                    }
                }
                break;
            case "s":
                for (int i = iPlanNext - 1; i > iPlanNext - 3; i--) {
                    if (animalMap[i][jPlanNext].equals("1" + campOpposite)) {
                        return -3;
                    }
                }
                break;
            case "a":
                for (int j = jPlanNext + 1; j < jPlanNext + 4; j++) {
                    if (animalMap[iPlanNext][j].equals("1" + campOpposite)) {
                        return -3;
                    }
                }
                break;
            case "d":
                for (int j = jPlanNext - 1; j > jPlanNext - 4; j--) {
                    if (animalMap[iPlanNext][j].equals("1" + campOpposite)) {
                        return -3;

                    }
                }
        }
        return 0;
    }

    //判断这个动物与下一个动物的阵营
    public static int testCamp(int iPlanNext, int jPlanNext, String camp) {
        if (animalMap[iPlanNext][jPlanNext].substring(1, 2).equals(camp))
            return -4;
        else
            return 1;
    }

    //比较这个动物与下一个动物的大小
    public static int testEat(String nowAnimal, String nextAnimal) {
        int nowAnimalInt = Integer.parseInt(nowAnimal.substring(0, 1));
        int nextAnimalInt = Integer.parseInt(nextAnimal.substring(0, 1));
        if (!nextAnimal.equals("0 ")) {
            if (nowAnimalInt - nextAnimalInt == -7) {
                return 1;
            } else if (nowAnimalInt - nextAnimalInt == 7) {
                return -5;
            } else {
                if (nowAnimalInt >= nextAnimalInt) {
                    return 1;
                } else {
                    return -5;
                }
            }
        }
        return 1;
    }

    //测试老鼠能否上岸
    public static int testMouseLand(int i, int j) {
        if (tilelMap[i][j].equals("1") && !tilelMap[iPlanNext][jPlanNext].equals("1")) {
            if (animalMap[iPlanNext][jPlanNext].substring(0, 1).equals("1")
                    || animalMap[iPlanNext][jPlanNext].substring(0, 1).equals("0"))//只有空地和对面老鼠的情况可以登陆
                return 1;
            else
                return -7;
        } else
            return 0;
    }

    //测试下一步是不是陷阱
    public static int trap(String camp) {
        if (tilelMap[iPlanNext][jPlanNext].equals("2") || tilelMap[iPlanNext][jPlanNext].equals("4")) {
            if (testCamp(iPlanNext, jPlanNext, camp) == -1)
                return 0;//陷阱内外阵营相同
            if (camp.equals("l")) {//左方棋子行动
                switch (tilelMap[iPlanNext][jPlanNext]) {
                    case "2":
                        return 1;//己方陷阱
                    case "4":
                        return -1;//敌方陷阱
                }
            }
            if (camp.equals("r")) {
                switch (tilelMap[iPlanNext][jPlanNext]) {
                    case "2":
                        return -1;//敌方陷阱
                    case "4":
                        return 1;//己方陷阱
                }
            }
        }
        return 0;//前方没有陷阱
    }

    //判断能不能吃时确切输出谁不能吃谁
    public static String matchAnimal(String animalNumberString) {
        switch (animalNumberString) {
            case "1":
                return "鼠";
            case "2":
                return "猫";
            case "3":
                return "狼";
            case "4":
                return "狗";
            case "5":
                return "豹";
            case "6":
                return "虎";
            case "7":
                return "狮";
            case "8":
                return "象";
            default:
                return "";
        }
    }

    //判断动物是不是要进自己家
    public static int home(int i, int j) {
        String camp = animalMap[i][j].substring(1, 2);
        switch (camp) {
            case "l":
                if (tilelMap[iPlanNext][jPlanNext].equals("3")) {
                    return -6;
                }
                break;
            case "r":
                if (tilelMap[iPlanNext][jPlanNext].equals("5")) {
                    return -6;
                }
                break;
        }
        return 0;
    }

    //胜负判断
    public static int winLose(String[][] animalMap) {
        if (!animalMap[3][0].equals("0 ")) {
            return 2;//左方玩家的兽穴被占领
        } else if (!animalMap[3][8].equals("0 ")) {
            return 1;//右方玩家的兽穴被占领
        }
        module1:
        {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (animalMap[i][j].substring(1, 2).equals("l")) {//只要有一个左方的兽还在
                        break module1;
                    }
                }
            }
            return 4;
        }//左方兽已死绝，右方胜

        module2:
        {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (animalMap[i][j].substring(1, 2).equals("l")) {//只要有一个左方的兽还在
                        if (move(i, j, "w", "l", "r") >= 0)
                            break module2;
                        else if (move(i, j, "a", "l", "r") >= 0)
                            break module2;
                        else if (move(i, j, "s", "l", "r") >= 0)
                            break module2;
                        else if (move(i, j, "d", "l", "r") >= 0)
                            break module2;
                    }
                }
            }
            return 6;//每一个方向都不能动
        }

        module3:
        {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (animalMap[i][j].substring(1, 2).equals("r")) {//只要有一个右方的兽还在
                        break module3;
                    }
                }
            }
            return 3;//右方兽已死绝，左方胜
        }

        module4:
        {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (animalMap[i][j].substring(1, 2).equals("r")) {//只要有一个右方的兽还在
                        if (move(i, j, "w", "r", "l") >= 0)
                            break module4;
                        else if (move(i, j, "a", "r", "l") >= 0)
                            break module4;
                        else if (move(i, j, "s", "r", "l") >= 0)
                            break module4;
                        else if (move(i, j, "d", "r", "l") >= 0)
                            break module4;
                    }
                }
            }
            return 5;//每一个方向都不能动
        }
        return 0;//无法判断输赢
    }

    //用处不大，就是打印帮助
    public static void help() {
        System.out.println("指令介绍:\n" +
                "\n" +
                "1. 移动指令\n" +
                "        移动指令由两个部分组成。\n" +
                "        第一个部分是数字1-8,根据战斗力分别对应鼠(1),猫(2),狼(3),狗(4),豹(5),虎(6),狮(7),象(8)\n" +
                "        第二个部分是字母wasd中的一个,w对应上方向,a对应左方向,s对应下方向,d对应右方向\n" +
                "        比如指令\"1d\" 表示鼠向右走, \"4w\" 表示狗向左走\n" +
                "\n" +
                "2. 游戏指令\n" +
                "        输入 restart 重新开始游戏\n" +
                "        输入 help 查看帮助\n" +
                "        输入 undo 悔棋\n" +
                "        输入 redo 取消悔棋\n" +
                "        输入 exit 退出游戏\n" + "\n");

    }

    //实现实时地图与历史地图的互相转化
    public static void copyArray(String[][] animalMap, String[][][] animalMapStore, int step) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                animalMap[i][j] = animalMapStore[step][i][j];
            }
        }
    }

    public static void copyArray(String[][][] animalMapStore, String[][] animalMap, int step) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                animalMapStore[step][i][j] = animalMap[i][j];
            }
        }
    }
}