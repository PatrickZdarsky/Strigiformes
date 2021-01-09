package net.nextvizion.strigiformes;

import net.nextvizion.strigiformes.parser.Parser;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser("${cloud:sysPrefix}%{§cRxcki|run_command:/pi Rxcki} befindet sich auf §{grey}Server %{§{gold}Lobby-§{white}1|run_command:/server lobby1} mit 6 anderen Spielern!");
        parser.parse();
    }


}
