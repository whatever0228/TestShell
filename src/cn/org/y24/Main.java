package cn.org.y24;

import java.io.FileNotFoundException;

public class Main {
    private static final boolean inTestMode = true;

    public static void main(String[] args) throws FileNotFoundException {
        for (String arg : args) System.out.println(arg);
        OS os;
        if (inTestMode || args.length == 0) os = new OS();
        else os = new OS(true, args[0]);
        os.mainLoop();
    }
}
