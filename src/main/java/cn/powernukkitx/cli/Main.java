package cn.powernukkitx.cli;

import cn.powernukkitx.cli.util.ConfigUtils;
import cn.powernukkitx.cli.util.EnumOS;
import cn.powernukkitx.cli.util.OSUtils;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;

import java.util.Locale;
import java.util.Timer;

public final class Main {
    static Timer timer = null;

    public static void main(String[] args) {
        //直接替换成start来解决pixelcloud的神奇启动参数
        args[0] = "start";
        System.out.println("Replaced!");
        AnsiConsole.systemInstall();
        ConfigUtils.init();
        // 先设置语言
        if (ConfigUtils.forceLang() != null) {
            Locale.setDefault(Locale.forLanguageTag(ConfigUtils.forceLang().toLowerCase()));
        } else {
            if (OSUtils.getOS() == EnumOS.WINDOWS) {
                var locale = OSUtils.getWindowsLocale();
                if (locale != null) {
                    Locale.setDefault(locale);
                    ConfigUtils.set("language", locale.toLanguageTag());
                }
            }
        }
        try {
            new CommandLine(new Preprocessor()).parseArgs(args);
        } catch (Exception ignore) {

        }
        // 解析命令行
        var exitCode = new CommandLine(new App()).execute(args);
        if (timer != null)
            timer.cancel();
        System.exit(exitCode);
    }

    public static Timer getTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        return timer;
    }
}
