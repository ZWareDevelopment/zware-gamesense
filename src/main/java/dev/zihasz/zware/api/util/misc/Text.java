package dev.zihasz.zware.api.util.misc;

import dev.zihasz.zware.client.command.Command;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class Text {
    public static String toUnicode(String s) {
        return s.toLowerCase()
                .replace("a", "\u1d00")
                .replace("b", "\u0299")
                .replace("c", "\u1d04")
                .replace("d", "\u1d05")
                .replace("e", "\u1d07")
                .replace("f", "\ua730")
                .replace("g", "\u0262")
                .replace("h", "\u029c")
                .replace("i", "\u026a")
                .replace("j", "\u1d0a")
                .replace("k", "\u1d0b")
                .replace("l", "\u029f")
                .replace("m", "\u1d0d")
                .replace("n", "\u0274")
                .replace("o", "\u1d0f")
                .replace("p", "\u1d18")
                .replace("q", "\u01eb")
                .replace("r", "\u0280")
                .replace("s", "\ua731")
                .replace("t", "\u1d1b")
                .replace("u", "\u1d1c")
                .replace("v", "\u1d20")
                .replace("w", "\u1d21")
                .replace("x", "\u02e3")
                .replace("y", "\u028f")
                .replace("z", "\u1d22");
    }
    public static String toSmallCapsText(String s) {
        return s.toLowerCase()
                .replace("a", "ᴀ")
                .replace("b", "ʙ")
                .replace("c", "ᴄ")
                .replace("d", "ᴅ")
                .replace("e", "ᴇ")
                .replace("f", "ғ")
                .replace("g", "ɢ")
                .replace("h", "ʜ")
                .replace("i", "ɪ")
                .replace("j", "ᴊ")
                .replace("k", "ᴋ")
                .replace("l", "ʟ")
                .replace("m", "ᴍ")
                .replace("n", "ɴ")
                .replace("o", "ᴏ")
                .replace("q", "ǫ")
                .replace("r", "ʀ")
                .replace("s", "s")
                .replace("t", "ᴛ")
                .replace("u", "ᴜ")
                .replace("v", "ᴠ")
                .replace("w", "ᴡ")
                .replace("x", "x")
                .replace("y", "ʏ")
                .replace("z", "ᴢ");
    }

    public static String suffixBuilder(String suffix, String separator) {
        String fullSuffix = " " + separator + " " + suffix;
        return fullSuffix;
    }
    public static boolean isCommand(String s) {
        if (s.startsWith(Command.getPrefix()) || s.startsWith("/"))
            return true;
        else
            return false;
    }
    public static String SHA512Hash (String s) {
        String out = "";
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

            byte[] hash = md.digest(s.getBytes(StandardCharsets.UTF_8));
            out = hash.toString();
        } catch (Exception e) {}
        return out;
    }
}
