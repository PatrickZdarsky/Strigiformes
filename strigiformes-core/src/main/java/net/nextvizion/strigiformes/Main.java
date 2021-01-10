package net.nextvizion.strigiformes;

import net.nextvizion.strigiformes.parser.Parser;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            var s1 = "${cloud:sysPrefix}%{§cRxcki|run_command:/pi Rxcki} befindet sich auf §{grey}Server %{§{gold}Lobby-§{white}1|run_command:/server lobby1} mit 6 anderen Spielern!";
            var colorParse = "§{green~random}I am a beautiful Gradient§{white}";
            var componentParse = "%{§cRxc§4ki|run_command:/pi Rxcki|show_text:§8This §7is a test\n§6new line}";
            long now = System.nanoTime();
            Parser parser = new Parser("§6This is gold");
            var message = parser.parse();
            System.out.println("Time: "+(System.nanoTime()-now)/1000000.0+"ms");
            now = System.nanoTime();
            System.out.println(message.toJson());
            System.out.println("JsonTime: "+(System.nanoTime()-now)/1000000.0+"ms");
            System.out.println("FINISH");
        }
    }
}
