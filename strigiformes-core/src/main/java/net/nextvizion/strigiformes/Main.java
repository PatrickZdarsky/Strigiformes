package net.nextvizion.strigiformes;

import net.nextvizion.strigiformes.parser.token.Tokenizer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Main {

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer("${cloud:sysPrefix}Du §7befindest §6dich auf §{grey}Server %{§{gold}Lobby-§{white}1|run_command:/server lobby1}");
        tokenizer.tokenize();
    }


}
