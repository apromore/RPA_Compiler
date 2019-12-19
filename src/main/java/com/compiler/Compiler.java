package com.compiler;

public class Compiler {

    public static void main(String[] args) {
        if (args[0].equals("--mode")) {
            if (args[1].equals("1")) {
                DeclarativeCompiler.FILE_NAME = args[2];
                DeclarativeCompiler.compile();
            } else if (args[1].equals("2")) {
                ImperativeCompiler.TRANSFORMATIONS_FILE_NAME = args[2];
                ImperativeCompiler.LOG_FILE_PATH = args[3];
                ImperativeCompiler.compile();
            }
        }
    }
}
