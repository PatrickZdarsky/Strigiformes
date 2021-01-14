package net.nextvizion.strigiformes;

import net.nextvizion.strigiformes.parser.Parser;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Main {

    public static void main(String[] args) {

        int tries = 10000;
        double time1 = 0, time2 = 0;


        for (int i = 0; i < tries; i++) {
            var s1 = "${cloud:sysPrefix}%{§cRxcki|run_command:/pi Rxcki} befindet sich auf §{grey}Server %{§{gold}Lobby-§{white}1|run_command:/server lobby1} mit 6 anderen Spielern!";
            var colorParse = "§{green~random}I am a beautiful Gradient§{white}";
            var componentParse = "%{§cRxc§4ki|run_command:/pi Rxcki|show_text:§8This §7is a test\n§6new line}";
            long now = System.nanoTime();
            var message = Parser.parse(s1+s1+s1+s1+s1+s1);
            time1 += ((System.nanoTime()-now)/1000000.0);
            now = System.nanoTime();
            var json = message.toJson();
            time2 += ((System.nanoTime()-now)/1000000.0);
        }

        System.out.println("Parsing time avg: "+(time1/tries)+"ms");
        System.out.println("Json time avg: "+(time2/tries)+"ms");
    }
}
