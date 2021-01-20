package at.rxcki.strigiformes;

import at.rxcki.strigiformes.parser.Parser;
import org.json.JSONArray;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Main {

    public static void main(String[] args) {

        int tries = 1;
        double time1 = 0, time2 = 0;


        for (int i = 0; i < tries; i++) {
            String s1 = "${cloud:sysPrefix}%{§cRxcki|run_command:/pi Rxcki} befindet sich auf §{grey}Server %{§{gold}Lobby-§{white}1|run_command:/server lobby1} mit 6 anderen Spielern!";
            String colorParse = "§{green~random}I am a beautiful Gradient§{white}";
            String componentParse = "%{§cRxc§4ki|run_command:/pi Rxcki|show_text:§8This §7is a test\n§6new line}";
            long now = System.nanoTime();
            Message message = Parser.parse("%{§cNotch|run_command:/tp Notch|show_text:Teleport to Notch!}");
            time1 += ((System.nanoTime()-now)/1000000.0);
            now = System.nanoTime();
            JSONArray json = message.toJson();
            System.out.println(json);
            time2 += ((System.nanoTime()-now)/1000000.0);
        }

        System.out.println("Parsing time avg: "+(time1/tries)+"ms");
        System.out.println("Json time avg: "+(time2/tries)+"ms");
    }
}
