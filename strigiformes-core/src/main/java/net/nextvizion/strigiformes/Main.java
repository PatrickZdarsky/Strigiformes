package net.nextvizion.strigiformes;

import net.nextvizion.strigiformes.parser.Parser;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Main {

    public static void main(String[] args) {
        var s1 = "${cloud:sysPrefix}%{§cRxcki|run_command:/pi Rxcki} befindet sich auf §{grey}Server %{§{gold}Lobby-§{white}1|run_command:/server lobby1} mit 6 anderen Spielern!";
        var colorParse = "Beginning §6Colored Simple §a§kImmediately Formatted-colored §2Colored Simple #2§kFormatted after §lcolored §{f~linear}Gradient!§{black}";
        Parser parser = new Parser(colorParse);
        var message = parser.parse();
        System.out.println("\n\n"+message);
    }


}
