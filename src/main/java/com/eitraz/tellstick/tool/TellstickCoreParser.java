package com.eitraz.tellstick.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse the telldus-core.h and output TelldusCoreLibrary.java Interface
 */
public class TellstickCoreParser {
    private static final Pattern DEFINE_PATTERN = Pattern.compile("#define ([\\w_]+?)\\s*?(-??[\\d]+?)");
    private static final Pattern EVENT_PATTERN = Pattern.compile("typedef void \\(WINAPI \\*(\\w+?)\\)\\s*?\\((.*?)\\);");
    private static final Pattern METHOD_PATTERN = Pattern.compile("TELLSTICK_API (.+?) WINAPI (\\w*?)\\((.*?)\\);");

    private static final Map<String, String> ARGUMENT_REPLACE_MAP = new LinkedHashMap<>();

    static {
        ARGUMENT_REPLACE_MAP.put("const char *", "String ");
        ARGUMENT_REPLACE_MAP.put("const char* ", "String ");
        ARGUMENT_REPLACE_MAP.put("void *", "Pointer ");
        ARGUMENT_REPLACE_MAP.put("void* ", "Pointer ");
        ARGUMENT_REPLACE_MAP.put("char *", "Pointer ");
        ARGUMENT_REPLACE_MAP.put("char* ", "Pointer ");
        ARGUMENT_REPLACE_MAP.put("unsigned char ", "int ");
        ARGUMENT_REPLACE_MAP.put("bool ", "boolean ");
        ARGUMENT_REPLACE_MAP.put("int *", "IntByReference ");
        ARGUMENT_REPLACE_MAP.put("int* ", "IntByReference ");
    }

    public TellstickCoreParser(File telldusCoreFile, File outputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(telldusCoreFile));

        List<String> defines = new ArrayList<>();
        List<String> events = new ArrayList<>();
        List<String> methods = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            Matcher matcher;
            // Define
            if ((matcher = DEFINE_PATTERN.matcher(line)).matches()) {
                String define = "\t// " + matcher.group(0) + "\n";
                define += "\tpublic static final int " + matcher.group(1) + " = " + matcher.group(2) + ";";
                defines.add(define);
            }
            // Event
            else if ((matcher = EVENT_PATTERN.matcher(line)).matches()) {
                String event = "\t// " + matcher.group(0) + "\n";
                event += "\tpublic interface " + matcher.group(1) + " extends Callback {\n";
                event += "\t\tpublic void event(" + getArguments(matcher.group(2)) + ");\n";
                event += "\t}";

                events.add(event);
            }
            // Method
            else if ((matcher = METHOD_PATTERN.matcher(line)).matches()) {
                String method = "\t// " + matcher.group(0) + "\n";
                method += "\tpublic " + replaceTypes(matcher.group(1) + " ").trim() + " " + matcher.group(2) + "(" + getArguments(matcher.group(3)) + ");";
                methods.add(method);
            }
        }

        reader.close();

        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

        writer.println("package com.eitraz.tellstick.core;");
        writer.println("");
        writer.println("import com.sun.jna.Callback;");
        writer.println("import com.sun.jna.Library;");
        writer.println("import com.sun.jna.Pointer;");
        writer.println("import com.sun.jna.ptr.IntByReference;");
        writer.println("");
        writer.println("/**");
        writer.println(" * Generated: " + new Date().toString());
        writer.println(" */");
        writer.println("public interface TellstickCoreLibrary extends Library {");

        writer.println("");
        writer.println("\t/**");
        writer.println("\t * Defines");
        writer.println("\t */");
        writer.println("");

        for (String string : defines) {
            writer.println(string);
            writer.println("");
        }

        writer.println("");
        writer.println("\t/**");
        writer.println("\t * Events");
        writer.println("\t */");
        writer.println("");

        for (String string : events) {
            writer.println(string);
            writer.println("");
        }

        writer.println("");
        writer.println("\t/**");
        writer.println("\t * Methods");
        writer.println("\t */");
        writer.println("");

        for (String string : methods) {
            writer.println(string);
            writer.println("");
        }

        writer.println("}");

        writer.flush();
        writer.close();
    }

    private String voidArgument(String string) {
        if (string.equals("void"))
            return "";
        return string;
    }

    private String replaceTypes(String type) {
        for (String find : ARGUMENT_REPLACE_MAP.keySet()) {
            type = type.replace(find, ARGUMENT_REPLACE_MAP.get(find));
        }
        return type;
    }

    private String getArguments(String string) {
        if (string == null)
            return "";

        string = string.trim();
        if (string.isEmpty())
            return "";

        List<String> arguments = new ArrayList<>();
        String[] split = string.split(",");

        for (String arg : split) {
            arguments.add(replaceTypes(arg.trim()));
        }

        String result = "";
        for (String arg : arguments) {
            result += ", " + arg;
        }
        return voidArgument(result.substring(2));
    }

    public static void main(String[] args) {
        try {
            new TellstickCoreParser(
                    new File("src/main/resources/telldus-core.h"),
                    new File("src/main/java/com/eitraz/tellstick/core/TellstickCoreLibrary.java")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
