package net.nextvizion.strigiformes;

import net.nextvizion.strigiformes.parser.Parser;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Main {

    public static void main(String[] args) {
        var s1 = "${cloud:sysPrefix}%{§cRxcki|run_command:/pi Rxcki} befindet sich auf §{grey}Server %{§{gold}Lobby-§{white}1|run_command:/server lobby1} mit 6 anderen Spielern!";
        var colorParse = "§{green~random}I am a beautiful Gradient§{white}";
        var componentParse = "%{§cRxc§4ki|run_command:/pi Rxcki}";
        Parser parser = new Parser(componentParse);
        var message = parser.parse();
        System.out.println("\n\n"+message);
        System.out.println("\n\n"+message.toJson());
    }


}
